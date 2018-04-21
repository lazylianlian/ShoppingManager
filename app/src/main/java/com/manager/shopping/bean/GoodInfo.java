package com.manager.shopping.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by 文捷 on 2018/4/7.
 */

public class GoodInfo extends BmobObject implements Serializable {

    /**
     * goodname : 华为荣耀畅玩7A
     * comments : 11000
     * sells : 20000
     * link : https://detail.tmall.com/item.htm?spm=a230r.1.14.3.3fa26fbe3azXrG&id=566514157182&cm_id=140105335569ed55e27b&abbucket=6&sku_properties=10004:827902415;5919063:6536025
     * goodtype : 2
     * goodid : 402881e76296791d016296791dea0000
     * source : 1
     * views : 1000
     * goodprice : 799
     */
    private UserInfo userInfo;
    private String goodname;
    private int comments;
    private int sells;
    private int views;
    private String link;
    private String goodtype;
    private String goodid;
    private String source;
    private String goodprice;
    private String descripe;
    private String goodImage;

    public String getDescripe() {
        return descripe;
    }

    public GoodInfo() {
    }

    public void setDescripe(String descripe) {
        this.descripe = descripe;
    }

    public String getGoodImage() {
        return goodImage;
    }

    public void setGoodImage(String goodImage) {
        this.goodImage = goodImage;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getGoodname() {
        return goodname;
    }

    public void setGoodname(String goodname) {
        this.goodname = goodname;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getSells() {
        return sells;
    }

    public void setSells(int sells) {
        this.sells = sells;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getGoodtype() {
        return goodtype;
    }

    public void setGoodtype(String goodtype) {
        this.goodtype = goodtype;
    }

    public String getGoodid() {
        return goodid;
    }

    public void setGoodid(String goodid) {
        this.goodid = goodid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String getGoodprice() {
        return goodprice;
    }

    public void setGoodprice(String goodprice) {
        this.goodprice = goodprice;
    }

    public GoodInfo(String goodname, int comments, int sells, int views, String link, String goodtype, String goodid, String source, String goodprice, String descripe, String goodImage) {
        this.goodname = goodname;
        this.comments = comments;
        this.sells = sells;
        this.views = views;
        this.link = link;
        this.goodtype = goodtype;
        this.goodid = goodid;
        this.source = source;
        this.goodprice = goodprice;
        this.descripe = descripe;
        this.goodImage = goodImage;
    }
}
