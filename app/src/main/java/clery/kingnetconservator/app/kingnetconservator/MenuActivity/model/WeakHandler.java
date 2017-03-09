package clery.kingnetconservator.app.kingnetconservator.MenuActivity.model;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

import clery.kingnetconservator.app.kingnetconservator.MenuActivity.view.ShowInternetStatus;

/**
 * Created by clery on 2017/3/1.
 */

public class WeakHandler extends Handler{

    private final WeakReference<Activity> mActivity;
    private static WeakHandler mInstance;

    private ShowInternetStatus showInternetStatus;

    private WeakHandler(Activity activity,ShowInternetStatus showInternetStatus) {

        mActivity = new WeakReference<Activity>(activity);
        this.showInternetStatus = showInternetStatus;

    }

    public static void initManager(Activity activity,ShowInternetStatus showInternetStatus){
        if(null == mInstance){
            mInstance = new WeakHandler(activity,showInternetStatus);

        }
    }

    public static WeakHandler getweakHandler(){
        return mInstance;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        Activity activity = mActivity.get();
        if(activity != null){
            switch (msg.what){
                case 0:

                    Bundle bundle = msg.getData();
                    showInternetStatus.JsonDataArrange(bundle.getString("response"));

                    break;
            }
        }
    }
}
