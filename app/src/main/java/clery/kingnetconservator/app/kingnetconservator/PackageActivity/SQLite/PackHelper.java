package clery.kingnetconservator.app.kingnetconservator.PackageActivity.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by clery on 2017/2/22.
 */

public class PackHelper extends SQLiteOpenHelper {

    public static String PACK_PATH = "/data/data/clery.kingnetconservator.app.kingnetconservator/databases/packnotdo.db";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "packnotdo.db";

    PackHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_TABLE = "CREATE TABLE "+PackFieldName.DATABASE_TABLE+ " "  + "("
                + PackFieldName.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PackFieldName.TRANSPORT_CODE+" TEXT, "
                + PackFieldName.P_NAME+" TEXT, "
                + PackFieldName.TABLET_NOTE+" TEXT, "
                + PackFieldName.POSTAL_TYPE+" TEXT, "
                + PackFieldName.POSTAL_LOGISTICS+" TEXT, "
                + PackFieldName.POSTAL_ID+" TEXT, "
                + PackFieldName.P_NOTE+" TEXT, "
                + PackFieldName.CREATE_DATE+" TEXT, "
                + PackFieldName.IS_PAIVATE+" TEXT)";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Drop older table if existed, all data will be gone
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PackFieldName.DATABASE_TABLE);
        // Create tables again
        onCreate(sqLiteDatabase);
    }
}
