<%@ page import="java.util.Map" %>

<html>
<head>

    <title>服务器线程信息</title>

</head>
<body>

<pre>
    <%
        for(Map.Entry<Thread, StackTraceElement[]> stackTrace : Thread.getAllStackTraces().entrySet()){
            Thread t = (Thread)stackTrace.getKey();
            StackTraceElement[] stack = (StackTraceElement[])stackTrace.getValue();
            if(t.equals(Thread.currentThread())) {
                continue;
            }
            out.print("\n线程：" + t.getName() + "\n");
            for(StackTraceElement ele: stack){
                out.print("\t" + ele + "\n");
            }


        }

    %>

</pre>
</body>
</html>