package com.bubblewrap.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bubblewrap.todolist.Adapter.ToDoAdapter;
import com.bubblewrap.todolist.Model.ToDoModel;
import com.bubblewrap.todolist.Utils.DatabaseHandler;

public class EditTask extends AppCompatActivity {

    private ToDoAdapter adapter;
    EditText title, desc, date;
    String id;
    String titleTXT;
    String descTXT;
    String dateTXT;
    Button btnSaveTask;
    Button btnCancelUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        title = findViewById(R.id.addTitle2);
        desc = findViewById(R.id.addDesc2);
        date = findViewById(R.id.addDate2);
        btnSaveTask = findViewById(R.id.btnSaveTask);
        btnCancelUpdate = findViewById(R.id.btnCancelUpdate);
        getAndSetIntentData();
        btnSaveTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler myDB = new DatabaseHandler(EditTask.this);
                myDB.getWritableDatabase();
                String title2 = title.getText().toString();
                String desc2 = desc.getText().toString();
                String date2 = date.getText().toString();
                ToDoModel task = new ToDoModel();
                task.setTaskTitle(title2);
                task.setTaskDesc(desc2);
                task.setTaskDate(date2);
                myDB.updateTask(id, task);
                Intent intent = new Intent(EditTask.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditTask.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditTask.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    void getAndSetIntentData(){
        id = getIntent().getStringExtra("item_id");
        titleTXT = getIntent().getStringExtra("addTitle2");
        descTXT = getIntent().getStringExtra("addDesc2");
        dateTXT = getIntent().getStringExtra("addDate2");
        title.setText(titleTXT);
        desc.setText(descTXT);
        date.setText(dateTXT);
    }
}