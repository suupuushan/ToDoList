package com.bubblewrap.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bubblewrap.todolist.Adapter.ToDoAdapter;
import com.bubblewrap.todolist.Model.ToDoModel;
import com.bubblewrap.todolist.Utils.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewTaskAct extends AppCompatActivity {

    EditText addTitle, addDesc;
    TextView addDate, addTime;
    Button btnDate, btnTime;
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
        addTime = findViewById(R.id.addTime);

        btnCreateTask = findViewById(R.id.btnCreateTask);
        btnCancelCreate = findViewById(R.id.btnCancelCreate);
        btnDate = findViewById(R.id.btnDate);
        btnTime = findViewById(R.id.btnTime);

        btnCreateTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler myDB = new DatabaseHandler(NewTaskAct.this);
                String titleTXT = addTitle.getText().toString();
                String descTXT = addDesc.getText().toString();
                String dateTXT = addDate.getText().toString();
                String timeTXT = addTime.getText().toString();
                ToDoModel task = new ToDoModel();
                task.setTaskTitle(titleTXT);
                task.setTaskDesc(descTXT);
                task.setTaskDate(dateTXT);
                task.setTaskTime(timeTXT);
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

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
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

    private void showDateDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                addDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(NewTaskAct.this, R.style.DatePicker, dateSetListener, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                addTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(NewTaskAct.this, R.style.TimePicker ,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();
    }
}