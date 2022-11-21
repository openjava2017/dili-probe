package org.openjava.probe.health;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.lang.reflect.Modifier;

public class HealthCheckClassTransformer extends ClassVisitor implements Opcodes {
    public HealthCheckClassTransformer(int api, ClassVisitor cv) {
        super(api, cv);
    }

    public void visit(final int version, final int access, final String name, final String signature,
                      final String superName, final String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
                                     String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

        if (mv != null && "doDispatch".equals(name) && Modifier.isProtected(access)) {
            mv = new HealthCheckMethodVisitor(ASM9, mv, access, name, desc);
        }
        return mv;
    }
}
