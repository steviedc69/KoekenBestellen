package com.example.steven.koekenbestellen.Persistence;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Steven on 5/02/15.
 */
public class BestellingContentProvider extends ContentProvider {

    private BestellingDB db;
    private static final String TAG = "BestellingContentProvider";
    private static final int BESTELLINGS = 10;
    private static final int BESTELLING_ID = 20;
    public static final int CHOCO_TOT = 30;
    public static final int VANILLE_TOT = 40;
    public static final int FRANCHI_TOT = 50;

    private static final String AUTH = "com.steven.koekenbestellen.provider.bestelling";
    private static final String BASE_PATH = "bestellingen";
    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTH+"/"+BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/bestellingen";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"/bestelling";

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTH,BASE_PATH,BESTELLINGS);
        sURIMatcher.addURI(AUTH,BASE_PATH+"/#",BESTELLING_ID);
        sURIMatcher.addURI(AUTH,BASE_PATH+"/#",CHOCO_TOT);
        sURIMatcher.addURI(AUTH,BASE_PATH+"/#",VANILLE_TOT);
        sURIMatcher.addURI(AUTH,BASE_PATH+"/#",FRANCHI_TOT);
    }



    @Override
    public boolean onCreate() {

        db = new BestellingDB(getContext());
        db.open();
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        Cursor c = null;
        queryBuilder.setTables(Constance.TABLE_NAME);

        int uriType = sURIMatcher.match(uri);
        switch (uriType){
            case BESTELLINGS: c = db.getItems();
                break;
            case CHOCO_TOT: c = db.getChocoCount();
                break;
            case VANILLE_TOT: c = db.getVanilleCount();
                break;
            case FRANCHI_TOT: c = db.getFranchiCount();
                break;
            default:throw new IllegalArgumentException("Unknown URI: "+uri);
        }
        c.setNotificationUri(getContext().getContentResolver(),uri);

        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = db.insertItem(values);
        Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
        getContext().getContentResolver().notifyChange(uri,null);
        return newUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        try{
            db.deleteItem(uri,selection,selectionArgs);
            getContext().getContentResolver().notifyChange(uri,null);
        }catch (Exception e)
        {
            Log.e(TAG,e.getMessage());
        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        int count = 0;
        try {
            count = db.updateRow(uri, values, selection, selectionArgs);

            getContext().getContentResolver().notifyChange(uri, null);

        }catch (IllegalArgumentException ex)
        {
            Log.e(TAG,ex.getMessage());
        }
        return count;
    }

}
