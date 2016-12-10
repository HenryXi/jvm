# Solve OutOfMemoryError UncaughtExceptionHandler 
In this page I will show you how to solve `Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread`.
This error means there is not enough memory to run your application. What you need to do is increasing memory. The
key question is there are a lot of arguments for java which is right?

**short answer**

Add arguments like following. The value of `PermSize` you can tune by your own environment.
```ini
-XX:PermSize=60M -XX:MaxPermSize=512M
```
   
**long answer**

You've seen this error before: `java.lang.OutOfMemoryError: PermGen space` and you know how to solve it. Just increase
the value of `PermSize`. I write a simple example to help me understand this error.
```java
/**
 * -XX:PermSize=10M -XX:MaxPermSize=10M
 */
public class JavaMethodAreaOOM {
    static class OOMObject {

    }

    public static void main(String[] args) {
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(OOMObject.class);
            enhancer.setUseCache(false);
            enhancer.setCallback(new MethodInterceptor() {
                @Override
                public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                    return methodProxy.invoke(o, objects);
                }
            });
            enhancer.create();
        }
    }
}
```
Use cglib to generate class you need add the dependence, the pom file like following.
```xml
<dependency>
    <groupId>cglib</groupId>
    <artifactId>cglib</artifactId>
    <version>2.2.2</version>
</dependency>
```
Add the arguments `-XX:PermSize=10M -XX:MaxPermSize=10M` before run this example. I run this example in different jdk and
get different result.

* jdk6
```    
    Exception in thread "main" net.sf.cglib.core.CodeGenerationException: java.lang.reflect.InvocationTargetException-->null
        at net.sf.cglib.core.AbstractClassGenerator.create(AbstractClassGenerator.java:237)
        at net.sf.cglib.proxy.Enhancer.createHelper(Enhancer.java:377)
        at net.sf.cglib.proxy.Enhancer.create(Enhancer.java:285)
        at com.henryxi.jvm.JavaMethodAreaOOM.main(JavaMethodAreaOOM.java:33)
        at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at com.intellij.rt.execution.application.AppMain.main(AppMain.java:144)
    Caused by: java.lang.reflect.InvocationTargetException
        at sun.reflect.GeneratedMethodAccessor1.invoke(Unknown Source)
        at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
        at java.lang.reflect.Method.invoke(Method.java:597)
        at net.sf.cglib.core.ReflectUtils.defineClass(ReflectUtils.java:384)
        at net.sf.cglib.core.AbstractClassGenerator.create(AbstractClassGenerator.java:219)
        ... 8 more
    Caused by: java.lang.OutOfMemoryError: PermGen space
        at java.lang.ClassLoader.defineClass1(Native Method)
        at java.lang.ClassLoader.defineClassCond(ClassLoader.java:631)
        at java.lang.ClassLoader.defineClass(ClassLoader.java:615)
        ... 13 more
```
* jdk7 
```
    Exception in thread "main" 
    Exception: java.lang.OutOfMemoryError thrown from the UncaughtExceptionHandler in thread "main"
```
* jdk8
    There no exception thrown.

After reading this blog [About G1 Garbage Collector, Permanent Generation and Metaspace](https://blogs.oracle.com/poonam/entry/about_g1_garbage_collector_permanent) I
understand. `PermGen` supported in jdk6 and jdk7 but not supported in jdk8. But in jdk7 when `PermGen` is not enough it
won't throw "OutOfMemoryError: PermGen space" it will throw "OutOfMemoryError thrown from the UncaughtExceptionHandler".
`Hotspot` is giving up "Permanent Generation" step by step it use "Metaspace" instead. You can learn the detail in above link. 
