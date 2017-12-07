package yjy.com.framelibrary.db.curd;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import yjy.com.framelibrary.db.DaoUtils;

/**
 * Created by yjy on 2017/7/8.
 * 用于查询类
 * 存储方法
 */
public class QuerySupport<T> {
    //查询的列
    private String[] mQueryColumns;
    //查询条件
    private String mQuerySelection;
    //查询的参数
    private String[] mQueryArgs;
    //查询分组
    private String mQueryGroupBy;
    //查询结果过滤
    private String mQueryHaving;
    //查询排序
    private String mQueryOrderBy;
    //查询分页
    private String mQueryLimit;

    private Class mClass;
    private SQLiteDatabase mSQLiteDatabase;
    private ArrayMap<String,Method> mCusorMethodMap = new ArrayMap<>();

    public QuerySupport(SQLiteDatabase mSQLiteDatabase,Class<T>clazz ){
        this.mClass = clazz;
        this.mSQLiteDatabase = mSQLiteDatabase;
    }

    public QuerySupport columns(String[] columns){
        mQueryColumns = columns;
        return this;
    }

    public QuerySupport setSelection(String mQuerySelection){
        this.mQuerySelection = mQuerySelection;
        return this;
    }

    public QuerySupport setSelectionArg(String... mQueryArgs){
        this.mQueryArgs = mQueryArgs;
        return this;
    }

    public QuerySupport groupBy(String mQueryGroupBy){
        this.mQueryGroupBy = mQueryGroupBy;
        return this;
    }

    public QuerySupport having(String having){
        this.mQueryHaving = having;
        return this;
    }

    public QuerySupport orderby(String mQueryOrderBy){
        this.mQueryOrderBy = mQueryOrderBy;
        return this;
    }

    public QuerySupport limit(String mQueryLimit){
        this.mQueryLimit = mQueryLimit;
        return this;
    }

    //将所有参数组合起来,查询所有
    public List<T> queryAll() {
        mSQLiteDatabase.beginTransaction();
        Cursor cursor = mSQLiteDatabase.query(DaoUtils.getTableName(mClass),null,null,null,null,null,null,null);
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
        return cursorToList(cursor);
    }

    //查询特定语句
    public List<T> query(){
        mSQLiteDatabase.beginTransaction();
        Cursor cursor = mSQLiteDatabase.query(DaoUtils.getTableName(mClass),mQueryColumns,mQuerySelection,
                mQueryArgs,mQueryGroupBy,mQueryHaving,mQueryOrderBy,mQueryLimit);
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();

        return cursorToList(cursor);
    }

    //清空参数
    public void clearQueryParams(){
        //查询的列
         mQueryColumns = null;
         mQuerySelection = null;
        mQueryArgs = null;
         mQueryGroupBy = null;
        mQueryHaving = null;
        mQueryOrderBy = null;
        mQueryLimit = null;
    }

    private List<T> cursorToList(Cursor cursor) {
        List<T> list = new ArrayList<>();
        if(cursor !=null &&cursor.moveToFirst()){
            do{
                try{
                    T instance =(T)mClass.newInstance();
                    Field[] fields = mClass.getDeclaredFields();
                    for(Field field :fields){
                        //遍历属性
                        field.setAccessible(true);
                        String name = field.getName();
                        //获取角标
                        int index = cursor.getColumnIndex(name);
                        if(index == 1){
                            continue;
                        }


                        Method cusorMethod = cursorMethod(field.getType());
                        if(cursor !=null){
                            Object value = cusorMethod.invoke(cursor,index);
                            if(value == null){
                                continue;
                            }

                            //处理一些特殊部分,如boolean,char转化为对应的符号
                            if(field.getType() == boolean.class || field.getType() == Boolean.class){
                                if("0".equals(String.valueOf(value))){
                                    value = false;
                                }else if("1".equals(String.valueOf(value))){
                                    value = true;
                                }
                            }else if(field.getType() == char.class || field.getType() == Character.class){
                                value = ((String)value).charAt(0);
                            }else if(field.getType() == Date.class){
                                long date = (long) value;
                                if(date <=0){
                                    value = null;
                                }else {
                                    value = new Date(date);
                                }
                            }
                            field.set(instance,value);
                        }

                    }
                    list.add(instance);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }while (cursor.moveToNext());
        }
        cursor.close();
        clearQueryParams();
        return list;
    }

    private Method cursorMethod(Class<?> type) throws Exception {
        Method method = null;
        String methodName = getColumnMethodName(type);
        //此处做缓存处理
        if(mCusorMethodMap.get(methodName) == null){
            method = Cursor.class.getMethod(methodName,int.class);
            mCusorMethodMap.put(methodName,method);
        }else {
            method = mCusorMethodMap.get(methodName);
        }
        return method;
    }

    private String getColumnMethodName(Class<?> type) {
        String typeName = "";
        if(type.isPrimitive()){
            typeName = DaoUtils.captialize(type.getName());
        }else {
            typeName = type.getSimpleName();
        }
        String methodName = "get" + typeName;
        if("getBoolean".equals(methodName)){
            methodName = "getInt";
        }else if("getChar".equals(methodName) || "geCharacter".equals(methodName)){
            methodName = "getString";
        }else if("getDate".equals(methodName)){
            methodName = "getLong";
        }else if("getInteger".equals(methodName)){
            methodName = "getInt";
        }
        return methodName;
    }








}
