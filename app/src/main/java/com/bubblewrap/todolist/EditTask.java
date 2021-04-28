package com.bubblewrap.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
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

public class EditTask extends AppCompatActivity {

    private ToDoAdapter adapter;
    EditText title, desc;
    TextView date, time;
    String id;
    String titleTXT;
    String descTXT;
    String dateTXT;
    String timeTXT;
    Button btnSaveTask;
    Button btnCancelUpdate;
    Button btnDate2;
    Button btnTime2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        title = findViewById(R.id.addTitle2);
        desc = findViewById(R.id.addDesc2);
        date = findViewById(R.id.addDate2);
        time = findViewById(R.id.addTime2);
        btnDate2 = findViewById(R.id.btnDate2);
        btnTime2 = findViewById(R.id.btnTime2);
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
                String time2 = time.getText().toString();
                ToDoModel task = new ToDoModel();
                task.setTaskTitle(title2);
                task.setTaskDesc(desc2);
                task.setTaskDate(date2);
                task.setTaskTime(time2);
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

        btnDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        btnTime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog();
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
        timeTXT = getIntent().getStringExtra("addTime2");
        title.setText(titleTXT);
        desc.setText(descTXT);
        date.setText(dateTXT);
        time.setText(timeTXT);
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
                date.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(EditTask.this, R.style.DatePicker, dateSetListener, calendar.get(Calendar.YEAR),
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
                time.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(EditTask.this, R.style.DatePicker ,timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false).show();
    }
}