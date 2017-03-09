package clery.kingnetconservator.app.kingnetconservator.Control.UIpattern;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import clery.kingnetconservator.app.kingnetconservator.Control.ScreenWH;
import clery.kingnetconservator.app.kingnetconservator.R;

/**
 * Created by clery on 2017/2/16.
 */

public class Titledesign extends RelativeLayout {

    private TextView textView;
    int Width;
    int Height;

    public Titledesign(Context context, AttributeSet attrs) {
        super(context, attrs);
        Width= ScreenWH.getScreenWidth();
        Height=ScreenWH.getNoStatus_bar_Height(context.getApplicationContext());

        textView = new TextView(context);

        RelativeLayout.LayoutParams r_title_lay =new RelativeLayout.LayoutParams((int)(double)Width,(int)(double)Height/10);
        textView.setLayoutParams(r_title_lay);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        this.addView(textView);

    }
    public void setTextView(String string){
        textView.setText(string);
    }
}
