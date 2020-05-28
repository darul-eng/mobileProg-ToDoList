package com.Final.todolist.ui.detail;

import android.app.Application;

import androidx.lifecycle.ViewModel;

import com.Final.todolist.data.Note;
import com.Final.todolist.data.NoteRepository;

public class DetailViewModel extends ViewModel {
    private NoteRepository mNoteRepository;

    public DetailViewModel(Application application){
        mNoteRepository = new NoteRepository(application);
    }
    public void insert(Note note) { mNoteRepository.insert(note);}

    public  void delete(Note note) { mNoteRepository.delete(note);}

    public  void update(Note note) { mNoteRepository.update(note);}
}
