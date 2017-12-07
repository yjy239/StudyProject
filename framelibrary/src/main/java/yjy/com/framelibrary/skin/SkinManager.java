package yjy.com.framelibrary.skin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;



import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import yjy.com.framelibrary.skin.attr.SkinView;
import yjy.com.framelibrary.skin.callback.ISkinChangeListener;
import yjy.com.framelibrary.skin.config.SkinConfig;
import yjy.com.framelibrary.skin.config.SkinPreUtils;

/**
 * Created by yjy on 2017/7/19.
 * 皮肤管理类
 */
public class SkinManager {

    private static SkinManager mInstance;
    private Context mContext;
    private SkinResource mSkinResources;
    private Map<ISkinChangeListener,List<SkinView>> mSkinViews = new HashMap<>();

    static{
        mInstance = new SkinManager();
    }

    public static SkinManager getInstance() {
        return mInstance;
    }

    public void init(Context context){
        mContext = context.getApplicationContext();
        //防止皮肤被删除
        String currentSkinPath = SkinPreUtils.getInstance(context).getSkinPath();
        File file = new File(currentSkinPath);
        if(!file.exists()){
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }

        String packageName = context.getPackageManager().getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES)
                .applicationInfo.packageName;
        if(TextUtils.isEmpty(packageName)){
            SkinPreUtils.getInstance(context).clearSkinInfo();
            return;
        }

        //校验签名

        //重新初始化
        mSkinResources = new SkinResource(mContext,currentSkinPath);
}

    //加载皮肤
    public int loadSkin(String skinPath) {
        //判断当前皮肤是否一样，一样则不换
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        File file = new File(currentSkinPath);
        if(!file.exists()){
            return SkinConfig.SKIN_NOTEXIST;
        }
        String packageName = mContext.getPackageManager().getPackageArchiveInfo(currentSkinPath, PackageManager.GET_ACTIVITIES)
                .applicationInfo.packageName;
        if(TextUtils.isEmpty(packageName)){
            return  SkinConfig.SKIN_ERROR;
        }

        if(skinPath.equals(skinPath)){
            return SkinConfig.SKIN_NOTHING;
        }
        //校验签名
        //初始化资源
        mSkinResources = new SkinResource(mContext,skinPath);

        changeSkin();
        //改变资源
        saveSkin(skinPath);
     return 0;
    }

    /*****
     * 改变皮肤*/
    private void changeSkin() {
        Set<ISkinChangeListener> keys =  mSkinViews.keySet();
        for(ISkinChangeListener key : keys){
            List<SkinView> skinViews = mSkinViews.get(key);
            for(SkinView skinView : skinViews){
                skinView.skin();
            }
            key.changeSkin(mSkinResources);
        }

    }

    private void saveSkin(String skinPath) {
        SkinPreUtils.getInstance(mContext).saveSkinPath(skinPath);
    }

    //皮肤默认
    public int restoreSkin() {
        //判断当前有没有皮肤,没有则return
        String currentSkinPath = SkinPreUtils.getInstance(mContext).getSkinPath();
        if(TextUtils.isEmpty(currentSkinPath)){
            return SkinConfig.SKIN_NOTHING;
        }
        //当前运行app路径
        String skinPath = mContext.getPackageResourcePath();
        mSkinResources = new SkinResource(mContext,skinPath);
        //改变资源
        changeSkin();
//        Set<Activity> keys =  mSkinViews.keySet();
//        for(Activity key : keys){
//            List<SkinView> skinViews = mSkinViews.get(key);
//            for(SkinView skinView : skinViews){
//                skinView.skin();
//            }
//        }
        SkinPreUtils.getInstance(mContext).clearSkinInfo();

        return SkinConfig.SKIN_SUCCESS;
    }

    //获取Activity对应的SkinView
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViews.get(activity);
    }

    public void register(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViews.put(skinChangeListener,skinViews);
    }

    //获取当前的皮肤资源
    public SkinResource getSkinResources(){
        return mSkinResources;
    }

    //检测是否换肤
    public void checkChangeSkin(SkinView mSkinView) {
        //如果当前有皮肤,则换一下皮肤
        String currentSkinPath  = SkinPreUtils.getInstance(mContext).getSkinPath();
        if(TextUtils.isEmpty(currentSkinPath)){
            mSkinView.skin();
        }
    }

    /***
     * 防止内存泄露**/
    public void unregister(ISkinChangeListener skinChangeListener) {
        mSkinViews.remove(skinChangeListener);
    }
}
