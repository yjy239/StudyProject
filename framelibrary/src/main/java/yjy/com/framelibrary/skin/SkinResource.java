package yjy.com.framelibrary.skin;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import java.lang.reflect.Method;

/**
 * Created by yjy on 2017/7/19.
 * 皮肤资源
 */
public class SkinResource {

    //皮肤资源
    private Resources mSkinResources;
    private String mPackageName;

    //加载资源
    public SkinResource(Context context, String skinPath) {
        try{
            Resources superRes = context.getResources();
            AssetManager asset = null;
            asset = AssetManager.class.newInstance();
            Method method = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            method.invoke(asset,skinPath);
            mSkinResources = new Resources(asset,superRes.getDisplayMetrics(),superRes.getConfiguration());
            //获取包名
            mPackageName = context.getPackageManager().getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
            .applicationInfo.packageName;
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //通过名字获取drawable
    public Drawable getDrawableByName(String resName){
        try{
            int resId =  mSkinResources.getIdentifier(resName,"drawable",mPackageName);
            Drawable drawable = mSkinResources.getDrawable(resId);
            return drawable;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }



    }

    //通过名字获取颜色
    public ColorStateList getColorByName(String resName){
        try{
            int resId =  mSkinResources.getIdentifier(resName,"color",mPackageName);
            ColorStateList color = mSkinResources.getColorStateList(resId);
            return color;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
