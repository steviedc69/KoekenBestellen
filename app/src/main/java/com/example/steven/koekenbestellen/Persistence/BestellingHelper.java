package com.example.steven.koekenbestellen.Persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Steven on 5/02/15.
 */
public class BestellingHelper extends SQLiteOpenHelper {

    private static final String TAG = "BestellingHelper";
    private static final String CREATE_Q = "create table "+Constance.TABLE_NAME+" ("+
            Constance.COLUMN_ID+" integer primary key autoincrement, "+
            Constance.COLUMN_NAAM+" text not null, "+
            Constance.COLUMN_VOORNAAM+" text not null, " +
            Constance.COLUMN_AFLEVERING+" text, " +
            Constance.COLUMN_GSM+" text, "+
            Constance.COLUMN_CHOCO+" integer not null, "+
            Constance.COLUMN_VANILLE+" integer not null, "+
            Constance.COLUMN_FRANCH+" integer not null," +
            Constance.COLUMN_BETAALD+" boolean not null );";


    public BestellingHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+Constance.TABLE_NAME);
        onCreate(db);
    }
}
