package com.gk.controller;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@MultipartConfig
public class UploadController {

    @RequestMapping(value="/upload")
    @ResponseBody
    public String upload(MultipartFile photo) throws IOException {//photo接收前台传过来的文件对象
        // 将传过来的图片放在d盘目录下,名字为原始资源名称
        File dir = new File("D:/images/");
        // 如果不存在则创建目录
        if(!dir.exists()){
            dir.mkdirs();
        }
        // 获取文件原始名字
        String originalFilename = photo.getOriginalFilename();
        // 文件存储位置拼接：意味着：存储在D:/images/a.png
        File file =new File(dir,originalFilename);
        //  文件保存
        photo.transferTo(file);
        System.out.println("test file upload...");
        return "ok";
    }

    @RequestMapping("/upload2")
    @ResponseBody
    public String upload2(MultipartFile photo, HttpServletRequest req) throws IOException {
        // 把文件放在服务器目录：tomcat服务器下的路径 D:\Java\Tomcat\tomcat10\webapps\demo2\images
        String realPath = req.getServletContext().getRealPath("/images");
        System.out.println(realPath);

        File dir = new File(realPath);
        if (!dir.exists()){// 如果目录不存在，创建目录
            dir.mkdirs();
        }

        // 获取上传图片的原始名字
        String originalFilename = photo.getOriginalFilename();

        // 文件存储位置拼接：存储d:/images/拼接图片原始名字 sm05.png
        // d:/images/sm05.png
        File file = new File(dir,originalFilename);
        // 文件保存：
        photo.transferTo(file);

        return "ok";
    }

    /**
     * 获取上传文件的原始名字，生成唯一文件名字，保存到服务器目录,防止文件名字重复而被替换掉
     * @param photo
     * @param req
     * @return
     * @throws IOException
     */

    @RequestMapping("/upload3")
    @ResponseBody
    public String upload3(MultipartFile photo, HttpServletRequest req) throws IOException {
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

        return "ok";
    }

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
        // 新增代码：
        return map;
    }
}
