package clery.kingnetconservator.app.kingnetconservator.Control.UIpattern;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/2/16.
 */

public class SquareView extends View {

    private int mWidth;
    private int mHight;

    Paint p;
    Paint p_white;
    Paint p_bg;
    Paint pClear;

    public SquareView(Context context) {
        super(context);

        initMyView();
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMyView();
    }

    public void initMyView(){

        mWidth= ScreenWH.getScreenWidth();
        mHight=ScreenWH.getNoStatus_bar_Height(getContext().getApplicationContext());

        pClear = new Paint();
        //清屏
        pClear.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        // 方框
        p = new Paint();
        p.setColor(Color.WHITE);
        p.setStrokeWidth(10);
        p.setAntiAlias(true);

        p_bg = new Paint();
        p_bg.setColor(Color.BLACK);
        p_bg.setAlpha(220);

        p_white = new Paint();
        p_white.setColor(Color.WHITE);
        p_white.setStyle(Paint.Style.STROKE);//设置空心

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPaint(pClear);

        canvas.drawRect(0,0,mWidth,mHight,p_bg);
        canvas.drawRect((float)((mWidth/6.3)*0.5),(float)((mHight/10)*1.4),(float)((mWidth/6.3)*5.7),(float)((mHight/10)*6.6),pClear);
        canvas.drawRect((float)((mWidth/6.3)*0.5),(float)((mHight/10)*1.4),(float)((mWidth/6.3)*5.7),(float)((mHight/10)*6.6),p_white);
        //左上框
        canvas.drawLine((float)((mWidth/6.3)*0.475),(float)((mHight/10)*1.4),(float)((mWidth/6.3)*0.8),(float)((mHight/10)*1.4),p);
        canvas.drawLine((float)((mWidth/6.3)*0.5),(float)((mHight/10)*1.4),(float)((mWidth/6.3)*0.5),(float)((mHight/10)*1.7), p);
        //左下框
        canvas.drawLine((float)((mWidth/6.3)*0.475),(float)((mHight/10)*6.6),(float)((mWidth/6.3)*0.8),(float)((mHight/10)*6.6),p);
        canvas.drawLine((float)((mWidth/6.3)*0.5),(float)((mHight/10)*6.6),(float)((mWidth/6.3)*0.5),(float)((mHight/10)*6.3), p);
        //右上框
        canvas.drawLine((float)((mWidth/6.3)*5.725),(float)((mHight/10)*1.4),(float)((mWidth/6.3)*5.4),(float)((mHight/10)*1.4),p);
        canvas.drawLine((float)((mWidth/6.3)*5.7),(float)((mHight/10)*1.4),(float)((mWidth/6.3)*5.7),(float)((mHight/10)*1.7), p);
        //右下框
        canvas.drawLine((float)((mWidth/6.3)*5.725),(float)((mHight/10)*6.6),(float)((mWidth/6.3)*5.4),(float)((mHight/10)*6.6),p);
        canvas.drawLine((float)((mWidth/6.3)*5.7),(float)((mHight/10)*6.6),(float)((mWidth/6.3)*5.7),(float)((mHight/10)*6.3), p);
    }

    public void setpColor(){
        p.setColor(Color.RED);
        invalidate();
    }
}
