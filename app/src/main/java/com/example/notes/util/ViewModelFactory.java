package com.example.notes.util;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.notes.NoteDataSource;
import com.example.notes.ui.MainViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {


    private NoteDataSource noteDataSource;

    public ViewModelFactory(NoteDataSource noteDataSource) {
        this.noteDataSource = noteDataSource;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(noteDataSource);
        }
        throw new IllegalArgumentException("Unknown viewmodel");
    }
}
