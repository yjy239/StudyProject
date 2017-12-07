package yjy.com.commonproject;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import yjy.com.framelibrary.BaseSkinActivity;
import yjy.com.framelibrary.selector.ImageSelector;
import yjy.com.framelibrary.selector.SelectImageActivity;

/**
 * Created by asus on 2017/8/3.
 */
public class TestImageActivity extends BaseSkinActivity {

    private ArrayList<String> mImageList;
    private static int RESULT = 0;


    @Override
    public void setContentView() {
        setContentView(R.layout.test_image_layout);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initTitle() {

    }

    public void selectImage(View v){
        ImageSelector.create()
                .multi()
                .showCamera(true)
                .count(8)
                .origin(mImageList)
                .start(TestImageActivity.this,RESULT);
//        Intent i = new Intent(TestImageActivity.this, SelectImageActivity.class);
//        i.putExtra(SelectImageActivity.EXTRA_SELECT_COUNT,9);
//        i.putExtra(SelectImageActivity.EXTRA_SELECT_MODE,SelectImageActivity.MODE_MULTI);
//        i.putStringArrayListExtra(SelectImageActivity.EXTRA_DEFALUT_SELECT_LIST,mImageList);
//        i.putExtra(SelectImageActivity.EXTRA_SHOW_CAMERA,true);
//        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == RESULT){
                Log.e("result",data.getStringArrayListExtra(SelectImageActivity.EXTRA_DEFALUT_SELECT_LIST)+"");
            }
        }
    }
}
