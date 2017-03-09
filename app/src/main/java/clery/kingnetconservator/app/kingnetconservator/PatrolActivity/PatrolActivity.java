package clery.kingnetconservator.app.kingnetconservator.PatrolActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import clery.kingnetconservator.app.kingnetconservator.Control.ButtonControl;
import clery.kingnetconservator.app.kingnetconservator.Control.DataTime.DataTime;
import clery.kingnetconservator.app.kingnetconservator.Control.FileUtils.FileUtils;
import clery.kingnetconservator.app.kingnetconservator.Control.SaveData;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Backbutton;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Titledesign;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/3/2.
 */

public class PatrolActivity extends Activity{

    public static final int ACTION_FLAG = 1;
    public static String PATROLINTARRAY = "patrolIntArray";
    private static String PATROLDATAARRAY = "patroldataArray";
    private static String PATROLDATA = "patrolData";
    public static String PARTROLPICTUREPATH = "partrolPicturePath";
    public static String PARTROLLOCATION = "patrolLocation";

    private RelativeLayout patrol_rel;

    private Titledesign packagetitle;

    private Backbutton backbutton;

    private Button patrolbtnfinish;

    private TextView patrolcount;

    private String[] patrol = new String[]{"門口","側門","A棟樓梯間","廁所","垃圾場","後門"};

    private String[] patrolFinishTime;

    private String[] partrolPicturePath;

    private int[] patrolFinish ;

    /**
     * 計算剩餘數量
     */
    private int losecount;

    private PatrolViewInside patrolViewInside;

    private SaveData saveData;

    private FileUtils fileUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patrol);

        initView();
        xmlArrange();
        setData();
        setViewListener();
    }

    private void initView() {

        FileUtils.initManager(getApplicationContext());
        fileUtils = FileUtils.getManager();

        SaveData.initManager(getApplicationContext());
        saveData = SaveData.getsaveData();

        patrol_rel = (RelativeLayout) findViewById(R.id.patrol_rel);
        packagetitle = (Titledesign) findViewById(R.id.packagetitle);
        backbutton = (Backbutton) findViewById(R.id.backbutton);

        patrolbtnfinish = (Button) findViewById(R.id.patrolbtnfinish);
        patrolcount = (TextView) findViewById(R.id.patrolcount);

        patrolViewInside = new PatrolViewInside(this,getApplicationContext(),patrol);

    }

    private void xmlArrange(){

        RelativeLayout.LayoutParams backbutton_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth() / 10, ScreenWH.getScreenWidth() / 10);
        backbutton_lay.setMargins(ScreenWH.getUISpacingY(), (int) (ScreenWH.getUISpacingY() * 1.3), 0, 0);
        backbutton.setLayoutParams(backbutton_lay);

        RelativeLayout.LayoutParams patrolViewInside_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)(ScreenWH.getNoStatus_bar_Height(getApplicationContext())/ 10*7.9));
        patrolViewInside_lay.addRule(RelativeLayout.BELOW,backbutton.getId());
        patrolViewInside_lay.setMargins(0,ScreenWH.getUISpacingY()*2-10,0,0);
        patrolViewInside.setLayoutParams(patrolViewInside_lay);

        patrol_rel.addView(patrolViewInside);

        RelativeLayout.LayoutParams patrolbtnfinish_lay = new RelativeLayout.LayoutParams((int)(double)(ScreenWH.getScreenWidth() /2), ScreenWH.getNoStatus_bar_Height(getApplicationContext())/ 10);
        patrolbtnfinish_lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        patrolbtnfinish_lay.setMargins(ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY());
        patrolbtnfinish.setLayoutParams(patrolbtnfinish_lay);
    }

    private void setData(){

        patrolFinish = new int[patrol.length];
        patrolFinishTime = new String[patrol.length];
        partrolPicturePath = new String[patrol.length];

        packagetitle.setTextView("巡邏位置");
        patrolbtnfinish.setText("完成巡邏上傳");
        patrolbtnfinish.setEnabled(false);

        if(saveData.readDataBoolean("patrolData")){
            patrolFinish = saveData.getIntArray(PATROLINTARRAY);
            patrolFinishTime = saveData.getStringArryData(PATROLDATAARRAY);
            partrolPicturePath = saveData.getStringArryData(PARTROLPICTUREPATH);

            losecount = patrolFinish.length;

            for (int i=0,j=patrolFinish.length;i<j;i++){
                if(patrolFinish[i]==1){
                    patrolViewInside.FinishPatrol(i);
                    patrolViewInside.ShowFinishTime(i,patrolFinishTime[i]);
                    losecount--;
                }
            }
            patrolcount.setText("巡邏剩餘數量： "+Integer.toString(losecount));

            setpatrolbtnfinish(losecount);
        }else{

            losecount = patrol.length;

            patrolcount.setText("巡邏剩餘數量： "+Integer.toString(patrol.length));

            saveInfomation();
            saveData.saveBooleanData(PATROLDATA,true);
            saveData.saveStringArray(PARTROLLOCATION,patrol);
        }
    }

    private void setViewListener(){

        backbutton.setOnTouchListener(touch);
        patrolbtnfinish.setOnTouchListener(touch);

    }

    private View.OnTouchListener touch = new View.OnTouchListener() {
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
                case R.id.patrolbtnfinish:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(patrolbtnfinish,true, Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(patrolbtnfinish,false,Color.BLACK);

                            saveData.saveBooleanData(PATROLDATA,false);
                            for(int i = 0,j=partrolPicturePath.length;i<j;i++){
                                fileUtils.selectDeleteFiled(partrolPicturePath[i]);
                            }
                            finish();

                            break;
                    }
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        doNext(requestCode, data);
    }

    //刪除確認包裹的資料
    private void doNext(int requestCode, Intent data) {
        if (ACTION_FLAG == requestCode && data != null) {
            if(data.getExtras().getBoolean("true")){

                String dataTime = DataTime.getNowTime("yyyy-MM-dd_HH:mm");
                String picturePath = data.getExtras().getString("picturePath");

                if(patrolFinish[patrolViewInside.getClickTag()] == 0){
                    losecount--;
                }

                patrolFinish[patrolViewInside.getClickTag()] = 1;
                patrolFinishTime[patrolViewInside.getClickTag()] = dataTime;
                partrolPicturePath[patrolViewInside.getClickTag()] = picturePath;

                patrolViewInside.FinishPatrol(patrolViewInside.getClickTag());
                patrolViewInside.ShowFinishTime(patrolViewInside.getClickTag(),dataTime);

            }else{
                losecount ++;
                patrolFinish[patrolViewInside.getClickTag()] = 0;
            }
        }
        if(losecount >= patrolFinish.length){
            losecount = patrolFinish.length;
        }
        patrolcount.setText("巡邏剩餘數量： "+Integer.toString(losecount));
        setpatrolbtnfinish(losecount);
        saveInfomation();
    }

    /**
     * 儲存記錄數據
     */
    public void saveInfomation(){
        saveData.saveIntArray(PATROLINTARRAY,patrolFinish);
        saveData.saveStringArray(PATROLDATAARRAY,patrolFinishTime);
        saveData.saveStringArray(PARTROLPICTUREPATH,partrolPicturePath);
    }

    /**
     * 當巡邏點為0時 開啟上傳功能
     * @param count
     */
    private void setpatrolbtnfinish(int count){
        if(count == 0){
            patrolbtnfinish.setEnabled(true);
        }
    }
}
