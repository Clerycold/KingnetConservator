package clery.kingnetconservator.app.kingnetconservator.Control;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by clery on 2017/2/15.
 */

public class RemindToast {
    private static Toast mtoast;

    /**
     * 單一模式 Singleton
     * @param context
     * @param str
     */
    public static void showText(Context context, String str){
        if(mtoast == null){
            synchronized(RemindToast.class) {
                if(mtoast == null){
                    mtoast = Toast.makeText(context,
                            str, Toast.LENGTH_SHORT);
                }
            }
        }else{
            mtoast.setText(str);
        }
        mtoast.show();
    }
}