package com.example.notes.data;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.example.notes.data.PetContract.PetEntry;
import android.os.Bundle;

import com.example.notes.R;

public class NoteDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = NoteDbHelper.class.getSimpleName();
    private static final String Database_Name="notes.db";
    private static final int Database_Version=1;
    public NoteDbHelper(@Nullable Context context) {
        super(context, Database_Name,null,Database_Version);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the table
        // Create a String that contains the SQL statement to create the  table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
                + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_NOTES + " TEXT NOT NULL);";
        // Execute the SQL statement
        db.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//not require to change the version
    }
}
