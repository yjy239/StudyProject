package yjy.com.baselibrary.CommonDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * Created by asus on 2017/6/20.
 */
public class CommonController {

    private CommonDialog mDialog;
    private Window mWindow;
    private DialogViewHelper viewHelper;

    public CommonController(CommonDialog commonDialog, Window window) {
        this.mDialog = commonDialog;
        this.mWindow = window;
    }

    public CommonDialog getDialog(){
        return mDialog;
    }

    public Window getWindow(){
        return mWindow;
    }

    public void setViewHelper(DialogViewHelper viewHelper) {
        this.viewHelper = viewHelper;
    }

    public void setText(int viewid, CharSequence text) {
        viewHelper.setText(viewid,text);
    }

    public <T extends View> T getView(int viewid) {
        return viewHelper.getView(viewid);
    }

    public void setClickListener(int i, View.OnClickListener listener) {
        viewHelper.setClickListener(i,listener);
    }


    public static class CommonParams{

        public Context mContext;
        public int thmeid;
        //点击空白是否消失
        public boolean mCancelable = true;
        //cancel监听
        public DialogInterface.OnCancelListener mOnCancelListener;
        //消失监听
        public DialogInterface.OnDismissListener mOnDismissListener;
        //按键监听
        public DialogInterface.OnKeyListener mOnKeyListener;
        public View mView;
        public int mViewLayoutResId;
        public SparseArray<CharSequence> textMap = new SparseArray<>();
        public SparseArray<WeakReference<View.OnClickListener>> clickMap = new SparseArray<>();
        //宽度
        public int mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        //动画
        public int mAniamtion = 0;
        //位置
        public int mGravity = Gravity.CENTER;
        public int mHeight=ViewGroup.LayoutParams.WRAP_CONTENT;

        public CommonParams(Context context, int themid) {
            this.mContext = context;
            this.thmeid = themid;

        }

        /***
         * 绑定甚至**/
        public void apply(CommonController mCommon) {
            //设置布局
            DialogViewHelper viewHelper = null;
            if(mViewLayoutResId != 0){
                viewHelper = new DialogViewHelper(mContext,mViewLayoutResId);
            }

            if(mView != null){
                viewHelper = new DialogViewHelper();
                viewHelper.setContentView(mView);
            }

            if(viewHelper == null){
                throw new IllegalArgumentException("没有调用setContentView");
            }

            mCommon.getDialog().setContentView(viewHelper.getContentView());

            //设置文本

            for(int i = 0;i<textMap.size();i++){
                viewHelper.setText(textMap.keyAt(i),textMap.valueAt(i));

            }
            //设置点击事件
            for(int i = 0;i<clickMap.size();i++){
                viewHelper.setClickListener(clickMap.keyAt(i),clickMap.valueAt(i).get());
            }

            mCommon.setViewHelper(viewHelper);

            //设置自定义效果
            Window window = mCommon.getWindow();
            window.setGravity(mGravity);
            if(mAniamtion!=0){
                window.setWindowAnimations(mAniamtion);
            }
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = mWidth;
            params.height = mHeight;
            window.setAttributes(params);
        }
    }
}
