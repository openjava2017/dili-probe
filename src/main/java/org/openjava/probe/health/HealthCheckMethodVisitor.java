package org.openjava.probe.health;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

public class HealthCheckMethodVisitor extends AdviceAdapter {
    protected HealthCheckMethodVisitor(int api, MethodVisitor mv, int access, String name, String desc) {
        super(api, mv, access, name, desc);
    }

    @Override
    protected void onMethodEnter() {
        mv.visitVarInsn(ALOAD, 2);
        mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletResponse", "getStatus", "()I", true);
        mv.visitIntInsn(SIPUSH, 404);
        Label label0 = new Label();
        mv.visitJumpInsn(IF_ICMPNE, label0);
        int identifier = newLocal(Type.LONG_TYPE);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitLdcInsn("javax.servlet.error.request_uri");
        mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getAttribute", "(Ljava/lang/String;)Ljava/lang/Object;", true);
        mv.visitTypeInsn(CHECKCAST, "java/lang/String");
        mv.visitVarInsn(ASTORE, identifier);
        mv.visitVarInsn(ALOAD, identifier);
        mv.visitJumpInsn(IFNULL, label0);
        mv.visitVarInsn(ALOAD, identifier);
        mv.visitLdcInsn("/api/health/heartbeat");
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "contains", "(Ljava/lang/CharSequence;)Z", false);
        mv.visitJumpInsn(IFEQ, label0);
        mv.visitVarInsn(ALOAD, 2);
        mv.visitIntInsn(SIPUSH, 204); // 204-HTTP状态码
        mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletResponse", "setStatus", "(I)V", true);
        mv.visitInsn(RETURN);
        mv.visitLabel(label0);
//        mv.visitInsn(RETURN);// 不能添加返回指令，否则无法执行原方法指令
    }
}
