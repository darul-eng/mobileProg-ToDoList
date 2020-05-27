package com.Final.todolist.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Final.todolist.R;
import com.Final.todolist.ViewModelFactory;
import com.Final.todolist.data.Note;
import com.Final.todolist.utils.DateHelper;

public class    DetailActivity extends AppCompatActivity {

    private boolean isEdit = false;
    public static final String DATA_DETAIL = "data_detail";
    private String actionBarTitle;

    private Button btnSave;

    private EditText etTitle, etDesc;
    private DetailViewModel detailViewModel;

    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        note = getIntent().getParcelableExtra(DATA_DETAIL);

        btnSave = findViewById(R.id.btnSave);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);

        detailViewModel = obtainViewModel(DetailActivity.this);

        if (note != null){
            isEdit = true;
        }else{
            note = new Note();
        }

        if (isEdit){
            actionBarTitle = getString(R.string.update);
            btnSave.setVisibility(View.INVISIBLE);

            if (note != null){
                //tampilkan data
            }
        }else{
            actionBarTitle = "Save";
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String desc = etDesc.getText().toString().trim();

                if (title.isEmpty()){
                    etTitle.setError("Please Input Title");
                }else if (desc.isEmpty()){
                    etDesc.setError("Please Input Desc");
                }else {
                    note.setTitle(title);
                    note.setDesc(desc);

                    note.setDate(DateHelper.getCurrentDate());
                    detailViewModel.insert(note);
                    finish();
                }
            }
        });
    }

    @NonNull
    private static DetailViewModel obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());

        return ViewModelProviders.of(activity, factory).get(DetailViewModel.class);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        if (isEdit){
            getMenuInflater().inflate(R.menu.detail, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.mDelete:

                break;
            case R.id.mEdit:

                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
