package com.example.buy2bidv2.Model;

public class User {
    private String fullname;
    private String id;
    private String imageurl;
    private String activeFlag;

    public User(String id,String fullname,String imageurl,String activeFlag) {
        this.id=id;
        this.fullname=fullname;
        this.imageurl=imageurl;
        this.activeFlag=activeFlag;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getactiveFlag()
    {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag)
    {
        this.activeFlag=activeFlag;
    }

    public User() {}
}
