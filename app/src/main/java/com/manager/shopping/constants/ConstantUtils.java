package com.manager.shopping.constants;

/**
 * Created by 文捷 on 2017/1/1.
 */

public class ConstantUtils {
    //聚合数据根据菜谱名（menu）查询菜谱接口地址
    public static final String JUHE_CATE_URL = "http://apis.juhe.cn/cook/query.php";
    //聚合数据根据tag查询相应菜谱列表接口地址
    public static final String JUHE_TAG_SEARCH_URL = "http://apis.juhe.cn/cook/index";
    //聚合数据根据菜谱id查询相应菜谱列表接口地址
    public static final String JUHE_ID_SEARCH_URL = "http://apis.juhe.cn/cook/queryid";
    //聚合数据菜谱大全开发者Key
    public static final String JUHE_CATE_KEY = "ae6c8f3896c98c96cdcdde813e9030d2";
    //Bomb接口地址
    public static final String BOMB_URL = "";
    public static final String GOOD_BASE_URL = "http://45.62.100.14:8080/upload_ssm/QueryGoods";
    public static final String GOOD_COLLECT_URL = "http://45.62.100.14:8080/upload_ssm/QueryCollect";
    public static final String GOOD_ADD_COLLECT_URL = "http://45.62.100.14:8080/upload_ssm/AddCollect";
    public static final String GOOD_DEL_COLLECT_URL = "http://45.62.100.14:8080/upload_ssm/DelCollect";
}
