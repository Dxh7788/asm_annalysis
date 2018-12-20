package com.asm;

import com.asm.jelly.Symbol;

public class World {
    private final int a = 10;
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