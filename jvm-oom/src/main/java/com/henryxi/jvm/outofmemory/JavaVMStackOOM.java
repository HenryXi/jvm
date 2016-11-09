package com.henryxi.jvm.outofmemory;

/**
 * -Xss2M
 * ? can not make OutOfMemoryError:unable to create new native thread
 */
public class JavaVMStackOOM {
    private void neverStop() {
        while (true) {

        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    neverStop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        JavaVMStackOOM oom = new JavaVMStackOOM();
        oom.stackLeakByThread();
    }
}
