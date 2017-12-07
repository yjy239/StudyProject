package yjy.com.baselibrary.CommonDialog;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by asus on 2017/6/20.
 * 视图富足处理类
 */
class DialogViewHelper {

    private Context mContext;
    private int mViewLayoutResId;
    private View mContentView = null;
    private SparseArray<WeakReference<View>> mViews;

    public DialogViewHelper(Context mContext, int mViewLayoutResId) {
        this.mContext = mContext;
        mContentView = LayoutInflater.from(mContext).inflate(mViewLayoutResId,null);
        mViews = new SparseArray<>();
    }

    public DialogViewHelper() {
        mViews = new SparseArray<>();
    }

    public void setContentView(View mView) {
        mContentView = mView;
    }

    //设置text
    public void setText(int viewid, CharSequence text) {
        TextView tv = getView(viewid);

        if(tv != null){
            tv.setText(text);
        }
    }

    public <T extends View> T getView(int viewid) {
        WeakReference<View> viewRef = mViews.get(viewid);
        View view = null;
        if(viewRef != null){
            view = viewRef.get();
        }
        if(view == null){
            view = mContentView.findViewById(viewid);
            if(view != null){
                mViews.put(viewid,new WeakReference<View>(view));
            }

        }
        return (T) view;
    }

    //设置点击事件
    public void setClickListener(int i, View.OnClickListener listener) {
        View view = getView(i);
        if(view !=null){
            view.setOnClickListener(listener);
        }
    }

    public View getContentView() {
        return mContentView;
    }
}
