package yjy.com.framelibrary.skin.support;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;



import java.util.ArrayList;
import java.util.List;

import yjy.com.framelibrary.skin.attr.SkinAttr;
import yjy.com.framelibrary.skin.attr.SkinType;

/**
 * Created by yjy on 2017/7/19.
 * skin的属性解析支持
 */
public class SkinAttrsSupport {

    private static final String TAG = "SkinAttrsSupport";

    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        //background textcolor src
        List<SkinAttr> mSkinAttrs = new ArrayList<>();

        int attrlength = attrs.getAttributeCount();
        for(int i =0;i<attrlength;i++){
            //获取属性名,属性值
            String attrsName = attrs.getAttributeName(i);
            String attrsValue = attrs.getAttributeValue(i);
//            Log.d(TAG,"attrsName:-->"+attrsName+"attrsValue:-->"+attrsValue);
            SkinType skinType = getSkinType(attrsName);
            if(skinType != null){
                //资源名称 只有@+int要转型
                String resName = getResName(context,attrsValue);
                if(TextUtils.isEmpty(resName)){
                    continue;
                }
                SkinAttr skinAttrs = new SkinAttr(resName,skinType);
                mSkinAttrs.add(skinAttrs);
            }
        }

        return null;
    }

    private static String getResName(Context context, String attrsValue) {
        if(attrsValue.startsWith("@")){
            attrsValue = attrsValue.substring(1);

            int resId = Integer.parseInt(attrsValue);

            return context.getResources().getResourceEntryName(resId);
        }
        return null;

    }

    //通过名称获取SkinType
    private static SkinType getSkinType(String attrsName) {
        SkinType[] skinTypes = SkinType.values();
        for(SkinType skinType : skinTypes){
            if(skinType.getResName().equals(attrsName)){
                return skinType;
            }
        }
        return null;
    }
}
