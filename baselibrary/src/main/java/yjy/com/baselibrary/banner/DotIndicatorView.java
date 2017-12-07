package yjy.com.baselibrary.banner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yjy on 2017/7/28.
 * 圆的指示器
 */
public class DotIndicatorView extends View {

    private Drawable drawable;

    public DotIndicatorView(Context context) {
        this(context,null);
    }

    public DotIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DotIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(drawable !=null){
//            drawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
//            drawable.draw(canvas);

            Bitmap bitmap = drawableToBitmap(drawable);

            Bitmap circlemap = getCircleBitmap(bitmap);

            canvas.drawBitmap(circlemap,0,0,null);
        }
    }

    /***
     * bitmap 变成圆**/
    private Bitmap getCircleBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvus = new Canvas(circleBitmap);

        Paint paint  = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);

        //画圆
        canvus.drawCircle(getMeasuredWidth() / 2,getMeasuredHeight() / 2,getMeasuredWidth() / 2,paint);

        //交集
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvus.drawBitmap(bitmap,0,0,paint);

        bitmap.recycle();
        bitmap = null;
        return circleBitmap;
    }

    /**
    ** 从drawble变成bitmap**/
    private Bitmap drawableToBitmap(Drawable drawable) {

        if(drawable instanceof BitmapDrawable){
            return ((BitmapDrawable)drawable).getBitmap();
        }

        //创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(getMeasuredWidth(),getMeasuredHeight(),Bitmap.Config.ARGB_8888);
        //创建一个画布
        Canvas canvus = new Canvas(outBitmap);
        //drawable画上去
        drawable.setBounds(0,0,getMeasuredWidth(),getMeasuredHeight());
        drawable.draw(canvus);
        return outBitmap;
    }


}
