# Generate OutOfMemoryError java heap space
`OutOfMemoryError` means the memory is not enough for your program. After this error Java virtual machine will tell
you the detail. When you want solve this problem, you should first of all get to understand it. In this page I will 
show you how to generate `OutOfMemoryError: Java heap space` programatically. 

**HeapOOM**
```
public class HeapOOM {
    static class OOMObject {

    }

    public static void main(String[] args) {
        List<OOMObject> list = new ArrayList<>();
        while (true) {
            list.add(new OOMObject());
        }
    }
}
```
Before run above simple code we need add arguments like following. These argument will help you generate `OutOfMemoryError`
quick. 
```
-Xms20m -Xmx20m
```
The output like following
```
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at java.util.Arrays.copyOf(Arrays.java:2245)
	at java.util.Arrays.copyOf(Arrays.java:2219)
	at java.util.ArrayList.grow(ArrayList.java:242)
	at java.util.ArrayList.ensureExplicitCapacity(ArrayList.java:216)
	at java.util.ArrayList.ensureCapacityInternal(ArrayList.java:208)
	at java.util.ArrayList.add(ArrayList.java:440)
	at com.henryxi.jvm.HeapOOM.main(HeapOOM.java:17)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:57)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:606)
	at com.intellij.rt.execution.application.AppMain.main(AppMain.java:144)
```
**Summary**

`OutOfMemoryError: Java heap space` means there is no more memory in heap to create new instance. In order to generate
this error quickly we add argument `-Xms20m -Xmx20m`. The first argument means the min memory to run this program is
20m. The second argument means the max memory to run this program is 20m. The result of using both of them is run this
program with 20m without increasing. If you meet this error in your program there are two ways to solve it.

* check your code to avoid instantiating so many object.
* increase the value of `-Xms -Xmx`