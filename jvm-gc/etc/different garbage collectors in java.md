# Different garbage collectors in java
In this page we will go through different garbage collectors in java. I have introduced the algorithms of garbage
collection in [last](http://www.henryxi.com/algorithm-of-garbage-collection-in-java) blog. There are 7 garbage
collections in hotspot virtual machine. 

**Serial**

It uses only one thread to collect garbage. When GC thread is collecting the garbage, all threads will be stopped to 
wait for it to complete. Freezing all thread for garbage collection, we call it "stop-the-world". It use "copying" 
algorithm to collect the garbage in young generation. Single thread collector means it won't waste time for threads 
interactions; therefore it is suited for single CPU environment. It is not the default collector generally, use 
`-XX:+UseSerialGC` to enable this collector.

**ParNew**

It is generally the same with "Serial" collector except using multiple threads to collect garbage. It is the only
one collector can work with "CMS" collector. (Described below) "ParNew" is the preferred collector for young generation, as multiple CPUs
becoming much more common. 

**Parallel Scavenge**

It is also a multiple threads collector for young generation. The different between "Parallel Scavenge" and "ParNew" is what they focus on.
"Parallel Scavenge" focuses on making "throughput" controllable. (Throughput = (total time - GC time)/total time)
Using `-XX:MaxGCPauseMillis` or `-XX:GCTimeRatio` can exactly control the throughput.

**Serial Old**

It is a single thread collector, use "Mark-Compact" algorithm to collect garbage in old generation. We do not use it 
as default collector, as multiple CPUs becoming much more common. 

**Parallel Old**

It use "Mark-Compact" algorithm to collect garbage. It can be used with "Parallel Scavenge" to control "throughput" in
both young and old generations.

**CMS**

This collector aims to reduce the pause time. "CMS" means "Concurrent-Mark-Sweep", therefore it uses multiple threads
to collect garbage in old generation. There are 5 phases in "CMS", "Initial Mark", "Concurrent Marking", "Remark", "Concurrent Sweep" and
"Resetting". The only time it "stop-the-world" is during "Initial Mark" and "Remark". When it collecting the garbage
GC threads and user threads are both running, therefore the pause time is low.

**G1**

"G1" is different from the collectors above; it splits memory into many fixed sized regions. These regions act as 
Eden, Survivor or Old Generation in different time. During young GC live objects copied to one or more survivor regions.
If the age of these objects reaches the aging threshold they will be copied to old generation regions. For old generation,
JVM will calculate which region is most need to be collected. "Most need" means use the least time to collect the most
memory size. G1 will remove empty old generation regions firstly, then find the "most need" region and collect them.
In this phase both young and old generation are collected. For more detail you can click [here](http://www.oracle.com/technetwork/tutorials/tutorials-1876574.html). 

**SUMMARY**

For young generation, using "Copying" algorithm (Serial, ParNew, Parallel Scavenge) is the best choice. For old generation, 
use "Mark-Sweep" or "Mark-Compact" algorithm to reduce pause time. G1 collector is little different from other collector, 
but collect garbage base on generation do not change. 