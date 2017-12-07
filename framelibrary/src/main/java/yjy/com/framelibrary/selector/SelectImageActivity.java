package yjy.com.framelibrary.selector;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import yjy.com.framelibrary.BaseSkinActivity;
import yjy.com.framelibrary.R;
import yjy.com.framelibrary.utils.StatusBarUtils;

/**
 * Created by yjy on 2017/8/2.
 * 图片选择器
 */
public class SelectImageActivity extends BaseSkinActivity {
    //单选还是多选 int类型
    public static final int MODE_MULTI = 0x0011;

    public static final int MODE_SINGLE = 0x0012;

    //boolean 类型是否显示拍照按钮
    //是否显示相机的key
    public static final String EXTRA_SHOW_CAMERA = "EXTRA_SHOW_CAMERA";

    //总共可以选择多少图片的key
    public static final String EXTRA_SELECT_COUNT = "EXTRA_SELECT_COUNT";

    //原始图片路径的EXTRA_KEY
    public static final String EXTRA_DEFALUT_SELECT_LIST = "EXTRA_DEFALUT_SELECT_LIST";

    //选择模式key
    public static final String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE";

    //返回选择图片列表
    public static final String EXTRA_RESULT = "EXTRA_RESULT";

    private TextView mSelectNumTv;
    private RecyclerView mImageListRv;
    private TextView mSelectPerView;
    private TextView mSelectSure;

    //数据全部读取

    private static final int LOADER_TYPE = 0X0021;

    //显示图片的adapter
    private ImageSelectorListAdapter mImageAdapter;

    private File mTempFile;

    private int mMode = MODE_MULTI;
    private int mMaxCount = 8;
    private boolean mShowCamera = true;
    private ArrayList<String> mResultList;

    private ImageSelectorListAdapter mAdapter;


    @Override
    public void setContentView() {
        //权限判断
        setContentView(R.layout.activity_image_selector);
        mSelectNumTv = (TextView) findViewById(R.id.select_num);
        mImageListRv = (RecyclerView)findViewById(R.id.img_list_rv);
        mSelectPerView = (TextView)findViewById(R.id.select_perview);
        mSelectSure = (TextView)findViewById(R.id.select_finish);

        Intent intent = getIntent();
        mMode = intent.getIntExtra(EXTRA_SELECT_MODE,mMode);
        mMaxCount = intent.getIntExtra(EXTRA_SELECT_COUNT,mMaxCount);
        mShowCamera = intent.getBooleanExtra(EXTRA_SHOW_CAMERA,mShowCamera);
        mResultList = intent.getStringArrayListExtra(EXTRA_DEFALUT_SELECT_LIST);
        if(mResultList == null){
            mResultList = new ArrayList<>();
        }
        //初始化图片列表
        initImageList();
        sure();
        //改变显示
        exchangeViewShow();
    }

    private void exchangeViewShow() {
        //预览是否点击，中间图片的张数也能显示
        mSelectNumTv.setText(mResultList.size()+"/"+mMaxCount);
        if(mResultList.size()>0){
            //预览可选择
            mSelectPerView.setSelected(true);
            mSelectPerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }else {
            mSelectPerView.setSelected(false);
            mSelectPerView.setOnClickListener(null);
        }
    }


    public void initImageList(){
        mImageListRv.setLayoutManager(new GridLayoutManager(this,4));
        getLoaderManager().initLoader(LOADER_TYPE,null,mLoaderCallBack);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initTitle() {
        StatusBarUtils.setColor(SelectImageActivity.this, Color.parseColor("#261f1f"));
    }


    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallBack = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.MIME_TYPE,
                MediaStore.Images.Media.SIZE,
                MediaStore.Images.Media._ID
        };

//        @Override
//        public Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        }

        @Override
        public android.content.Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            CursorLoader cusorLoader = new CursorLoader(SelectImageActivity.this,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4]+">0 AND "+IMAGE_PROJECTION[3]+"=? OR "+IMAGE_PROJECTION[3]+"=? "
            ,new String[]{"image/jpeg","image/png"},IMAGE_PROJECTION[2]+" DESC");
            return cusorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            //如果有数据变量数据
            if(data != null&&data.getCount() > 0){
                ArrayList<ImageEntity> images = new ArrayList<>();

                if(mShowCamera){
                    ImageEntity camera = new ImageEntity("","",0);
                    images.add(camera);
                }

                while(data.moveToNext()){
                    String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                    String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                    long time = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                    if(!pathExist(path)){
                        continue;
                    }


                    ImageEntity image = new ImageEntity(path,name,time);
                    images.add(image);
                }
                showListData(images);
            }

        }

        @Override
        public void onLoaderReset(android.content.Loader<Cursor> loader) {

        }

//        @Override
//        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
//        }

        private boolean pathExist(String path) {
            if(!TextUtils.isEmpty(path)){
                return new File(path).exists();
            }
            return false;
        }

    };

    /**
     * 显示图片**/
    private void showListData(ArrayList<ImageEntity> images) {
        mAdapter = new ImageSelectorListAdapter(SelectImageActivity.this,images,R.layout.meida_chooser_item,mResultList,mMaxCount);
        mAdapter.setSelectImageListener(new SelectImageListener() {
            @Override
            public void select() {
                exchangeViewShow();
            }
        });
        mImageListRv.setLayoutManager(new GridLayoutManager(SelectImageActivity.this,4));
        mImageListRv.setAdapter(mAdapter);

    }

    private void sure(){
        mSelectSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //选择好的图片传过去
                Intent i = new Intent();
                i.putStringArrayListExtra(EXTRA_DEFALUT_SELECT_LIST,mResultList);
                setResult(RESULT_OK,i);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            //图片添加到集合
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTempFile)));
            mResultList.add(mTempFile.getAbsolutePath());
            setResult();
            //调用sure

            //系统图片改变了

        }

    }

    private void setResult() {
        Intent i = new Intent();
        i.putStringArrayListExtra(EXTRA_DEFALUT_SELECT_LIST,mResultList);
        setResult(RESULT_OK,i);
        finish();
    }

}
