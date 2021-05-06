package com.bubblewrap.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bubblewrap.todolist.Adapter.ToDoAdapter;
import com.bubblewrap.todolist.Model.ToDoModel;
import com.bubblewrap.todolist.Utils.DatabaseHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Collections;
import java.util.List;


import static com.bubblewrap.todolist.NewTaskAct.cTime;
import static com.bubblewrap.todolist.NewTaskAct.dict;

public class MainActivity extends AppCompatActivity{
    

    private DatabaseHandler myDB;
    private RecyclerView tasksRecyclerView;
    private ToDoAdapter tasksAdapter;
    private FloatingActionButton fab;
    AlarmManager alarmManager;

    private List<ToDoModel> taskList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DatabaseHandler(this);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        tasksRecyclerView = findViewById(R.id.tasksText);
        tasksRecyclerView.setHasFixedSize(true);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new ToDoAdapter(this, myDB, MainActivity.this);
        tasksRecyclerView.setAdapter(tasksAdapter);

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(tasksAdapter));
        itemTouchHelper.attachToRecyclerView(tasksRecyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTaskAct.class);
                startActivity(intent);
                finish();
            }
        });

        taskList = myDB.getAllTasks();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);
        tasksRecyclerView.setAdapter(tasksAdapter);
        tasksAdapter.notifyDataSetChanged();

    }
}