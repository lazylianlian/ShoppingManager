package com.manager.shopping.service;

import com.manager.shopping.bean.GoodInfo;
import com.manager.shopping.utils.JsonUtil;

import org.json.JSONObject;

/**
 * Created by 文捷 on 2018/4/21.
 */

public class GoodInfoService {
    public static GoodInfo getGootInfoFromJson(JSONObject object){
        GoodInfo goodInfo = new GoodInfo();
        String goodName = JsonUtil.getString(object,"goodname");
        int comments = JsonUtil.getInt(object,"comments");
        int views = JsonUtil.getInt(object,"views");
        int sells = JsonUtil.getInt(object,"sells");
        String link = JsonUtil.getString(object,"link");
        String goodtype = JsonUtil.getString(object,"goodtype");
        String source = JsonUtil.getString(object,"source");
        String goodImage = JsonUtil.getString(object,"goodImage");
        String descripe = JsonUtil.getString(object,"descripe");
        String goodprice = JsonUtil.getString(object,"goodprice");
        String goodid = JsonUtil.getString(object,"goodid");
        goodInfo.setGoodid(goodid);
        goodInfo.setGoodname(goodName);
        goodInfo.setViews(views);
        goodInfo.setSells(sells);
        goodInfo.setLink(link);
        goodInfo.setGoodtype(goodtype);
        goodInfo.setSource(source);
        goodInfo.setGoodImage(goodImage);
        goodInfo.setDescripe(descripe);
        goodInfo.setGoodprice(goodprice);
        goodInfo.setComments(comments);

        return goodInfo;
    }
}
