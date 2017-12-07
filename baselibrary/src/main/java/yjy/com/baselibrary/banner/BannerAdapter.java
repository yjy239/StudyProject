package yjy.com.baselibrary.banner;

import android.view.View;

/**
 * Created by asus on 2017/7/27.
 */
public abstract class BannerAdapter {

    /**根据位置获取子view***/
    public abstract View getView(int position,View convertView) ;

    /***获取自view的数量**/
    public abstract int getCount();

    /**获取广告描述***/
    public  String getBannerDesc(int position){
        return "";
    }
}
