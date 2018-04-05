package com.manager.shopping.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by 文捷 on 2017/1/9.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String TABLENAME = "cate_collection";
    private String sql = "create table cate_collection(id integer primary key not null,title varhar,albums varchar,checked integer)";

    public DBHelper(Context context) {
        super(context, "cate_collection.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i<i1){
            //删除原来的表格
            sqLiteDatabase.execSQL("drop table cate_collection if exit");
        }else{
            sqLiteDatabase.execSQL(sql);
        }
    }

}
