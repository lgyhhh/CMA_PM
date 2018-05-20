package com.example.admin.cma;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/6.
 */

public class StaffFile implements Serializable{
    private long key;
    private String name;//名称
    private String department;//部门
    private String position;//职位
    private String fileId;//档案编号
    private String location;//档案位置
    private String fileImage;//档案图片

    public  StaffFile(long key,String name,String department,String position,String fileId,String location,String fileImage){
        this.key=key;
        this.name=name;
        this.department=department;
        this.position=position;
        this.fileId=fileId;
        this.location=location;
        this.fileImage=fileImage;//uri
    }

    public long getKey(){return key;}

    public void setKey(long key){
        this.key=key;
    }
    public  String getName(){return name;}

    public void setName(String name){this.name=name;}


    public  String getDepartment(){return department;}

    public void setDepartment(String department){this.department=department;}


    public  String getPosition(){return position;}

    public void setPosition(String position){this.position=position;}


    public  String getFileId(){return fileId;}

    public void setId(String fileId){this.fileId=fileId;}


    public  String getLocation(){return location;}

    public void setLocation(String location){this.location=location;}

    public String getFileImage() {return  fileImage;}

    public void setFileImage(String fileImage) {
        this.fileImage = fileImage;
    }

}
