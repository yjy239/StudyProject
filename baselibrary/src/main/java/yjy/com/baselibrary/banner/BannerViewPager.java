package yjy.com.baselibrary.banner;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2017/7/27.
 */
public class BannerViewPager extends ViewPager {

    //适配器
    private BannerAdapter mAdapter;

    private static final int SCROLL_MSG = 0x0011;

    private BannerScroller mScroller;

    //复用的view
    private List<View> mConvertViews;

    //切换间隔时间
    private int mCutDown = 5000;

    private Activity mActivity;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //每隔多少秒后切换到下一页
            setCurrentItem(getCurrentItem()+1);
            //轮询
            startRoll();
        }
    };

    public BannerViewPager(Context context) {
        this(context,null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        //改变Banner的速率，改变mScroller
        try {
            Field scrollfield =  ViewPager.class.getDeclaredField("mScroller");
            scrollfield.setAccessible(true);
            //设置参数,当前属性在那个类，必要设置的值
            mScroller = new BannerScroller(context);
            scrollfield.set(this,mScroller);

        }catch (Exception e){
            e.printStackTrace();
        }
        mConvertViews = new ArrayList<>();

        mActivity = (Activity)context;

    }

    public void setSrollDuration(int duration){
        mScroller.setScrollerDuartion(duration);
    }


    public void setAdapter(BannerAdapter adapter) {
        this.mAdapter = adapter;
        //设置viewpager的adapter
        setAdapter(new BannerPagerAdapter());

        //管理activity生命周期
        mActivity.getApplication().registerActivityLifecycleCallbacks(activityLifeCallBack);
    }

    /**启动自动轮播**/
    public void startRoll(){
        mHandler.removeMessages(SCROLL_MSG);
        mHandler.sendEmptyMessageDelayed(SCROLL_MSG,mCutDown);
    }

    /****停止自动轮播****/
    @Override
    protected void onDetachedFromWindow() {
        mHandler.removeMessages(SCROLL_MSG);
        mHandler = null;
        //解除绑定
        mActivity.getApplication().unregisterActivityLifecycleCallbacks(activityLifeCallBack);
        super.onDetachedFromWindow();
    }

    /**
     * 设置适配器**/
    private class BannerPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            //无限轮播
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        //创建页面
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //为了适配万用，最好用适配器模式
            View banneritem = mAdapter.getView(position%mAdapter.getCount(),getConvertView());
            //把子view添加到viewpager
            container.addView(banneritem);
            return banneritem;
        }

        //删除页面
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //删除页面释放view
            container.removeView((View)object);
            mConvertViews.add((View) object);
            object = null;
        }
    }

    private View getConvertView() {
        for(int i = 0;i<mConvertViews.size();i++){
            //判断复用界面没有已经加载进去viewpager,防止快速滑动时需要创建两个复用界面
            if(mConvertViews.get(i).getParent() == null){
                return mConvertViews.get(i);
            }
        }
        return null;
    }

    //管理activity生命周期
    Application.ActivityLifecycleCallbacks activityLifeCallBack = new DefaultActivityLifeCallBack() {

        @Override
        public void onActivityResumed(Activity activity) {
            //判断是不是当前Acivity的生命周期
            if(activity == mActivity){
                //开启轮播
                mHandler.sendEmptyMessageDelayed(SCROLL_MSG,mCutDown);
            }

        }

        @Override
        public void onActivityPaused(Activity activity) {
            if(activity == mActivity){
                //停止轮播
                mHandler.removeMessages(SCROLL_MSG);
            }

        }

    };


}
