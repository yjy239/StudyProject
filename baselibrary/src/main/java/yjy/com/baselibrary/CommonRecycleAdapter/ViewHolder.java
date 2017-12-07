package yjy.com.baselibrary.CommonRecycleAdapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by yjy on 2017/7/16.
 */
public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View.OnClickListener listener;

    public ViewHolder(View itemView) {
        super(itemView);
        //缓存itemview
        mViews = new SparseArray<>();
    }

    /***
     * 使用泛型，不需要强转**/
    public <T extends View> T getView(int viewid){
        View view = mViews.get(viewid);
        if(view == null){
            view = itemView.findViewById(viewid);
            mViews.put(viewid,view);
        }
        return (T)view;
    }

    /**通用功能进行封装**/
    public ViewHolder setText(int viewid,CharSequence text){
        TextView tv = getView(viewid);
        tv.setText(text);
        return this;
    }

    /**设置资源图片**/
    public ViewHolder setImageResource(int viewid,int resid){
        ImageView iv =getView(viewid);
        iv.setImageResource(resid);
        return this;
    }

    /***设置visible***/
    public ViewHolder setVisiblity(int viewid,int visible){
        getView(viewid).setVisibility(visible);
        return this;
    }

    /**处理常用的第三方**/
    public ViewHolder setImagePath(int viewid,String path,
                                   HolderImageLoader imageLoader){
        ImageView iv =getView(viewid);
        imageLoader.loadImage(iv,imageLoader.getPath());
//        iv.setImageResource(resid);
        return this;
    }

    public void setOnItemClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    public abstract static class HolderImageLoader{
        private String mPath;
        public HolderImageLoader(String path){
            this.mPath = path;
        }

        /**
         * 加载方法**/
        public abstract void loadImage(ImageView img,String path);

        public String getPath(){
            return mPath;
        }
    }


}
