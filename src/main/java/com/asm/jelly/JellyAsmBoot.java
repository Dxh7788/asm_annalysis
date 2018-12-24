package com.asm.jelly;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;

import static org.objectweb.asm.ClassReader.EXPAND_FRAMES;

/**
 * @author dongxiaohong
 * @date 2018/12/14 16:47
 */
public class JellyAsmBoot {

    public static void main(String[] args) throws IOException {
        ClassReader reader = new ClassReader("com.asm.World");
        ClassPrinter visitor = new ClassPrinter();
        reader.accept(visitor,EXPAND_FRAMES);
        System.out.println(reader.getMaxStringLength());
    }
}
