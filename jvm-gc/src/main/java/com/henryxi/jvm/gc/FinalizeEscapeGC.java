package com.henryxi.jvm.gc;

public class FinalizeEscapeGC {
    public static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive() {
        System.out.println("yes, i am still alive");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("finalize method executed!");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws InterruptedException {
        SAVE_HOOK = new FinalizeEscapeGC();
        //first time to save itself
        SAVE_HOOK = null;
        System.gc();
        //pause 5 second to waite finalize method executed
        Thread.sleep(5000);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead");
        }

        //second time save itself but fail, finalize method will executed only once
        SAVE_HOOK = null;
        System.gc();
        //pause 5 second to waite finalize method executed
        Thread.sleep(5000);
        if (SAVE_HOOK != null) {
            SAVE_HOOK.isAlive();
        } else {
            System.out.println("no, i am dead");
        }
    }
}
