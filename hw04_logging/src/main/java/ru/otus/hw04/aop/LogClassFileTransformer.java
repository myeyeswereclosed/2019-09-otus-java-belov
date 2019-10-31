package ru.otus.hw04.aop;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import ru.otus.hw04.aop.visitor.LogClassVisitor;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class LogClassFileTransformer implements ClassFileTransformer {
    @Override
    public byte[] transform(
        ClassLoader loader,
        String className,
        Class<?> classBeingRedefined,
        ProtectionDomain protectionDomain,
        byte[] classfileBuffer
    ) {
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new LogClassVisitor(Opcodes.ASM5, classWriter);

        classReader.accept(classVisitor, Opcodes.ASM5);

        return classWriter.toByteArray();
    }
}
