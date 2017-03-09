package clery.kingnetconservator.app.kingnetconservator.OkPackActivity;

import android.app.Activity;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import clery.kingnetconservator.app.kingnetconservator.Control.ButtonControl;
import clery.kingnetconservator.app.kingnetconservator.Control.Receiver.NetChangeReceiver;
import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Backbutton;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.HorizontalProgressBarWithNumber;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Titledesign;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackDataArrange;
import clery.kingnetconservator.app.kingnetconservator.R;
import clery.kingnetconservator.app.kingnetconservator.OkPackActivity.SQLite.OkPackSQData;

/**
 * Created by clery on 2017/2/23.
 */

public class OkPackActivity extends Activity{

    private String TAG = "OkPackActivity";
    /**
     * 完成進度表
     */
    private HorizontalProgressBarWithNumber mProgressBar;
    /**
     * 功能名
     */
    Titledesign okpack_title;
    /**
     * ListView空值 網路狀況 完成包裹數量
     */
    TextView oklistEmptyView,internetText, finsh_count;
    /**
     * 返回包裹上傳按鈕
     */
    Backbutton backbutton;
    /**
     * 完成包裹上傳按鈕
     */
    Button finsh_btn;

    /**
     * 完成包裹的資料庫
     */
    private OkPackSQData okPackSQData;
    /**
     * 網路狀況接收器
     */
    private NetChangeReceiver netChangeReceiver;
    /**
     * sendhandMessage
     */
    private static final int MSG_PROGRESS_UPDATE = 0x110;
    /**
     * SharedPreferences
     */
    private SaveData saveData;
    /**
     * 取得資料後的包裹數 完成的包裹數
     */
    private int updatapackcount,okpackcount;
    private ListView okpacklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okpack_activity);

        initView();
        xmlArrange();
        setData();
        setViewListener();

    }

    private void initView(){

        OkPackSQData.initManager(getApplicationContext());
        okPackSQData = OkPackSQData.getManager();

        SaveData.initManager(getApplicationContext());
        saveData = SaveData.getsaveData();

        okpack_title = (Titledesign) findViewById(R.id.okpack_title);
        mProgressBar = (HorizontalProgressBarWithNumber) findViewById(R.id.id_progressbar);

        backbutton = (Backbutton) findViewById(R.id.backbutton);
        okpacklist = (ListView) findViewById(R.id.okpacklist);
        oklistEmptyView = (TextView) findViewById(R.id.oklistEmptyView);
        oklistEmptyView.setVisibility(View.GONE);
        okpacklist.setEmptyView(oklistEmptyView);

        internetText = (TextView) findViewById(R.id.internetText);
        finsh_count = (TextView) findViewById(R.id.finsh_count);
        finsh_btn = (Button) findViewById(R.id.finsh_btn);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netChangeReceiver = new NetChangeReceiver(internetText);
        registerReceiver(netChangeReceiver, intentFilter);
    }

    private void xmlArrange(){

        RelativeLayout.LayoutParams backbutton_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth() / 10, ScreenWH.getScreenWidth() / 10);
        backbutton_lay.setMargins(ScreenWH.getUISpacingY(), (int) (ScreenWH.getUISpacingY() * 1.3), 0, 0);
        backbutton.setLayoutParams(backbutton_lay);

        RelativeLayout.LayoutParams okpacklist_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenWH.getNoStatus_bar_Height(getApplicationContext())/ 10*7-ScreenWH.getUISpacingY());
        okpacklist_lay.addRule(RelativeLayout.BELOW,mProgressBar.getId());
        okpacklist_lay.setMargins(0, ScreenWH.getUISpacingY(), 0, ScreenWH.getUISpacingY());
        okpacklist.setLayoutParams(okpacklist_lay);

        RelativeLayout.LayoutParams finsh_btn_lay = new RelativeLayout.LayoutParams((int)(double)(ScreenWH.getScreenWidth() /5*2.2), ScreenWH.getNoStatus_bar_Height(getApplicationContext())/ 10);
        finsh_btn_lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        finsh_btn_lay.setMargins(ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY());
        finsh_btn.setLayoutParams(finsh_btn_lay);

    }

    private void setData(){

        if(saveData.readDataBoolean("isgetData")){

            updatapackcount = saveData.getDataInt("UPDATAPACKCOUNT");
            okpackcount = okPackSQData.getCount();
            mHandler.sendEmptyMessage(MSG_PROGRESS_UPDATE);

            //整理後的住戶資料
            OkPackData okPackData = new OkPackData();
            okPackData.setTabletNote(PackDataArrange.nonDuplicate(okPackSQData.getAllDatatoList()));
            okPackData.setPackNewDatalist(PackDataArrange.ComparisonPackData(okPackSQData.getAllDatatoList(), okPackData.getTabletNote()));

            OkPackAdapter okPackAdapter = new OkPackAdapter(getApplicationContext(), okPackData.getTabletNote(), okPackData.getPackNewDatalist());
            okpacklist.setAdapter(okPackAdapter);
        }

        okpack_title.setTextView("完成包裹");
        oklistEmptyView.setText("目前尚未有完成包裹");
        finsh_count.setText("包裹完成數量："+Integer.toString(okpackcount));
        finsh_btn.setText("上傳更新並清除");
    }

    private void setViewListener(){
        backbutton.setOnTouchListener(Touch);
        finsh_btn.setOnTouchListener(Touch);
    }

    private View.OnTouchListener Touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.backbutton:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            backbutton.setPaintColor(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            backbutton.setPaintColor(false);
                            finish();
                            break;
                    }
                    break;
                case R.id.finsh_btn:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(finsh_btn,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(finsh_btn,false,Color.BLACK);
                            break;
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        unregisterReceiver(netChangeReceiver);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            double progress = mProgressBar.getProgress();
            double progresspercent = new BigDecimal((double)okpackcount/updatapackcount).setScale(2, RoundingMode.HALF_UP).doubleValue()*100;
            if (progress >= progresspercent) {
                mHandler.removeMessages(MSG_PROGRESS_UPDATE);
            }else{
                mProgressBar.setProgress((int)++progress);
                mHandler.sendEmptyMessageDelayed(MSG_PROGRESS_UPDATE, 50);
            }
        }
    };
}
