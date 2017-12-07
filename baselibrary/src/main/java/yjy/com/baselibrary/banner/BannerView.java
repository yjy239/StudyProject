package yjy.com.baselibrary.banner;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import yjy.com.baselibrary.R;

/**
 * Created by asus on 2017/7/28.
 */
public class BannerView extends RelativeLayout {

    private BannerViewPager mBannerVp;
    private TextView mBannerDescTv;
    private LinearLayout mDotContainerView;
    private BannerAdapter mAdapter;
    private Context mContext;
    //选中的点
    private Drawable mIndicatorFoucsDrawable;
    //默认的点
    private Drawable mIndicatorNormalDrawable;

    //当前位置
    private int mCurrentPosition = 0;

    //点的位置
    private int mDotGravity = 1;
    //点的大小
    private int mDotSize = 8;
    //点的间距
    private int mDotDistance =8;

    private RelativeLayout mBannnerbottom;

    //底部颜色
    private int mBottomColor = Color.TRANSPARENT;

    //宽高比例
    private float mWidthPropertion,mHeightPropertion;

    public BannerView(Context context) {
        this(context,null);

    }

    public BannerView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //把布局添加到Banner里面
        inflate(context, R.layout.ui_banner_layout,this);
        initAttribute(attrs);
        initView();

    }

    private void initAttribute(AttributeSet attrs) {
        TypedArray tArray = mContext.obtainStyledAttributes(R.styleable.BannerView);
        mDotGravity = tArray.getInt(R.styleable.BannerView_dotGravity,mDotGravity);
        mIndicatorFoucsDrawable =tArray.getDrawable(R.styleable.BannerView_DotIndicatorFocusDrawable);
        if(mIndicatorFoucsDrawable == null){
            mIndicatorFoucsDrawable = new ColorDrawable(Color.RED);
        }
        mIndicatorNormalDrawable=tArray.getDrawable(R.styleable.BannerView_DotIndicatorNormalDrawable);
        if(mIndicatorNormalDrawable == null){
            mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        }

        mDotSize = (int) tArray.getDimension(R.styleable.BannerView_dotSize,dip2px(mDotSize));

        mBottomColor = tArray.getColor(R.styleable.BannerView_bottomColor,mBottomColor);

        mDotDistance = (int) tArray.getDimension(R.styleable.BannerView_dotDistance,dip2px(mDotDistance));

        mWidthPropertion = tArray.getFloat(R.styleable.BannerView_widthPropertion,mWidthPropertion);
        mHeightPropertion = tArray.getFloat(R.styleable.BannerView_heightPropertion,mHeightPropertion);
        tArray.recycle();
    }

    /*****初始化view****/
    private void initView() {
        mBannerVp = (BannerViewPager)findViewById(R.id.banner_vp);
        mBannerDescTv = (TextView)findViewById(R.id.banner_desc_tv);
        mDotContainerView = (LinearLayout)findViewById(R.id.dot_container);
        mBannnerbottom = (RelativeLayout)findViewById(R.id.bottom_view);
        mIndicatorFoucsDrawable = new ColorDrawable(Color.RED);
        mIndicatorNormalDrawable = new ColorDrawable(Color.WHITE);
        mBannnerbottom.setBackgroundColor(mBottomColor);
    }

    //动态计算宽高比
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

    /**
     * 设置适配器**/
    public void setAdapter(BannerAdapter adapter){
        mBannerVp.setAdapter(adapter);
        mAdapter = adapter;
        initDot();

        mBannerVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //当前选择位置
                pageSelect(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //默认去取第一张描述
        String firstDesc = mAdapter.getBannerDesc(0);
        mBannerDescTv.setText(firstDesc);

        //动态指定高度
        if(mHeightPropertion == 0 || mWidthPropertion == 0){
            return;
        }
        int width = getMeasuredWidth();
        int height = (int) (width * mHeightPropertion / mWidthPropertion);
        getLayoutParams().height = height;

    }


    /**
     * 页面切换回调
     * **/
    private void pageSelect(int position) {
        //当前位置的点亮，之前亮着点默认
       DotIndicatorView oldIndicator = (DotIndicatorView) mDotContainerView.getChildAt(mCurrentPosition);
        oldIndicator.setDrawable(mIndicatorNormalDrawable);

        mCurrentPosition = position%mAdapter.getCount();
        DotIndicatorView currentIndicator = (DotIndicatorView) mDotContainerView.getChildAt(mCurrentPosition);
        currentIndicator.setDrawable(mIndicatorFoucsDrawable);

        //设置广告描述
        String bannerDesc = mAdapter.getBannerDesc(position);
        mBannerDescTv.setText(bannerDesc);
    }

    /**
    ** 初始化点的指示器***/
    private void initDot() {
        int count = mAdapter.getCount();
        mDotContainerView.setGravity(getDotGravity());
        for(int i = 0;i<count;i++){
            //不断的添加点到容器
            DotIndicatorView indicatorView = new DotIndicatorView(mContext);
            //设置大小
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDotSize,mDotSize);
            params.leftMargin = dip2px(mDotDistance);
            indicatorView.setLayoutParams(params);

            if(i == 0){
                indicatorView.setDrawable(mIndicatorFoucsDrawable);
            }else {
                indicatorView.setDrawable(mIndicatorNormalDrawable);
            }

            mDotContainerView.addView(indicatorView);
        }
    }

    private int getDotGravity() {
        switch(mDotGravity){
            case -1:
                return Gravity.LEFT;

            case 0:
                return Gravity.CENTER;

            case 1:
                return Gravity.RIGHT;

        }
        return Gravity.LEFT;
    }

    private int dip2px(int dip) {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }

    public void startScroll(){
        mBannerVp.startRoll();
    }


}
