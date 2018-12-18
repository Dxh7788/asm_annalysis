package com.asm.jelly;

/**
 * @author dongxiaohong
 * @date 2018/12/17 13:58
 */
public abstract class Symbol {

    /**
     * The tag value of CONSTANT_Utf8_info JVMS structures.
     * UTF-8编码的字符串
     * */
    public static int CONSTANT_utf8_info= 1;
    /**
     * The tag value of CONSTANT_Integer_info JVMS structures.
     * 整形字面量
     * */
    public static int CONSTANT_Integer_info=3;
    /**
     * The tag value of CONSTANT_Float_info JVMS structures.
     * 浮点型字面量
     * */
    public static int CONSTANT_Float_info=4;
    /**
     * The tag value of CONSTANT_Long_info JVMS structures.
     * 长整型字面量
     * */
    public static int CONSTANT_Long_info=5;
    /**
     * The tag value of CONSTANT_Double_info JVMS structures.
     * 双精度浮点型字面量
     * */
    public static int CONSTANT_Double_info=6;
    /**
     * The tag value of CONSTANT_Class_info JVMS structures.
     * 类或接口的符号引用
     * */
    public static int CONSTANT_Class_info=7;
    /**
     * The tag value of CONSTANT_String_info JVMS structures.
     * 字符串类型字面量
     * */
    public static int CONSTANT_String_info=8;
    /** 
     * The tag value of CONSTANT_Fieldref_info JVMS structures. 
     * 字段的符号引用
     * */
    public static int CONSTANT_Fieldref_info=9;
    /** 
     * The tag value of CONSTANT_Methodref_info JVMS structures. 
     * 类中方法的符号引用
     * */
    public static int CONSTANT_Methodref_info=10;
    /** 
     * The tag value of CONSTANT_InterfaceMethodref_info JVMS structures. 
     * 接口中方法的符号引用
     * */
    public static int CONSTANT_InterfaceMethodref_info=11;
    /** 
     * The tag value of CONSTANT_NameAndType_info JVMS structures. 
     * 字段或类型的符号引用
     * */
    public static int CONSTANT_NameAndType_info=12;
    /** 
     * The tag value of CONSTANT_MethodHandle_info JVMS structures. 
     * 表示方法句柄
     * */
    public static int CONSTANT_MethodHandle_info=15;
    /** 
     * The tag value of CONSTANT_MethodType_info JVMS structures. 
     * 标志方法类型
     * */
    public static int CONSTANT_MothodType_info=16;
    /** 
     * The tag value of CONSTANT_InvokeDynamic_info JVMS structures. 
     * 表示一个动态方法调用点
     * */
    public static int CONSTANT_InvokeDynamic_info=18;
}
