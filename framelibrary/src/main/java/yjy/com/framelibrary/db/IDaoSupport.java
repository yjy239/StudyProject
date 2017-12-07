package yjy.com.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import yjy.com.framelibrary.db.curd.QuerySupport;

/**
 * Created by asus on 2017/7/5.
 */
public interface IDaoSupport<T> {

    void init(SQLiteDatabase mSQLiteDatabase, Class<T> clazz);

    //插入操作
    public long insert(T t);

    //批量插入
    void insert(List<T> datas);


    //获取查询支持类
    QuerySupport querySupport();

    //更新
    int update(T obj, String whereClause, String... whereArg);

    //删除
    int delete(String whereClause, String... whereArg);
}
