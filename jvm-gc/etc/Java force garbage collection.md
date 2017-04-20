# Java force garbage collection
As we all know, JVM will do garbage collection when there is not enough memory to use. You can use `System.gc()` in
your code to trigger garbage collection. But when JVM does this is not uncertainty. 

For full GC, you can use `jmap -histo:live <pid>` to trigger. This command is used to show how many alive object in memory.
Before you can see the output JVM will do full GC to make sure all object in JVM is alive. It is not the straightforward way
 to trigger GC but it works.
 
**Example**

Use `jstat -gcutil 122329 2000` to monitor the GC. In my environment, the output is like following.
```
> jstat -gcutil 122329 2000
  S0     S1     E      O      P     YGC     YGCT    FGC    FGCT     GCT   
 10.70   0.00  41.06  34.28  61.30    924    8.625     0    0.000    8.625
 10.70   0.00  59.00  34.28  61.30    924    8.625     0    0.000    8.625
 10.70   0.00  78.74  34.28  61.30    924    8.625     0    0.000    8.625
 10.70   0.00  99.13  34.28  61.30    924    8.625     0    0.000    8.625
  0.00  17.39  15.74  34.29  61.30    925    8.634     0    0.000    8.634 <-- execute "jmap -histo:live 122329 > /tmp/testFullGC.txt"
  0.00  17.39  30.95  34.29  61.30    925    8.634     0    0.000    8.634
  0.00   0.00   6.07   6.23  60.97    926    8.644     1    0.453    9.097
  0.00   0.00  23.72   6.23  60.97    926    8.644     1    0.453    9.097
  0.00   0.00  43.61   6.23  60.97    926    8.644     1    0.453    9.097
  0.00   0.00  59.87   6.23  60.97    926    8.644     1    0.453    9.097
  0.00   0.00  79.21   6.23  60.97    926    8.644     1    0.453    9.097
  0.00   0.00  95.96   6.23  60.97    926    8.644     1    0.453    9.097
```

Use `jmap -histo:live 122329 > /tmp/testFullGC.txt` to force JVM do full GC. In the line 5, JVM do a full GC.