package com.example.notes.data;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import  com.example.notes.data.PetContract.PetEntry;
import com.example.notes.R;

public class PetProvider extends ContentProvider {
    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    private NoteDbHelper mdbhelper;
    public static final int Notes = 100;
    public static final int Note_Id = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_NOTES, Notes);
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_NOTES+"/#", Note_Id);
    }

    @Override
    public boolean onCreate() {
        mdbhelper = new NoteDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = mdbhelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;
        switch (match) {
            case Notes:
                cursor=db.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case Note_Id:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Notes:
                return PetEntry.CONTENT_LIST_TYPE;
            case Note_Id:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

       final int match = sUriMatcher.match(uri);
        switch (match) {
            case Notes:
                return insertNote(uri, values);

            default:
                throw new IllegalArgumentException("Cannot query uri" + uri);
        }

    }

    private Uri insertNote(@NonNull Uri uri, @Nullable ContentValues values) {
        String name = values.getAsString(PetEntry.COLUMN_NOTES);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
        long id = db.insert(PetEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "fail to insert" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mdbhelper.getWritableDatabase();
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case Notes:
                // Delete all rows that match the selection and selection args
                rowsDeleted=database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);

                break;
            case Note_Id:
                // Delete a single row given by the ID in the URI
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted=database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);}
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

       final int match = sUriMatcher.match(uri);
        switch (match) {
            case Notes:
                return updatePet(uri, values,selection,selectionArgs);
            case Note_Id:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return  updatePet(uri,values, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Cannot query uri" + uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(PetEntry.COLUMN_NOTES)) {
            String name = values.getAsString(PetEntry.COLUMN_NOTES);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mdbhelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated=database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }


}