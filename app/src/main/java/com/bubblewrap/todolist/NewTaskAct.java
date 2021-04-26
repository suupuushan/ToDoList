package com.bubblewrap.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bubblewrap.todolist.Adapter.ToDoAdapter;
import com.bubblewrap.todolist.Model.ToDoModel;
import com.bubblewrap.todolist.Utils.DatabaseHandler;

public class NewTaskAct extends AppCompatActivity {

    private static boolean act = false;
    EditText addTitle, addDesc, addDate;
    Button btnCreateTask, btnCancelCreate;
    ToDoAdapter taskAdapter;


    public static NewTaskAct newInstance() {
        return new NewTaskAct();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        addTitle = findViewById(R.id.addTitle);
        addDesc = findViewById(R.id.addDesc);
        addDate = findViewById(R.id.addDate);
        btnCreateTask = findViewById(R.id.btnCreateTask);
        btnCancelCreate = findViewById(R.id.btnCancelCreate);

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler myDB = new DatabaseHandler(NewTaskAct.this);
                String titleTXT = addTitle.getText().toString();
                String descTXT = addDesc.getText().toString();
                String dateTXT = addDate.getText().toString();
                ToDoModel task = new ToDoModel();
                task.setTaskTitle(titleTXT);
                task.setTaskDesc(descTXT);
                task.setTaskDate(dateTXT);
                myDB.insertTask(task);

                Intent intent = new Intent(NewTaskAct.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnCancelCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewTaskAct.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(NewTaskAct.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}