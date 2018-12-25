package com.asm.loader;

import org.objectweb.asm.ClassWriter;

/**
 * @author dongxiaohong
 * @date 2018/12/25 10:49
 */
public class StubClassLoader extends ClassLoader {
    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        if (name.endsWith("_Stub")){
            ClassWriter cw = new ClassWriter(0);
            byte[] b = cw.toByteArray();
            return defineClass(name, b,0,b.length);
        }
        return super.findClass(name);
    }
}
