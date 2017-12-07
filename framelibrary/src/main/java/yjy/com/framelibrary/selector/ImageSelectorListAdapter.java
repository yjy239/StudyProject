package yjy.com.framelibrary.selector;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import yjy.com.baselibrary.CommonRecycleAdapter.MulitpleTypeSupport;
import yjy.com.baselibrary.CommonRecycleAdapter.RecyclerCommonAdapter;
import yjy.com.baselibrary.CommonRecycleAdapter.ViewHolder;
import yjy.com.framelibrary.R;

/**
 * Created by asus on 2017/8/3.
 */
public class ImageSelectorListAdapter extends RecyclerCommonAdapter<ImageEntity>{

    private Context mContext;
    //选择图片的集合
    private ArrayList<String> mResultImageList;
    private int mMaxCount;
    public ImageSelectorListAdapter(Context context, List<ImageEntity> data, int layoutid, ArrayList<String> mResultImageList,int mMaxCount) {
        super(context, data, layoutid);
        mContext = context;
        this.mResultImageList =mResultImageList;
        this.mMaxCount = mMaxCount;
    }

    public ImageSelectorListAdapter(Context context, List<ImageEntity> data, MulitpleTypeSupport typeSupport) {
        super(context, data, typeSupport);
    }

    @Override
    protected void convert(ViewHolder holder, final ImageEntity data, int position) {
        if(TextUtils.isEmpty(data.path)&&TextUtils.isEmpty(data.name)){
            //显示拍照图片
            holder.setVisiblity(R.id.ll_camera,View.VISIBLE);
            holder.setVisiblity(R.id.media_selected_indicator,View.INVISIBLE);
            holder.setVisiblity(R.id.image,View.INVISIBLE);
            //调用拍照
            holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }else {
            //显示图片
            holder.setVisiblity(R.id.ll_camera, View.INVISIBLE);
            holder.setVisiblity(R.id.media_selected_indicator, View.VISIBLE);
            holder.setVisiblity(R.id.image, View.VISIBLE);
            ImageView imageView = holder.getView(R.id.image);
            Glide.with(mContext)
                    .load(data.path)
                    .centerCrop()
                    .placeholder(R.drawable.mis_default_error)
                    .into(imageView);

            ImageView selector = holder.getView(R.id.media_selected_indicator);

            if (mResultImageList.contains(data.path)) {
                selector.setSelected(true);
            } else {
                selector.setSelected(false);
            }

            holder.getView(R.id.item).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mResultImageList.contains(data.path)) {
                        if(mMaxCount <= mResultImageList.size()){
                            Toast.makeText(mContext,"最多只能选取"+mMaxCount+"图片",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        mResultImageList.add(data.path);
                    } else {
                        mResultImageList.remove(data.path);
                    }

                    notifyDataSetChanged();
                    if(listener !=null){
                        listener.select();
                    }
                }
            });


            holder.setOnItemClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

        }
    }

    private SelectImageListener listener;
    public void setSelectImageListener(SelectImageListener listener){
        this.listener = listener;
    }
}
