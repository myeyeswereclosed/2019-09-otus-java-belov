package ru.otus.hw04.aop.visitor;

import org.objectweb.asm.*;
import ru.otus.hw04.logged.Log;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class LogMethodVisitor extends MethodVisitor {
    private boolean shouldLogMethodCall = false;
    private String methodName;
    private String descriptor;

    public LogMethodVisitor(
        int api,
        MethodVisitor methodVisitor,
        String methodName,
        String descriptor
    ) {
        super(api, methodVisitor);
        this.methodName = methodName;
        this.descriptor = descriptor;
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        shouldLogMethodCall = descriptor.equals(Log.class.descriptorString());

        return super.visitAnnotation(descriptor, visible);
    }

    public void visitCode() {
        super.visitCode();

        if (shouldLogMethodCall) {
           processLoggedMethodCalled();
        }
    }

    private void processLoggedMethodCalled() {
        Type[] types = Type.getArgumentTypes(descriptor);

        Handle handle = makeHandle();

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        for (int i = 0; i < types.length; i++) {
            mv.visitVarInsn(types[i].getOpcode(Opcodes.ILOAD), i + 1);
        }

        mv.visitInvokeDynamicInsn(
            "makeConcatWithConstants",
            methodArgumentsAsString(types) + "Ljava/lang/String;",
            handle,
            "executed method: " + methodName + ", " + methodParamsAsString(types)
        );
        mv.visitMethodInsn(
            Opcodes.INVOKEVIRTUAL,
            "java/io/PrintStream",
            "println",
            "(Ljava/lang/String;)V",
            false
        );
    }

    private String methodArgumentsAsString(Type[] types) {
        return
            "(" +
                Arrays.stream(types)
                    .map(Type::getDescriptor)
                    .collect(Collectors.joining("")) +
            ")";
    }

    private Handle makeHandle() {
        return
            new Handle(
                H_INVOKESTATIC,
                Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                "makeConcatWithConstants",
                MethodType.methodType(
                    CallSite.class,
                    MethodHandles.Lookup.class,
                    String.class,
                    MethodType.class,
                    String.class, Object[].class
                ).toMethodDescriptorString(),
                false
            );
    }

    private String methodParamsAsString(Type[] types) {
        if (types.length == 0) {
            return "no params";
        }

        if (types.length == 1) {
            return "param: \u0001";
        }

        return "params: {" + Arrays.stream(types).map(type -> "\u0001").collect(Collectors.joining(", ")) + '}';
    }
}
