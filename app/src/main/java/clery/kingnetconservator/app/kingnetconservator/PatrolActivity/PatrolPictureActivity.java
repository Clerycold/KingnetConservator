package clery.kingnetconservator.app.kingnetconservator.PatrolActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import clery.kingnetconservator.app.kingnetconservator.Control.BestBitmap.BitmapRotation;
import clery.kingnetconservator.app.kingnetconservator.Control.BestBitmap.BitmapUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.Camrea.CameraCheck;
import clery.kingnetconservator.app.kingnetconservator.Control.Camrea.CameraPreview;
import clery.kingnetconservator.app.kingnetconservator.Control.Camrea.OrientationDetector;
import clery.kingnetconservator.app.kingnetconservator.Control.FileUtils.FileUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.SysApplication;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/3/2.
 */

public class PatrolPictureActivity extends Activity{

    private static String TAG = "PatrolPictureActivity";

    private FrameLayout frameLayoutPreview;

    private Camera mCamera;

    private CameraPreview mPreview;

    private Button button_take;
    /**
     * 判斷鏡頭為前還後
     */
    public static int CURRENTCAMERAID;
    /**
     * 縮屏判斷
     */
    public static Boolean ENMERGENCE = false;
    /**
     * 判斷拍照
     */
    private Boolean isTakePicture=false;
    /**
     * 檢測手機的角度
     */
    private OrientationDetector orientationDetector;
    /**
     * 新增檔案資料類
     */
    private FileUtils fileUtils;
    /**
     * 暫存拍照後的bitmap檔
     */
    private Bitmap temppicture;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrolpicture);

        initView();
        xmlArrange();
        setOnClickListener();
    }

    private void initView(){

        FileUtils.initManager(getApplicationContext());
        fileUtils = FileUtils.getManager();

        OrientationDetector.initManager(getApplicationContext());
        orientationDetector = OrientationDetector.getOrientationDetector();

        if (CameraCheck.checkCameraHardware(this)) {
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(this, mCamera);
        }

        frameLayoutPreview = (FrameLayout) findViewById(R.id.camera_preview);
        button_take = (Button)findViewById(R.id.button_take);

        frameLayoutPreview.addView(mPreview);
    }

    private void xmlArrange(){

        RelativeLayout.LayoutParams camerabutton_lay =new RelativeLayout.LayoutParams((int) (double) ((ScreenWH.getScreenWidth()/10)*2), (int) (double) ((ScreenWH.getScreenWidth()/10)*2));
        camerabutton_lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        camerabutton_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        camerabutton_lay.setMargins(0,0,0,(int) (double) ((ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10)));
        button_take.setLayoutParams(camerabutton_lay);

    }

    private void setOnClickListener(){
        View.OnClickListener onClickListener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.camera_preview:
                        isTakePicture=false;
                        CameraautoFocus();
                        break;
                    case R.id.button_take:
                        isTakePicture=true;
                        CameraautoFocus();

                        break;
                }
            }
        };
        frameLayoutPreview.setOnClickListener(onClickListener);
        button_take.setOnClickListener(onClickListener);
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {

            c = Camera.open();
            CURRENTCAMERAID = CameraCheck.getDefaultCameraId(this);
        } catch (Exception e) {

        }

        return c;
    }

    private void CameraautoFocus(){
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if(isTakePicture){
                    capturePicture();
                    isTakePicture=!isTakePicture;
                }
            }
        });
    }

    private void capturePicture() {
        /**
         ** 監聽當前螢幕旋轉開啟
         */
        orientationDetector.enable();
        mCamera.takePicture(
                shutterCallback,//// 指定快門回呼
                null,// 指定raw data可讀回呼
                mPicture);// 指定jpeg data可讀回呼
    }
    /**
     * 內建拍照音效
     */
    private final Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {
            AudioManager mgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            mgr.playSoundEffect(AudioManager.FLAG_PLAY_SOUND);
        }
    };

    final transient private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            if (data != null) {
                temppicture = BitmapUtils.decodeSampledBitmapData(data,ScreenWH.getScreenHidth(),ScreenWH.getScreenWidth());
                temppicture=new BitmapRotation().bitmapRotation(temppicture,orientationDetector.getORIENTATION(),CURRENTCAMERAID);

                Bitmap icon = BitmapUtils.decodeSampledBitmapFromResource(getResources(),R.drawable.logo,ScreenWH.getScreenWidth(),ScreenWH.getScreenWidth());

                temppicture = BitmapUtils.Bitmapcomposite(temppicture,icon);

            }
            File pictureFile = fileUtils.getDataFilePath();
            String picturePath = fileUtils.getPictureUrl();

            if (pictureFile == null) {
                return;
            }
            if (CameraCheck.isExternalStorageWritable()) {
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    //第二個參數0~100  100表示不壓縮
                    temppicture.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    byte[] b = new byte[1024 * 8];
                    fos.write(b);
                    fos.flush();
                    fos.close();

                } catch (FileNotFoundException e) {
                    Log.d("TAG", "File not found: " + e.getMessage());
                } catch (IOException e) {
                    Log.d("TAG", "Error accessing file: " + e.getMessage());
                }
            }
            /**
             ** 監聽當前螢幕旋轉關閉
             */
            orientationDetector.disable();

            Bundle bundle = new Bundle();
            bundle.putBoolean("true", true);
            bundle.putString("picturePath",picturePath);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(PatrolActivity.ACTION_FLAG, intent);

            finish();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            Bundle bundle = new Bundle();
            bundle.putBoolean("true", false);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(PatrolActivity.ACTION_FLAG, intent);

            finish();
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(ENMERGENCE &&mCamera==null){
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(this, mCamera);
            frameLayoutPreview.addView(mPreview);
            ENMERGENCE =false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        frameLayoutPreview.removeView(mPreview);
        mCamera = null;
        mPreview = null;
        ENMERGENCE =true;
    }
}
