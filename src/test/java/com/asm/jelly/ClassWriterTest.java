package com.asm.jelly;

import org.junit.Test;
import org.objectweb.asm.ClassWriter;

import java.io.*;

import static org.objectweb.asm.Opcodes.*;

/**
 * @author dongxiaohong
 * @date 2018/12/25 09:42
 */
public class ClassWriterTest {
    @Test
    public void generateOneTest() throws Exception {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(V1_7, ACC_PUBLIC+ACC_ABSTRACT+ACC_INTERFACE,"com.asm.Robbin",null,"java/lang/Object",new String[]{"com/asm/Base"} );
        cw.visitField(ACC_PUBLIC+ACC_FINAL+ACC_STATIC,"LESS","I",null,new Integer(10)).visitEnd();
        cw.visitMethod(ACC_PUBLIC+ACC_ABSTRACT,"compareTo","(Ljava/lang/String;)I",null,null).visitEnd();
        cw.visitEnd();
        byte[] bytes = cw.toByteArray();
        File file = new File("Robbin.class");
        InputStream in = new ByteArrayInputStream(bytes);
        OutputStream out = new FileOutputStream(file);
        byte[] data = new byte[1024];
        while (in.read(data)!=-1){
            out.write(data);
        }
        out.close();
        in.close();
    }
}
