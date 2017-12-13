package phototest2.com.phototest2.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amanda on 08/12/2017.
 */

public class PicturesDAO extends SQLiteOpenHelper {

    public PicturesDAO(Context context) {
        super(context, "C0719157_photobook", null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE Pictures (id INTEGER PRIMARY KEY, " +
                "photographerName TEXT, photoPath TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "";
        switch (oldVersion) {
            case 1:
                sql = "ALTER TABLE Pictures ADD COLUMN photoPath TEXT";
                db.execSQL(sql);
        }
    }

    public void dbInsert(Pictures pictures) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues picturesData = getContentValues(pictures);
        db.insert("Pictures", null, picturesData);
    }

    @NonNull
    private ContentValues getContentValues(Pictures pictures) {
        ContentValues picturesData = new ContentValues();
        picturesData.put("photographerName", pictures.getPhotographerName());
        picturesData.put("photoPath",pictures.getPhotoPath());

        return picturesData;
    }

    public ArrayList<Pictures> dbSearch(String photographerName) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT * FROM Pictures;";

        Cursor c = db.rawQuery(sql, null);
        ArrayList<Pictures> picturesList = new ArrayList<Pictures>();

        while (c.moveToNext()) {
            if (c.getString(c.getColumnIndex("photographerName")).equals(photographerName)) {
                Pictures pictures = new Pictures();

                pictures.setId(c.getInt(c.getColumnIndex("id")));
                pictures.setPhotographerName(c.getString(c.getColumnIndex("photographerName")));
                pictures.setPhotoPath(c.getString(c.getColumnIndex("photoPath")));

                picturesList.add(pictures);
            }
        }
        c.close();

        return picturesList;
    }
}
