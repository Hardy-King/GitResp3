<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2026/1/22
  Time: 20:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="js/jquery.js"></script>
    <script type="text/javascript">
        $(function () {
            alert("a")
            // 发送ajax请求
            $.ajax(
                {
                    type:"post",
                    url:"showUsers",
                    success:function(result){
                        $.each(result,function(i,e){// 对响应内容遍历
                            // 遍历的结果放入tbody中：
                            $("#tb").append("<tr>\n"+
                                " <th>"+e.id+"</th>\n"+
                                " <th>"+e.name+"</th>\n"+
                                " <th>"+e.age+"</th>\n"+
                                "<th>"+e.birthdate+"</th>\n"+
                                "<th>"+e.filename+"</th>\n"+
                                "<th>"+e.filetype+"</th>\n"+
                                "<th><img src='images/" + e.filename + "' width='60px'/></th>\n"+
                                "<th><a href='download?filename="+e.filename+"&filetype="+e.filetype+"'>下载</a></th>\n"+
                                "</tr>")// 新增：下载超链接的处理
                        })
                    }
                }
            )
        })
    </script>
</head>
<body>
<%--
定义一个表来展示数据
table>tr>th*8 点击tab按键就可以显示8行
--%>
<table>
    <tr>
        <th>用户编号</th>
        <th>名字</th>
        <th>年龄</th>
        <th>出生日期</th>
        <th>文件名字</th>
        <th>文件类型</th>
        <th>图片</th>
        <th>操作</th>
    </tr>
    <tbody id="tb"></tbody>
</table>



</body>
</html>
