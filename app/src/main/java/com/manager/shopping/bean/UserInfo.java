package com.manager.shopping.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by 文捷 on 2017/1/12.
 */

public class UserInfo extends BmobUser{
    private String userWord = "我的美食宣言";
    private BmobFile userHeadImg;
    public static UserInfo getCurrentUser(){
        UserInfo currentUser = BmobUser.getCurrentUser(UserInfo.class);

        return currentUser;
    }
    public String getUserWord() {
        return userWord;
    }

    public void setUserWord(String userWord) {
        this.userWord = userWord;
    }

    public BmobFile getUserHeadImg() {
        return userHeadImg;
    }

    public void setUserHeadImg(BmobFile userHeadImg) {
        this.userHeadImg = userHeadImg;
    }
}
