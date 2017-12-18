package clery.kingnetconservator.app.kingnetconservator.PackageActivity.UIpattern;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicInteger;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;

/**
 * Created by clery on 2017/2/18.
 */

public class LinearHTextCheckBox extends RelativeLayout {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    Context context;

    TextView table_note;

    CheckBox checkBox;

    public LinearHTextCheckBox(Context context) {
        super(context);

        this.context = context;

        initView();
        xmlArrange();

    }

    private void initView() {

        table_note = new TextView(context);
        table_note.setId(generateViewId());
        checkBox = new CheckBox(context);
    }

    private void xmlArrange() {

        RelativeLayout.LayoutParams table_note_lay = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        table_note_lay.setMargins(ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX(),ScreenWH.getUISpacingX());
        table_note.setLayoutParams(table_note_lay);
        table_note.setTextColor(Color.WHITE);
        table_note.setGravity(Gravity.CENTER_VERTICAL);

        RelativeLayout.LayoutParams checkBox_lay = new RelativeLayout.LayoutParams(ScreenWH.getScreenWidth()/15,
                ScreenWH.getScreenWidth()/15);
        checkBox_lay.setMargins(0,0,ScreenWH.getScreenWidth()/20,0);
        checkBox_lay.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        checkBox_lay.addRule(RelativeLayout.CENTER_VERTICAL);
        checkBox.setLayoutParams(checkBox_lay);
        checkBox.setChecked(true);

        addView(table_note);
        addView(checkBox);

        this.setOnClickListener(click);
    }

    public void setTextView(String s){
        table_note.setText(s);
    }
    public void setTextColor(String s){
        if(s.equals("1")) table_note.setTextColor(Color.RED);

    }
    public boolean CheckBoxSelected(){
        return checkBox.isChecked();
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
    private View.OnClickListener click = new View.OnClickListener(){

        @Override
        public void onClick(View view) {
            checkBox.setChecked(!checkBox.isChecked());
        }
    };
}
