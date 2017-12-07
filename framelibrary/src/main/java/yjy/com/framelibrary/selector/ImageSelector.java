package yjy.com.framelibrary.selector;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by asus on 2017/8/5.
 */
public class ImageSelector {
    public static final String EXTRA_RESULT = SelectImageActivity.EXTRA_RESULT;

    private boolean mShowCamera = true;
    private int mMaxCount = 9;
    private int mMode = SelectImageActivity.MODE_MULTI;
    private ArrayList<String> mOriginData;
    private static ImageSelector sSelector;

    @Deprecated
    private ImageSelector(Context context){

    }

    private ImageSelector(){}

    @Deprecated
    public static ImageSelector create(Context context){
        if(sSelector == null){
            sSelector = new ImageSelector(context);
        }
        return sSelector;
    }

    public static ImageSelector create(){
        if(sSelector == null){
            sSelector = new ImageSelector();
        }
        return sSelector;
    }

    public ImageSelector showCamera(boolean show){
        mShowCamera = show;
        return sSelector;
    }

    public ImageSelector count(int count){
        mMaxCount = count;
        return sSelector;
    }

    public ImageSelector single(){
        mMode = SelectImageActivity.MODE_SINGLE;
        return sSelector;
    }

    public ImageSelector multi(){
        mMode = SelectImageActivity.MODE_MULTI;
        return sSelector;
    }

    public ImageSelector origin(ArrayList<String> images){
        mOriginData = images;
        return sSelector;
    }

    public void start(Activity activity, int requestCode){
        final Context context = activity;
        if(hasPermission(context)) {
            activity.startActivityForResult(createIntent(context), requestCode);
        }else{
            Toast.makeText(context, "请允许权限", Toast.LENGTH_SHORT).show();
        }
    }

    public void start(Fragment fragment, int requestCode){
        final Context context = fragment.getContext();
        if(hasPermission(context)) {
            fragment.startActivityForResult(createIntent(context), requestCode);
        }else{
            Toast.makeText(context, "请允许权限", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean hasPermission(Context context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            // Permission was added in API Level 16
            return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private Intent createIntent(Context context){
        Intent intent = new Intent(context, SelectImageActivity.class);
        intent.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA, mShowCamera);
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT, mMaxCount);
        if(mOriginData != null){
            intent.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFALUT_SELECT_LIST, mOriginData);
        }
        intent.putExtra(SelectImageActivity.EXTRA_SELECT_MODE, mMode);
        return intent;
    }
}
