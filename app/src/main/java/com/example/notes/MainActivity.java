package com.example.notes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import  com.example.notes.data.PetContract.PetEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    public static final int Loader_Id=0;
    NoteCursorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView noteList=(ListView)findViewById(R.id.list);
        View empty=findViewById(R.id.empty_view);
        noteList.setEmptyView(empty);
        adapter=new NoteCursorAdapter(this,null);
        noteList.setAdapter(adapter);
        noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,EditorActivity.class);
                Uri uri= ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
                intent.setData(uri);
                startActivity(intent);
            }
        } );
        getSupportLoaderManager().initLoader(Loader_Id,null,this);
    }
    private void insertNote() {
        ContentValues values=new ContentValues();
        values.put(PetEntry.COLUMN_NOTES,"i am mayank");
        Uri newColumnId=getContentResolver().insert(PetEntry.CONTENT_URI,values);
        Log.v("CatalogActivity","new id"+newColumnId);
    }
    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.dummy_notes:
                // Do nothing for now
                insertNote();
                return true;
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.delete_all_notes:
                deleteAllPets();

                return true;
            // Respond to a click on the "Delete all entries" menu option

        }

        return super.onOptionsItemSelected(item);
    }



    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String a;
        if((PetEntry.COLUMN_NOTES).length()>7){
            a=PetEntry.COLUMN_NOTES.substring(0,7)+".....................";
        }
        else{
            a=PetEntry.COLUMN_NOTES;
        }
        String[] projection={
                PetEntry._ID,a

        };
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor(data);
    }


    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
