package com.Final.todolist.ui.detail;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.Final.todolist.R;
import com.Final.todolist.ui.ViewModelFactory;
import com.Final.todolist.data.Note;
import com.Final.todolist.ui.main.MainActivity;
import com.Final.todolist.utils.DateHelper;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.datepicker.MaterialDatePicker;

public class    DetailActivity extends AppCompatActivity {

    private EditText etTitle, etDesc;

    public static final String EXTRA_NOTE = "extra_note";
    public static final String EXTRA_POSITION = "extra_position";

    private boolean isEdit = false;

    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int REQUEST_UPDATE = 200;
    public static final int RESULT_UPDATE = 201;
    public static final int RESULT_DELETE = 301;

    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;

    private Note note;
    private int position;

//    public static final String DATA_DETAIL = "data_detail";
    private String actionBarTitle;
    private String btnTitle;

//    private Button btnSave;
    private DetailViewModel detailViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        note = getIntent().getParcelableExtra(DATA_DETAIL);
//        btnSave = findViewById(R.id.btnSave);
        detailViewModel = obtainViewModel(DetailActivity.this);

        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);
        Button btnSave = findViewById(R.id.btnSave);

        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null){
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }else{
            note = new Note();
        }

        if (isEdit){
            actionBarTitle = getString(R.string.update);
            btnTitle = getString(R.string.update);

            if (note != null){
                etTitle.setText(note.getTitle());
                etDesc.setText(note.getDesc());
            }
        }else{
            actionBarTitle = getString(R.string.add);
            btnTitle = getString(R.string.save);
        }

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(actionBarTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnSave.setText(btnTitle);
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

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_NOTE, note);
                    intent.putExtra(EXTRA_POSITION, position);

                    if (isEdit){
                        detailViewModel.update(note);
                        setResult(RESULT_UPDATE, intent);
                        finish();
                    }else {
                        note.setDate(DateHelper.getCurrentDate());
                        detailViewModel.insert(note);
                        setResult(RESULT_ADD, intent);
                        finish();
                    }
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
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
//            case R.id.mEdit:
//
//                break;
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
//                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;

        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel);
            dialogMessage = getString(R.string.message_cancel);
        } else {
            dialogMessage = getString(R.string.message_delete);
            dialogTitle = getString(R.string.delete);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            detailViewModel.delete(note);

                            Intent intent = new Intent();
                            intent.putExtra(EXTRA_POSITION, position);
                            setResult(RESULT_DELETE, intent);
                            finish();

                        }
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
}
