package com.asm.loader;

/**
 * @author dongxiaohong
 * @date 2018/12/25 10:42
 */
public class MyClassLoader extends ClassLoader {
    public Class defineClass(String name, byte[] b){
        return defineClass(name, b,0, b.length);
    }
}
