<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="js/jquery.js"></script>
    <%--声明js代码域--%>
    <script type="text/javascript">
        /****************资源上传功能实现**********************************/
        $(function () {
            //给图片按钮绑定值改变事件
            $("#filebtn").change(function () {
                //获取要上传的文件资源
                var file=$("#filebtn")[0].files[0];
                //创建FormData对象存储要上传的资源(文件上传数据的载体)
                var formData = new FormData();
                formData.append("photo",file);
                //发起ajax请求完成文件上传
                $.ajax({
                    /*
                    在文件上传时文件和其他请求参数是在请求体中进行传递，
                    所以不支持GET类型请求，要使用POST类型请求。
                    GET请求方式提交的数据量大小有限制，
                    如果文件过大肯定不能用GET方式。
                     */
                    type:"post",//使用post类型的请求
                    data:formData,//请求数据
                    url:"upload4",//请求地址
                    /*
                    如果该页面文件上传使用的是非ajax方式，那么必须在form表单中加入：
                    enctype="multipart/form-data"
                    加入该属性代表：表单提交的内容转为二进制方式
                    因为enctype默认值="application/x-www-form-urlencoded"，代表普通文本
                    姓名、年龄可以按照普通文本，但是文件不可以呀！
                    所以需要将表单提交的内容转为二进制方式
                    必须使用
                    enctype="multipart/form-data"

                    现在我们使用的是ajax方式，需要设置processData、contentType为false
                    ① 将processData属性的值设置为false，告诉浏览器发送对象请求数据
                    ② 将contentType属性的值设置为false，设置请求数据的类型为二进制类型。
                    所以enctype="multipart/form-data"等价于processData:false+contentType:false
                     */
                    processData:false,
                    contentType:false,
                    success:function (data) {//回调函数
                        console.log(data)
                        if (data.msg == 1){
                            alert("上传成功")
                        }
                        $("#img_span").html("<img src='images/" + data.filename + "' width = 60px/>")

                        /*新增回显处理*/
                        $("#filename").val(data.filename);
                        $("#filetype").val(data.filetype);
                    }
                })
            })
        })
    </script>
</head>
<body>
<h1>用户账户注册</h1>
<form action="login" method="post" enctype="multipart/form-data">
    <p>
        姓名：<input type="text" name="name"/>
    </p>
    <p>
        年龄：<input type="text" name="age"/>
    </p>
    <p>
        生日：<input type="date" name="birthdate"/>
    </p>
    <p>
        头像：<input type="file" name="photo" id="filebtn"/>
        <span id="img_span"></span>
    </p>
    <p>
        <%--新增：隐藏数据的具体数值，在ajax请求文件回显的时候处理--%>
        <input type="hidden" name="filename" id="filename" >
        <input type="hidden" name="filetype" id="filetype" >

        <input type="submit" value="提交"/>
    </p>
</form>
</body>
</html>
