package com.asm;

import com.asm.annotation.Value;
import com.asm.jelly.Symbol;
import com.sun.istack.internal.NotNull;

public class World <T> implements Readable<@Value T>{
    @NotNull
    private final Integer a=null;
    private final int b = 10;
    private float c = 11f;
    private float d = 11f;
    private float e = 11f;

    /**
     * 内部类是合成的,也就数说
     * Symbol symbol = new Symbol() {
     *         };
     * 是ACC_SYNTHETIC
     * */
    public String joi(){
        Symbol symbol = new Symbol() {
        };
        return symbol.toString();
    }
}