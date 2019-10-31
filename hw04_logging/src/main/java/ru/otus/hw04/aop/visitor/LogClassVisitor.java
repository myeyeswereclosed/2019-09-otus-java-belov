package ru.otus.hw04.aop.visitor;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LogClassVisitor extends ClassVisitor {
    public LogClassVisitor(int api, ClassWriter classWriter) {
        super(api, classWriter);
    }

    public MethodVisitor visitMethod(
        int access,
        String name,
        String descriptor,
        String signature,
        String[] exceptions
    ) {
        return new LogMethodVisitor(
            Opcodes.ASM5,
            super.visitMethod(access, name, descriptor, signature, exceptions),
            name,
            descriptor
        );
    }
}
