package org.openjava.probe.agent;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.openjava.probe.health.HealthCheckClassTransformer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestProbeAgent {
    public static void main(String[] args) throws Exception {
        InputStream is = new FileInputStream("/Users/brenthuang/Work/projects/openjava-probe/build/classes/java/test/org/openjava/probe/health/TestHttp.class");
        ClassReader reader = new ClassReader(is);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        HealthCheckClassTransformer cv = new HealthCheckClassTransformer(Opcodes.ASM9, cw);
        // 如果MethodVisitor存在LocalVariablesSorter则，需使用EXPAND_FRAMES参数
        reader.accept(cv, ClassReader.EXPAND_FRAMES);
        byte[] packet = cw.toByteArray();
        OutputStream os = new FileOutputStream("/Users/brenthuang/Desktop/TestHttp.class");
        os.write(packet);
        os.flush();
        os.close();
    }
}
