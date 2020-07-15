package com.example.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import  com.example.notes.data.PetContract.PetEntry;
import com.example.notes.data.PetContract;

public class NoteCursorAdapter extends CursorAdapter {

    public NoteCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView notetext=(TextView)view.findViewById(R.id.note_text);

        int noteindex=cursor.getColumnIndex(PetEntry.COLUMN_NOTES);
        String note=cursor.getString(noteindex);
        notetext.setText(note);

    }
}
