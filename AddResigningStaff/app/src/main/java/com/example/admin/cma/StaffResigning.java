package com.example.admin.cma;

import java.io.Serializable;

/**
 * Created by new on 2018/5/23.
 */

public class StaffResigning implements Serializable {
    private long  key;
    private String name;          //姓名
    private String title;         //职称
    private String degree;        //文化程度
    private String major;         //所学专业
    private String department;    //所在部门
    private String position;      //岗位
    private String resignDate;    //离任日期

    public StaffResigning(long key,String name,String title,String degree,String major,String department,String position,String resignDate)
    {
         this.key=key;
         this.name=name;
         this.title=title;
         this.degree=degree;
         this.major=major;
         this.department=department;
         this.position=position;
         this.resignDate=resignDate;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getResignDate() {
        return resignDate;
    }

    public void setResignDate(String resignDate) {
        this.resignDate = resignDate;
    }
}
