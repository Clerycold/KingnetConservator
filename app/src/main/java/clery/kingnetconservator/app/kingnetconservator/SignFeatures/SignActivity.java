package clery.kingnetconservator.app.kingnetconservator.SignFeatures;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import clery.kingnetconservator.app.kingnetconservator.Control.ButtonControl;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.Control.UIpattern.Backbutton;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/2/15.
 */

public class SignActivity extends Activity {

    private RelativeLayout relativeLayout;
    private TextView activitytitle;
    private Button btn_save, btn_resume;

    private DrawView drawView;
    private Backbutton backbutton;

    private Intent intent;
    private static final int ACTION_FLAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        initView();
        xmlArrange();
        setData();

        backbutton.setOnTouchListener(Touch);
        btn_resume.setOnTouchListener(Touch);
        btn_save.setOnTouchListener(Touch);

    }

    private void initView() {

        relativeLayout = (RelativeLayout) findViewById(R.id.relative);

        activitytitle = (TextView)findViewById(R.id.activitytitle);
        backbutton = (Backbutton) findViewById(R.id.backbutton);
        btn_save = (Button) findViewById(R.id.btnsave);
        btn_resume = (Button) findViewById(R.id.btnresume);
        drawView = new DrawView(this, ScreenWH.getScreenWidth(),
                ScreenWH.getNoStatus_bar_Height(getApplicationContext()) / 10 * 7+ScreenWH.getUISpacingY());

        relativeLayout.addView(drawView);
    }

    private void xmlArrange() {

        RelativeLayout.LayoutParams activitytitle_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/10*8,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext()) / 10);
        activitytitle_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        activitytitle_lay.setMargins(0, ScreenWH.getUISpacingY(), 0, ScreenWH.getUISpacingY());
        activitytitle.setLayoutParams(activitytitle_lay);

        RelativeLayout.LayoutParams backbutton_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/10, ScreenWH.getScreenWidth()/10);
        backbutton_lay.setMargins(ScreenWH.getUISpacingY()/2,ScreenWH.getNoStatus_bar_Height(getApplicationContext())/30,0,0);
        backbutton.setLayoutParams(backbutton_lay);

        RelativeLayout.LayoutParams btn_save_lay = new RelativeLayout.LayoutParams((ScreenWH.getScreenWidth() / 2) - ScreenWH.getUISpacingY() / 2,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext()) / 10);
        btn_save_lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btn_save_lay.setMargins(ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY(), 0, ScreenWH.getUISpacingY());
        btn_save.setLayoutParams(btn_save_lay);
        btn_save.bringToFront();

        RelativeLayout.LayoutParams btn_resume_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth() / 2,
                ScreenWH.getNoStatus_bar_Height(getApplicationContext()) / 10);
        btn_resume_lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        btn_resume_lay.addRule(RelativeLayout.RIGHT_OF, btn_save.getId());
        btn_resume_lay.setMargins(ScreenWH.getUISpacingY() / 2, ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY(), ScreenWH.getUISpacingY());
        btn_resume.setLayoutParams(btn_resume_lay);
        btn_resume.bringToFront();

        RelativeLayout.LayoutParams drawView_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        drawView_lay.addRule(RelativeLayout.BELOW, activitytitle.getId());
        drawView.setLayoutParams(drawView_lay);

    }

    private void setData() {
        intent = this.getIntent();
        if(intent != null){
            drawView.signTable_note(intent.getStringExtra("makeSureTable"));
        }

        activitytitle.setText("包 裹 簽 收");
    }

    private View.OnTouchListener Touch = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (view.getId()) {
                case R.id.btnsave:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(btn_save,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(btn_save,false,Color.BLACK);
                            drawView.signData();
                            drawView.savecacheBitmap();

                            boolean SING_FINSH = true;
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("SING_FINSH", SING_FINSH);
                            bundle.putString("PICTURE",drawView.getPictureUrl());
                            intent.putExtras(bundle);
                            setResult(ACTION_FLAG, intent);

                            finish();
                            break;
                    }
                    break;
                case R.id.btnresume:
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            ButtonControl.ButtonSelect(btn_resume,true,Color.WHITE);
                            break;
                        case MotionEvent.ACTION_UP:
                            ButtonControl.ButtonSelect(btn_resume,false,Color.BLACK);
                            drawView.clearCanvas();
                            break;
                    }
                    break;
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
            }
            return true;
        }
    };
}
