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

public class DetailActivity extends AppCompatActivity {


//    inisialisasi inputan
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

    private String actionBarTitle;
    private String btnTitle;

//  inisialisasi viewModel
    private DetailViewModel detailViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailViewModel = obtainViewModel(DetailActivity.this);

//        inisialisasi data
        etTitle = findViewById(R.id.etTitle);
        etDesc = findViewById(R.id.etDesc);

//        inisialisasi button save
        Button btnSave = findViewById(R.id.btnSave);
//        mengambil data berdasarkan intent
        note = getIntent().getParcelableExtra(EXTRA_NOTE);
//        pengkondisian ketika floating action button di tekan,
        if (note != null){//jika tidak kosong maka lakukan edit,
            position = getIntent().getIntExtra(EXTRA_POSITION, 0);
            isEdit = true;
        }else{//sebaliknya buat note baru.
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

                if (title.isEmpty()){//validasi pada form inputan
                    etTitle.setError("Please Input Title");
                }else if (desc.isEmpty()){//validasi pada form inputan
                    etDesc.setError("Please Input Desc");
                }else {//jika data terisi maka set datanya
                    note.setTitle(title);
                    note.setDesc(desc);

                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_NOTE, note);
                    intent.putExtra(EXTRA_POSITION, position);

                    if (isEdit){//jika edit maka lakukan update
                        detailViewModel.update(note);
                        setResult(RESULT_UPDATE, intent);
                        finish();
                    }else {// jika fieldnya kosong lakukan add (add new)
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

//    fungsi untuk menu delete dan back
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
            //jika tombol delete maka ALert_Dialog_Delete akan muncul
            case R.id.mDelete:
                showAlertDialog(ALERT_DIALOG_DELETE);
                break;
//                jika tombol back maka alert_dialog_close akan muncul
            case android.R.id.home:
                showAlertDialog(ALERT_DIALOG_CLOSE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//    fungsi untuk menampilkan dialog
    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;
//        jika isDialogClose, maka doalognya seperti di bawah:
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel);
            dialogMessage = getString(R.string.message_cancel);
        } else {//jika selain isDialogClose, maka doalognya seperti di bawah:
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
                        //jika isDialogClose maka kita teredirect ke dashboar
                        if (isDialogClose) {
                            finish();
                        } else {//selain isDialogClose maka item akan di hapus
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
