package yjy.com.baselibrary.CommonDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

import yjy.com.baselibrary.R;


/**
 * Created by yjy on 2017/6/20.
 */
public class CommonDialog extends Dialog implements DialogInterface{

    private CommonController mCommon;

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int themeResId) {
        super(context, themeResId);
        mCommon = new CommonController(this,getWindow());
    }

    public CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    //设置text
    public void setText(int viewid, CharSequence text) {
      mCommon.setText(viewid,text);
    }

    private <T extends View> T getView(int viewid) {
      return mCommon.getView(viewid);
    }

    //设置点击事件
    public void setClickListener(int i, View.OnClickListener listener) {
        mCommon.setClickListener(i,listener);
    }



    public static class Builder{

        private final CommonController.CommonParams P;

        public Builder(Context context){
            this(context, R.style.dialog);
        }

        /**
         * Set a custom view to be the contents of the Dialog. If the supplied view is an instance
         * of a {@link ListView} the light background will be used.
         *
         * @param view The view to use as the contents of the Dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setContentView(View view) {
            P.mView = view;
            P.mViewLayoutResId = 0;
            return this;
        }

        //设置内容id
        public Builder setContentView(int resid) {
            P.mView = null;
            P.mViewLayoutResId = resid;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setCancelable(boolean cancelable) {
            P.mCancelable = cancelable;
            return this;
        }

        /**
         * Sets the callback that will be called if the dialog is canceled.
         *
         * <p>Even in a cancelable dialog, the dialog may be dismissed for reasons other than
         * being canceled or one of the supplied choices being selected.
         * If you are interested in listening for all cases where the dialog is dismissed
         * and not just when it is canceled, see
         * {@link #setOnDismissListener(OnDismissListener) setOnDismissListener}.</p>
         * @see #setCancelable(boolean)
         * @see #setOnDismissListener(OnDismissListener)
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            P.mOnCancelListener = onCancelListener;
            return this;
        }

        /**
         * Sets the callback that will be called when the dialog is dismissed for any reason.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnDismissListener(OnDismissListener onDismissListener) {
            P.mOnDismissListener = onDismissListener;
            return this;
        }

        /**
         * Sets the callback that will be called if a key is dispatched to the dialog.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            P.mOnKeyListener = onKeyListener;
            return this;
        }

        //设置文本
        public Builder setText(int resid,CharSequence text){
            P.textMap.put(resid,text);
            return this;
        }

        //设置点击事件
        public Builder setOnClickListener(int resid, WeakReference<View.OnClickListener> listener){
            P.clickMap.put(resid,listener);
            return this;
        }

        //配置一些通用参数
        //设置满屏幕
        public Builder fullWidth(){
            P.mWidth = ViewGroup.LayoutParams.MATCH_PARENT;
            return this;
        }

        //从底部
        public Builder fromBottom(boolean isAnimation){
            if(isAnimation){
                P.mAniamtion = R.style.dialog_from_bottom;
            }
            P.mGravity = Gravity.BOTTOM;
            return this;
        }

        //设置宽高
        public Builder setWidthAndHeight(int width,int height){
            P.mWidth = width;
            P.mHeight = height;
            return this;
        }

        //添加默认动画
        public Builder addDefaultAnimation(){
            P.mAniamtion = R.style.default_animation;
            return this;
        }

        //添加动画
        public Builder addAnimation(int styleAnimation){
            P.mAniamtion = styleAnimation;
            return this;
        }


        public Builder(Context context,int themid){
           P =  new CommonController.CommonParams(context,themid);
        }


        public CommonDialog create() {
            final CommonDialog dialog = new CommonDialog(P.mContext,P.thmeid);
            P.apply(dialog.mCommon);
            dialog.setCancelable(P.mCancelable);
            if (P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(P.mOnCancelListener);
            dialog.setOnDismissListener(P.mOnDismissListener);
            if (P.mOnKeyListener != null) {
                dialog.setOnKeyListener(P.mOnKeyListener);
            }
            return dialog;
        }

        public CommonDialog show() {
            CommonDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
