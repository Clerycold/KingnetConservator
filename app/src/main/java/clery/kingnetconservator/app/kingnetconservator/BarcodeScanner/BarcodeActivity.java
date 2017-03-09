package clery.kingnetconservator.app.kingnetconservator.BarcodeScanner;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import java.util.Timer;
import java.util.TimerTask;

import clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.presenter.CodeScannerPreCompl;
import clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.view.CodeScannerView;
import clery.kingnetconservator.app.kingnetconservator.Control.Camrea.CameraCheck;
import clery.kingnetconservator.app.kingnetconservator.Control.Camrea.CameraPreview;
import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.ScannerLine;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.SquareView;
import clery.kingnetconservator.app.kingnetconservator.R;
import me.dm7.barcodescanner.zbar.BarcodeFormat;

/**
 * Created by clery on 2017/2/16.
 */

public class BarcodeActivity extends Activity implements CodeScannerView{

    private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    private TextView scannerExTxt;
    private TextView titleTxt;
    private ImageScanner scanner;

    FrameLayout preview;
    SquareView squareView;
    ScannerLine scannerLine;

    CodeScannerPreCompl codeScannerPreCompl;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_barcode);

        codeScannerPreCompl = new CodeScannerPreCompl(getApplicationContext(),this);

        initControls();
        ShowSquareView();
    }

    private void initControls() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        autoFocusHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 1:
                        scannerLine.setmoveY();
                        break;
                }
            }
        };
        Timer timer01 = new Timer();
        timer01.schedule(task, 0, 5);

        if(CameraCheck.checkCameraHardware(getApplicationContext())){
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(BarcodeActivity.this, mCamera, previewCb,
                    autoFocusCB);

            preview = (FrameLayout) findViewById(R.id.cameraPreview);
            preview.addView(mPreview);

            setScanner();

        }

        titleTxt = (TextView)findViewById(R.id.titleTxt);
        scannerExTxt=(TextView)findViewById(R.id.qrscannerex);

        RelativeLayout.LayoutParams titleTxt_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        titleTxt_lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        titleTxt.setLayoutParams(titleTxt_lay);

        RelativeLayout.LayoutParams txt_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        txt_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        txt_lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        txt_lay.setMargins(0,0,0, ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        scannerExTxt.setLayoutParams(txt_lay);

        titleTxt.setText("QR code掃描器");
        scannerExTxt.setText("將掃描器對準QR code即可讀取。");
    }

    private void setScanner(){
        // Instance barcode scanner
        scanner = new ImageScanner();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        scanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
        for (BarcodeFormat format : BarcodeFormat.ALL_FORMATS) {
            scanner.setConfig(format.getId(), Config.ENABLE, 1);
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     */
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            RemindToast.showText(getApplicationContext(),"開啟相機發生錯誤");
        }
        return c;
    }

    Camera.PreviewCallback previewCb = new Camera.PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);

            if (result != 0) {

                codeScannerPreCompl.setPreviewingState(false);

                SymbolSet syms = scanner.getResults();

                for (Symbol sym : syms) {

                    String scanResult = sym.getData().trim();

                    Intent intent = new Intent();       //創建一個Intent，聯繫Activity之用
                    Bundle bundleBack = new Bundle();//創建一個Bundle，傳值之用

                    bundleBack.putString("scanResult", scanResult);
                    intent.putExtras(bundleBack);
                    setResult(RESULT_OK, intent);       //回傳給A_1，RESULT_OK為回傳狀態

                    break;
                }
                finish();
            }
        }
    };

    // Mimic continuous auto-focusing
    Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if(codeScannerPreCompl.CheckPreviewingState()){
                mCamera.autoFocus(autoFocusCB);

            }
        }
    };

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            autoFocusHandler.sendMessage(message);
        }
    };

    /**
     * 移除攝影功能 釋放攝影鏡頭
     */
    private void releaseCamera() {
        if (mCamera != null) {

            mCamera.setPreviewCallback(null);
            mCamera.release();
            mCamera = null;

            codeScannerPreCompl.setPreviewingState(false);
            codeScannerPreCompl.setEnmergenceState();

            preview.removeAllViews();
            squareView = null;

            autoFocusHandler.removeCallbacks(doAutoFocus);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(codeScannerPreCompl.CheckEnmergenceState() && mCamera == null){
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(BarcodeActivity.this, mCamera, previewCb,
                    autoFocusCB);
            preview.addView(mPreview);
            ShowSquareView();

            codeScannerPreCompl.setEnmergenceState();
            codeScannerPreCompl.setPreviewingState(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            releaseCamera();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 建立方框
     */
    @Override
    public void ShowSquareView() {
        if(squareView==null){
            squareView = new SquareView(getApplicationContext());
            preview.addView(squareView);

            scannerLine = new ScannerLine(getApplicationContext());
            preview.addView(scannerLine);
        }
    }
}