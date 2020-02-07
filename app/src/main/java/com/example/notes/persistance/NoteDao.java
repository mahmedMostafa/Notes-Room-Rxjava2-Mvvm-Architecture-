package com.example.notes.persistance;



import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.models.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;


@Dao
public interface NoteDao {


    @Query("SELECT * FROM notes")
    Flowable<List<Note>> getAllNotes();

    //this is to insert a new user into the db with a completable observer to alert me as soon as the insertion operation is completed
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertNote(Note note);


    @Update
    Completable updateNote(Note note);

    @Query("DELETE FROM notes")
    Completable deleteAllNotes();


}
