# Spring MVC第一天

## 内容概述

<img src="01-SpringMVC第一天.assets/Servlet-78.png" style="zoom:67%;" />



## Spring MVC的引入

### 现有程序问题

目前我们的程序中分为：mapper层（持久层）、service层（业务层），controller层（控制层）。

目前我们所写的项目，持久层使用mybatis后已经够简单了，并且持久层和业务层的类都放入到Spring容器之中进行管理了，他们之间需要注入非常方便，只需要通过@Autowired注解即可。所以现在遗留的问题就剩controller层了，之前的controller层我们是利用servlet完成的。

**那使用servlet完成的controller层存在哪些问题呢？**

观察如下代码：

![](01-SpringMVC第一天.assets/servlet1.png)

问题1：DeptService这个业务层对象，无法通过@Autowire注解注入进来，因为使用@Autowire注解注入的前提需要构建ShowDeptServlet对象，但是该对象是我们创建的吗？Servlet整个生命周期都是被Tomcat进行管理的，无法把Servlet放入到Spring容器中。所以每次在编写Servlet时都需要编写init方法先获取到Spring容器，然后从Spring容器中取出需要使用的Bean。

问题2：用户的每一个请求都对应一个Servlet，那如果用户发来十个请求就要对应十个Servlet，所以当时学习servlet的时候，我们可以去做一些简化，让每次请求都走入一个servlet，然后在service方法中进行判断：（此方式属于Front设计模式）

```java
@WebServlet("/msb/StudentServlet")
public class StudentServlet extends HttpServlet {
    private StudentService studentService = new StudentServiceImpl();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if("saveStu".equals(method)){
            savaStu(req,resp);
        }else if ("findAllStus".equals(method)){
            findAllStus(req,resp);
        }else if ("findStusByPoi".equals(method)){
            findStusByPoi(req,resp);
        }
    }
    protected void savaStu(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //省略具体逻辑代码.....
    }

    protected void findAllStus(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //省略具体逻辑代码.....
    }

    protected void findStusByPoi(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //省略具体逻辑代码.....
    }

}

```

Front（前端）设计模式就是有一个前端（不是前端专业那个前端，是最前面的意思）统一入口，在统一入口根据请求url调用自己的编写的普通方法。

<img src="01-SpringMVC第一天.assets/servlet2.png" style="zoom:67%;" />



上面的方法虽然简化了，但是如果你再写另一业务TeacherServlet，那么还要继续在service中加判断，太麻烦了！

问题3：在servlet中参数的接收非常麻烦，前台的每个参数后台都需要先利用req.getParameter("XXX")接收，如果前台传过来十个参数，后台就要调用这个方法十次。并且参数类型不对还要自己进行类型的转化，转化后还要将参数存入具体对象中。

### Sping MVC的介绍

上述问题，我们可以使用一个框架来帮我们解决：Spring MVC框架。也有人使用structs2或者jfinal来解决，但是现在公司中使用最多的是Spring MVC了。

Spring MVC虽然在平时认为是一个独立的框架。但其本质为Spring 框架的一个扩展，Spring MVC在Spring官方的Projects里面顶级项目并没有，可以认为Spring MVC属于Spring Framework的二级子项目。因为它本身属于Spring，所以与Spring的整合肯定是更贴切更好的。之前图中可以看出，web部分就是mvc部分：

<img src="01-SpringMVC第一天.assets/servlet3.png" style="zoom:67%;" />

Spring MVC也是基于Front设计模式，总体效果和上面我们自己写的Front结果类似，但本身作为一个框架，肯定要比我们写的代码复杂很多，功能也强大很多。Spring MVC把很多东西都封装到前端入口DispatcherServlet中了，Spring MVC就是对servlet的封装，底层仍然是servlet，只是把全部功能都封装到一个servlet中了，但是我们使用的时候是看不到servlet影子的。DispatcherServlet里面编写了请求分发功能，既然是servlet，就需要我们在web.xml手动编写`<servlet>`的配置。

Spring MVC既然敢称自己是一个MVC框架，这个框架必定在MVC三层中都有自己的功能。例如：

M：在模型层包含：数据校验

V：在视图层包含：国际化、标签库

C：在控制层包含的功能就更多了：转发重定向、参数、拦截器、作用域等

MyController在Spring MVC称为控制器类（Handler），里面的方法称为：控制单元（HandlerMethod）

在Spring MVC框架中，最主要是在学习如何编写自定义的控制器类。

![](01-SpringMVC第一天.assets/servlet4.png)

### Spring中的父子容器问题

因为Spring MVC属于Spring的子框架，所以Spring MVC中可以使用Spring框架的全部内容。

Spring 官方为Spring MVC专门定义了一个容器，这个容器里面放Spring MVC中全部Bean。且这个容器属于Spring容器的子容器。

有这样的一个规定：Spring MVC子容器可以调用Spring 父容器的全部内容。但是Spring父容器不能调用Spring MVC子容器内容。

![](01-SpringMVC第一天.assets/servlet5.png)

## Spring MVC环境搭建

之前的spring和mybatis项目都不需要使用tomcat容器，不是必须放入tomcat才能被运行的，但是由于springmvc底层是对servlet的封装，servlet的运行是必须要用tomcat的，所以springmvc项目必须使用tomcat容器来运行。所以构建项目就要创建war项目了。



案例：实现一个小功能，实现一个控制单元即可，在浏览器访问。



### 创建项目

（1）先创建最外层空项目：

<img src="01-SpringMVC第一天.assets/servlet6.png" style="zoom:67%;" />

（2）创建新Module，使用原型创建web项目

<img src="01-SpringMVC第一天.assets/servlet7.png" style="zoom:67%;" />

（3）补全`java`目录： 

<img src="01-SpringMVC第一天.assets/servlet8.png" style="zoom:67%;" />

### 添加依赖

在pom.xml中添加Spring MVC的依赖。

Spring MVC 在平时随意可以当成一个独立框架看待，但其本质只是Spring Framework中的spring-webmvc.jar文件（算是Spring Framework衍生出来的二级框架），这个jar文件依赖了spring web模块。所以在只使用Spring MVC框架时需要导入spring-webmvc依赖即可。导入spring-webmvc的依赖，核心功能的5个依赖以及spring-web依赖就都过来了：

<img src="01-SpringMVC第一天.assets/servlet9.png" style="zoom:67%;" />

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.msb</groupId>
  <artifactId>SpringMVCDemo01</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>SpringMVCDemo01 Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <!-- 依赖了Spring框架核心功能的5个依赖以及Spring整合Web的依赖spring-web -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>6.0.11</version>
    </dependency>

  </dependencies>
  <build>
    <finalName>SpringMVCDemo01</finalName>
  </build>
</project>

```

### 创建跳转页面

<img src="01-SpringMVC第一天.assets/servlet10.png" style="zoom:67%;" />

### 创建控制器类

新建com.msb.controller.FirstController。

```java
package com.msb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @Author: zhaoss
 */
@Controller// 放入到Spring MVC容器中
public class FirstController {
    /*
     * 请求转发的简单写法（平时使用的方式）
     * 返回值是String，表示跳转的资源路径
     */
    @RequestMapping("/first2")
    public String test2(){
        return "/first.jsp";//    请求转发到first.jsp中    / ： 根路径
    }   
}
```

### 创建Spring MVC配置文件

在src/main/resources中新建Spring MVC框架配置文件springmvc.xml。这个文件的名称是随意的，只要和web.xml中配置param-value对应上就可以。

里面的约束和之前约束一样，你把之前applicationContext.xml中约束拿过来就行。

我们在控制层配置的注解啊

@Controller  这个是org.springframework的注解  -》使用context:component-scan解析

@RequestMapping 这个是org.springframework.web的注解 -》通过mvc:annotation-driven让Spring MVC的注解生效

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
		https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 扫描控制器类，千万不要把service等扫描进来，也千万不要在Spring配置文件扫描控制器类所在包 -->
    <context:component-scan base-package="com.msb.controller"></context:component-scan>
    <!-- 让Spring MVC的注解生效 ：@RequestMapping，这个位置别忘记加入mvc的命名空间-->
    <mvc:annotation-driven></mvc:annotation-driven>
</beans>
```

### 编写web.xml内容

那你说springmvc.xml要不要被解析？肯定要啊！不解析怎么扫描，不扫描注解怎么识别？

在入口解析（在网页访问后，首先会访问Servlet的配置信息 web.xml），在web.xml中配置：

（1）把web.xml的模板先粘贴到web.xml中：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
</web-app>
```

（2）SpringMVC的底层是servlet，把Servlet给我们封装了，程序入口DispatcherServlet还是需要我们配置一下的。

web.xml的配置是为了让前端控制器DispatcherServlet生效。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <!-- /表示除了.jsp以外的请求都可以访问DispatcherServlet，但是.jsp的请求不可以访问-->
        <!-- /表示除了.jsp结尾的uri，其他的uri都会触发DispatcherServlet，都会被拦截。此处不要写成 /* -->
        <url-pattern>/</url-pattern>
    </servlet-mapping>
</web-app>
```

<img src="01-SpringMVC第一天.assets/servlet19.png" style="zoom:67%;" />

![](01-SpringMVC第一天.assets/servlet11.png)



DispatcherServlet是springmvc的入口，springmvc.xml的解析，是通过DispatcherServlet的一个参数,把xml名字给这个参数即可加载Spring MVC的配置文件。

![servlet12](01-SpringMVC第一天.assets/servlet12.png)



将参数配置：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
  <servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <!-- 参数名称必须叫做：contextConfigLocation。单词和大小写错误都导致配置文件无法正确加载 -->
      <param-name>contextConfigLocation</param-name>
      <!-- springmvc.xml 名称自定义，只要和我们创建的配置文件的名称对应就可以了。 -->
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
  </servlet>
  <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <!-- /表示除了.jsp以外的请求都可以访问DispatcherServlet，如：first、first2，但是.jsp的请求不可以访问-->
    <!-- /表示除了.jsp结尾的uri，其他的uri都会触发DispatcherServlet，都会被拦截。此处不要写成 /* -->
    <url-pattern>/</url-pattern>
  </servlet-mapping>
</web-app>
```

加入配置load-on-startup：让在启动服务器的时候就加载springmvc.xml:

其实不写也行，不写的话就会在第一次访问的时候加载springmvc.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
  <servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <!-- 参数名称必须叫做：contextConfigLocation。单词和大小写错误都导致配置文件无法正确加载 -->
      <param-name>contextConfigLocation</param-name>
      <!-- springmvc.xml 名称自定义，只要和我们创建的配置文件的名称对应就可以了。 -->
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
    <!-- Tomcat启动立即加载Servlet，而不是等到访问Servlet才去实例化DispatcherServlet -->
    <!-- 配置上的效果：Tomcat启动立即加载Spring MVC框架的配置文件-->
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <!-- /表示除了.jsp以外的请求都可以访问DispatcherServlet，但是.jsp的请求不可以访问-->
    <!-- /表示除了.jsp结尾的uri，其他的uri都会触发DispatcherServlet，都会被拦截。此处不要写成 /* -->
    <url-pattern>/</url-pattern>
  </servlet-mapping>
</web-app>
```

PS：<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class> 报红线错误，编译报错不用管，运行的时候不会报错，因为编译的时候需要你程序中有DispatcherServlet，那么就需要加入：

**tomcat10之后[servlet](https://so.csdn.net/so/search?q=servlet&spm=1001.2101.3001.7020)依赖包名不是javax.servlet，而是jakarta.servlet**。可以用以下两个依赖

```xml
    <dependency>
      <groupId>jakarta.servlet.jsp</groupId>
      <artifactId>jakarta.servlet.jsp-api</artifactId>
      <version>3.0.0</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>jakarta.servlet</groupId>
      <artifactId>jakarta.servlet-api</artifactId>
      <version>5.0.0</version>
      <scope>provided</scope>
    </dependency>
```



如果你懒得加，那么就让他报错就行了，反正对运行没有影响。

上面的配置只对编译有效，对运行无效，因为运行的时候tomcat中是有servlet和jsp的。

### 配置tomcat

<img src="01-SpringMVC第一天.assets/servlet15.png" style="zoom:67%;" />

<img src="01-SpringMVC第一天.assets/servlet16.png" alt="servlet16" style="zoom:67%;" />

### 启动项目

<img src="01-SpringMVC第一天.assets/servlet17.png" style="zoom:67%;" />

### 访问控制单元

访问我们要访问的控制单元：

在浏览器输入：http://localhost:8080/demo01/first2 和http://localhost:8080/demo01/first 如果都能转发到first.jsp页面说明整体环境搭建成功。

<img src="01-SpringMVC第一天.assets/servlet18.png" style="zoom:67%;" />

### 总结流程

http://localhost:8080/demo01/first2  --》先找web.xml---》/first2找到拦截地址是/ 那么可以走DispatcherServlet -》并把springmvc.xml解析-》扫描springmvc.xml中的包、注解-》找RequestMapping 中是first2 的控制单元-》执行方法test2()，若有参数接收参数 -》执行方法中逻辑, 请求转发到first.jsp中



现在用了springmvc的好处：

（1）作用和servlet作用一样，但是又和servlet解耦了，你看代码看不出来servlet。

（2）对象已经交给容器了，那么比如用到service对象就可以注入进来了。

## 映射路径

### 映射路径介绍

**（1）映射路径是什么**

映射路径在之前Java EE阶段中学习过，就是web.xml中`<url-pattern>`的值或者@WebServlet("")注解的值。

映射路径无论是在Servlet中还是在Spring MVC中，都表示：当URL中出现指定路径时会执行Servlet的方法或执行Spring MVC的控制单元。

例如下面代码：

```java
    @RequestMapping("/first2")
    public String test2(){
        return "/first.jsp";//    请求转发到first.jsp中    / ： 根路径
    } 
```

只要访问URL中 http://localhost:8080/demo01/first2 中 URI 是/demo01/first2 ，其中/demo01 表示当前项目的名称，/first2表示映射路径。Spring MVC 发现映射路径是/first2 时就会执行上面的控制单元。

由于在Servlet中url-pattern必须以 / 开头，所以在Spring MVC中定义映射路径时也是习惯以 / 开头，但是这个/可以省略不写加不加都行，但是我们一般都加上。



**（2）一个控制单元可以对应多个路径：**

@RequestMapping注解中发现：

<img src="01-SpringMVC第一天.assets/servlet20.png" style="zoom:67%;" />

所以我们可以加入数组格式的路径，如：

```java
    @RequestMapping(value={"/first2","/first02"})
    public String test2(){
        return "/first.jsp";
    }
```

那么用户访问： http://localhost:8080/demo01/first2也可以， http://localhost:8080/demo01/first02 也可以



 **（3）不同控制单元路径不可重复：**

```java
@Controller
public class MyController {
    @RequestMapping("/test01")
    public String test01(){
        return "/index.jsp";
    }
}
```

```java
@Controller
public class MyController2 {
    @RequestMapping("/test01")
    public String test01(){
        return "/index.jsp";
    }
}
```

此时，就建议在当前类上也加入路径：

```java
@Controller
@RequestMapping("/MyController")
public class MyController {
    @RequestMapping(value={"/test01","/test1"})
    public String test01(){
        return "/index.jsp";
    }
}
```

```java
@Controller
@RequestMapping("/MyController2")
public class MyController2 {
    @RequestMapping(value={"/test01","/test1"})
    public String test01(){
        return "/index.jsp";
    }
}
```

那么访问就变成：http://localhost:8080/msb/demo01/MyController2/test01

 

### 多级路径

映射路径支持路径的写法。

在计算机中路径都是以\进行分隔，例如在我的电脑中的路径:

<img src="01-SpringMVC第一天.assets/servlet21.png" style="zoom:67%;" />

但是在Java中 \ 表示转义字符，所以在Java中路径是使用 / 进行分隔，表示目录层次。

在Spring MVC 的映射路径也支持多层写法，例如下面的代码表示URL为 http://localhost:8080/msb/demo01/test/test2 时执行这个控制单元。

```java
    @RequestMapping("/test/test2")
    public String test2(){
        return "/first.jsp";
    }
```

### 多级路径中注意事项

在下面的代码中，当使用浏览器访问/test/test2时会出现404

```java
    @RequestMapping("/test/test3")
    public String test3(){
        return "first.jsp"; // 注意这里没有使用/
    }
```

请求http://localhost:8080/demo01/test/test3报错：

<img src="01-SpringMVC第一天.assets/servlet22.png" style="zoom:67%;" />

上面的404，并不是控制单元（控制器）的404，而是没有找到first.jsp。

仔细看上面图中红框中的错误，first.jsp为什么会从/test下去寻找呢？

因为return "first.jsp";是相对路径，Tomcat会在webapp/test目录下找一个叫做first.jsp的文件。显然是不存在这样的资源，first.jsp是在webapp目录下。

**解决办法：多层路径中最优写法**

只需要在返回值中使用绝对路径就可以减少出错的情况。

跳转时 / 表示项目根目录，也就是webapp目录的根目录。

### Ant风格的映射路径

在Spring MVC中支持Ant风格的映射路径写法。所谓的Ant风格就是支持三种特殊的符号：

| 符号 | 解释                    |
| ---- | ----------------------- |
| `?`  | 匹配任意单字符          |
| `*`  | 匹配0或者任意数量的字符 |
| `**` | 匹配0或者更多数量的目录 |

解释说明：

​	使用Ant的特殊符号时，表示模糊匹配。可能出现客户端发送过来的URL能匹配上多个映射路径，这时匹配的优先级为：

​	固定值 (test1) > `?` 形式(test?) > `*`形式(`/*`) > `**`形式（/**）

```java
    // 优先级最高
    @RequestMapping("/test1")
    public String testAnt1(){
        System.out.println("testAnt1");
        return "/first.jsp";
    }
    // 优先级低于test1。总长度为6，test开头，后面跟个任意内容符号
    @RequestMapping("/test?")
    public String testAnt2(){
        System.out.println("testAnt2");
        return "/first.jsp";
    }
    // 优先级低于？。一层路径，任意内容
    @RequestMapping("/*")
    public String testAnt3(){
        System.out.println("testAnt3");
        return "/first.jsp";
    }
    // 优先级低于**。任意层路径
    @RequestMapping("/**")
    public String testAnt4(){
        System.out.println("testAnt4");
        return "/first.jsp";
    }
```

请求http://localhost:8080/demo01/test1测试。

## @RequestMapping注解

![](01-SpringMVC第一天.assets/servlet23.png)

先看元注解@Target({ElementType.TYPE, ElementType.METHOD})的含义：

代表@RequestMapping注解可以写在控制器类上，也可以写在控制单元方法上。

如果写在类上，表示当前类所有控制单元的映射路径，都以指定路径开头。

如果写在方法上，表示当前方法的映射路径。最终访问这个控制单元的映射路径为:类上@RequestMapping映射路径+方法上@RequestMapping映射路径。例如：下面的控制单元访问时的映射路径为/global/first3.

这种在类上写@RequetMapping的写法，在以后做管理类型项目、或网站后台项目中使用的比较多。平时在练习的时候绝大多数是直接在控制单元上写@RequestMapping注解，而不在类上写@RequestMapping

```java
@Controller
@RequestMapping("/global")
public class FirstController {
    @RequestMapping("/first3")
    public String test3(){
        return "/first.jsp";
    }
}
```

### 属性总览

在@RequestMapping注解中提供了很多参数

```java
public @interface RequestMapping {
    String name() default "";

    @AliasFor("path")
    String[] value() default {};

    @AliasFor("value")
    String[] path() default {};

    RequestMethod[] method() default {};

    String[] params() default {};

    String[] headers() default {};

    String[] consumes() default {};

    String[] produces() default {};
}
```

#### value属性

value：定义映射路径。URL中出现指定映射路径时会执行当前控制单元。支持一个方法多个映射路径。value属性可以省略不写，且Java的注解中，如果属性是数组类型，且取值只有一个时，{}可以省略不写。所以一共有四种写法。

但是需要注意：如果@RequestMapping只需要设置value属性的话可以省略。但是需要设置多个属性时value不能省略。

取值前面的`/`表示映射到项目根目录，可以省略不写，但是从规范上建议写上

```java
    @RequestMapping("/first3")
    public String first3(){
        return "/first.jsp";
    }
    @RequestMapping({"/first4","first5"})
    public String first4(){
        return "/first.jsp";
    }
    @RequestMapping(value="/first6")
    public String first6(){
        return "/first.jsp";
    }
    @RequestMapping(value={"/first7","first8"})
    public String first7(){
        return "/first.jsp";
    }
```

#### name属性

name：给控制单元定义一个名称。可以理解name是控制单元的注释。

```java
    @RequestMapping(value = "/testName",name = "测试下name属性")
    public String testName(){
        return "/first.jsp";
    }
```

#### path属性

path属性和value属性使用方式是相同的，都是设置控制单元的映射路径。

用法与value一致。

```java
    @RequestMapping(path = "/testPath")
    public String testPath(){
        return "first.jsp";
    }
```

#### method属性

method属性类型是RequestMethod[]，RequestMethod是枚举类型，支持HTTP协议中绝大多数请求类型。

<img src="01-SpringMVC第一天.assets/servlet24.png" style="zoom:67%;" />

当设置了method属性后，表示只有指定类型请求方式才能访问这个控制单元方法，其他的请求方式访问时，响应会出现405状态码。

```java
 // 请求方式只能是DELETE和POST类型。
    @RequestMapping(value = "/testMethod",method = {RequestMethod.DELETE,RequestMethod.POST})
    public String testMethod(){
        return "/first.jsp";
    }
```

在浏览器输入http://localhost:8080/demo01/testMethod直接回车相当于get请求，后会出现下面效果

<img src="01-SpringMVC第一天.assets/servlet25.png" style="zoom:67%;" />

##### 简写方式

Spring MVC 框架针对不同请求方式提供了5个专门请求方式的注解

| @PostMapping("/first")   | 等效于 @RequestMapping(value = "/first",method = RequestMethod.POST) |
| ------------------------ | ------------------------------------------------------------ |
| @GetMapping("/first")    | 等效于 @RequestMapping(value = "/first",method = RequestMethod.GET) |
| @DeleteMapping("/first") | 等效于 @RequestMapping(value = "/first",method = RequestMethod.DELETE) |
| @PutMapping("/first")    | 等效于 @RequestMapping(value = "/first",method = RequestMethod.PUT) |
| @PatchMapping("/first")  | 等效于 @RequestMapping(value = "/first",method = RequestMethod.PATCH) |

多个可以一起使用。

#### params属性

params属性类型是String[]，表示请求中必须包含指定名称的请求参数。

```java
    @RequestMapping(value="/testParam",params = {"name"})
    public String testParam(){
        return "/first.jsp";
    }
```

如果请求中没有包含指定类型参数，响应会出现400状态码。并且明确提示在实际的请求参数中没有明确设置name属性。

<img src="01-SpringMVC第一天.assets/servlet26.png" style="zoom:67%;" />



<img src="01-SpringMVC第一天.assets/servlet27.png" alt="servlet27" style="zoom:67%;" />

#### headers属性

headers属性类型是String[],表示请求头中必须包含指定的请求头参数。

```java
	@RequestMapping(value="/testHeaders",headers = "Cookie11")
    public String testHeaders(){
        return "/first.jsp";
    }
```

如果请求头中没有指定的请求头参数，浏览器会报404

<img src="01-SpringMVC第一天.assets/servlet28.png" style="zoom:67%;" />

请求头在哪看：

<img src="01-SpringMVC第一天.assets/servlet29.png" style="zoom:67%;" />

> 小提示：
>
> ​	一些属性在谷歌浏览器中虽然看不到对应的请求参数，但是实际上已经包含了。
>
> ​	例如：Content-Type 随意在谷歌浏览器中Request Header无法查看到，但设置@RequestMapping(value="/testHeaders",headers = "Content-Type")后会发现依然可以访问控制单元。

#### consumes属性

consumers表示处理请求内容(Content-Type)的类型，平时多不设置，由Spring MVC自动判断。

#### produces属性

后面@ResponseBody注解中进行讲解。

## 静态资源放行

按照SpringMVC的使用流程，需要在web.xml文件中配置DispatcherServlet的拦截范围，而我们配置的拦截范围为”/”,表示拦截除jsp请求以外的所有请求。这样造成，请求是js,css,图片等静态资源的请求，也会被匹配到拦截，然后调用对应的单元方法来处理请求，比如你请求：http://localhost:8080/demo01/msb/js/jquery.js，它会到控制类中找路径是js/jquery.js的资源，很显然找不到的，我们是一个静态资源的请求，不应该按照普通单元方法请求的流程来处理，而是应该将对应的静态资源响应给浏览器使用。

怎么解决呢？

（1）方式1：缩小拦截范围，将拦截范围设置为*.do,这样就只对.do的资源拦截，其余的静态资源就会找对应的静态资源了：

```xml
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <!-- /表示除了.jsp结尾的uri，其他的uri都会触发DispatcherServlet。此处前往不要写成 /* -->
        <url-pattern>*.do</url-pattern>
    </servlet-mapping>
```

如果这样的话，那么你的所有控制单元的路径都要带.do ，不太方便。

（2）方式2：静态资源放行

Spring MVC 支持静态资源配置，当URL满足指定路径要求时不再去找控制单元，而是直接转发到特定路径中静态资源。

例如项目结构是

<img src="01-SpringMVC第一天.assets/servlet30.png" style="zoom:67%;" />

需要在springmvc.xml中配置静态资源放行

```xml
<!--配置静态资源放行-->
<!--mapping：当URI是什么样格式时，不再执行控制器，而是寻找静态资源。 ** 是通配符，表示任意层路径 -->
<!--location:去哪个目录中寻找静态资源。mapping中**的值是什么，就去location目录中找对应资源-->
<!--例如URL是http://localhost:8080/demo01/js/jquery.js 其中mapping的**就是jquery.js,就会去location的/js/目录中寻找jquery.js -->
<mvc:resources mapping="/js/**" location="/js/"></mvc:resources>
<mvc:resources mapping="/css/**" location="/css/"></mvc:resources>
<mvc:resources mapping="/images/**" location="/images/"></mvc:resources>
```

如果把静态资源放在WEB-INF中，例如项目结构是

<img src="01-SpringMVC第一天.assets/servlet31.png" style="zoom:67%;" />

需要在springmvc.xml中配置

```xml
<mvc:resources mapping="/js/**" location="/WEB-INF/js/"></mvc:resources>
<mvc:resources mapping="/css/**" location="/WEB-INF/css/"></mvc:resources>
<mvc:resources mapping="/images/**" location="/WEB-INF/images/"></mvc:resources>
```

这样访问http://localhost:8080/demo01/images/aaa.png即可访问到静态资源。

## 控制单元的方法参数（接收请求参数）

### 控制单元方法参数写法

控制单元方法参数一共有两种写法：

（1）紧耦方式：获取原生Servlet API，通过原生Servlet API获取请求参数、设置响应内容、设置作用域的值。-》不用了

（2）解耦方式（松耦合方式）：使用Spring MVC提供的方式获取请求参数、设置响应内容、设置作用域的值。-》以后用这个

### 紧耦方式

之前在学习Servlet时，Servlet中service方法参数为HttpServletRequest、HttpServletResponse，通过这两个对象可以获取到HttpSession、ServletContext、PrintWriter等其他常用对象：

![](01-SpringMVC第一天.assets/servlet32.png)

在Spring MVC中，可以直接在控制单元的方法参数中按需注入HttpServletRequest、HttpServletResponse、HttpSession对象。注入后就可以像之前学习Servlet一样进行获取参数。

**完成案例：前台表单录入参数，后台接收参数**

webapp目录下编写前台show.jsp页面：

```jsp
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
  <form  action="testform">
    <p>
        名字：<input type="text" name="name"/>
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
        <input type="submit" value="提交">
    </p>
  </form>
</body>
</html>

```

编写控制器com.msb.controller.MyController:

```java
package com.msb.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * @Author: zhaoss
 */
@Controller// 放入到Spring MVC容器中
public class MyController {
    @RequestMapping("/testform")// 当前方法的映射路径
    public String demo01(HttpServletRequest req){
        String name = req.getParameter("name");
        String pwd = req.getParameter("pwd");
        String sex = req.getParameter("sex");
        String[] hobbies = req.getParameterValues("hobby");
        System.out.println(name + "---" + pwd + "---" + sex + "---" + Arrays.toString(hobbies));
        // 跳转到index.jsp中
        return "/index.jsp";
    }
}

```

请求http://localhost:8080/demo01/show.jsp，录入数据：

<img src="01-SpringMVC第一天.assets/servlet33.png" style="zoom:67%;" />

后台结果：

![](01-SpringMVC第一天.assets/servlet34.png)

这种需要在控制单元方法参数中注入Servlet API的方式，称为**Spring MVC 紧耦方法**。因为使用的是原生Servlet API，和原生Servlet API紧耦。

上面使用servlet api的代码不报错，也意味着你的pom.xml中配置了：

```xml
<dependency>
      <groupId>jakarta.servlet.jsp</groupId>
      <artifactId>jakarta.servlet.jsp-api</artifactId>
      <version>3.0.0</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>jakarta.servlet</groupId>
      <artifactId>jakarta.servlet-api</artifactId>
      <version>5.0.0</version>
      <scope>provided</scope>
    </dependency>
```

所以缺点：与servlet耦合性太高了,参数接收费劲。你在这就知道我们用原生的东西可以就行了，但是我们以后肯定不这么用，体现不出springmvc的好处啊！

### 解耦方式

解耦方式是Spring MVC独有方式（使用过程中看不到servlet 的影子），是Spring MVC给开发者提供的：

（1）获取请求中内容

（2）设置作用域值

（3）设置响应内容

等写法。

#### 获取普通表单参数

案例：

webapp目录下定义前台jsp页面，编写表单：

```jsp
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
  <form  action="testform2">
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
        <input type="submit" value="提交">
    </p>
  </form>
</body>
</html>

```

后台com.msb.controller.MyController控制类：

```java
package com.msb.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;

/**
 * @Author: zhaoss
 */
@Controller// 放入到Spring MVC容器中
public class MyController {
    @RequestMapping("/testform2")// 当前方法的映射路径
    public String demo02(int age,String name,String pwd,String sex,String[] hobby){
        System.out.println(name + "---" + age + "---" + pwd + "---" + sex + "---" + Arrays.toString(hobby));
        // 跳转到index.jsp中
        return "/index.jsp";
    }
}

```

前台请求：

<img src="01-SpringMVC第一天.assets/servlet35.png" style="zoom:67%;" />

后台结果：

<img src="01-SpringMVC第一天.assets/servlet36.png" style="zoom:67%;" />

需要注意的是：

**（1）**获取普通表单参数，只需要包含在控制单元中提供与请求参数同名的方法参数即可

**（2）**Spring MVC会自动进行类型转换，比如age在后台我们并没有进行类型转换即可转为int类型，以前用getParamter获取的是String类型，再转为int类型。

**（3）**如果你在表单的年龄框中录入的数据是12abc，那么会报400错误的。

**（4）**如果参数是基本数据类型如int，在控制单元方法参数中提供了参数，但是在请求中没有对应参数，这时Spring MVC会把空值赋予给参数，但是控制方法是无法接收空值的：

<img src="01-SpringMVC第一天.assets/servlet37.png" style="zoom:67%;" />

<img src="01-SpringMVC第一天.assets/servlet38.png" alt="servlet38" style="zoom:67%;" />

<img src="01-SpringMVC第一天.assets/servlet39.png" alt="servlet39" style="zoom:67%;" />

所以一些有经验的开发者在Spring MVC的参数中都是用封装类型，这样即使是空值也能正常接收。但此处不是说以后Spring MVC控制单元参数必须使用封装类类型，如果可以保证每次都会传递这个参数，也可以使用八大基本数据类型。

控制单元参数修改为Integer类型：

```java
    @RequestMapping("/testform2")// 当前方法的映射路径
    public String demo02(Integer age,String name,String pwd,String sex,String[] hobby){
        System.out.println(name + "---" + age + "---" + pwd + "---" + sex + "---" + Arrays.toString(hobby));
        // 跳转到index.jsp中
        return "/index.jsp";
    }
```

 这样即使age没有传递，后台也不会报错：

<img src="01-SpringMVC第一天.assets/servlet40.png" style="zoom:67%;" />

####  @RequestParam 注解的使用

@RequestParam是方法参数级注解。每个控制单元方法参数前面都能写这个注解。在@RequestParam注解里面提供了四个属性，分别：

<img src="01-SpringMVC第一天.assets/servlet41.png" style="zoom:67%;" />

这些属性只需要按需设置即可。可以都不使用，也可以都使用，也可以只使用里面的部分。

（1）name：当请求参数名和控制单元参数名不对应时，可以使用name指定请求参数名。这样方法参数就可以不与请求参数对应了。

```java
@Controller// 放入到Spring MVC容器中
public class MyController {
    @RequestMapping("/testParam1")
    public String testParam1(@RequestParam(name="name") String username){
        System.out.println(username);
        return "/index.jsp";
    }
}

```

含义：接收前台名字为name的参数的值赋值给username参数。

如访问： http://localhost:8080/demo01/testParam1?name=zs后台即可接受到。

（2）value：是name属性的别名。功能和name属性相同。之所以再次设置一个和name属性相同的value，是因为在Java注解中，当需要设置value属性，且只需要设置value属性时可以省略value属性名，这样写起来更加简单。

下面代码和上面（1）中的代码完全相同

```java
@Controller// 放入到Spring MVC容器中
public class MyController {
    @RequestMapping("/testParam1")
    public String testParam1(@RequestParam("name") String username){
        System.out.println(username);
        return "/index.jsp";
    }
}
```

（3）defaultValue：默认值。表示当请求参数中没有这个参数时给与的默认值。

```java
@RequestMapping("/testParam2")
    public String testParam2(@RequestParam(defaultValue="lili") String name,@RequestParam(defaultValue = "18")Integer age){
        System.out.println(name + "----" + age);
        return "/index.jsp";
    }
```

在浏览器地址栏输入：http://localhost:8080/demo01/testParam2 会在控制台打印:

<img src="01-SpringMVC第一天.assets/servlet42.png" style="zoom:67%;" />

很明显URL中没有name、age参数，但是赋予了默认值。

（4）required：boolean类型，表示请求中是否必须包含参数。

```java
    @RequestMapping("/testParam3")
    public String testParam3(@RequestParam(required = true) String name){
        System.out.println(name);
        return "/index.jsp";
    }
```

如果地址栏忘记录入参数，访问http://localhost:8080/demo01/testParam3：

<img src="01-SpringMVC第一天.assets/servlet43.png" style="zoom:67%;" />

#### 接收多个同名表单参数

在提交表单数据时，可能在里面包含复选框。当选中多个复选框时会出现多个同名参数。在Spring MVC中可以使用数组和List接收多个同名参数。

（1）当使用数组进行接收时，需要数组对象名和请求参数名一致。如果不想一致，可以使用@RequestParam("hovers")定义请求参数名。

```java
    @RequestMapping("/testParam4")
    public String testParam4(@RequestParam("hobby") String[] hobbies){
        System.out.println(Arrays.toString(hobbies));
        return "/index.jsp";
    }
```

（2）在使用List进行接收时，必须在参数前面添加@RequestParam注解，注解中内容就是请求参数名

```java
    @RequestMapping("/testParam5")
    public String testParam5(@RequestParam("hobby") List hobbies){
        System.out.println(hobbies);
        return "/index.jsp";
    }
```

访问：http://localhost:8080/demo01/testParam5?hobby=html&hobby=java即可获取[html, java]。

#### 使用JavaBean作为参数（使用类对象作为控制单元参数）

JavaBean：就是具体（非抽象）公共（public）的类，一个包含私有属性，getter/setter方法和无参构造方法的Java类。是不是感觉和实体类特别像。其实写法上和实体类相同。唯一区别是实体类是数据库层面的概念，类型中属性要和数据库字段对应。而JavaBean的属性是灵活的，不是必须和哪里对应的。

JavaBean是一个专业概念，可以简单点理解：使用类对象做为控制单元参数，接收请求参数。如果不是特别较真，狭义上可以认为JavaBean就是项目中的实体类。

之前7.3.1中我们接收每个参数，后续可以将参数封装为具体的对象，其实也挺麻烦的，所以SpringMVC帮我们提供了一个方式可以直接使用类对象作为参数：

当使用类对象作为参数时，**要求属性名和参数名对应**，类型转换由Spring MVC自动完成。不支持@RequestParam注解。所以需要先建立一个类。且类中必须提供属性的getter和setter方法，因为Spring MVC就是**通过setter方法**把请求参数的值设置到类的属性中。**（注意，javabean的属性和前台参数名字可以不一致，但是一定要确保该属性对应的setXXX方法的XXX与前台参数名字一致，因为底层通过反射找set方法）**

前台jsp页面：

```jsp
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
  <form   action="test6">
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
        <input type="submit" value="提交">
    </p>
  </form>
</body>
</html>

```

javabean:com.msb.pojo.User

```java
package com.msb.pojo;

import java.util.Arrays;

/**
 * @Author: zhaoss
 */
public class User {
    private String name;
    private Integer age;
    private String pwd;
    private String sex;
    private String[] hobby;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("name的setter方法");
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        System.out.println("age的setter方法");
        this.age = age;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        System.out.println("pwd的setter方法");
        this.pwd = pwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        System.out.println("sex的setter方法");
        this.sex = sex;
    }

    public String[] getHobby() {
        return hobby;
    }

    public void setHobby(String[] hobby) {
        System.out.println("hobby的setter方法");
        this.hobby = hobby;
    }

    public User() {
        System.out.println("空构造器");
    }

    public User(String name, Integer age, String pwd, String sex, String[] hobby) {
        System.out.println("有参构造器");
        this.name = name;
        this.age = age;
        this.pwd = pwd;
        this.sex = sex;
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", pwd='" + pwd + '\'' +
                ", sex='" + sex + '\'' +
                ", hobby=" + Arrays.toString(hobby) +
                '}';
    }
}

```

控制类：在控制单元中放置一个User类型对象，对象名称没有要求，只需要保证请求参数名和类的属性名相同就可以了。

```java
package com.msb.controller;

import com.msb.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhaoss
 */
@Controller// 放入到Spring MVC容器中
public class MyController {

    @RequestMapping("/test6")
    public String test6(User user){
        System.out.println(user);
        return "/index.jsp";
    }
}

```

![](01-SpringMVC第一天.assets/servlet45.png)

![servlet46](01-SpringMVC第一天.assets/servlet46.png)

如果需要用多个对象接收，也是可以的，如前端：有学生信息，有班级信息,show.jsp:

```jsp
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
  <form   action="test6">
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
        <input type="submit" value="提交">
    </p>

      <p>
          班级名字：<input type="text" name="cname"/>
      </p>
      <p>
          班级编号：<input type="text" name="cno"/>
      </p>
  </form>
</body>
</html>

```

javabean：

```java
package com.msb.pojo;

/**
 * @Author: zhaoss
 */
public class Clazz {
    private String cname;
    private Integer cno;

    public Clazz(String cname, Integer cno) {
        this.cname = cname;
        this.cno = cno;
    }

    public Clazz() {
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getCno() {
        return cno;
    }

    public void setCno(Integer cno) {
        this.cno = cno;
    }

    @Override
    public String toString() {
        return "Clazz{" +
                "cname='" + cname + '\'' +
                ", cno=" + cno +
                '}';
    }
}

```

```java
package com.msb.pojo;

import java.util.Arrays;

/**
 * @Author: zhaoss
 */
public class User {
    private String name;
    private Integer age;
    private String pwd;
    private String sex;
    private String[] hobby;
    private Clazz clazz;

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("name的setter方法");
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        System.out.println("age的setter方法");
        this.age = age;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        System.out.println("pwd的setter方法");
        this.pwd = pwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        System.out.println("sex的setter方法");
        this.sex = sex;
    }

    public String[] getHobby() {
        return hobby;
    }

    public void setHobby(String[] hobby) {
        System.out.println("hobby的setter方法");
        this.hobby = hobby;
    }

    public User() {
        System.out.println("空构造器");
    }

    public User(String name, Integer age, String pwd, String sex, String[] hobby) {
        System.out.println("有参构造器");
        this.name = name;
        this.age = age;
        this.pwd = pwd;
        this.sex = sex;
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", pwd='" + pwd + '\'' +
                ", sex='" + sex + '\'' +
                ", hobby=" + Arrays.toString(hobby) +
                ", clazz=" + clazz +
                '}';
    }
}

```

控制器：

```java
package com.msb.controller;

import com.msb.pojo.Clazz;
import com.msb.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhaoss
 */
@Controller// 放入到Spring MVC容器中
public class MyController {
    @RequestMapping("/test6")
    public String test6(User user, Clazz cla){
        user.setClazz(cla);
        System.out.println(user);
        return "/index.jsp";
    }
}

```

这样后台结果：

<img src="01-SpringMVC第一天.assets/servlet47.png" style="zoom:67%;" />

如果你觉得这样都麻烦，你可以先改前端：

```java
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
  <form   action="test6">
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
        <input type="submit" value="提交">
    </p>

      <p>
          班级名字：<input type="text" name="clazz.cname"/>
      </p>
      <p>
          班级编号：<input type="text" name="clazz.cno"/>
      </p>
  </form>
</body>
</html>

```

控制类：

```java
package com.msb.controller;

import com.msb.pojo.Clazz;
import com.msb.pojo.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: zhaoss
 */
@Controller// 放入到Spring MVC容器中
public class MyController {
    @RequestMapping("/test6")
    public String test6(User user){
        System.out.println(user);
        return "/index.jsp";
    }
}

```

也可以。

#### JavaBean和简单数据类型混合使用

既然Spring MVC中又支持JavaBean的形式，又支持使用简单类型接收。当两种方式都使用时，且类中和简单类型重名时，Spring MVC会“雨露均沾”都给设置上。

```java
    @RequestMapping("/test6")
    public String test6(User user,String name){
        System.out.println(user);
        System.out.println(name);
        return "/index.jsp";
    }
```

<img src="01-SpringMVC第一天.assets/servlet48.png" style="zoom:67%;" />

这种写法中对于同学们存在一个非常常见的错误。当JavaBean中属性特别多时，原本希望使用普通属性类型接收参数，但是JavaBean中还存在个同名属性，且这个属性的类型和参数类型不一样。Spring MVC会进行类型转换，如果能够转换没有问题。如果无法转换会出现400

```java
    @RequestMapping("/test6")
    public String test6(User user,String name,String age){
        System.out.println(user);
        System.out.println(name);
        return "/index.jsp";
    }
```

<img src="01-SpringMVC第一天.assets/servlet49.png" style="zoom:67%;" />

<img src="01-SpringMVC第一天.assets/servlet50.png" alt="servlet50" style="zoom:67%;" />

出现400，主要原因是无法把test赋值给User类中Integer age属性。

#### 接收日期类型参数

如果希望使用Date类型接收客户端传递过来的数据，默认情况下必须保证客户端参数格式和服务器日期格式一致。可以在计算机屏幕右下角查看到服务器的日期格式：

![](01-SpringMVC第一天.assets/servlet51.png)

所以只要保证客户端传递过来的日期是yyyy/MM/dd hh:mm:ss的格式，Spring MVC会自动进行类型转换。其中小时分钟秒可以省略不写。

控制单元：

```java
    @RequestMapping("/test7")
    public String test7(java.util.Date date){//想年月日时分秒都用，用util.Date
        System.out.println(date);
        return "/index.jsp";
    }
```

在浏览器地址栏输入只包含日期的数据 http://localhost:8080/demo01/test7?date=2023/4/9

也可以在浏览器地址栏输入包含日期和小时分钟秒的数据 http://localhost:8080/demo01/test7?date=2023/4/9%205:19:23其中2023/4/9和5:19:23之间是空格，当回车发送请求后，会自动把空格解析为%20,因为空格是URL中的特殊字符。

默认情况下不支持只输入小时分钟秒。

如果觉得默认的格式无法满足要求，可以使用@DateTimeFormat自定义时间格式，定义前端要传过来的格式。

控制单元：

```java
    @RequestMapping("/test7")
    public String test7(@DateTimeFormat(pattern = "yyyy-MM-dd")java.util.Date date){
        System.out.println(date);
        return "/index.jsp";
    }
```

访问：http://localhost:8080/demo01/test7?date=2022-5-6即可。

小提示：

​	需要注意的是，当使用了@DateTimeFormat以后默认的时间格式就不能使用了。

如果你传过来的Date是要给对象的，那么@DateTimeFormat也可以写在JavaBean的属性上面。

控制类：

```java
    @RequestMapping("/test8")
    public String test8(User user){
        System.out.println(user);
        return "/index.jsp";
    }
```

javabean:

```java
package com.msb.pojo;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Arrays;
import java.util.Date;

/**
 * @Author: zhaoss
 */
public class User {
    private String name;
    private Integer age;
    private String pwd;
    private String sex;
    private String[] hobby;
    private Clazz clazz;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;


    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        this.clazz = clazz;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("name的setter方法");
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        System.out.println("age的setter方法");
        this.age = age;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        System.out.println("pwd的setter方法");
        this.pwd = pwd;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        System.out.println("sex的setter方法");
        this.sex = sex;
    }

    public String[] getHobby() {
        return hobby;
    }

    public void setHobby(String[] hobby) {
        System.out.println("hobby的setter方法");
        this.hobby = hobby;
    }

    public User() {
        System.out.println("空构造器");
    }

    public User(String name, Integer age, String pwd, String sex, String[] hobby) {
        System.out.println("有参构造器");
        this.name = name;
        this.age = age;
        this.pwd = pwd;
        this.sex = sex;
        this.hobby = hobby;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", pwd='" + pwd + '\'' +
                ", sex='" + sex + '\'' +
                ", hobby=" + Arrays.toString(hobby) +
                ", clazz=" + clazz +
                ", date=" + date +
                '}';
    }
}

```

访问：http://localhost:8080/demo01/test8?date=2022-5-6即可

#### 接收请求头数据

在HTTP协议中，请求头参数会有很多。如果希望接收请求头数据可以使用@RequestHeader进行接收。

以上面的/testDate请求举例，可以在谷歌开发者工具（F12）里面看到下面请求头参数。

<img src="01-SpringMVC第一天.assets/servlet52.png" style="zoom:67%;" />

在控制单元方法参数中添加对应名称的参数，并在参数前面添加@RequestHeader注解

```java
    @RequestMapping("/test10")
    public String test10(@RequestHeader String  Accept){
        System.out.println(Accept);
        return "/index.jsp";
    }
```

上面这种写法中方法参数名和请求参数名完全对应。但是在Java中参数名的命名规范是首字母小写。Spring MVC对于接收请求参数时名称不区分大小写。所以下面的写法也是可以的。

```java
    @RequestMapping("/test10")
    public String test10(@RequestHeader String  Accept){
        System.out.println(Accept);
        return "/index.jsp";
    }
```

对于请求参数名就是一个单词，不是xxx-xxx或xxx-xxx-xxx形式的参数名可以像上面的写法，让方法参数和请求头参数名对应。但是Java中方法参数命名要求中不允许存在-，也就是说请求参数中的Accept-Encoding是无法在Java代码中提供出来同名参数的。如下：

![](01-SpringMVC第一天.assets/servlet53.png)

当希望去接收多个单词组成的请求参数时，需要在@RequestHeader注解中指定要接收的请求参数名，这时方法参数名就没有特殊要求了。

```java
    @RequestMapping("/test11")
    public String test11( @RequestHeader("Accept-Encoding") String suiyi){
        System.out.println(suiyi);
        return "/index.jsp";
    }
```

## 转发和重定向

在前面Java EE阶段中学习过两个概念：转发和重定向。这两个概念都是出现在资源之间相互跳转的，如图：

请求转发：

![](01-SpringMVC第一天.assets/Servlet-54.png)

重定向：

![Servlet-55](01-SpringMVC第一天.assets/Servlet-55.png)

两者区别

​	（1）转发只能跳转到当前项目内部资源。重定向可以跳转到外部资源。例如：从自己的项目中跳转到百度应该使用重定向。

​	（2）转发是一次请求，无论服务器内部转发多少次，请求对象都不变。所以转发可以共享请求域的值。同时对于客户端浏览器URL是不变的。

​		      重定向后需要客户端重新发起请求，和重定向之前不是一个请求。所以重定向后不能获取到之前设置在请求域的值。同时客户端浏览器URL是改变的。

​	（3）转发时资源路径如果是绝对路径，第一个 / 表示当前项目根目录。重定向时资源路径时绝对路径，第一个 / 表示 Tomcat 的 webapps目录，即：当前项目的上层目录。

​	（4）代码实现

请求转发：

```java
request.getRequestDispatcher("/first.jsp").forward(request,response);
```

重定向：

```java
response.sendRedirect("/demoprojectname/first.jsp");
```

### Spring MVC中的转发和重定向

在Spring MVC中无论是转发还是重定向，使用绝对路径时/都表示项目根目录。

这种设计对于开发者来说更加友好，不用在区分到底是转发，还是重定向了。

在Spring MVC框架中，默认情况下都使用转发进行寻找资源。例如下面代码表示转发到当前项目根目录下的first.jsp文件。

```java
@RequestMapping("/test11")
public String test11(){
    return "/first.jsp";
}
```

上面代码等效于下面代码。

在资源路径前面添加forward: 表示转发。因为写不写forward:都是转发，所以为了代码写起来简单一些，多省略forward:

```java
@RequestMapping("/test11")
public String test11(){
    return "forward:/first.jsp";
}
```

如果希望使用重定向跳转到其他资源，只能在资源路径最前面明确添加redirect:，下面代码就是使用重定向方式的写法。

```java
@RequestMapping("/test12")
public String test12(){
    return "redirect:/first.jsp";
}
```

![](01-SpringMVC第一天.assets/Servlet-56.png)

![Servlet-57](01-SpringMVC第一天.assets/Servlet-57.png)

### 使用View视图转发和重定向

使用View视图也可以完成。

```java
    @RequestMapping("/test13")
    public View test13(){
        // 请求转发
        View  v = new InternalResourceView("/index.jsp");
        return  v;
    }
```

```java
    @RequestMapping("/test14")
    public View test14(HttpServletRequest  req){
        // 重定向
        View  v = new RedirectView(req.getContextPath() + "/index.jsp");// req.getContextPath()获取上下文路径
        
        return  v;
    }
```

### 使用ModelAndView 转发重定向

ModelAndView 对象——springmvc底层进行转发重定向视图展示的时候都是通过该对象进行的。你在进行跳转的时候，无论你返回的时候用字符串还是View其实底层都是保存在ModelAndView进行操作的，如何保存的呢？按照下面方式：

```java
    @RequestMapping("/test15")
    public ModelAndView test15(){
        ModelAndView mv=new ModelAndView();
        // 转发方式一
        mv.setViewName("forward:/index.jsp");
        // 转发方式二
        mv.setView(new InternalResourceView("/index.jsp"));
        return mv;
    }
```

```java
    @RequestMapping("/test16")
    public ModelAndView test16(HttpServletRequest  req){

        ModelAndView mv=new ModelAndView();
        // 重定向方式一
        mv.setViewName("redirect:/index.jsp");
        // 重定向方式二
        mv.setView(new RedirectView(req.getContextPath()+"/index.jsp"));
        return mv;
    }
```

底层按照上面方式处理，我们自己写的时候也可以用这种，只是自己写出来比较麻烦。

(ModelAndView中的Model代表模型，View代表视图，这个名字就很好地解释了该类的作用。业务处理器调用模型层处理完用户请求后，把结果数据存储在该类的model属性中，把要返回的视图信息存储在该类的view属性中，然后让该ModelAndView返回该Spring MVC框架)

## 设置作用域的值

页面跳转后，是否可以携带数据呢？肯定是可以的呀，这里就需要在作用域中设置值，将数据存在域对象中。都有什么作用域呢？

| 作用域      | 范围     |
| ----------- | -------- |
| request     | 一次请求 |
| session     | 一次会话 |
| application | 一个项目 |



**案例：**

控制类：

```java
@RequestMapping("/test17")
    public String test17(HttpServletRequest req, HttpSession session){
        req.setAttribute("reqmsg","test");
        session.setAttribute("semsg","test2");
        // 全局对象不可以通过参数直接写，需要自己获取：
        ServletContext servletContext = req.getServletContext();
        //也可以ServletContext servletContext1 = session.getServletContext();
        servletContext.setAttribute("scmsg","test3");
        return "/first.jsp";
    }
```

first.jsp：

```jsp
<%--
  Created by IntelliJ IDEA.
  User: zhaoss-msb
  Date: 2023/7/25
  Time: 13:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
会跳转到这个first.jsp页面
request中取值：${requestScope.reqmsg}
session中取值：${sessionScope.semsg}
application中取值：${applicationScope.scmsg}
</body>
</html>

```

访问：http://localhost:8080/demo01/test17

结果：

<img src="01-SpringMVC第一天.assets/Servlet-58.png" style="zoom:67%;" />

上面的作用域用的最多的作用域就是request作用域，所以springmvc干脆直接给我们封装了一个request作用域供我们使用，就不需要使用原生request作用域了，因为要是使用原生作用域还需要再pom.xml中先导入servlet的东西，耦合性高。

Spring MVC 中 提供了request作用域的解耦写法，没有提供dession作用域和application作用域的解耦写法。也就是说当想给request作用域设置内容时有两种写法，给session和application作用域设置值只有紧耦方式。

Spring MVC 中提供的等价request作用域两种方式：在控制单元方法参数中添加org.springframework.ui.Model或java.util.Map对象。这两种方式的本质都是使用了Servlet中request.setAttribute(String,String);方法。

```java
    @RequestMapping("/test18")
    public String test18(Map<String,Object> map){
        map.put("reqmsg","test");
        return "/first.jsp";
    }

    @RequestMapping("/test19")
    public String test19(Model model){
        // 设置一个作用域值
        model.addAttribute("reqmsg","test");
        // 设置多个作用域值
        Map<String,Object> map = new HashMap<>();
        map.put("reqmsg2","test2");
        model.addAllAttributes(map);
        return "/first.jsp";
    }
```

 

## @ResponseBody注解

### @ResponseBody介绍

该注解用于将 控制单元 的方法返回的对象，通过适当的 转换器转换为指定格式后，写入到 Response 对象的 body 数据区。

返回的数据不是 html 标签的页面，而是其他某种格式的数据时（如**普通文本、 json、xml** 等）使用（**通常用于ajax 请求**）。

@ResponseBody注解是类或方法级注解。

![](01-SpringMVC第一天.assets/Servlet-59.png)

当方法上添加@ResponseBody注解后，控制单元方法返回值将不再被视图解析器进行解析。而是把返回值放入到响应流中进行响应。

### 响应普通文本

直接在方法上添加上@ResponseBody，Spring MVC会把返回值设置到响应流中。

```java
    @RequestMapping("/demo1")
    @ResponseBody
    public String demo1(){
        return "马士兵msbyjx";
    }
```

访问控制器后的效果是在浏览器直接打印马士兵msbyjx字符串。(中文会出现乱码)

<img src="01-SpringMVC第一天.assets/Servlet-60.png" style="zoom:67%;" />

上面代码等效于直接使用PrintWriter对象进行打印:

```java
    @RequestMapping("/demo1")
    public void demo1(HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.print("马士兵msbyjx");
        out.flush();
        out.close();
    }
```

### 乱码问题处理

在使用@ResponseBody注解时，只要返回值类型不是类或Map或List等满足键值对类型。Spring MVC 都会设置响应内容类型为text/html;charset=ISO-8859-1。

例如：String、八大基本数据类型等Content-Type都是这个类型。

<img src="01-SpringMVC第一天.assets/Servlet-61.png" style="zoom:67%;" />

很明显text/html;charset=ISO-8859-1中编码是不支持中文的。所以返回值中包含中文，打印在浏览器中会出现乱码。

想要改变@ResonseBody注解的响应内容类型(Content-Type)只能通过@RequestMapping的produces属性进行设置。

```java
    @RequestMapping(value="/demo1",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String demo1(){
        return "马士兵msbyjx";
    }
```

结果：
<img src="01-SpringMVC第一天.assets/Servlet-62.png" style="zoom:67%;" />

### 响应json数据

@ResponseBody注解可以把控制单元返回值自动转换为json格式的数据（json对象）。

主要完成下面几个事情：

（1）判断返回值是否为JavaBean、JavaBean数组、List<JavaBean类型>、Map等满足键值对的类型。

（2）如果满足键值对类型，会使用Jackson把对象转换为JSON数据，设置到响应流中。同时会设置响应内容类型(Content-Type)为application/json;charset=utf-8

因为Spring MVC默认使用Jackson作为JSON转换工具，把java对象进行json转化，所以必须保证项目中存在Jackson的依赖，在pom.xml中加入下面依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

**案例：返回值是否为JavaBean类型，会使用Jackson把对象转换为JSON数据**

编写javabean：

```java
package com.msb.pojo;

import java.util.Date;

/**
 * @Author: zhaoss
 */
public class Student {
    private int id;
    private String name;
    private Date date;

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

```

在控制类中编写控制单元。

```java
    @RequestMapping(value="/demo2")
    @ResponseBody
    public Student demo2(){
        Student s = new Student();
        s.setId(17);
        s.setName("丽丽");
        s.setDate(new Date());
        return s;// 直接return这个对象，前端就会直接得到json对象，不用自己进行任何转化（都是json帮我们处理的）
    }
```

在浏览器访问后会在浏览器打印内容

<img src="01-SpringMVC第一天.assets/Servlet-63.png" style="zoom:67%;" />

但是上面日期的格式不正确，是一串数字，怎么解决呢？在javabean的属性中加入一个注解，通过该注解设置响应json数据中日期的格式：

```java
package com.msb.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @Author: zhaoss
 */
public class Student {
    private int id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date date;

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}

```

加入注解后再访问：

<img src="01-SpringMVC第一天.assets/Servlet-64.png" style="zoom:67%;" />

注意：

（1）

@DateTimeFormat注解：设置请求参数的日期格式

@JsonFormat注解：设置响应json数据中的日期格式

（2）

除了javabean对象以外，也可以使用Map或List<实体类>或List<Map>等类型,这些类型都可以被转换为JSON数据。可自行练习。

### 响应xml文件

在Spring MVC中支持把返回值转换为XML文件。如果项目中所有控制单元返回值结果都希望是XML格式，可以按照下面步骤完成。

先导入依赖：

ps：jackson-databind和jackson-dataformat-xml依赖只能导入一个。

```xml
<!--<dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.15.2</version>
    </dependency>-->

    <dependency>
      <groupId>com.fasterxml.jackson.dataformat</groupId>
      <artifactId>jackson-dataformat-xml</artifactId>
      <version>2.15.2</version>
    </dependency>
```

控制单元方法和转换为JSON时写法完全相同。

```java
    @RequestMapping(value="/demo2")
    @ResponseBody
    public Student demo2(){
        Student s = new Student();
        s.setId(17);
        s.setName("丽丽");
        s.setDate(new Date());
        return s;
    }
```

访问浏览器测试即可

<img src="01-SpringMVC第一天.assets/Servlet-65.png" style="zoom:67%;" />

## @RestController注解

如果控制器中所有的方法都包含@ResponseBody注解，类似下面效果：

```java
package com.msb.controller;

import com.msb.pojo.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: zhaoss
 */
@Controller
public class MyController2 {
    @RequestMapping(value="/demo01",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String demo01(){
        return "马士兵msbyjx";
    }


    @RequestMapping(value="/demo02")
    @ResponseBody
    public Student demo02(){
        Student s = new Student();
        s.setId(17);
        s.setName("丽丽");
        s.setDate(new Date());
        return s;
    }
}

```

像这种类中所有的控制单元方法都有@ResponseBody，那么可以直接将@ResponseBody注解放在类上：

```java
package com.msb.controller;

import com.msb.pojo.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: zhaoss
 */
@Controller
@ResponseBody
public class MyController2 {
    @RequestMapping(value="/demo01",produces = "text/html;charset=utf-8")
    public String demo01(){
        return "马士兵msbyjx";
    }


    @RequestMapping(value="/demo02")
    public Student demo02(){
        Student s = new Student();
        s.setId(17);
        s.setName("丽丽");
        s.setDate(new Date());
        return s;
    }
}

```

@Controller、@ResponseBody注解可以使用@RestController进行简化。

@RestController = @Controller + @ResponseBody

当类上使用的是@RestController而不是@Controller时，控制单元方法不再需要写@ResponseBody(也不能写@ResponseBody)，Spring MVC在解析控制单元方法时会自动带有@ResponseBody注解。

所以：@RestController写起来更加简单了。

但是需要注意：

​	一旦类上使用了@RestController，所有控制单元返回都是普通文本或XML或JSON数据，而无法实现页面跳转功能了。

​	所以：只要类中有一个方法是希望实现页面跳转功能，类上就不能使用@RestController。只有类中所有的方法都是返回普通文本或JSON或XML的情况才能使用@RestController注解。

```java
package com.msb.controller;

import com.msb.pojo.Student;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author: zhaoss
 */
@RestController  // 此处换成了@RestController，而不是@Controller了
public class MyController2 {
    // 下面所有方法都不写@ResponseBody注解
    
    
    @RequestMapping(value="/demo01",produces = "text/html;charset=utf-8")
    public String demo01(){
        return "马士兵msbyjx";
    }


    @RequestMapping(value="/demo02")
    public Student demo02(){
        Student s = new Student();
        s.setId(17);
        s.setName("丽丽");
        s.setDate(new Date());
        return s;
    }
}

```



访问：http://localhost:8080/demo01/demo01、http://localhost:8080/demo01/demo02即可。

## @RequestBody注解

通过案例：前台传递参数后端接收，一点点引入，我们通过ajax请求传递数据到后端

### ajax请求-数据为json对象

（1）在webapp目录下添加jquery.js文件：

<img src="01-SpringMVC第一天.assets/Servlet-66.png" style="zoom:67%;" />

（2）对js文件放行：springmvc.xml中：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
		https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <!-- 扫描控制器类，千万不要把service等扫描进来，也千万不要在Spring配置文件扫描控制器类所在包 -->
    <context:component-scan base-package="com.msb.controller"></context:component-scan>
    <!-- 让Spring MVC的注解生效 ：@RequestMapping，这个位置别忘记加入mvc的命名空间-->
   <mvc:annotation-driven></mvc:annotation-driven>
    <!-- 静态资源放行-->
    <mvc:resources mapping="/js/**" location="/js/"></mvc:resources>
</beans>
```

（3）webapp目录下创建test.jsp:

```jsp
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
                  url:"test1",
                  data:{"id":1,"name":"张三"},  //json对象
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

```

（4）控制类：

```java
package com.msb.controller;

import com.msb.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: zhaoss
 */
@Controller
public class MyController3 {
    @RequestMapping(value="/test1")
    @ResponseBody
    public String demo01(Student s){
        System.out.println(s);
        return "ok";
    }
}

```

（5）对应实体类：

```java
package com.msb.pojo;
/**
 * @Author: zhaoss
 */
public class Student {
    private int id;
    private String name;

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

```



启动服务器，请求：

前端效果：

<img src="01-SpringMVC第一天.assets/Servlet-67.png" style="zoom:67%;" />

后端效果：

<img src="01-SpringMVC第一天.assets/Servlet-68.png" style="zoom:67%;" />



### ajax请求-数据为json字符串

如果在ajax请求中发送的数据是json字符串：

```jsp
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
                  url:"test1",
                  data:'{"id":1,"name":"张三"}',//json字符串
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

```

启动服务器发送请求，发现后台无法接受到数据

<img src="01-SpringMVC第一天.assets/Servlet-69.png" style="zoom:67%;" />

**怎么处理呢？需要使用@RequestBody注解。**

@RequestBody注解底层依赖的依然是Jackson工具包，其作用是把客户端传递过来的请求体中JSON或XML数据转换为Map、类、List<类>、List<Map>等类型。

使用@RequestBody注解需要导入依赖：

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.15.2</version>
</dependency>
```

控制类变为：

```java
package com.msb.controller;

import com.msb.pojo.Student;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: zhaoss
 */
@Controller
public class MyController3 {
    @RequestMapping(value="/test1")
    @ResponseBody
    public String demo01(@RequestBody Student s){
        System.out.println(s);
        return "ok";
    }
}

```



前台有三次需要重点注意的地方：

（1）contentType：必须设置。常见取值“application/json”或"application/xml"。如果没有设置这个属性，取值默认是application/x-www-form-urlencoded，表示普通表单参数。当设置为"application/json"时，会把data取值设置到请求体中，所以服务端接收参数时就不能按照普通表单参数进行接收。

（2）data: 请求参数。必须是字符串类型，不能是JSON格式的对象。因为在JSON中key两侧必须有双引号，所以data取值两侧用单引号包含。因为在JavaScript中字符串string类型可以使用单引号包含，也可以使用双引号包含。

（3）type: 既然@RequestBody注解是处理请求体中的数据，所以前端不能是GET类型请求（GET没有请求体），多用在POST类型的请求中。



修改test.jsp:

```java
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
                  url:"test1",
                  contentType:"application/json",// 修改请求内容类型 
                  data:'{"id":1,"name":"张三"}',//json字符串
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

```

重启运行结果：

前端：

<img src="01-SpringMVC第一天.assets/Servlet-70.png" style="zoom:67%;" />

后端：

<img src="01-SpringMVC第一天.assets/Servlet-71.png" style="zoom:67%;" />



## SSM整合

### 新建模块

<img src="01-SpringMVC第一天.assets/Servlet-72.png" style="zoom:67%;" />

### 添加依赖和资源拷贝插件

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>org.example</groupId>
  <artifactId>SSMDemo</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>SSMDemo Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>3.8.1</version>
      <scope>test</scope>
    </dependency>
    <!-- 【必备】mybatis的依赖 -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.5.9</version>
    </dependency>
    <!-- 【必备】连接mysql的依赖 -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>8.0.28</version>
    </dependency>
    <!-- 【必备】日志slf4j依赖：需要导入log4j,slf4j和slf4j整合log4j的依赖-->
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
      <version>1.6.1</version>
    </dependency>
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-log4j12</artifactId>
      <version>1.7.2</version>
    </dependency>
    <!-- 【必备】spring的依赖  -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>6.0.5</version>
    </dependency>
    <!-- 【必备】springjdbc依赖：配置连接数据库数据源
    DriverManagerDataSource属于spring-jdbc包-->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>6.0.5</version>
    </dependency>
    <!--AOP命名空间的依赖：上面spring的依赖中包含aop了，但是还需要额外导入命名空间的依赖，运行时生效的-->
    <dependency>
      <groupId>org.aspectj</groupId>
      <artifactId>aspectjweaver</artifactId>
      <version>1.9.9.1</version>
      <scope>runtime</scope>
    </dependency>
    <!-- 【必备】spring整合mybatis的依赖  -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>3.0.1</version>
    </dependency>
    <!--jsp、servletapi，其实可以不导入，以防万一需要-->
    <dependency>
      <groupId>jakarta.servlet.jsp</groupId>
      <artifactId>jakarta.servlet.jsp-api</artifactId>
      <version>3.0.0</version>
      <scope>provided</scope>
    </dependency>
    <dependency>
      <groupId>jakarta.servlet</groupId>
      <artifactId>jakarta.servlet-api</artifactId>
      <version>5.0.0</version>
      <scope>provided</scope>
    </dependency>
    <!-- 【必备】依赖了Spring框架核心功能的5个依赖以及Spring整合Web的依赖spring-web -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>6.0.11</version>
    </dependency>

    <!--Jackson依赖：进行把java对象进行json转化-->
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.15.2</version>
    </dependency>
  </dependencies>
  <build>
    <finalName>SSMDemo</finalName>
    <!-- 【必备】加入资源拷贝插件 -->
    <resources>
      <resource>
        <directory>src/main/java</directory>
        <includes>
          <include>**/*.xml</include>
        </includes>
      </resource>
      <resource>
        <directory>src/main/resources</directory>
        <includes>
          <include>**/*.txt</include>
          <include>**/*.xml</include>
          <include>**/*.properties</include>
        </includes>
      </resource>
    </resources>
  </build>
</project>

```

标识【必备】是ssm整合所必须的环境依赖，没有标注的可以等什么时候用什么时候放进来就可以。

### 添加java目录

<img src="01-SpringMVC第一天.assets/Servlet-73.png" style="zoom:67%;" />

### 添加log4j.properties

在resources目录下加入log4j.properties

```properties
# log4j中定义的级别：fatal(致命错误) > error(错误) >warn(警告) >info(普通信息) >debug(调试信息)>trace(跟踪信息)
log4j.rootLogger = DEBUG , console , D 

### console ###
log4j.appender.console = org.apache.log4j.ConsoleAppender
log4j.appender.console.Target = System.out
log4j.appender.console.layout = org.apache.log4j.PatternLayout
log4j.appender.console.layout.ConversionPattern = [%p] [%-d{yyyy-MM-dd HH\:mm\:ss}] %C.%M(%L) | %m%n
```



### 处理spring整合mybatis部分

添加applicationContext.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">
    <!-- 【1】连接数据库，获取数据源，配置数据源，设置数据库连接的四个参数  -->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <!-- 利用setter方法完成属性注入，四个参数名固定的，注意源码中虽然没有driverClassName属性，但是有driverClassName的setter方法 -->
        <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/msbsys?useUnicode=true&amp;characterEncoding=utf-8&amp;useSSL=false&amp;serverTimezone=GMT%2B8&amp;allowPublicKeyRetrieval=true"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>
    <!-- 【2】获取SqlSessionFactory对象  -->
    <!-- 以前SqlSessionFactory都是在测试代码中我们自己创建的，但是现在不用了，整合包中提供的对于SqlSessionFactory的封装。里面提供了MyBatis全局配置文件所有配置的属性 -->
    <bean id="factory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!-- 注入数据源       -->
        <property name="dataSource" ref="dataSource"/>
        <!-- 给包下类起别名       -->
        <property name="typeAliasesPackage" value="com.msb.pojo"></property>
    </bean>
    <!-- 【3】扫描mapper文件   -->
    <!-- 设置扫描哪个包，进行接口绑定-->
    <!-- 所有Mapper接口代理对象都能创建出来，可以直接从容器中获取出来。 -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!-- 和SqlSessionFactory产生联系，以前接口绑定sqlSession.getMapper(BookMapper.class);
        都是通过以前接口绑定sqlSession来调用mapper，所以这里一定要注入工厂啊
         注意这里sqlSessionFactoryBeanName类型为String，所以用value把工厂名字写过来就行-->
        <property name="sqlSessionFactoryBeanName" value="factory"></property>
        <!-- 扫描的包 -->
        <property name="basePackage" value="com.msb.mapper"></property>
    </bean>

    <!-- 【4】扫描包下注解 -->
    <context:component-scan base-package="com.msb.service"></context:component-scan>
</beans>

```

### 处理整合springmvc部分

创建springmvc.xml:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="
        http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd
        http://www.springframework.org/schema/mvc
        https://www.springframework.org/schema/mvc/spring-mvc.xsd">
    <!-- 扫描控制器类，千万不要把service等扫描进来，也千万不要在Spring配置文件扫描控制器类所在包 -->
    <context:component-scan base-package="com.msb.controller"></context:component-scan>
    <!-- 让Spring MVC的注解生效 ：@RequestMapping-->
    <mvc:annotation-driven></mvc:annotation-driven>
    <!--配置静态资源放行-->
    <mvc:resources mapping="/js/**" location="/js/"></mvc:resources>
    <mvc:resources mapping="/css/**" location="/css/"></mvc:resources>
    <mvc:resources mapping="/images/**" location="/images/"></mvc:resources>
</beans>
```

### web.xml中加入springmvc.xml、applicationContext.xml的解析

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd"
         version="4.0">
  <servlet>
    <servlet-name>springmvc</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
      <!-- 参数名称必须叫做：contextConfigLocation。单词和大小写错误都导致配置文件无法正确加载 -->
      <param-name>contextConfigLocation</param-name>
      <!-- springmvc.xml 名称自定义，只要和我们创建的配置文件的名称对应就可以了。 -->
      <param-value>classpath:springmvc.xml</param-value>
    </init-param>
    <!-- Tomcat启动立即加载Servlet，而不是等到访问Servlet才去实例化DispatcherServlet -->
    <!-- 配置上的效果：Tomcat启动立即加载Spring MVC框架的配置文件-->
    <load-on-startup>1</load-on-startup>
  </servlet>
  <servlet-mapping>
    <servlet-name>springmvc</servlet-name>
    <!-- /表示除了.jsp结尾的uri，其他的uri都会触发DispatcherServlet。此处前往不要写成 /* -->
    <url-pattern>/</url-pattern>
  </servlet-mapping>
  
  <!--新加入-->
  <!--解析applicationContext.xml：利用监听器监听-->
  <listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
  </listener>
  <!--给全局参数contextConfigLocation设置值，contextConfigLocation是ContextLoaderListener父类ContextLoader中的属性-->
  <context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>classpath:applicationContext.xml</param-value>
  </context-param>
</web-app>

```

### 创建数据库表

<img src="01-SpringMVC第一天.assets/Servlet-74.png" style="zoom:67%;" />



并在数据库表中添加数据。

### 创建项目目录结构

【13.1】-【13.7】步骤将整合的配置内容已经配置好了，接下来开始创建项目的目录结构，项目要分层，有controller层、service层、有dao层（mapper层）、实体类层。

### 实体类构建

```java
package com.msb.pojo;

/**
 * @Author: zhaoss
 */
public class Book {
    private int id;
    private String name;
    private String author;
    private double price;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }
}

```

### mapper层

com.msb.mapper下创建BookMapper接口：

```java
package com.msb.mapper;

/**
 * @Author: zhaoss
 */
import java.util.List;

public interface BookMapper {
    public abstract List selectAll();
}

```

resources目录下创建com.msb.mapper文件夹，创建BookMapper.xml文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.msb.mapper.BookMapper">
    <select id="selectAll" resultType="book">
        select * from book
    </select>
</mapper>
```

### service层

com.msb.service下创建BookService接口：

```java
package com.msb.service;

/**
 * @Author: zhaoss
 */
import java.util.List;

public interface BookService {
    public abstract List findAll();
}
```

com.msb.service.impl下构建BookServiceImpl实现类：

```java
package com.msb.service.impl;

import com.msb.mapper.BookMapper;
import com.msb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zhaoss
 */
@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookMapper bookMapper;
    public List findAll() {
        return bookMapper.selectAll();
    }
}

```

### controller层

```java
package com.msb.controller;

import com.msb.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author: zhaoss
 */
@Controller
public class BookController {
    @Autowired
    private BookService bookService;
    @RequestMapping("/findAllBooks")
    @ResponseBody
    public List findAll(){
        List list = bookService.findAll();
        return list;
    }
}
```

最终目录结构：

<img src="01-SpringMVC第一天.assets/Servlet-75.png" style="zoom:67%;" />

### 测试

将项目添加入tomcat：

<img src="01-SpringMVC第一天.assets/Servlet-76.png" style="zoom:67%;" />

访问：http://localhost:8080/ssm/findAllBooks

结果：

![](01-SpringMVC第一天.assets/Servlet-77.png)