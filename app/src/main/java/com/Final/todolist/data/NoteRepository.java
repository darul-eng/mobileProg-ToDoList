package com.Final.todolist.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//    function untuk Mengambil semua data yang berkaitan dengan kelas noteDao
//    function ini berfungsi sebagai jembatan antara activity dengan db
public class NoteRepository {
    private NoteDao mNotesDao;
    private ExecutorService executorService;
    public NoteRepository(Application application){
        executorService = Executors.newSingleThreadExecutor();

        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNotesDao = db.noteDao();
    }

    public LiveData<List<Note>> getAllNotes(){
        return mNotesDao.getAllNotes();
    }
    public void insert(final Note note){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mNotesDao.insert(note);
            }
        });
    }
    public void delete(final Note note){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mNotesDao.delete(note);
            }
        });
    }
    public void update(final Note note){
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                mNotesDao.update(note);
            }
        });
    }
}
