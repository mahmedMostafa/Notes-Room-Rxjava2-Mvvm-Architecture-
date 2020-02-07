package com.example.notes;

import android.content.Context;

import com.example.notes.persistance.LocalNoteDataSource;
import com.example.notes.persistance.NoteDatabase;
import com.example.notes.util.ViewModelFactory;

/*
    this is just pure dependency injection without any libraries
 */
public class DependencyInjection {

    //injecting the data source
    public static NoteDataSource provideNoteDataSource(Context context){
        NoteDatabase noteDatabase = NoteDatabase.getInstance(context);
        return new LocalNoteDataSource(noteDatabase.getNoteDao());
    }

    //injecting the viewmodel factory
    public static ViewModelFactory provideViewModelFactory(Context context){
        NoteDataSource noteDatabase = provideNoteDataSource(context);
        return new ViewModelFactory(noteDatabase);
    }

}
