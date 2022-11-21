package org.openjava.probe.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openjava.probe.health.HealthCheckClassTransformer;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class ProbeAgent {

    public static void premain(String args, Instrumentation inst) {
        //-Xbootclasspath/a:asm-9.4.jar:asm-commons-9.4.jar -javaagent:openjava-probe-1.0.jar=HealthCheck
        System.out.println(String.format("premain args: %s, retransformClassesSupported: %s, redefineClassesSupported: %s",
            args, inst.isRetransformClassesSupported(), inst.isRedefineClassesSupported()));
        inst.addTransformer(new ClassFileTransformer() {
            // premain是在main函数之前执行(类加载之前执行)，因此参数Class<?> c为null
            @Override
            public byte[] transform(ClassLoader loader, String className, Class<?> c, ProtectionDomain domain, byte[] classBytes) throws IllegalClassFormatException {
                if ("org/springframework/web/servlet/DispatcherServlet".equals(className)) {
                    try {
                        ClassReader reader = new ClassReader(classBytes);
                        // 扩展ClassWriter，优先使用线程上下文类加载器，避免加载到项目类出现ClassNotFoundException
                        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES) {
                            protected ClassLoader getClassLoader() {
                                return loader;
                            }
                        };

                        HealthCheckClassTransformer cv = new HealthCheckClassTransformer(Opcodes.ASM9, cw);
                        reader.accept(cv, ClassReader.EXPAND_FRAMES);
                        return cw.toByteArray();
                    } catch (Throwable ex) {
                        ex.printStackTrace();
                    }
                }
                return classBytes;
            }
        }, true);
    }
}
