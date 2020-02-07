package com.example.notes.persistance;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.notes.models.Note;

import static com.example.notes.util.C.DATABASE_NAME;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {



    public abstract NoteDao getNoteDao();

    private static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

}
