package yjy.com.framelibrary.selector;

import android.text.TextUtils;

/**
 * Created by asus on 2017/8/5.
 */
public class ImageEntity {

    public String path;
    public String name;
    public long time;


    public ImageEntity(String path, String name, long time) {
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof ImageEntity){
            ImageEntity compare = (ImageEntity)o;
            return TextUtils.equals(this.path,compare.path);
        }
        return false;
    }

}
