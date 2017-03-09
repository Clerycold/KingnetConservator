package clery.kingnetconservator.app.kingnetconservator.Control.BestBitmap;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.util.Log;

/**
 * Created by clery on 2017/3/3.
 */

public class BitmapRotation {
    /**
     * 照片儲存的長寬
     */
    private int mNewPictureH;
    private int mNewPictureW;

    public Bitmap bitmapRotation(Bitmap picture, int getRotation, int CURRENTCAMERAID){
        //Android提供的一个矩阵工具类，本身不能对图像或组件进行变换，但它可以和其它API结合起来控制图形、组件的变换；

        Matrix matrix = new Matrix();
        float degrees = 0f;
        if (CURRENTCAMERAID == Camera.CameraInfo.CAMERA_FACING_BACK) {
            switch (getRotation) {
                case 0:
                    degrees = 90f;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
                case 90:
                    degrees = 180f;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
                case 270:
                    degrees = 0;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
                case 180:
                    degrees = 180f;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
            }
        } else {
            switch (getRotation) {
                case 0:
                    degrees = -90;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
                case 90:
                    degrees = 180f;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
                case 270:
                    degrees = 0;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
                case 180:
                    degrees = 180f;
                    mNewPictureW = picture.getWidth();
                    mNewPictureH = picture.getHeight();
                    break;
            }
        }
        matrix.postRotate(degrees);
        Bitmap rotatedBitmap = Bitmap.createBitmap(
                picture, //Bitmap source：要从中截图的原始位图
                0,//int x:起始x坐标
                0,//int y：起始y坐标
                mNewPictureW,//int width：要截的图的宽度
                mNewPictureH,//int height：要截的图的宽度
                matrix,
                true
        );
        return rotatedBitmap;
    }
}