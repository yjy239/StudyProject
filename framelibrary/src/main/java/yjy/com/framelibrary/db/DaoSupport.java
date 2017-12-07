package yjy.com.framelibrary.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import yjy.com.framelibrary.db.curd.QuerySupport;

/**
 * Created by asus on 2017/7/5.
 */
public class DaoSupport<T> implements IDaoSupport<T> {

    private SQLiteDatabase mSQLiteDatabase;
    private Class<T> clazz;
    private static final String TAG = "DaoSupport";
    private static final String CREATETABLE1 = "create table if not exists";
    private static final String CREATETABLE2 = "(id integer primary key autoincrement,";

    private static final Object[] mPutMethodArg = new Object[2];
    private static final Map<String,Method> mPutMethodMap = new ArrayMap<>();
    private QuerySupport<T> mQuerySupport;

    public void init(SQLiteDatabase mSQLiteDatabase,Class<T> clazz){
        this.mSQLiteDatabase = mSQLiteDatabase;
        this.clazz = clazz;

        //创建表
        StringBuffer buffer = new StringBuffer();
        buffer.append(CREATETABLE1);
        buffer.append(DaoUtils.getTableName(clazz)).append(CREATETABLE2);
        //反射获取对应的类里面的参数
        Field[] fields = clazz.getDeclaredFields();
        for(Field field :fields){
            field.setAccessible(true);
            String name = field.getName();
            //转换类型
            String type = field.getType().getSimpleName();
            //int ->Integer String->text
            buffer.append(name).append(DaoUtils.getColumnType(type)).append(", ");
        }
        buffer.replace(buffer.length()-2,buffer.length(),");");
        Log.e(TAG,"数据库语句-->"+buffer.toString());
        mSQLiteDatabase.execSQL(buffer.toString());
    }

    @Override
    public long insert(T obj) {
        ContentValues values = contentValuesByObj(obj);
        return mSQLiteDatabase.insert(DaoUtils.getTableName(clazz),null,values);
    }

    @Override
    public void insert(List<T> datas) {
        mSQLiteDatabase.beginTransaction();
        for(T data :datas){
            insert(data);
        }
        mSQLiteDatabase.setTransactionSuccessful();
        mSQLiteDatabase.endTransaction();
    }

    @Override
    public QuerySupport querySupport() {
        if(mQuerySupport == null){
            synchronized (DaoSupport.class){
                if(mQuerySupport == null){
                    mQuerySupport = new QuerySupport<T>(mSQLiteDatabase,clazz);
                }
            }
        }
        return mQuerySupport;
    }


    @Override
    public int update(T obj, String whereClause, String... whereArg) {
        ContentValues values = contentValuesByObj(obj);
        return mSQLiteDatabase.update(DaoUtils.getTableName(clazz),values,whereClause,whereArg);
    }



    @Override
    public int delete(String whereClause, String... whereArg) {
        return mSQLiteDatabase.delete(DaoUtils.getTableName(clazz),whereClause,whereArg);
    }


    private ContentValues contentValuesByObj(T obj) {
        ContentValues values = new ContentValues();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            try {
                field.setAccessible(true);
                String name = field.getName();
                Object value = field.get(obj);
                //反射执行put操作
                mPutMethodArg[0] = name;
                mPutMethodArg[1] = value;

                String fieldName = field.getType().getName();
                Method putMethod = mPutMethodMap.get(fieldName);
                if(putMethod == null){
                    putMethod = ContentValues.class.getDeclaredMethod("put", String.class, value.getClass());
                    mPutMethodMap.put(fieldName,putMethod);
                }


                //优化缓存方法
                putMethod.invoke(values,mPutMethodArg);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                mPutMethodArg[0] = null;
                mPutMethodArg[1] = null;
            }

        }
        return values;
    }

}
