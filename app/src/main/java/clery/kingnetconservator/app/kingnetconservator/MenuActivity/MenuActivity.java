package clery.kingnetconservator.app.kingnetconservator.MenuActivity;

import android.Manifest;
import android.app.Activity;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import clery.kingnetconservator.app.kingnetconservator.Control.ButtonControl;
import clery.kingnetconservator.app.kingnetconservator.Control.FileUtils.FileUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.IntentActivity;
import clery.kingnetconservator.app.kingnetconservator.Control.Net.KingnetServerLocation;
import clery.kingnetconservator.app.kingnetconservator.Control.Net.NetUtils;
import clery.kingnetconservator.app.kingnetconservator.LoginActivity.view.MainActivity;
import clery.kingnetconservator.app.kingnetconservator.MenuActivity.model.WeakHandler;
import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;
import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.SysApplication;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Titledesign;
import clery.kingnetconservator.app.kingnetconservator.Control.Receiver.NetChangeReceiver;
import clery.kingnetconservator.app.kingnetconservator.MenuActivity.view.ShowInternetStatus;
import clery.kingnetconservator.app.kingnetconservator.OkPackActivity.OkPackActivity;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.PackActivity;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackData;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackDataArrange;
import clery.kingnetconservator.app.kingnetconservator.PatrolActivity.PatrolActivity;
import clery.kingnetconservator.app.kingnetconservator.R;
import clery.kingnetconservator.app.kingnetconservator.OkPackActivity.SQLite.OkPackeHelper;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.SQLite.PackHelper;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.SQLite.PackSQData;
import clery.kingnetconservator.app.kingnetconservator.TourActivity.TourActivity;

/**
 * Created by clery on 2017/2/16.
 */

public class MenuActivity extends Activity implements Runnable,ShowInternetStatus{

    private String TAG = "MenuActivity";

    int PERMISSION_WRITE ;

    int PERMISSION_CAMERA ;

    private Titledesign menu_title;

    private TextView internetText;

    private Button packagemenu, okpackmenu, tourmenu,patrolmenu,textmenu;

    private static final int REQUEST_SIGN_PERMISSION = 100;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    /**
     * 判斷是否退出程式
     */
    private boolean isQuit = false;

    private WeakHandler weakHandler;

    private SaveData saveData;

    private PackSQData packSQData;

    private NetChangeReceiver netChangeReceiver;

    private FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        initView();
        xmlArrange();
        setData();
        setViewListener();

        RemindToast.showText(getApplicationContext(),"登入成功");
        new Thread(this).start();
    }

    private void initView() {

        SaveData.initManager(getApplicationContext());
        saveData = SaveData.getsaveData();

        WeakHandler.initManager(this,this);
        weakHandler = WeakHandler.getweakHandler();

        FileUtils.initManager(getApplicationContext());
        fileUtils = FileUtils.getManager();

        PackSQData.initManager(getApplicationContext());
        packSQData = PackSQData.getManager();

        menu_title = (Titledesign) findViewById(R.id.menu_title);
        internetText = (TextView) findViewById(R.id.internetText);
        packagemenu = (Button) findViewById(R.id.packagemenu);
        okpackmenu = (Button) findViewById(R.id.okpackmenu);
        tourmenu = (Button) findViewById(R.id.tourmenu);
        patrolmenu = (Button) findViewById(R.id.patrolmenu);
        textmenu = (Button) findViewById(R.id.textmenu);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        netChangeReceiver = new NetChangeReceiver(internetText);
        registerReceiver(netChangeReceiver, intentFilter);

    }

    private void xmlArrange() {

        RelativeLayout.LayoutParams packagemenu_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/3*2,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        packagemenu_lay.addRule(RelativeLayout.BELOW,menu_title.getId());
        packagemenu_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        packagemenu_lay.setMargins(0,ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10,0,0);
        packagemenu.setLayoutParams(packagemenu_lay);

        RelativeLayout.LayoutParams okpackmenu_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/3*2,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        okpackmenu_lay.addRule(RelativeLayout.BELOW,packagemenu.getId());
        okpackmenu_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        okpackmenu_lay.setMargins(0,ScreenWH.getUISpacingY()*2,0,0);
        okpackmenu.setLayoutParams(okpackmenu_lay);

        RelativeLayout.LayoutParams tourmenu_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/3*2,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        tourmenu_lay.addRule(RelativeLayout.BELOW,okpackmenu.getId());
        tourmenu_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tourmenu_lay.setMargins(0,ScreenWH.getUISpacingY()*2,0,0);
        tourmenu.setLayoutParams(tourmenu_lay);

        RelativeLayout.LayoutParams patrolmenu_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/3*2,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        patrolmenu_lay.addRule(RelativeLayout.BELOW,tourmenu.getId());
        patrolmenu_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        patrolmenu_lay.setMargins(0,ScreenWH.getUISpacingY()*2,0,0);
        patrolmenu.setLayoutParams(patrolmenu_lay);

        RelativeLayout.LayoutParams textmenu_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/3*2,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext())/10);
        textmenu_lay.addRule(RelativeLayout.BELOW,patrolmenu.getId());
        textmenu_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        textmenu_lay.setMargins(0,ScreenWH.getUISpacingY()*2,0,0);
        textmenu.setLayoutParams(textmenu_lay);

    }

    private void setData() {

        PERMISSION_WRITE = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        PERMISSION_CAMERA = ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CAMERA);

        menu_title.setTextView("管理服務項目選單");
        packagemenu.setText("包    裹");
        okpackmenu.setText("完成包裹");
        tourmenu.setText("導覽教學");
        patrolmenu.setText("巡    邏");
        textmenu.setText("刪除包裹資料");

    }

    private void setViewListener() {

        packagemenu.setOnTouchListener(touch);
        okpackmenu.setOnTouchListener(touch);
        tourmenu.setOnTouchListener(touch);
        patrolmenu.setOnTouchListener(touch);
        textmenu.setOnTouchListener(touch);

    }

    private View.OnTouchListener touch = new View.OnTouchListener(){

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.okpackmenu:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(okpackmenu,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(okpackmenu,false,Color.BLACK);
                            IntentActivity.IntentActivity(MenuActivity.this,OkPackActivity.class);

                            break;
                    }
                    break;
                case R.id.packagemenu:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(packagemenu,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(packagemenu,false,Color.BLACK);

                            if (PERMISSION_WRITE != PackageManager.PERMISSION_GRANTED ) {
                                ActivityCompat.requestPermissions(MenuActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_SIGN_PERMISSION);
                            }else IntentActivity.IntentActivity(MenuActivity.this,PackActivity.class);
                            break;
                    }
                    break;
                case R.id.tourmenu:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(tourmenu,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(tourmenu,false,Color.BLACK);
                            IntentActivity.IntentActivity(MenuActivity.this, TourActivity.class);
                            break;
                    }
                    break;
                case R.id.patrolmenu:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(patrolmenu, true, Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(patrolmenu, false, Color.BLACK);

                            if (PERMISSION_WRITE != PackageManager.PERMISSION_GRANTED && PERMISSION_CAMERA != PackageManager.PERMISSION_GRANTED) {
                                // 無權限，向使用者請求
                                ActivityCompat.requestPermissions(MenuActivity.this,
                                        new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CAMERA_PERMISSION);
                            }else{
                                IntentActivity.IntentActivity(MenuActivity.this, PatrolActivity.class);
                            }

                    }
                    break;
                case R.id.textmenu:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(textmenu,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(textmenu,false,Color.BLACK);

                            if(saveData.readDataBoolean("isgetData")){
                                fileUtils.selectDeleteFiled(PackHelper.PACK_PATH);
                                fileUtils.selectDeleteFiled(OkPackeHelper.PACK_PATH);

                                PackDataArrange.clearArrangeData();
                                saveData.saveBooleanData("isgetData", false);

                                RemindToast.showText(getApplication(),"資料刪除成功");
                            }else{
                                RemindToast.showText(getApplication(),"目前尚無資料");
                            }
                            break;
                    }
            }
            return true;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        doNext(requestCode,grantResults);
    }
    private void doNext(int requestCode, int[] grantResults) {
        if(requestCode == REQUEST_SIGN_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                IntentActivity.IntentActivity(this,PackActivity.class);

            } else {
                RemindToast.showText(getApplicationContext(),"無法開啟，請確認權限");
            }
        }else if(requestCode == REQUEST_CAMERA_PERMISSION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                IntentActivity.IntentActivity(this,PatrolActivity.class);

            } else {
                RemindToast.showText(getApplicationContext(),"無法開啟，請確認權限");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            if (!isQuit) {
                Timer timer = new Timer();
                isQuit = true;
                RemindToast.showText(getApplicationContext(),"再按一次返回鍵離開程式");
                /**
                 * 設定task兩秒後啟動
                 */
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 2000);
            } else {
                finish();
                SysApplication.getInstance().exit();
                System.exit(0);
            }
        }
        return false;
    }

    @Override
    public void run() {
        if(!saveData.readDataBoolean("isgetData")){
            NetUtils.doJsonDowland(KingnetServerLocation.getKingnetService(),
                    "GET",null, weakHandler, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(saveData.readDataBoolean("isgetData")){
            PackDataArrange.resetListdata(packSQData.getAllDatatoList());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(netChangeReceiver);
    }
    @Override
    public void JsonDataArrange(String json) {
        try{
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject;
            PackData packData;
            if(jsonArray.length()!=0){
                for(int i = 0,j=jsonArray.length();i < j;i++){
                    jsonObject = (JSONObject) jsonArray.get(i);
                    packData = new PackData();
                    packData.setPostal_id(jsonObject.getString("postal_id"));
                    packData.setTablet_note(jsonObject.getString("tablet_note").replaceAll(" ", ""));
                    packData.setCreate_date(jsonObject.getString("create_date"));
                    packData.setPostal_type(jsonObject.getString("postal_type"));
                    packData.setP_name(jsonObject.getString("p_name"));
                    packData.setSerial_num(jsonObject.getString("serial_num"));
                    packData.setIs_private(jsonObject.getString("is_private"));
                    packData.setPostal_logistics(jsonObject.getString("postal_logistics"));
                    packData.setTransport_code(jsonObject.getString("transport_code"));
                    packData.setP_note(jsonObject.getString("p_note"));

                    packSQData.insert(packData);

                }
                saveData.saveBooleanData("isgetData",true);
                saveData.saveIntData("UPDATAPACKCOUNT",packSQData.getCount());

                PackDataArrange.resetListdata(packSQData.getAllDatatoList());

                RemindToast.showText(getApplicationContext(),"資料更新完成");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
