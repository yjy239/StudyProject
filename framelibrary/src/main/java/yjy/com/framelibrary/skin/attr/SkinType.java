package yjy.com.framelibrary.skin.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import yjy.com.framelibrary.skin.SkinManager;
import yjy.com.framelibrary.skin.SkinResource;

/**
 * Created by asus on 2017/7/19.
 */
public enum  SkinType {

    TEXT_COLOR("textColor") {
        @Override
        public void skin(View v, String resName) {
            SkinResource skinResource = getSkinResources();
            ColorStateList color = skinResource.getColorByName(resName);
            if(color==null){
                return;
            }
            TextView textview = (TextView) v;
            textview.setTextColor(color);
        }
    },BACKGROUND("background") {
        @Override
        public void skin(View v, String resName) {
            //背景可能是图片也可能是颜色
            SkinResource skinResource = getSkinResources();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if(drawable!=null){
                ImageView iv = (ImageView) v;
                iv.setBackgroundDrawable(drawable);
                return;
            }
            //可能是颜色
            ColorStateList color = skinResource.getColorByName(resName);
            if(color !=null){
                v.setBackgroundColor(color.getDefaultColor());
            }


        }
    },SRC("src") {
        @Override
        public void skin(View v, String resName) {
            SkinResource skinResource = getSkinResources();
            Drawable drawable = skinResource.getDrawableByName(resName);
            if(drawable!=null){
                ImageView iv = (ImageView) v;
                iv.setImageDrawable(drawable);
                return;
            }
        }
    };

    public SkinResource getSkinResources() {
        return SkinManager.getInstance().getSkinResources();
    }

    private String mResName;

   SkinType(String resName){

    }

    public abstract void skin(View v, String resName) ;

    public String getResName() {
        return mResName;
    }


}
