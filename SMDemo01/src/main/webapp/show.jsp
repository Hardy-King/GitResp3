<%--
  Created by IntelliJ IDEA.
  User: zhaoss-msb
  Date: 2023/7/26
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form  action="demo02">
    <p>
        名字：<input type="text" name="name"/>
    </p>
    <p>
        年龄：<input type="text" name="age"/>
    </p>
    <p>
        密码：<input type="text" name="pwd"/>
    </p>
    <p>
        性别：
        男：<input type="radio" name="sex" value="男"/>
        女：<input type="radio" name="sex" value="女"/>
    </p>
    <p>
        爱好：
        吃：<input type="checkbox" name="hobby" value="吃"/>
        喝：<input type="checkbox" name="hobby" value="喝"/>
    </p>
    <p>
        班级：<input type="text" name="clazz.cname"/>
    </p>
    <p>
        班级编号：<input type="text" name="clazz.cid"/>
    </p>
    <p>
        日期：<input type="date" name="date"/>
    </p>
    <p>
        <input type="submit" value="提交">
    </p>
</form>
</body>
</html>