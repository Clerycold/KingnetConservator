package clery.kingnetconservator.app.kingnetconservator.OkPackActivity.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackData;

/**
 * Created by clery on 2017/2/23.
 */

public class OkPackSQData {

    private OkPackeHelper okPackeHelper;
    private static OkPackSQData mInstance;

    public static void initManager(Context context){
        if(mInstance == null){
            mInstance = new OkPackSQData(context);
        }
    }

    public static OkPackSQData getManager(){
        return mInstance;
    }

    private OkPackSQData(Context context){

        okPackeHelper = new OkPackeHelper(context);
    }

    public void insert(PackData packData){
        SQLiteDatabase db = okPackeHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(OkPackField.TRANSPORT_CODE, packData.getTransport_code());
            values.put(OkPackField.P_NAME, packData.getP_name());
            values.put(OkPackField.TABLET_NOTE, packData.getTablet_note());
            values.put(OkPackField.POSTAL_TYPE, packData.getPostal_type());
            values.put(OkPackField.POSTAL_LOGISTICS, packData.getPostal_logistics());
            values.put(OkPackField.POSTAL_ID, packData.getPostal_id());
            values.put(OkPackField.P_NOTE, packData.getP_note());
            values.put(OkPackField.CREATE_DATE, packData.getCreate_date());
            values.put(OkPackField.IS_PAIVATE, packData.getIs_private());
            values.put(OkPackField.PICTUREURL,packData.getPicture_Url());

            db.insert(OkPackField.DATABASE_TABLE, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }

        db.close();
    }
    public int getCount(){
        Cursor cursor = getCursor();

        return cursor.getCount();
    }

    private Cursor getCursor(){

        SQLiteDatabase db = okPackeHelper.getWritableDatabase();

        String[] columns = FiledString();
        Cursor cursor = db.query(OkPackField.DATABASE_TABLE, columns, null,null, null, null, null);

        if(cursor != null ){
            cursor.moveToFirst();
        }

        return cursor;
    }

    private String[] FiledString(){
        return new String[]{OkPackField.KEY_ID,
                OkPackField.TRANSPORT_CODE,
                OkPackField.P_NAME,
                OkPackField.TABLET_NOTE,
                OkPackField.POSTAL_TYPE,
                OkPackField.POSTAL_LOGISTICS,
                OkPackField.POSTAL_ID,
                OkPackField.P_NOTE,
                OkPackField.CREATE_DATE,
                OkPackField.IS_PAIVATE,
                OkPackField.PICTUREURL};
    }

    public List<PackData> getAllDatatoList(){
        List<PackData> packDataList = new ArrayList<>();

        Cursor cursor = getCursor();

        for(int i =0,j=cursor.getCount();i<j;i++){
            PackData packData = new PackData();
            cursor.moveToPosition(i);
            packData.setP_note(cursor.getString(cursor.getColumnIndex(OkPackField.P_NOTE)));
            packData.setPostal_type(cursor.getString(cursor.getColumnIndex(OkPackField.POSTAL_TYPE)));
            packData.setPostal_logistics(cursor.getString(cursor.getColumnIndex(OkPackField.POSTAL_LOGISTICS)));
            packData.setIs_private(cursor.getString(cursor.getColumnIndex(OkPackField.IS_PAIVATE)));
            packData.setTransport_code(cursor.getString(cursor.getColumnIndex(OkPackField.TRANSPORT_CODE)));
            packData.setPostal_id(cursor.getString(cursor.getColumnIndex(OkPackField.POSTAL_ID)));
            packData.setCreate_date(cursor.getString(cursor.getColumnIndex(OkPackField.CREATE_DATE)));
            packData.setP_name(cursor.getString(cursor.getColumnIndex(OkPackField.P_NAME)));
            packData.setTablet_note(cursor.getString(cursor.getColumnIndex(OkPackField.TABLET_NOTE)));
            packData.setPicture_Url(cursor.getString(cursor.getColumnIndex(OkPackField.PICTUREURL)));

            packDataList.add(packData);
        }

        return packDataList;
    }
}