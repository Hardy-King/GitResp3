<%--
  Created by IntelliJ IDEA.
  User: zhaoss-msb
  Date: 2023/8/2
  Time: 12:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="js/jquery.js" ></script>
    <script type="text/javascript">
        $(function (){
            $("#btn").click(function (){
                $.ajax({
                    url:"demo101",
                    contentType:"application/json",
                    data:'{"id":1,"name":"张三"}',  //json对象和json字符串是不一样的，json字符串需要配置：1.contentType:"application/json" 2.@RequestBody
                    type:"post",
                    success:function (data) {
                        console.log(data);
                    }
                });
            })
        });
    </script>
</head>
<body>
<button type="button" id="btn" >ajax请求</button>
</body>
</html>