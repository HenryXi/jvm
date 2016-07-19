package com.henryxi.jvm.outofmemory;

import java.util.ArrayList;
import java.util.List;

/**
 * -XX:PermSize=10M -XX:MaxPermSize=10M
 * this jvm config is not support in java8 and not work in java7
 */
public class RuntimeConstantPoolOOM {
    public static void main(String[] args) {
        // use list contain constant pool reference. avoid full GC collection constant pool
        List<String> list = new ArrayList<String>();
        // 10M PermSize is enough for generate OOM
        int i =0;
        while(true){
            list.add(String.valueOf(i++).intern());
            System.out.println(i);
        }
    }
}
