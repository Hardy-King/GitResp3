package com.gk.pojo;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Date;

public class User {

    private String name;
    private Integer age;
    private String pwd;
    private String sex;
    private String[] hobby;

    private Clazz clazz;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    public User() {
        System.out.println("空构造器被调用了");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        System.out.println("setName方法被调用了");
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        System.out.println("setAge方法被调用了");
        this.age = age;
    }

    public String getPwd() {

        return pwd;
    }

    public void setPwd(String pwd) {
        System.out.println("setPwd方法被调用了");
        this.pwd = pwd;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        System.out.println("setSex方法被调用了");
        this.sex = sex;
    }

    public String[] getHobby() {
        return hobby;
    }

    public void setHobby(String[] hobby) {
        System.out.println("setHobby方法被调用了");
        this.hobby = hobby;
    }

    public Clazz getClazz() {
        return clazz;
    }

    public void setClazz(Clazz clazz) {
        System.out.println("setClazz方法被调用了");
        this.clazz = clazz;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
