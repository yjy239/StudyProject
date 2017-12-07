package yjy.com.framelibrary.skin.config;

import android.content.Context;

/**
 * Created by asus on 2017/7/20.
 */
public class SkinPreUtils {

    private static SkinPreUtils mInstance;
    private Context mContext;

    private SkinPreUtils(Context context){
        mContext = context;
    }

    public static SkinPreUtils getInstance(Context context){
        if(mInstance == null){
            synchronized (SkinPreUtils.class){
                if(mInstance == null){
                    mInstance = new SkinPreUtils(context);
                }
            }
        }
        return mInstance;
    }

    /***
     * 保存当前皮肤路径*/
    public void saveSkinPath(String path){
        mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .edit().putString(SkinConfig.SKIN_INFO_PATH,path).commit();

    }

    /**
     * 获取当前皮肤路径**/
    public String getSkinPath(){
        return mContext.getSharedPreferences(SkinConfig.SKIN_INFO_NAME,Context.MODE_PRIVATE)
                .getString(SkinConfig.SKIN_INFO_PATH,"");
    }

    /**
     * 清空皮肤路径**/
    public void clearSkinInfo() {
        saveSkinPath("");
    }
}
