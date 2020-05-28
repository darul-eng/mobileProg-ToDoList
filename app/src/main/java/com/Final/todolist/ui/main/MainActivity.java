package com.Final.todolist.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.Final.todolist.R;
import com.Final.todolist.ui.ViewModelFactory;
import com.Final.todolist.data.Note;
import com.Final.todolist.ui.detail.DetailActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import static com.Final.todolist.ui.detail.DetailActivity.REQUEST_UPDATE;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        adapter = new NoteAdapter(MainActivity.this);

        recyclerView = findViewById(R.id.rv_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.fab_add){
                    Intent intent = new Intent(MainActivity.this, DetailActivity.class );
                    startActivity(intent);
                }
            }
        });
        MainViewModel mainViewModel = obtainViewModel(MainActivity.this);
        mainViewModel.getAllNotes().observe(this, noteObserver);
    }
    @NonNull
    private static MainViewModel obtainViewModel(AppCompatActivity activity){
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(MainViewModel.class);
    }
    private final Observer<List<Note>> noteObserver = new Observer<List<Note>>() {
        @Override
        public void onChanged(List<Note> noteList) {
            if (noteList != null){
                adapter.setListNotes(noteList);
            }
        }
    };

    private void showSnackbarMessage(String message){
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == DetailActivity.REQUEST_ADD) {
                if (resultCode == DetailActivity.RESULT_ADD) {
                    showSnackbarMessage(getString(R.string.added));
                }
            } else if (requestCode == REQUEST_UPDATE) {
                if (resultCode == DetailActivity.RESULT_UPDATE) {
                    showSnackbarMessage(getString(R.string.changed));
                } else if (resultCode == DetailActivity.RESULT_DELETE) {
                    showSnackbarMessage(getString(R.string.deleted));
                }
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
