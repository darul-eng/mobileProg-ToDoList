package com.Final.todolist.ui.main;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.Final.todolist.data.Note;
import com.Final.todolist.data.NoteRepository;

import java.util.List;

public class MainViewModel extends ViewModel {
    private NoteRepository mNoteRepository;

    public MainViewModel(Application application){
        mNoteRepository = new NoteRepository(application);
    }

    LiveData<List<Note>> getAllNotes(){
        return mNoteRepository.getAllNotes();
    }
}
