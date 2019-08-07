package am.ze.wookoo.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 2;
    public  DBHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }




    @Override
    public void onCreate(SQLiteDatabase db) {

        // String 보다 StringBuffer가 Query 만들기 편하다.
        StringBuffer sb = new StringBuffer();
        sb.append(" CREATE TABLE user ( ");
        sb.append(" NAME TEXT, ");
        sb.append(" AGE TEXT, ");
        sb.append(" WEIGHT TEXT, ");
        sb.append(" ImageURL TEXT, ");
        sb.append(" ALARM TEXT )");

        // SQLite Database로 쿼리 실행
        db.execSQL(sb.toString());
        Log.d("sql","DB 생성됨");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }




}

