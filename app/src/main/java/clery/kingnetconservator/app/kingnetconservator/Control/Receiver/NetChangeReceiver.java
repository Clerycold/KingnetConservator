package clery.kingnetconservator.app.kingnetconservator.Control.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import clery.kingnetconservator.app.kingnetconservator.Control.Net.NetworkUtil;
import clery.kingnetconservator.app.kingnetconservator.MenuActivity.view.ShowInternetStatus;


/**
 * Created by clery on 2017/3/1.
 */

public class NetChangeReceiver extends BroadcastReceiver {

    TextView textView;

    public NetChangeReceiver(TextView textView){
        this.textView = textView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String s = NetworkUtil.getConnectivityStatusString(context);
        textView.setText(s);
        switch (s) {
            case "WIFI":
                textView.setTextColor(Color.BLUE);
                break;
            case "無線網路":
                textView.setTextColor(Color.GREEN);
                break;
            default:
                textView.setTextColor(Color.RED);
                break;
        }
    }
}