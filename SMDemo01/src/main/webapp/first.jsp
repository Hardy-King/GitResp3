<%@page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<br>
<h2>Hello World!</h2>
<h3>first跳转页面</h3>

request:${requestScope.get("reqmsg")}
session:${sessionScope.get("seqmsg")}
application:${applicationScope.get("appmsg")}
</body>
</html>
