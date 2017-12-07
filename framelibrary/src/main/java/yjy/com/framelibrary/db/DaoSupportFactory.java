package yjy.com.framelibrary.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.io.File;

/**
 * Created by yjy on 2017/7/5.
 */
public class DaoSupportFactory {

    private static DaoSupportFactory mFractory;
    private SQLiteDatabase mSQLiteDatabase;


    private DaoSupportFactory(){
        //数据库放在内存里面,判断有无内存卡，申请权限
        File dbRoot = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
        +File.separator+"prject"+File.separator+"database");

        if(!dbRoot.exists()){
            dbRoot.mkdir();
        }

        File dbfile = new File(dbRoot,"project.db");
        mSQLiteDatabase =  SQLiteDatabase.openOrCreateDatabase(dbfile,null);


    }

    public static DaoSupportFactory getFactroy(){
        if(mFractory == null){
            synchronized(DaoSupportFactory.class){
                if(mFractory ==null){
                    mFractory = new DaoSupportFactory();
                }
            }
        }
        return mFractory;
    }

    public <T> IDaoSupport<T> getDao(Class clazz){
        IDaoSupport<T> daoSupport = new DaoSupport<T>();
        daoSupport.init(mSQLiteDatabase,clazz);
        return daoSupport;
    }
}
