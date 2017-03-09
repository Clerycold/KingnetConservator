package clery.kingnetconservator.app.kingnetconservator.Control;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * Created by clery on 2017/2/16.
 */

public class SaveData {

    private final static String SETTING = "getSharedPreferences";

    private SharedPreferences settings;
    private Context context;

    private static SaveData mInstance;

    private SaveData(Context context){

        this.context=context;
        settings = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE);

    }

    public static void initManager(Context context){
        if(null == mInstance){
            mInstance = new SaveData(context);
        }
    }

    public static SaveData getsaveData() {
        return mInstance;
    }

    public void saveBooleanData(String filename,Boolean data){
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(filename,data);
        editor.apply();
    }
    public Boolean readDataBoolean(String filename){
        return settings.getBoolean(filename,false);
    }

    public void saveIntData(String filename,int data){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(filename,data);
        editor.apply();
    }

    public int getDataInt(String filename){
        return settings.getInt(filename,0);
    }

    public void saveIntArray(String filename,int[] booleanArray) {

        int size = booleanArray.length;
        putIntValue(context,filename + "size", size);
        JSONArray jsonArray = new JSONArray();
        for (int b : booleanArray) {
            jsonArray.put(b);
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(filename,jsonArray.toString());
        editor.apply();
    }
    public int[] getIntArray(String filename)
    {

        int[] resArray = new int[getIntValue(context,filename + "size", 0)];
        Arrays.fill(resArray, 0);
        try {
            JSONArray jsonArray = new JSONArray(settings.getString(filename, "[]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                resArray[i] = jsonArray.getInt(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resArray;
    }

    public void saveApkEnalbleArray(String filename,boolean[] booleanArray) {
        JSONArray jsonArray = new JSONArray();
        for (boolean b : booleanArray) {
            jsonArray.put(b);
        }
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(filename,jsonArray.toString());
        editor.apply();
    }

    public boolean[] getApkEnableArray(String filename,int arrayLength)
    {
        boolean[] resArray=new boolean[arrayLength];
        Arrays.fill(resArray, true);
        try {
            JSONArray jsonArray = new JSONArray(settings.getString(filename, "[]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                resArray[i] = jsonArray.getBoolean(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resArray;
    }

    public void saveStringData(String filename,String string){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(filename,string);
        editor.apply();
    }
    public String getStringData(String filename){
        String aString= settings.getString(filename,"");

        return aString;
    }
    public void saveStringArray(String filename,String[] string){
        SharedPreferences.Editor editor=settings.edit();
        int size = string.length;
        putIntValue(context,filename + "size", size);
        for (int i=0,j=string.length;i<j;i++){
            editor.putString(filename+i,string[i]);
        }
        editor.apply();
    }
    public String[] getStringArryData(String filename){
        int size = getIntValue(context,filename + "size", 0);
        String[] temp = new String[size];
        for(int i=0;i<size;i++){
            temp[i] = settings.getString(filename+i,"");
        }
        return temp;
    }
    private static int getIntValue(Context context,String key, int defValue) {
        SharedPreferences sp = context.getSharedPreferences(SETTING,
                Context.MODE_PRIVATE);
        int value = sp.getInt(key, defValue);
        return value;
    }
    private static void putIntValue(Context context, String key, int value) {
        SharedPreferences.Editor sp = context.getSharedPreferences(SETTING, Context.MODE_PRIVATE)
                .edit();
        sp.putInt(key, value);
        sp.commit();
    }
}