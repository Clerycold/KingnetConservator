package clery.kingnetconservator.app.kingnetconservator.PackageActivity.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import clery.kingnetconservator.app.kingnetconservator.PackageActivity.model.PackData;

/**
 * Created by clery on 2017/2/22.
 */

public class PackSQData {

    private PackHelper packHelper;
    private static PackSQData mInstance;

    public static void initManager(Context context){
        if(mInstance == null){
            mInstance = new PackSQData(context);
        }
    }

    public static PackSQData getManager(){
        return mInstance;
    }

    private PackSQData(Context context){

        packHelper = new PackHelper(context);
    }

    public void insert(PackData packData){
        SQLiteDatabase db = packHelper.getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(PackFieldName.TRANSPORT_CODE, packData.getTransport_code());
            values.put(PackFieldName.P_NAME, packData.getP_name());
            values.put(PackFieldName.TABLET_NOTE, packData.getTablet_note());
            values.put(PackFieldName.POSTAL_TYPE, packData.getPostal_type());
            values.put(PackFieldName.POSTAL_LOGISTICS, packData.getPostal_logistics());
            values.put(PackFieldName.POSTAL_ID, packData.getPostal_id());
            values.put(PackFieldName.P_NOTE, packData.getP_note());
            values.put(PackFieldName.CREATE_DATE, packData.getCreate_date());
            values.put(PackFieldName.IS_PAIVATE, packData.getIs_private());

            db.insert(PackFieldName.DATABASE_TABLE, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }

        db.close();
    }
    public void delete(String field,List<String> SearchString){
        SQLiteDatabase db = packHelper.getWritableDatabase();
        String whereClause = field + " = ?;";

        for(int i=0,j=SearchString.size();i<j;i++){
            String[] whereArgs = { SearchString.get(i) };
            db.delete(PackFieldName.DATABASE_TABLE , whereClause , whereArgs);
        }

        db.close();
    }

    public int getCount(){
        Cursor cursor = getCursor();

        return cursor.getCount();
    }

    public List<PackData> getAllDatatoList(){
        List<PackData> packDataList = new ArrayList<>();

        Cursor cursor = getCursor();

        for(int i =0,j=cursor.getCount();i<j;i++){
            PackData packData = new PackData();
            cursor.moveToPosition(i);
            packData.setP_note(cursor.getString(cursor.getColumnIndex(PackFieldName.P_NOTE)));
            packData.setPostal_type(cursor.getString(cursor.getColumnIndex(PackFieldName.POSTAL_TYPE)));
            packData.setPostal_logistics(cursor.getString(cursor.getColumnIndex(PackFieldName.POSTAL_LOGISTICS)));
            packData.setIs_private(cursor.getString(cursor.getColumnIndex(PackFieldName.IS_PAIVATE)));
            packData.setTransport_code(cursor.getString(cursor.getColumnIndex(PackFieldName.TRANSPORT_CODE)));
            packData.setPostal_id(cursor.getString(cursor.getColumnIndex(PackFieldName.POSTAL_ID)));
            packData.setCreate_date(cursor.getString(cursor.getColumnIndex(PackFieldName.CREATE_DATE)));
            packData.setP_name(cursor.getString(cursor.getColumnIndex(PackFieldName.P_NAME)));
            packData.setTablet_note(cursor.getString(cursor.getColumnIndex(PackFieldName.TABLET_NOTE)));

            packDataList.add(packData);
        }

        return packDataList;
    }

    //搜尋資料庫 全部的指定欄位的 特定值
    public String getPackField(int position,String field){
        Cursor cursor = getCursor();

        Log.d("--count",Integer.toString(cursor.getCount()));
        for (int i=0,j=cursor.getCount();i<j;i++){
            cursor.moveToPosition(i);
            Log.d("--TRANSPORT_CODE",cursor.getString(1));
            Log.d("--TABLET_NOTE",cursor.getString(3));
        }
//        cursor.moveToPosition(position);

        return cursor.getString(cursor.getColumnIndex(field));
    }
    //搜尋相同欄位的值
    public void getPackField(String Fiedld,String SearchString){
        Cursor cursor = getSameCursor(Fiedld,SearchString);

        for (int i=0,j=cursor.getCount();i<j;i++){
            cursor.moveToPosition(i);
        }

    }

    private Cursor getCursor(){

        SQLiteDatabase db = packHelper.getWritableDatabase();

        String[] columns = FiledString();
        Cursor cursor = db.query(PackFieldName.DATABASE_TABLE, columns, null,null, null, null, null);

        if(cursor != null && cursor.getCount() != 0){
            cursor.moveToFirst();

        }
        return cursor;
    }
    private Cursor getSameCursor(String Fiedld,String SearchString){

        SQLiteDatabase db = packHelper.getWritableDatabase();

        String[] columns = FiledString();
        Cursor cursor = db.query(PackFieldName.DATABASE_TABLE,
                columns, Fiedld+ "=?",new String[]{SearchString}, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }

        return cursor;
    }

    private String[] FiledString(){
        return new String[]{PackFieldName.KEY_ID,
                PackFieldName.TRANSPORT_CODE,
                PackFieldName.P_NAME,
                PackFieldName.TABLET_NOTE,
                PackFieldName.POSTAL_TYPE,
                PackFieldName.POSTAL_LOGISTICS,
                PackFieldName.POSTAL_ID,
                PackFieldName.P_NOTE,
                PackFieldName.CREATE_DATE,
                PackFieldName.IS_PAIVATE};
    }
}
