# Java heap dump example
We use `jmap` command to dump the Java heap. Examples are here.

**dump all objects in heap**
```bash
jmap -dump:format=b,file=/tmp/dump_file.hprof <pid> 
```


**dump live objects in heap**

If you want only dump live objects, use following command.
```bash
jmap -dump:live,format=b,file=/tmp/dump_file.hprof <pid>
```

EOF
