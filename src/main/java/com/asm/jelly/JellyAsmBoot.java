package com.asm.jelly;

import org.objectweb.asm.ClassReader;

import java.io.IOException;

/**
 * @author dongxiaohong
 * @date 2018/12/14 16:47
 */
public class JellyAsmBoot {

    public static void main(String[] args) throws IOException {
        ClassReader reader = new ClassReader("java.lang.Runnable");
        System.out.println(reader.getClassName());
        System.out.println(reader.getAccess());
        System.out.println(reader.getItemCount());
        System.out.println(reader.getSuperName());
        for (String intf:reader.getInterfaces()){
            System.out.println(intf);
        }
        System.out.println(reader.getMaxStringLength());
    }
}
