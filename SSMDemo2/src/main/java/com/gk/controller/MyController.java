package com.gk.controller;

import com.gk.pojo.User;
import com.gk.service.UserService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
//@MultipartConfig
public class MyController {

    @RequestMapping("/upload4")
    @ResponseBody
    public Map upload4(MultipartFile photo, HttpServletRequest req) throws IOException {
        // 把文件放在服务器目录：tomcat服务器下的路径 D:\Java\Tomcat\tomcat10\webapps\demo2\images
        String realPath = req.getServletContext().getRealPath("/images");
        System.out.println(realPath);

        File dir = new File(realPath);
        if (!dir.exists()){// 如果目录不存在，创建目录
            dir.mkdirs();
        }

        // 获取上传图片的原始名字
        String originalFilename = photo.getOriginalFilename();
        // 获取文件后缀：
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        System.out.println("suffix:" + suffix);
        // 创建文件唯一名字：
        // 方式1：利用时间戳+随机数+后缀：
        /*long time01 = System.currentTimeMillis();
        String filename = time01 + "" + new Random().nextInt(1000) + suffix;*/

        // 方式2：UUID
        UUID uuid = UUID.randomUUID();
        String filename = uuid + suffix;

        // 文件存储位置拼接：存储d:/images/拼接图片原始名字 sm05.png
        // d:/images/sm05.png
        File file = new File(dir,filename);
        // 文件保存：
        photo.transferTo(file);
        // 新增代码：
        Map map = new HashMap();
        map.put("msg",1);// 上传图片成功返回：码1
        map.put("filename",filename);// 存入文件名字
        map.put("filetype",photo.getContentType());// 新增：filetype文件类型的存储
        return map;
    }

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(User user){
        int n = userService.saveUser(user);
        if(n > 0){// 成功跳转success.jsp页面
            return "success.jsp";
        }
        return "fail.jsp";// 失败跳转到fail.jsp
    }

    @RequestMapping("/showUsers")
    @ResponseBody
    public List<User> showUsers(User user){
        List<User> users = userService.getUsers();
        return  users;
    }


    /**
     * 文件下载
     * @param req
     * @param resp
     * @param filename
     * @param filetype
     * @throws IOException
     */
    @RequestMapping(value="/download")
    public void download(HttpServletRequest req, HttpServletResponse resp, String filename, String filetype) throws IOException {
        // 设置响应头和响应类型
        // 设置以后，浏览器会出现下载图片的效果
        resp.setHeader("Content-Disposition","attachment;filename=" + filename);
        resp.setContentType(filetype);


        // 【1】先将图片从服务器读过来：
        String realPath = req.getServletContext().getRealPath("/images");// 获取图片在服务器中的真实路径
        InputStream is = new FileInputStream(realPath + "/" + filename );
        // 【2】再将读过来的读片写入到服务器中：
        // 下面这种写法是错误的，如果这样写，你点击下载是将图片下载到我的本地了
        // OutputStream os = new FileOutputStream("d:/demo.png");
        // 我们要做的是写入到当前你的客户端：
        ServletOutputStream os = resp.getOutputStream();

        // 将读进来的内容写出到你的本地
        int n = is.read();
        while(n != -1){
            os.write(n);
            n = is.read();
        }

        os.close();
        is.close();
    }


}
