package clery.kingnetconservator.app.kingnetconservator.Control;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by clery on 2017/2/15.
 */

public class ScreenWH {
    private static DisplayMetrics Metrics = Resources.getSystem().getDisplayMetrics();

    public static int getScreenWidth(){
        return Metrics.widthPixels;
    }
    public static int getScreenHidth(){
        return Metrics.heightPixels;
    }
    public static int getNoStatus_bar_Height(Context context){
        Resources resources = context.getApplicationContext().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);

        return getScreenHidth() - height;
    }
    /**
     * Numerical=15
     * @return
     */
    public static int getUISpacing(){
        return Metrics.widthPixels/72;
    }

    public static int getUISpacingY(){
        return (int)(double)(Metrics.heightPixels/77);
    }

    public static int getUISpacingX(){
        return (int)(double)((Metrics.widthPixels/43.2));
    }
}