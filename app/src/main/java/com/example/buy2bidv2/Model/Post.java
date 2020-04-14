package com.example.buy2bidv2.Model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Post {
    private String postId;
    private String postImage;
    private String postTitle;
    private String lastDate;
    private String description;
    private String publisher;
    private String activeFlag;
    private String isVerified;
    private String expectedPrice;

    public Post(String postId, String postImage,String postTitle, String lastDate,String description, String expectedPrice, String publisher, String activeFlag, String isVerified)
    {
        this.postId=postId;
        this.postImage=postImage;
        this.postTitle=postTitle;
        this.lastDate=lastDate;
        this.description=description;
        this.expectedPrice=expectedPrice;
        this.publisher=publisher;
        this.activeFlag=activeFlag;
        this.isVerified=isVerified;
    }
     public Post() {

     }

    public String getLastDate(){return lastDate;}
    public void setLastDate(String lastDate){this.lastDate=lastDate; }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public long days(String currentDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        LocalDate current = LocalDate.parse(currentDate,formatter);
        LocalDate last = LocalDate.parse(lastDate,formatter);
            return ChronoUnit.DAYS.between(current,last);
        }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPostimage()
    {
        return postImage;
    }
    public void setPostimage(String postimage)
    {
        this.postImage=postimage;
    }
    public String getPostTitle()
    {
        return postTitle;
    }
    public void setPostTitle(String postTitle)
    {
        this.postTitle=postTitle;
    }
    public String getPostId()
    {
        return postId;
    }
    public void setPostId(String postId)
    {
        this.postId=postId;
    }

    public String getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(String expectedPrice) {
        this.expectedPrice = expectedPrice;
    }
    public String getactiveFlag()
    {
        return activeFlag;
    }
    public void setActiveFlag(String activeFlag)
    {
        this.activeFlag=activeFlag;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsisVerified() {
        this.isVerified = isVerified;
    }
}
