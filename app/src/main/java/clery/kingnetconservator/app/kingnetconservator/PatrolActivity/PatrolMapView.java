package clery.kingnetconservator.app.kingnetconservator.PatrolActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.View;

import clery.kingnetconservator.app.kingnetconservator.Control.DataTime.DataTime;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by clery on 2017/3/2.
 */

public class PatrolMapView extends View{

    private Paint paint,paintLine,paintrectDown,paintrectUp,paintText,canvasPaint,canvasPaintText;
    private Paint paintData;
    private boolean ShowFinishTime = false;
    private Bitmap cacheBitmap;
    // 缓冲区上的Canvas对象
    private Canvas cacheCanvas;

    int center_x,center_y;
    String numberString;
    String dataTime;

    public PatrolMapView(Context context) {
        super(context);

        center_x = ScreenWH.getScreenWidth()/10;
        center_y = ScreenWH.getScreenWidth()/15;

        cacheBitmap = createBitmap(ScreenWH.getScreenWidth()/2 , ScreenWH.getScreenWidth() / 4, Bitmap.Config.ARGB_8888);
        // 初始化缓存画布，把bitmap内容画到缓存画布上
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
        canvasPaint.setStyle(Paint.Style.STROKE);
        canvasPaint.setColor(Color.RED);
        canvasPaint.setStrokeWidth(5);
        canvasPaint.setAntiAlias(true);
        canvasPaint.setDither(true);

        canvasPaintText = new Paint(Paint.DITHER_FLAG);
        canvasPaintText.setColor(Color.RED);
        canvasPaintText.setTextSize(60);
        canvasPaintText.setAntiAlias(true);
        canvasPaintText.setDither(true);

        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setDither(true);

        paintLine= new Paint(Paint.DITHER_FLAG);
        paintLine.setColor(Color.BLACK);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeWidth(5);
        paintLine.setAntiAlias(true);
        paintLine.setDither(true);

        paintText = new Paint(Paint.DITHER_FLAG);
        paintText.setColor(Color.BLACK);
        paintText.setTextSize(48);
        paintText.setAntiAlias(true);
        paintText.setDither(true);

        paintrectDown = new Paint(Paint.DITHER_FLAG);
        paintrectDown.setColor(Color.BLACK);
        paintrectDown.setStyle(Paint.Style.FILL);
        paintrectDown.setAntiAlias(true);
        paintrectDown.setDither(true);

        paintrectUp = new Paint(Paint.DITHER_FLAG);
        paintrectUp.setColor(Color.BLACK);
        paintrectUp.setStyle(Paint.Style.FILL);
        paintrectUp.setAntiAlias(true);
        paintrectUp.setDither(true);

        paintData = new Paint(Paint.DITHER_FLAG);
        paintData.setColor(Color.RED);
        paintData.setTextSize(24);
        paintData.setAntiAlias(true);
        paintData.setDither(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(ScreenWH.getScreenWidth()/10-ScreenWH.getUISpacingX()/2,
                0,
                ScreenWH.getScreenWidth()/10+ScreenWH.getUISpacingX()/2,
                canvas.getHeight()/2+ScreenWH.getUISpacingX()/2,paintrectUp);

        canvas.drawRect(ScreenWH.getScreenWidth()/10-ScreenWH.getUISpacingX()/2,
                canvas.getHeight()/2-ScreenWH.getUISpacingX()/2,
                ScreenWH.getScreenWidth()/10+ScreenWH.getUISpacingX()/2,
                canvas.getHeight(),paintrectDown);

        canvas.drawCircle(ScreenWH.getScreenWidth()/10, (ScreenWH.getScreenWidth()/5 / 2), (ScreenWH.getScreenWidth() / 5 / 2)-20, paint);
        canvas.drawCircle(ScreenWH.getScreenWidth()/10, (ScreenWH.getScreenWidth()/5 / 2), (ScreenWH.getScreenWidth() / 5 / 2)-20, paintLine);

        canvas.drawText(numberString,(ScreenWH.getScreenWidth()/10)+(canvas.getHeight() / 2),
                (int) ((canvas.getHeight() / 2) - ((paintText.descent() + paintText.ascent()) / 2)),
                paintText);

        if(ShowFinishTime){
            canvas.drawText("完成時間:"+dataTime,
                    canvas.getWidth()/4,
                    (canvas.getHeight()-paintText.descent()),
                    paintData);
        }

        canvas.drawBitmap(cacheBitmap, 0, 0, canvasPaint);

    }
    public void setPaintrectUp(){
        paintrectUp.setColor(Color.TRANSPARENT);
        invalidate();
    }
    public void setPaintrectDown(){
        paintrectDown.setColor(Color.TRANSPARENT);
        invalidate();
    }

    public void setLocationText(String s){
        numberString = s;
        invalidate();
    }

    public void ShowFinishData(String s){
        ShowFinishTime = true;
        dataTime = s ;
        invalidate();
    }
    public void ClearFinishData(){
        ShowFinishTime = false;
        invalidate();
    }

    public void FinishPatrol(){

        cacheCanvas.save();
        cacheCanvas.rotate((float)-10,cacheCanvas.getWidth()/2,cacheCanvas.getHeight()/2);

        RectF rect = new RectF(ScreenWH.getUISpacingX()
                , ScreenWH.getUISpacingY()
                , cacheCanvas.getWidth()/2
                ,cacheCanvas.getHeight()/2);
        cacheCanvas.drawRect(rect,canvasPaint);

        cacheCanvas.drawText("完  成",
                ScreenWH.getUISpacingX()*3,
                (int) ((cacheCanvas.getHeight() / 3) - ((canvasPaintText.descent() + canvasPaintText.ascent()) / 2)),canvasPaintText);

        cacheCanvas.restore();

        invalidate();
    }

    public void ClearFinishPatrol(){

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        cacheCanvas.drawPaint(paint);

        invalidate();
    }
}
