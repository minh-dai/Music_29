package com.framgia.music_29.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import com.framgia.music_29.data.model.Song;
import java.util.ArrayList;
import java.util.List;

public class SqliteFavouriteSong extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favotite";
    private static final String TABLE_NAME = "favotiteMp3";
    private static final String ID = "id";
    private static final String mTitle = "title";
    private static final String mArtworkUrl = "artwork_url";
    private static final String mDownloadUrl = "download_url";
    private static final String mUserFullName = "username";

    private Context context;

    public SqliteFavouriteSong(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + ID + " TEXT primary key, "
                + mTitle + " TEXT, "
                + mArtworkUrl + " TEXT, "
                + mDownloadUrl + " TEXT,"
                + mUserFullName + " TEXT)";
        db.execSQL(sqlQuery);
        Toast.makeText(context, "Create successfylly", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Toast.makeText(context, "Drop successfylly", Toast.LENGTH_SHORT).show();
    }

    //Add new a student
    public void addStudent(Song student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(mTitle, student.getTitle());
        values.put(mDownloadUrl, student.getDownloadUrl());
        values.put(mArtworkUrl, student.getArtworkUrl());
        values.put(mUserFullName, student.getUserFullName());
        //Neu de null thi khi value bang null thi loi

        db.insert(TABLE_NAME, null, values);

        db.close();
    }

    /*
     Getting All Student
      */

    public List<Song> getAllStudent() {
        List<Song> listStudent = new ArrayList<Song>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song student = new Song();
                student.setId(cursor.getString(0));
                student.setTitle(cursor.getString(1));
                student.setArtworkUrl(cursor.getString(2));
                student.setDownloadUrl(cursor.getString(3));
                student.setUserFullName(cursor.getString(4));
                listStudent.add(student);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return listStudent;
    }

    /*
    Delete a student by ID
     */
    public void deleteStudent(Song student) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, ID + " = ?", new String[] { String.valueOf(student.getId()) });
        db.close();
    }

    /*
    Get Count Student in Table Student
     */
    public int getStudentsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}

