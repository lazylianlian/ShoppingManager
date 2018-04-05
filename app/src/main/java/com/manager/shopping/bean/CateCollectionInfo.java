package com.manager.shopping.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by 文捷 on 2017/1/9.
 */

public class CateCollectionInfo extends BmobObject{
    private String id;
    private String title;
    private String albums;
    private UserInfo userInfo;

    public CateCollectionInfo() {
        super();
    }

    public CateCollectionInfo(String id, String title, String albums) {
        this.id = id;
        this.title = title;
        this.albums = albums;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }
    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
