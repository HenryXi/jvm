# Linux find which Java thread consuming cpu
Sometimes Java process used 100% or 200% CPU. It depends on the core number of your CPU. If your CPU has 8 cores that means
the highest usage of a process is 800%. When Java process consuming most CPU you can use the following steps to find which 
Java threads is consuming the most CPU.

1. Use `top` command to find which Java process use most CPU and get the process id(PID).
```
top - 20:57:30 up 419 days,  8:31,  3 users,  load average: 0.13, 0.21, 0.18
Tasks: 517 total,   2 running, 515 sleeping,   0 stopped,   0 zombie
Cpu(s):  6.5%us,  4.7%sy,  0.0%ni, 88.5%id,  0.0%wa,  0.0%hi,  0.3%si,  0.0%st
Mem:  132043276k total, 129229128k used,  2814148k free,   525172k buffers
Swap: 12582904k total,        0k used, 12582904k free, 74824960k cached

   PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                              
 31139 work      20   0 31.7g 3.7g  29m S 81.5  2.9 175:29.48 java                                                  
154940 work      20   0 22.2g 5.1g  20m S 47.5  4.0   2093:09 java                                                  
196098 work      20   0 24.5g 5.4g  26m S 20.8  4.3   2691:57 java                                                  
153662 root      20   0  160m 6672 2944 R 18.1  0.0   0:00.55 python                                                
119580 work      20   0 31.7g 3.6g  20m S  8.2  2.9 593:02.50 java                                                  
 12933 work      20   0 29.4g 3.0g  28m S  7.9  2.4  26:16.11 java                                                  
 90888 work      20   0 21.6g 2.8g  24m S  4.6  2.2 348:13.82 java                                                  
102742 work      20   0 30.4g 6.7g  28m S  3.3  5.3 211:09.18 java  
```
2. Save the result of jstack.
```
jstack 31139 > /tmp/jstack31139
```

3. Use `top -H -p 31139` to show which Java thread consuming most CPU and get the PID.
```
top - 20:55:13 up 419 days,  8:29,  3 users,  load average: 0.31, 0.27, 0.20
Tasks: 4861 total,   0 running, 4861 sleeping,   0 stopped,   0 zombie
Cpu(s):  2.5%us,  1.0%sy,  0.0%ni, 96.3%id,  0.0%wa,  0.0%hi,  0.1%si,  0.0%st
Mem:  132043276k total, 129113652k used,  2929624k free,   525156k buffers
Swap: 12582904k total,        0k used, 12582904k free, 74714344k cached

   PID USER      PR  NI  VIRT  RES  SHR S %CPU %MEM    TIME+  COMMAND                                              
 31159 work      20   0 31.7g 3.7g  29m S  5.1  2.9   0:23.24 java                                                  
 31163 work      20   0 31.7g 3.7g  29m S  5.1  2.9   0:22.99 java                                                  
 31151 work      20   0 31.7g 3.7g  29m S  3.4  2.9   0:23.19 java                                                  
 31155 work      20   0 31.7g 3.7g  29m S  3.4  2.9   0:23.43 java                                                  
 31156 work      20   0 31.7g 3.7g  29m S  3.4  2.9   0:23.25 java                                                  
 31157 work      20   0 31.7g 3.7g  29m S  3.4  2.9   0:23.44 java                                                  
 31158 work      20   0 31.7g 3.7g  29m S  3.4  2.9   0:23.16 java                                                  
 31166 work      20   0 31.7g 3.7g  29m S  3.4  2.9   0:23.19 java                                                  
 31169 work      20   0 31.7g 3.7g  29m S  3.4  2.9   0:23.35 java                                                  
```

4. Get the hexadecimal of thread id in last step.
```
printf 0x%x 32036 # will get 0x7d24
```

5. Use `vi` to find thread id(0x7d24) in jstack file.
```
"epollEventLoopGroup-3-1" #96 prio=10 os_prio=0 tid=0x00007f04c000d000 nid=0x7d25 runnable [0x00007f02f8f25000]
   java.lang.Thread.State: RUNNABLE
    at io.netty.channel.epoll.Native.epollWait0(Native Method)
    at io.netty.channel.epoll.Native.epollWait(Native.java:93)
    at io.netty.channel.epoll.EpollEventLoop.epollWait(EpollEventLoop.java:185)
    at io.netty.channel.epoll.EpollEventLoop.run(EpollEventLoop.java:210)
    at io.netty.util.concurrent.SingleThreadEventExecutor$2.run(SingleThreadEventExecutor.java:112)
    at io.netty.util.concurrent.DefaultThreadFactory$DefaultRunnableDecorator.run(DefaultThreadFactory.java:137)
    at java.lang.Thread.run(Thread.java:745)

"pool-2-thread-1" #274 prio=5 os_prio=0 tid=0x00007f042c128000 nid=0x7d24 waiting on condition [0x00007f02f8fa6000]
   java.lang.Thread.State: WAITING (parking)
    at sun.misc.Unsafe.park(Native Method)
    - parking to wait for  <0x00000004878b1498> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
    at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
    at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
    at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:1081)
    at java.util.concurrent.ScheduledThreadPoolExecutor$DelayedWorkQueue.take(ScheduledThreadPoolExecutor.java:809)
    at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1067)
    at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1127)
    at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:617)
    at java.lang.Thread.run(Thread.java:745)
```

**Summary**

find high CPU process id -> get jstack output file -> get high thread id -> get hexadecimal nid(thread id) -> find java code in jstack output file


EOF 
