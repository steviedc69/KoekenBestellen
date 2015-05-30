package com.example.steven.koekenbestellen.Persistence;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Steven on 5/02/15.
 */
public class BestellingDB {

    private static final String TAG = "BestellingDB";

    private BestellingHelper helper;

    private SQLiteDatabase db;

    public BestellingDB(Context context)
    {
        helper = new BestellingHelper(context,Constance.DB_NAME,null,Constance.DB_VERSION);
    }

    public void close()
    {
        helper.close();
    }
    public void open()
    {
        try{
            db = helper.getWritableDatabase();}
        catch (SQLiteException ex)
        {
            Log.e(TAG, ex.getMessage());
            db = helper.getReadableDatabase();
        }

    }

    public long insertItem(ContentValues values)
    {
        try{
            return db.insert(Constance.TABLE_NAME,null,values);
        }
        catch (SQLiteException ex)
        {
            Log.e(TAG,"Fout bij insert: "+ex.getMessage());
            return -1;
        }

    }
    public int deleteItem(Uri uri, String selection, String []args)
    {
        int i = 0;

        try
        {
        i = db.delete(Constance.TABLE_NAME, selection, args);
        Log.d(TAG, "delete " + uri);

        }catch (Exception ex)
        {
            Log.e(TAG,"wrong "+ex.getMessage());
        }
        return i;
    }

    public Cursor getItems(){
        Cursor c = db.query(Constance.TABLE_NAME,null,null,null,null,null,null);
        return c;
    }

    public Cursor getChocoCount()
    {
        Cursor c = db.rawQuery("SELECT Sum("+Constance.COLUMN_CHOCO+") AS "+Constance.COLUMN_CHOCO_TOTAL+" FROM "+Constance.TABLE_NAME,null);
        return c;
    }
    public Cursor getVanilleCount()
    {
        Cursor c = db.rawQuery("SELECT Sum("+Constance.COLUMN_VANILLE+") AS "+Constance.COLUMN_VANILLE_TOTAL+" FROM "+Constance.TABLE_NAME,null);
        return c;
    }
    public Cursor getFranchiCount()
    {
        Cursor c = db.rawQuery("SELECT Sum("+Constance.COLUMN_FRANCH+") AS "+Constance.COLUMN_FRANCHI_TOTAL+" FROM "+Constance.TABLE_NAME,null);
        return c;
    }
    public int updateRow(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        try{
            return db.update(Constance.TABLE_NAME,values,selection,selectionArgs);
        }catch (SQLiteException ex)
        {
            Log.e(TAG,ex.getMessage());
            return -1;
        }

    }
    public int getChocoCountTot()
    {
        try
        {
            Cursor c = db.rawQuery("SELECT Sum("+Constance.COLUMN_CHOCO+") AS "+Constance.COLUMN_CHOCO_TOTAL+" FROM "+Constance.TABLE_NAME,null);
            int i = c.getInt(c.getColumnIndex(Constance.COLUMN_CHOCO_TOTAL));
            Log.d(TAG,"aantal choco : "+i);

            return i;
        }catch (IndexOutOfBoundsException e)
        {
            Log.e(TAG,"Choco cursor : "+e.getMessage());
            return 0;
        }

    }
    public int getVanilleCountTot()
    {
        try {
            Cursor c = db.rawQuery("SELECT Sum(" + Constance.COLUMN_VANILLE + ") AS " + Constance.COLUMN_VANILLE_TOTAL + " FROM " + Constance.TABLE_NAME, null);
            int i = c.getInt(c.getColumnIndex(Constance.COLUMN_VANILLE_TOTAL));
            Log.d(TAG,"aantal vanille : "+i);

            return i;
        }catch (IndexOutOfBoundsException e)
        {
            Log.e(TAG,"Vanille cursor : "+e.getMessage());
            return 0;
        }
    }
    public int getFranchiCountTot()
    {
        try {
            Cursor c = db.rawQuery("SELECT SUM( " + Constance.COLUMN_FRANCH + " ) AS " + Constance.COLUMN_FRANCHI_TOTAL + " FROM " + Constance.TABLE_NAME, null);
            int i = c.getInt(c.getColumnIndex(Constance.COLUMN_FRANCHI_TOTAL));
            Log.d(TAG,"franchi totaal : "+i);
            return i;
        }catch (IndexOutOfBoundsException|SQLiteException e)
        {
            Log.e(TAG,"Franchi cursor : "+e.getMessage());
            return 0;
        }
    }

}
