package com.gk.pojo;

public class Clazz {
    private String cname;
    private Integer cid;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "Clazz{" +
                "cname='" + cname + '\'' +
                ", cid=" + cid +
                '}';
    }
}
