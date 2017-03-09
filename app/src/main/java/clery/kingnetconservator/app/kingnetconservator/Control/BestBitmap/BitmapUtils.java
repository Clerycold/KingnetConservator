package clery.kingnetconservator.app.kingnetconservator.Control.BestBitmap;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;

/**
 * Created by clery on 2017/2/16.
 */

public class BitmapUtils {
//    1. 使用inJustDecodeBounds，读bitmap的长和宽。
//    2. 根据bitmap的长款和目标缩略图的长和宽，计算出inSampleSize的大小。
//    3. 使用inSampleSize，载入一个大一点的缩略图A
//    4. 使用createScaseBitmap，将缩略图A，生成我们需要的缩略图B。
//    5. 回收缩略图A。

    //計算該使用的大小
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        //如果圖片大小大於設定的大小時
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,
                                            int dstHeight) {
        Bitmap dst = null;
        if(src != null){
            dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        }

        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    // 从sd卡上加载图片
    public static Bitmap decodeSampledBitmapFromFd(String pathName,
                                                   int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(pathName, options);
        return createScaleBitmap(src, reqWidth, reqHeight);
    }
    /**
     * 将彩色图转换为纯黑白二色
     *
     * @param bmp 黑白
     * @return 返回转换好的位图
     */
    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth(); // 获取位图的宽
        int height = bmp.getHeight(); // 获取位图的高
        int[] pixels = new int[width * height]; // 通过位图的大小创建像素点数组

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = 0xFF << 24;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int grey = pixels[width * i + j];

                //分离三原色
                int red = ((grey & 0x00FF0000) >> 16);
                int green = ((grey & 0x0000FF00) >> 8);
                int blue = (grey & 0x000000FF);

                //转化成灰度像素 灰階公式(int) (red * 0.3 + green * 0.59 + blue * 0.11)
                grey = (int) (red * 0.3 + green * 0.59 + blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        //新建图片
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //设置图片数据
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);

        newBmp = getSmallerBitmap(newBmp);

        return newBmp;
    }

    /**
     * 陣列的黑白效果
     * @param bitmap
     * @return
     */
    public static Bitmap handleColorMatrix(Bitmap bitmap){
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp); // 创建一个画布
        Paint paint = new Paint(); // 新建paint
        paint.setAntiAlias(true); //抗锯齿
        //黑白
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.213f, 0.715f, 0.072f, 0, 0,
                0.213f, 0.715f, 0.072f, 0, 0,
                0.213f, 0.715f, 0.072f, 0, 0,
                0,       0,    0, 1, 0,
        });
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));// 设置颜色变换效果
        canvas.drawBitmap(bitmap, 0, 0, paint);

        return bitmap;
    }

    public static Bitmap getThumbnail(Context context, Uri uri, int size) throws FileNotFoundException, IOException {
        InputStream input = context.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1))
            return null;
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;
        double ratio = (originalSize > size) ? (originalSize / size) : 1.0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = context.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
    }

    /**
     *
     * @param bitmap 原始圖片大小
     * 將圖片的長*寬比較 如果比螢幕大就縮成跟螢幕大小一樣
     * @return
     */
    public static Bitmap getSmallerBitmap(Bitmap bitmap){
        int size = bitmap.getWidth()*bitmap.getHeight() / (ScreenWH.getScreenWidth()*ScreenWH.getScreenHidth());
        if (size <= 1) return bitmap; // 如果小于
        else {
            Matrix matrix = new Matrix();
            matrix.postScale((float) (1 / Math.sqrt(size)), (float) (1 / Math.sqrt(size)));
            Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return resizeBitmap;
        }
    }

    /**
     * 調整亮度 對比 飽和度
     * @param bitmap
     * @return
     */
    public static Bitmap convertToHeighLight(Bitmap bitmap){
        Bitmap bmp = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


        ColorMatrix cm = new ColorMatrix();
        float brightness = -15;  //亮度
        float contrast = 1.1f;        //对比度
        cm.set(new float[] {
                contrast, 0, 0, 0, brightness,
                0, contrast, 0, 0, brightness,
                0, 0, contrast, 0, brightness,
                0, 0, 0, contrast, 0
        });

        // 飽和度
        ColorMatrix saturationMatrix = new ColorMatrix();
        saturationMatrix.setSaturation(1.4f);

        // 亮度
        ColorMatrix lumMatrix = new ColorMatrix();
        lumMatrix.setScale(1.1f, 1.1f, 1.1f, 1);

        ColorMatrix imageMatrix = new ColorMatrix();
        imageMatrix.postConcat(cm);
        imageMatrix.postConcat(saturationMatrix);
        imageMatrix.postConcat(lumMatrix);

        paint.setColorFilter(new ColorMatrixColorFilter(imageMatrix));
        canvas.drawBitmap(bitmap, 0, 0, paint);

        bmp = getSmallerBitmap(bmp);

        return bmp;
    }

    // 从Resources中加载图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //解析图片的长度和宽度，不载入图片。
        options.inScaled = false;
        BitmapFactory.decodeResource(res, resId,options); // 读取图片长款
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId,options);
        if(options.inSampleSize != 1) {
            src =createScaleBitmap(src, reqWidth, reqHeight);
        }
        return src;
    }

    public static Bitmap decodeSampledBitmapData(byte[] data,
                                                 int reqWidth, int reqHeight) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data,0,data.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeByteArray(data,0,data.length, options);

        return createScaleBitmap(src, reqWidth, reqHeight);
    }

    /**
     * 圖片合成
     * @param src
     * @param watermark
     * @return
     */
    public static Bitmap Bitmapcomposite(Bitmap src, Bitmap watermark )
    {
        if( src == null )
        {
            return null;
        }
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap( src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888 );//創建一個新的和SRC長度寬度一樣的點陣圖
        Canvas cv = new Canvas( newb );
        //draw src into
        cv.drawBitmap( src, 0, 0, null );//在 0，0座標開始畫入src
        //draw watermark into
        cv.drawBitmap( watermark, (src.getWidth()-watermark.getWidth())/8,
                (src.getHeight()-watermark.getHeight())/4 , null );
        //save all clip
        cv.save( Canvas.ALL_SAVE_FLAG );//保存
        //store
        cv.restore();//存儲
        return newb;
    }
}

