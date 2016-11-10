# Algorithm of garbage collection in java
There are several garbage collectors in java. If your program is small you do not need change the default collector
in virtual machine. But if your program become bigger and bigger you need think about change the JVM arguments or even change 
the collector of virtual machine to make it run efficiently. In this blog I will introduce the algorithm of them. 
In general, there are 4 algorithms to garbage collection. 

**Mark-Sweep**

As the name shows, this algorithm does two things to collect garbage. The first thing is marking which objects need to
be collected. The second thing is collecting these objects. There are 2 problems about this algorithm. "Mark" and "Sweep"
are both inefficient. There are a lot of discontinuous memory fragmentations after marking and sweeping. If virtual
machine needs large memory size it has to collect garbage again.

**Copying**

In order to resolve the issue of efficiency another algorithms called "Copying" appeared. It divides memory into 
two same size zones. When the virtual machine is collecting garbage it copies surviving objects to another zone and 
remove the remaining objects. Virtual Machine will not need to think about the problem of discontinuous fragments. 
The cost of this algorithm is "waste" half of memory.

**Mark-Compact**

It is inefficient to use "Copying" algorithms collecting the objects of high survival rate. There are a lot of objects
need to be copied. The more important is "Copying" algorithms have to waste half of memory. Another algorithm is "Mark-Compact".
Like "Mark-Sweep", first step is marking then move surviving object to one end of memory, at last remove the garbage.

**Generational Collection**

In this algorithm, virtual machine divides the memory into several generations and use different algorithms mentioned above to collect garbage.
There are lots of objects created in young generation and most of them soon become unreachable. "Copying" algorithm 
is the most suitable way to collect garbage in young generation. The objects of old generation are survived from the 
young generation. The life cycle of these objects is long. So virtual machine uses "Mark-Sweep" or "Mark-Compact" to 
collect garbage in old generation.
 