package clery.kingnetconservator.app.kingnetconservator.Control.UIpattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;

/**
 * Created by clery on 2017/2/15.
 */

public class Backbutton extends View{

    private Paint paint;
    int Width;
    int Height;

    public Backbutton(Context context) {
        super(context);

        Width = ScreenWH.getScreenWidth();
        Height = ScreenWH.getNoStatus_bar_Height(context);

        initView();

    }

    public Backbutton(Context context, AttributeSet attrs) {
        super(context, attrs);

        Width = ScreenWH.getScreenWidth();
        Height = ScreenWH.getNoStatus_bar_Height(context);

        initView();

    }

    private void initView() {

        // 初始化画笔
        // 防抖动
        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(7);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawLine(Width/15,ScreenWH.getUISpacingY(),ScreenWH.getUISpacingX(),Width/20,paint);
        canvas.drawLine(ScreenWH.getUISpacingX()-1,Width/20-1,Width/15,Width/10-ScreenWH.getUISpacingY(),paint);

    }
    public void setPaintColor(Boolean isClick){
        if(isClick){
            paint.setColor(Color.BLUE);
            invalidate();
        }else{
            paint.setColor(Color.BLACK);
            invalidate();
        }
    }
}
