package com.example.notes;

import com.example.notes.models.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/*
    this will be the access point to access the note data in the viewmodel as we wanna keep track of the disposables in our viewmodel,
    The only data source here will be just room
 */
public interface NoteDataSource {


    /*
        get all the users from the data source
     */
    Flowable<List<Note>> getAllNotes();


    /*
        Inserts the user into the data source, or, if this is an existing user, updates it.
     */
    Completable insertNote(Note note);

    /*
       Inserts the user into the data source, or, if this is an existing user, updates it.
    */
    Completable updateNote(Note note);


    /*
        deleted all the notes from the data source
     */
    Completable deleteAllNotes();


}
