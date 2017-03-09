package clery.kingnetconservator.app.kingnetconservator.LoginActivity.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import clery.kingnetconservator.app.kingnetconservator.BarcodeScanner.BarcodeActivity;
import clery.kingnetconservator.app.kingnetconservator.Control.ButtonControl;
import clery.kingnetconservator.app.kingnetconservator.Control.IntentActivity;
import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;
import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.SysApplication;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.EditAnimation;
import clery.kingnetconservator.app.kingnetconservator.LoginActivity.presenter.LoginPresenterCompl;
import clery.kingnetconservator.app.kingnetconservator.MenuActivity.MenuActivity;
import clery.kingnetconservator.app.kingnetconservator.R;

public class MainActivity extends Activity implements LoginView{

    private static final int REQUEST_CAMERA_PERMISSION = 100;

    private TextView logintitle;

    private CheckBox rememberbox, displaybox;

    private EditAnimation editaccount, editpassword, editCodes;

    private Button loginbtn, screenbtn;

    private SaveData saveData;

    public static String OPENCODE;

    LoginPresenterCompl loginPresenterCompl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginPresenterCompl = new LoginPresenterCompl(getApplicationContext(),this);

        initView();
        xmlArrange();
        setData();
        setViewListener();

    }

    private void initView() {

        logintitle = (TextView) findViewById(R.id.logintitle);
        editaccount = (EditAnimation) findViewById(R.id.editaccount);
        editpassword = (EditAnimation) findViewById(R.id.editpassword);
        editCodes = (EditAnimation) findViewById(R.id.editCodes);
        displaybox = (CheckBox) findViewById(R.id.displaybox);
        rememberbox = (CheckBox)findViewById(R.id.rememberbox);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        screenbtn = (Button) findViewById(R.id.screenbtn);

        SaveData.initManager(getApplicationContext());
        saveData = SaveData.getsaveData();

    }
    private void xmlArrange() {

        RelativeLayout.LayoutParams editaccount_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editaccount_lay.addRule(RelativeLayout.BELOW,logintitle.getId());
        editaccount_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editaccount.setLayoutParams(editaccount_lay);

        RelativeLayout.LayoutParams editpassword_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editpassword_lay.addRule(RelativeLayout.BELOW,editaccount.getId());
        editpassword_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editpassword.setLayoutParams(editpassword_lay);
        editpassword.setTransformationMethod(false);

        RelativeLayout.LayoutParams editCodes_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editCodes_lay.addRule(RelativeLayout.BELOW,rememberbox.getId());
        editCodes_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        editCodes.setLayoutParams(editCodes_lay);

        RelativeLayout.LayoutParams rememberbox_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rememberbox_lay.addRule(RelativeLayout.BELOW,editpassword.getId());
        rememberbox_lay.setMargins((int)(ScreenWH.getScreenWidth()/6.3*1.1),0,0,0);
        rememberbox.setLayoutParams(rememberbox_lay);

        RelativeLayout.LayoutParams loginbtn_lay = new RelativeLayout.LayoutParams((int)(ScreenWH.getScreenWidth()/6.3*2),
                ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        loginbtn_lay.addRule(RelativeLayout.BELOW,editCodes.getId());
        loginbtn_lay.setMargins((int)(ScreenWH.getScreenWidth()/6.3*0.8),ScreenWH.getUISpacingY()*2,0,0);
        loginbtn.setLayoutParams(loginbtn_lay);

        RelativeLayout.LayoutParams screenbtn_lay = new RelativeLayout.LayoutParams((int)(ScreenWH.getScreenWidth()/6.3*2),
                ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        screenbtn_lay.addRule(RelativeLayout.BELOW,editCodes.getId());
        screenbtn_lay.addRule(RelativeLayout.RIGHT_OF,loginbtn.getId());
        screenbtn_lay.setMargins(ScreenWH.getUISpacingX(),ScreenWH.getUISpacingY()*2,0,0);
        screenbtn.setLayoutParams(screenbtn_lay);

    }

    private void setViewListener() {

        loginbtn.setOnTouchListener(touch);
        screenbtn.setOnTouchListener(touch);

        displaybox.setOnCheckedChangeListener(boxCheck);
        rememberbox.setOnCheckedChangeListener(boxCheck);

    }

    private void setData() {

        logintitle.setText("今網智慧管理端服務");

        editaccount.setTextEx("帳號");
        editpassword.setTextEx("密碼");
        editCodes.setTextEx("驗證碼");
        editCodes.setInputType(InputType.TYPE_CLASS_NUMBER);

        displaybox.setText("顯示密碼");
        rememberbox.setText("記住帳號密碼");

        loginPresenterCompl.ShowAccount();

    }

    private View.OnTouchListener touch = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.loginbtn:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(loginbtn,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(loginbtn,false,Color.BLACK);
                            if(editaccount.CheckEditString())editaccount.setExitEmptyColor("請輸入行動管家帳號");
                            else if(editpassword.CheckEditString())editpassword.setExitEmptyColor("請輸入行動管家密碼");
                            else if(editCodes.CheckEditString())editCodes.setExitEmptyColor("請輸入行動管家驗證碼");
                        else{
                                IntentActivity.IntentActivity(MainActivity.this,MenuActivity.class);
                            }
                            break;
                    }
                    break;
                case R.id.screenbtn:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(screenbtn,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(screenbtn,false,Color.BLACK);
                            int permission = ActivityCompat.checkSelfPermission(getApplicationContext(),
                                    Manifest.permission.CAMERA);
                            if (permission != PackageManager.PERMISSION_GRANTED ) {
                                // 無權限，向使用者請求
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_PERMISSION);
                            }else{
                                loginPresenterCompl.SaveAccount();
                                IntentActivity.setIntentResult(MainActivity.this,getApplicationContext(),BarcodeActivity.class,REQUEST_CAMERA_PERMISSION);
                            }
                            break;
                    }
            }
            return true;
        }
    };

    private CompoundButton.OnCheckedChangeListener boxCheck = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            switch (compoundButton.getId()){
                case R.id.displaybox:
                    editpassword.setTransformationMethod(b);
                    break;
                case R.id.rememberbox:
                    loginPresenterCompl.setRemberBox(b);
                    break;
            }
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doIntentNext(requestCode,data);
    }

    private void doIntentNext(int requestCode, Intent data) {
        if(requestCode == REQUEST_CAMERA_PERMISSION){
            if(data != null){
                Bundle b=data.getExtras();
                OPENCODE =b.getString("scanResult");
                editCodes.setEditText(OPENCODE);

                IntentActivity.IntentActivity(MainActivity.this,MenuActivity.class);
            }else{
                RemindToast.showText(getApplicationContext(),"無掃描資料");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }
    private void doNext(int requestCode, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                IntentActivity.setIntentResult(this,this,BarcodeActivity.class,REQUEST_CAMERA_PERMISSION);

            } else {
                RemindToast.showText(getApplicationContext(), "無法開啟，請確認權限");
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        loginPresenterCompl.SaveAccount();
        SysApplication.getInstance().addActivity(this);

    }

    @Override
    public void ShowAccount() {

        editaccount.setEditText(saveData.getStringData("editaccount"));
        editpassword.setEditText(saveData.getStringData("editpassword"));
        editCodes.setEditText(saveData.getStringData("OPENCODE"));

    }

    @Override
    public void ShowCheckBox(boolean b) {
        rememberbox.setChecked(b);
    }

    @Override
    public String geteditaccount() {
        return editaccount.getEditString();
    }

    @Override
    public String geteditpassword() {
        return editpassword.getEditString();
    }

    @Override
    public String geteditCodes() {
        return editCodes.getEditString();
    }
}
