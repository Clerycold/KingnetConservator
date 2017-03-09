package clery.kingnetconservator.app.kingnetconservator.SignFeatures;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import clery.kingnetconservator.app.kingnetconservator.Control.DataTime.DataTime;
import clery.kingnetconservator.app.kingnetconservator.Control.FileUtils.FileUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.MediaScanner;
import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;

import static android.graphics.Bitmap.createBitmap;

/**
 * Created by clery on 2017/2/15.
 */

public class DrawView extends View {

    private File pictureFile;
    // 点击的坐标
    private float lastX = 0, lastY = 0;
    private Path path;
    private Paint paint,dataPaint;
    // 使用内存中的图片作为缓冲区
    private Bitmap cacheBitmap;
    // 缓冲区上的Canvas对象
    private Canvas cacheCanvas;
    private Context context;

    private String tempTableCheck;
    private String pictureUrl;

    FileUtils fileUtils;

    public DrawView(Context context, int width, int height) {
        super(context);
        this.context = context;
        // 创建确定大小的bitmap

        FileUtils.initManager(context);
        fileUtils = FileUtils.getManager();

        cacheBitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 初始化缓存画布，把bitmap内容画到缓存画布上
        cacheCanvas = new Canvas();
        cacheCanvas.setBitmap(cacheBitmap);
        cacheCanvas.drawColor(Color.WHITE);

        path = new Path();

        paint = new Paint(Paint.DITHER_FLAG);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setTextSize(18);
        paint.setAntiAlias(true);
        paint.setDither(true);

        dataPaint = new Paint();
        dataPaint.setColor(Color.RED);
        dataPaint.setTextSize(36);
        dataPaint.setAntiAlias(true);
        dataPaint.setDither(true);
    }
    /**
     * 设置画笔粗细
     * @param width
     */
    public void setPaintWidth(int width) {
        paint.setStrokeWidth(width);
    }
    /**
     * 簽收日期
     */
    public void signData(){
        if(ScreenWH.getScreenWidth()>=1080){
            cacheCanvas.drawText("簽收日期:"+ DataTime.getNowTime("yyyy-MM-dd_HH:mm"),
                    ScreenWH.getScreenWidth()/2,
                    ScreenWH.getNoStatus_bar_Height(context)/10*7,
                    dataPaint);
        }else{
            cacheCanvas.drawText("簽收日期:"+DataTime.getNowTime("yyyy-MM-dd_HH:mm"),
                    ScreenWH.getScreenWidth()/3,
                    ScreenWH.getNoStatus_bar_Height(context)/10*7,
                    dataPaint);
        }

    }
    public void signTable_note(String s){

        if(s != null){
            tempTableCheck = s;

            TextPaint tp = new TextPaint();
            tp.setColor(Color.RED);
            tp.setTextSize(36);
            StaticLayout myStaticLayout = new StaticLayout(tempTableCheck, tp, cacheCanvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            cacheCanvas.save();
            cacheCanvas.translate(ScreenWH.getUISpacingX(), ScreenWH.getUISpacingX());
            myStaticLayout.draw(cacheCanvas);
            cacheCanvas.restore();
        }
    }
    /**
     * 设置画笔的颜色
     * @param color，颜色的字符串
     */
    public void setPaintColor(String color) {
        switch (color) {
            case "red" :
                paint.setColor(Color.RED);
                break;
            case "green" :
                paint.setColor(Color.GREEN);
                break;
            case "blue" :
                paint.setColor(Color.BLUE);
                break;
            case "yellow" :
                paint.setColor(Color.YELLOW);
                break;
            default:
                paint.setColor(Color.BLACK);
                break;
        }
    }

    public void clearCanvas() {
        // 清除path轨迹
        path.reset();
        path.moveTo(lastX, lastY);
        // 清除cacheCanvas图像
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        cacheCanvas.drawPaint(paint);
        cacheCanvas.drawColor(Color.WHITE);
        signTable_note(tempTableCheck);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX(), y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 记录点击坐标，把当前点定义为线段的前一个点
                lastX = x;
                lastY = y;
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE: // 绘制线段
                path.quadTo(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
                break;
            case MotionEvent.ACTION_UP: // 把线段绘制到画布上
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        // 绘制之前画的轨迹
        canvas.drawBitmap(cacheBitmap, 0, 0, paint);
        // 绘制正在画的轨迹
        canvas.drawPath(path, paint);

    }
    public void savecacheBitmap(){
        if(isExternalStorageWritable()){
            try {
                // 保存图片到SD卡上
                FileOutputStream stream = new FileOutputStream(fileUtils.getDataFilePath());
                cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] b = new byte[1024 * 8];
                stream.write(b);
                stream.flush();
                stream.close();

                pictureUrl = fileUtils.getPictureUrl();
                fileUtils.GalleryAddPic();

                RemindToast.showText(context,"簽收儲存成功");

            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }
        }else{
            RemindToast.showText(context,"無法製作檔案，請確認使用權限");
        }

    }

    public String getPictureUrl(){
        return pictureUrl;
    }

    private Boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}