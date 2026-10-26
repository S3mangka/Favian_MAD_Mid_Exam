package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "StudentDB";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "students";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "student_id TEXT PRIMARY KEY, " +
                "name TEXT, " +
                "enrolled INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // ADD
    public boolean addStudent(String name, String id, boolean enrolled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("student_id", id);
        cv.put("name", name);
        cv.put("enrolled", enrolled ? 1 : 0);
        return db.insert(TABLE, null, cv) != -1;
    }

    // EDIT
    public boolean updateStudent(String name, String id, boolean enrolled) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("enrolled", enrolled ? 1 : 0);
        return db.update(TABLE, cv, "student_id = ?", new String[]{id}) > 0;
    }

    // DELETE
    public boolean deleteStudent(String id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE, "student_id = ?", new String[]{id}) > 0;
    }

    // VIEW
    public Cursor getAllStudents() {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE, null);
    }
}