package com.manager.shopping.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.manager.shopping.bean.CateCollectionInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 文捷 on 2017/1/9.
 */

public class DBManager {
    private static DBManager manager;
    private SQLiteDatabase writeDB;
    private SQLiteDatabase readDB;

    private DBHelper helper;
    private Context context;
    private DBManager(Context context){
        this.context = context;
        helper = new DBHelper(context);
        writeDB = helper.getWritableDatabase();
        readDB = helper.getReadableDatabase();
    }
    //对外提供本类对象
    public static DBManager getInstance(Context context){
        if (manager ==null){
            manager = new DBManager(context);
        }
        return manager;
    }
    public boolean addCateCollecInfo(CateCollectionInfo cInfo){
        if (isCollected(cInfo.getId())==false){
            if (delCateCollecInfo(cInfo.getId())==true){
                return false;
            }else{
                return true;
            }
        }
        ContentValues values = new ContentValues();
        values.put("id",cInfo.getId());
        values.put("title",cInfo.getTitle());
        values.put("albums",cInfo.getAlbums());
        writeDB.insert(DBHelper.TABLENAME,null,values);
        return true;
    }
    public boolean isCollected(String id){
        String sql = "select * from "+DBHelper.TABLENAME+" where id ="+id;
        Cursor cursor = readDB.rawQuery(sql,null);
        if (cursor.moveToNext()){
            //数据库中已存在
            return false;
        }else{
            //数据库中没有
            return true;
        }
    }
    public List<CateCollectionInfo> findCateCollecInfo(){
        List<CateCollectionInfo> list = new ArrayList<>();
        Cursor cursor = readDB.query(DBHelper.TABLENAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String albums = cursor.getString(cursor.getColumnIndex("albums"));
            int checked = cursor.getInt(cursor.getColumnIndex("checked"));
            CateCollectionInfo collectionInfo = new CateCollectionInfo(id,title,albums);
            list.add(collectionInfo);
        }
        return list;
    }
    public boolean delCateCollecInfo(String id){
        writeDB.delete(DBHelper.TABLENAME,"id=?",new String[]{id});
        return true;
    }
}
