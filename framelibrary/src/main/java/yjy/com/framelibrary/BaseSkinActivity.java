package yjy.com.framelibrary;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yjy.com.baselibrary.base.BaseActivity;
import yjy.com.framelibrary.skin.SkinManager;
import yjy.com.framelibrary.skin.SkinResource;
import yjy.com.framelibrary.skin.attr.SkinAttr;
import yjy.com.framelibrary.skin.attr.SkinView;
import yjy.com.framelibrary.skin.callback.ISkinChangeListener;
import yjy.com.framelibrary.skin.support.SkinAppCompatViewInflater;
import yjy.com.framelibrary.skin.support.SkinAttrsSupport;

/**
 * Created by asus on 2017/6/4.
 */
public abstract class BaseSkinActivity extends BaseActivity implements ISkinChangeListener{

    private static final String TAG ="BaseSkinActivity" ;
    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(this);
//        LayoutInflaterCompat.setFactory(inflater, new LayoutInflaterFactory() {
//            @Override
//            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

//                return null;
//            }
//        });

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {

        //拦截view创建创建view
        View view = createView(parent, name, context, attrs);
        //2.解析属性
        Log.e(TAG,view + "");

        //一个Activity布局对应多个SkinView
        if(view != null){
            List<SkinAttr>  skinAttrs = SkinAttrsSupport.getSkinAttrs(context,attrs);
            SkinView mSkinView = new SkinView(view,skinAttrs);
            //统一交给skinmanager 管理
            managerSkinView(mSkinView);

            //判断是否换肤
            SkinManager.getInstance().checkChangeSkin(mSkinView);
        }



        return view;
    }

    //统一管理skinview
    private  void managerSkinView(SkinView mSkinView){
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if(skinViews == null){
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this,skinViews);
        }
        skinViews.add(mSkinView);
    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        //第三方
        Toast.makeText(BaseSkinActivity.this,"换肤",Toast.LENGTH_SHORT).show();
    }

    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        final boolean isPre21 = Build.VERSION.SDK_INT < 21;

        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        // We only want the View to inherit it's context if we're running pre-v21
        final boolean inheritContext = isPre21 && true
                && shouldInheritContext((ViewParent) parent);

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                isPre21, /* Only read android:theme pre-L (L+ handles this anyway) */
                true /* Read read app:theme as a fallback at all times for legacy reasons */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == getWindow().getDecorView() || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    protected void onDestroy() {
        SkinManager.getInstance().unregister(this);
        super.onDestroy();
    }
}
