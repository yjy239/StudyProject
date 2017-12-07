package yjy.com.baselibrary.banner;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * Created by asus on 2017/7/28.
 */
public class BannerScroller extends Scroller {

    //动画持续时间
    private int mScrollerDuartion = 850;

    public BannerScroller(Context context) {
        super(context);
    }

    public BannerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public BannerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    public void setScrollerDuartion(int scrollerDuartion){
        this.mScrollerDuartion = scrollerDuartion;
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollerDuartion);
    }
}
