package com.example.notes.persistance;

import com.example.notes.NoteDataSource;
import com.example.notes.models.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/*
    our only data source will be ROOM
 */
public class LocalNoteDataSource implements NoteDataSource {

    private final NoteDao noteDao;

    public LocalNoteDataSource(NoteDao noteDao) {
        this.noteDao = noteDao;
    }

    @Override
    public Flowable<List<Note>> getAllNotes() {
        return noteDao.getAllNotes();
    }

    @Override
    public Completable insertNote(Note note) {
        return noteDao.insertNote(note);
    }

    @Override
    public Completable updateNote(Note note) {
        return noteDao.updateNote(note);
    }

    @Override
    public Completable deleteAllNotes() {
        return noteDao.deleteAllNotes();
    }
}
