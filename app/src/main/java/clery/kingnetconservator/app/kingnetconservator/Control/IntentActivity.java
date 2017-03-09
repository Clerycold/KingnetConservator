package clery.kingnetconservator.app.kingnetconservator.Control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import clery.kingnetconservator.app.kingnetconservator.PatrolActivity.PatrolActivity;

/**
 * Created by clery on 2017/2/15.
 */

public class IntentActivity {

    public static void IntentActivity(Context context, Class<?> cls){
        Intent intent=new Intent();
        Context mContext=context.getApplicationContext();
        intent.setClass(mContext,cls);
        try{
            mContext.startActivity(intent);
        }catch (Exception e){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
    public static void IntentActivity(Context context, Class<?> cls,String path,String strings){
        Intent intent=new Intent();
        Context mContext=context.getApplicationContext();
        intent.putExtra(path,strings);
        intent.setClass(mContext,cls);
        try{
            mContext.startActivity(intent);
        }catch (Exception e){
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }
    public static void setIntentResult(Activity activity,Context context, Class<?> cls,int REQUEST){
        Intent intent=new Intent();
        Context mContext=context.getApplicationContext();
        intent.setClass(mContext,cls);

        activity.startActivityForResult(intent,REQUEST);

    }

}
