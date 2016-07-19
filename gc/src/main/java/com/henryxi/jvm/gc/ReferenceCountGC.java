package com.henryxi.jvm.gc;

/**
 * -XX:+PrintGCDetails
 */
public class ReferenceCountGC {
    public Object instance = null;
    private static final int _1MB = 1024 * 1024;
    private byte[] bigSize = new byte[2 * _1MB];

    public static void main(String[] args) {
        ReferenceCountGC.testGC();
    }
    public static void testGC() {
        ReferenceCountGC objA = new ReferenceCountGC();
        ReferenceCountGC objB = new ReferenceCountGC();
        objA.instance = objB;
        objB.instance = objA;
        objA = null;
        objB = null;
//        System.gc();
    }
}
