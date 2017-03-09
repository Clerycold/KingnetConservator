package clery.kingnetconservator.app.kingnetconservator.OkPackActivity.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by clery on 2017/2/23.
 */

public class OkPackeHelper extends SQLiteOpenHelper {

    public static String PACK_PATH = "/data/data/clery.kingnetconservator.app.kingnetconservator/databases/packok.db";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packok.db";

    OkPackeHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE "+OkPackField.DATABASE_TABLE+ " "  + "("
                + OkPackField.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + OkPackField.TRANSPORT_CODE+" TEXT, "
                + OkPackField.P_NAME+" TEXT, "
                + OkPackField.TABLET_NOTE+" TEXT, "
                + OkPackField.POSTAL_TYPE+" TEXT, "
                + OkPackField.POSTAL_LOGISTICS+" TEXT, "
                + OkPackField.POSTAL_ID+" TEXT, "
                + OkPackField.P_NOTE+" TEXT, "
                + OkPackField.CREATE_DATE+" TEXT, "
                + OkPackField.IS_PAIVATE+" TEXT, "
                + OkPackField.PICTUREURL+" TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed, all data will be gone
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + OkPackField.DATABASE_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }
}
