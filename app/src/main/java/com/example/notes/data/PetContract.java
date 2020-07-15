package com.example.notes.data;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;

import com.example.notes.R;

public class PetContract  {
    public final static String CONTENT_AUTHORITY="com.example.notes";
    public final static Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);
    public final static String PATH_NOTES="notes";

    private PetContract(){

    }
    public static class PetEntry implements BaseColumns{
        public  final static String TABLE_NAME="notes";
        public  final static String _ID=BaseColumns._ID;
        public final static String COLUMN_NOTES="note";
        public  static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NOTES);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;


        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTES;

    }
}
