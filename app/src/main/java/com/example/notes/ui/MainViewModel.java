package com.example.notes.ui;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.notes.NoteDataSource;
import com.example.notes.models.Note;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/*
we will be sharing this viewmodel between the two fragment for cleaner and better code
 */
public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";

    private NoteDataSource noteDataSource;
    private CompositeDisposable disposables = new CompositeDisposable();

    public MainViewModel(NoteDataSource noteDataSource) {
        this.noteDataSource = noteDataSource;
    }

    public CompositeDisposable getDisposables() {
        return disposables;
    }

    @SuppressLint("CheckResult")
    public Flowable<List<Note>> getAllNotes() {
        return noteDataSource.getAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> Log.e(TAG, "getAllNotes: Error retrieving data"));
    }

    public void insertNote(Note note) {
        disposables.add(noteDataSource.insertNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(TAG, "onComplete inserted successfully"))
                .doOnError(i -> Log.e(TAG, "Insert Error : " + i.getMessage()))
                .subscribe());
    }

    public void updateNote(Note note) {
        disposables.add(noteDataSource.updateNote(note)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> Log.d(TAG, "updateNote: updated successfully"))
                .doOnError(i -> Log.e(TAG, "updateNote: " + i.getMessage()))
                .subscribe());
    }

    public void deleteAllNotes() {
        disposables.add(noteDataSource.deleteAllNotes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        //clearing the disposables to stop observing the data to clear up some memory
        disposables.clear();
    }
}
