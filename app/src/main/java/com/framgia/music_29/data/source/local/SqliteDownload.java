package com.framgia.music_29.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.framgia.music_29.data.model.Song;
import java.util.ArrayList;
import java.util.List;

public class SqliteDownload extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "download";
    private static final String TABLE_NAME = "downloadMp3";
    private static final String ID = "id";

    public SqliteDownload(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " (" + ID + " TEXT primary key )";
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID, song.getId());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Song> getAllSong() {
        List<Song> listStudent = new ArrayList<Song>();
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setId(cursor.getString(0));
                listStudent.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listStudent;
    }

    public boolean getSongById(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME + " WHERE " + ID + " = '" + id + "'";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null)
            return false;
        cursor.moveToFirst();
        if (cursor.getCount() > 0)
            return true;
        return false;
    }

    public void deleteSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(song.getId()) });
        db.close();
    }

    public int getSongCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cout = cursor.getCount();
        cursor.close();
        return cout;
    }
}
