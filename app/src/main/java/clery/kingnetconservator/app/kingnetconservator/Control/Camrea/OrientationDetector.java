package clery.kingnetconservator.app.kingnetconservator.Control.Camrea;

import android.content.Context;
import android.view.OrientationEventListener;

/**
 * Created by clery on 2017/3/3.
 */

public class OrientationDetector extends OrientationEventListener {

    private int orientation;

    private static OrientationDetector mInstance;

    public static void initManager(Context context){
        if(null == mInstance){
            mInstance = new OrientationDetector(context);
        }
    }

    public static OrientationDetector getOrientationDetector(){
        return mInstance;
    }

    private OrientationDetector(Context context) {
        super(context);
    }

    @Override
    public void onOrientationChanged(int orientation) {

        if(orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
            return;  //手机平放时，检测不到有效的角度
        }
        //只检测是否有四个角度的改变
        if( orientation > 350 || orientation< 10 ) { //0度
            this.orientation = 0;
        }
        else if( orientation > 80 &&orientation < 100 ) { //90度
            this.orientation= 90;
        }
        else if( orientation > 170 &&orientation < 190 ) { //180度
            this.orientation= 180;
        }
        else if( orientation > 260 &&orientation < 280  ) { //270度
            this.orientation= 270;
        }
        else {
            return ;
        }
    }
    public int getORIENTATION(){

        return orientation;
    }
}