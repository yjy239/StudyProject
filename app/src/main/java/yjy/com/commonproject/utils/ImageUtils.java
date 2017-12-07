package yjy.com.commonproject.utils;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by asus on 2017/8/6.
 */

public class ImageUtils {

    static{
        System.loadLibrary("compress-lib");
    }

    public static native void comproess(Context context, Bitmap bitmap,int quality,String fileNameStr);

    public static native void test(int i,String s);
}
