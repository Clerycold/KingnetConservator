package clery.kingnetconservator.app.kingnetconservator.Control.Camrea;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;

import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;

/**
 * Created by clery on 2017/2/16.
 */

public class CameraCheck {

    //判斷有無照相機功能
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            RemindToast.showText(context.getApplicationContext(),"無相機功能");
            return false;
        }
    }

    //得到相機編號
    public static int getDefaultCameraId(Context context) {
        int defaultId = -1;
        // Find the total number of cameras available
        int mNumberOfCameras = Camera.getNumberOfCameras();
        // Find the ID of the default camera
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < mNumberOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                defaultId = i;
            }
        }

        if (-1 == defaultId) {
            if (mNumberOfCameras > 0) {
                // 如果没有后向摄像头
                defaultId = 0;
            } else {
                // 没有摄像头
                RemindToast.showText(context.getApplicationContext(),"沒有鏡頭");
            }
        }
        return defaultId;
    }
    //判斷是否可寫入
    public static Boolean isExternalStorageWritable(){
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }
}