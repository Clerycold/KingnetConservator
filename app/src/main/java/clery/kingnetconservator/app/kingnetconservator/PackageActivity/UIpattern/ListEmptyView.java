package clery.kingnetconservator.app.kingnetconservator.PackageActivity.UIpattern;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.Control.ButtonControl;
import clery.kingnetconservator.app.kingnetconservator.Control.RemindToast;
import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackDataArrange;
import clery.kingnetconservator.app.kingnetconservator.PackageActivity.View.PackListShow;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/2/20.
 */

public class ListEmptyView extends RelativeLayout implements View.OnTouchListener{

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    Context context;
    TextView nodatatext;
    Button afreshBtn;

    PackListShow packListShow;

    public ListEmptyView(Context context,PackListShow packListShow) {
        super(context);


        this.context = context;
        this.packListShow = packListShow;

        initView();
        xmlArrange();

    }

    public ListEmptyView(Context context, AttributeSet attrs){
        super(context,attrs);
        this.context = context;

        initView();
        xmlArrange();
    }

    private void initView() {

        nodatatext = new TextView(context);
        nodatatext.setId(generateViewId());
        afreshBtn = new Button(context);
    }
    private void xmlArrange(){

        RelativeLayout.LayoutParams nodatatext_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth() / 3 * 2, ScreenWH.getScreenWidth() / 5);
        nodatatext_lay.setMargins(0,ScreenWH.getNoStatus_bar_Height(context)/10,0,0);
        nodatatext.setLayoutParams(nodatatext_lay);
        nodatatext.setTextSize(24);
        nodatatext.setTextColor(Color.BLACK);
        nodatatext.setGravity(Gravity.CENTER);
        nodatatext.setTypeface(Typeface.DEFAULT_BOLD);
        nodatatext.setText("無包裹資料！");

        addView(nodatatext);

        RelativeLayout.LayoutParams afreshBtn_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth() / 3, ScreenWH.getScreenWidth() / 5);
        afreshBtn_lay.addRule(RelativeLayout.BELOW,nodatatext.getId());
        afreshBtn_lay.addRule(RelativeLayout.CENTER_HORIZONTAL);
        afreshBtn_lay.setMargins(0,ScreenWH.getUISpacingY(),0,0);
        afreshBtn.setLayoutParams(afreshBtn_lay);
        afreshBtn.setBackgroundResource(R.drawable.bg_box_black);
        afreshBtn.setText("重新取得資料");
        afreshBtn.setTextColor(Color.BLACK);
        afreshBtn.setOnTouchListener(this);

        addView(afreshBtn);
    }

    //得到ViewId
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
                if (sNextGeneratedId.compareAndSet(result, newValue)) {
                    return result;
                }
            }
        } else {
            return View.generateViewId();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ButtonControl.ButtonSelect(afreshBtn,true,Color.WHITE);
                break;
            case MotionEvent.ACTION_UP:
                ButtonControl.ButtonSelect(afreshBtn,false,Color.BLACK);
                if(PackDataArrange.getTabletNoteOk() != null){
                    packListShow.packListShow();
                }else{
                    //設定有網路重新下載資料
                    RemindToast.showText(context,"請確認網路後，再重新取得資料");
                }
                break;
        }
        return false;
    }
}
