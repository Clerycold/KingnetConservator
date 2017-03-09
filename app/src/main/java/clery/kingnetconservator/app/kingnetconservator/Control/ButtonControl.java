package clery.kingnetconservator.app.kingnetconservator.Control;

import android.widget.Button;

/**
 * Created by clery on 2017/2/20.
 */

public class ButtonControl {

    public static void ButtonSelect(Button button,boolean b,int resId){
        button.setSelected(b);
        button.setTextColor(resId);
    }
}
