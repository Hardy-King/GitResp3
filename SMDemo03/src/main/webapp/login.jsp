<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2026/1/27
  Time: 11:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title>i18n</title>
</head>
<body>
<form action="" method="post">
  <spring:message code="gk.username"></spring:message> <input type="text" name="username"/><br/>
  <spring:message code="gk.password"></spring:message> <input type="text" name="password"/><br/>
  <input type="submit" value="<spring:message code="gk.submit"></spring:message>"/>
</form>
</body>
</html>
