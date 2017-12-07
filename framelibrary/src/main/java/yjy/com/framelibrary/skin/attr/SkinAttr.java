package yjy.com.framelibrary.skin.attr;

import android.view.View;

/**
 * Created by asus on 2017/7/19.
 */
public class SkinAttr {
    private String mReName;
    private SkinType mType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mReName = resName;
        this.mType = skinType;
    }

    public void skin(View v){
        mType.skin(v,mReName);
    }
}
