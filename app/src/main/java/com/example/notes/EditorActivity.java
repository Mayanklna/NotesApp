package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.notes.data.NoteDbHelper;
import  com.example.notes.data.PetContract.PetEntry;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private  boolean mNotesHasChanged=false;
    private static final int EXIST_LOADER_ID=0;
    private EditText nametext;
    private Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent=getIntent();
      uri =intent.getData();
        if(uri==null){
            setTitle("Add A Note");
            invalidateOptionsMenu();
        }
        else{
            setTitle("Edit Note");

            getSupportLoaderManager().initLoader(EXIST_LOADER_ID,null,this);

        }
        nametext=(EditText)findViewById(R.id.Whole_note);


    }
    private void insertNote(){

        String note=nametext.getText().toString().trim();
        NoteDbHelper mDbHelper=new NoteDbHelper(this);

        ContentValues values=new ContentValues();
        values.put(PetEntry.COLUMN_NOTES,note);


        if(uri==null && TextUtils.isEmpty(note)){
            return;
        }
        else if(uri==null ) {
            Uri newColumnId = getContentResolver().insert(PetEntry.CONTENT_URI, values);
            if (newColumnId == null) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(this, "Error with saving  note", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
            }
        }

        else{     int rowsAffected = getContentResolver().update( uri, values, null, null);


            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this,"no updated pet",
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, "pet updated",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
        if(uri!= null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete( uri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, "Cannot delete",Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, "successfully deleted",Toast.LENGTH_SHORT).show();
            }

        }

        // Close the activity
        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.save_Action:
                // Do nothing for now
                insertNote();
                finish();
                return true;
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.delete_Action:
                deletePet();
                finish();
                return true;

            case android.R.id.home:
                // If the pet hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.


                if (!mNotesHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

        }return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection={
                PetEntry._ID,
                PetEntry.COLUMN_NOTES
        };
        return new CursorLoader(this,uri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if(cursor==null ||cursor.getCount()<1){
            return;
        }
        else if(cursor.moveToFirst()){
            int noteIndex=cursor.getColumnIndex(PetEntry.COLUMN_NOTES);
            String noteText=cursor.getString(noteIndex);
            nametext.setText(noteText);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        nametext.setText("");
    }
}
