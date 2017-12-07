package yjy.com.framelibrary.http;

import java.util.List;

import yjy.com.baselibrary.utils.MD5;
import yjy.com.framelibrary.db.DaoSupportFactory;
import yjy.com.framelibrary.db.IDaoSupport;

/**
 * Created by asus on 2017/7/13.
 */
public class CacheUtils {

    public static String getCacheJson(String joinUrl){
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactroy().getDao(CacheData.class);
        List<CacheData> cacheDatas = dataDaoSupport.querySupport().setSelection("mUrlKey = ?").setSelectionArg(MD5.stringToMD5(joinUrl)).query();
        //有数则立即回调
        if(cacheDatas.size() != 0){
            CacheData cacheData = cacheDatas.get(0);
            String result = cacheData.getmResultJson();
            //已经有缓存
            return result;
        }
        return null;
    }

    public static long cacheData(String joinUrl,String requestResult){
        final IDaoSupport<CacheData> dataDaoSupport = DaoSupportFactory.getFactroy().getDao(CacheData.class);
        dataDaoSupport.delete("mUrlKey = ?", MD5.stringToMD5(joinUrl));
        long number = dataDaoSupport.insert(new CacheData(joinUrl,MD5.stringToMD5(requestResult)));
        return number;
    }
}
