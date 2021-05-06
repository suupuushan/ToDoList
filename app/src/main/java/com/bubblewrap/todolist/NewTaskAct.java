package com.bubblewrap.todolist;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bubblewrap.todolist.Adapter.ToDoAdapter;
import com.bubblewrap.todolist.Model.ToDoModel;
import com.bubblewrap.todolist.Utils.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

public class NewTaskAct extends AppCompatActivity {

    EditText addTitle, addDesc;
    TextView addDate, addTime;
    Button btnDate, btnTime;
    Button btnCreateTask, btnCancelCreate;
    public static int id;
    public static Calendar cTime;
    public static Hashtable<Integer, Calendar> dict = new Hashtable<Integer, Calendar>();

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

        cTime = Calendar.getInstance();

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
                int id = myDB.insertTask(task);
                startAlarm(id, titleTXT, descTXT);

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
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cTime.set(Calendar.YEAR, year);
                cTime.set(Calendar.MONTH, month);
                cTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                addDate.setText(simpleDateFormat.format(cTime.getTime()));
            }
        };

        new DatePickerDialog(NewTaskAct.this, R.style.DatePicker, dateSetListener, cTime.get(Calendar.YEAR),
                        cTime.get(Calendar.MONTH), cTime.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                cTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cTime.set(Calendar.MINUTE, minute);
                cTime.set(Calendar.SECOND, 0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                addTime.setText(simpleDateFormat.format(cTime.getTime()));
            }
        };

        new TimePickerDialog(NewTaskAct.this, R.style.TimePicker ,timeSetListener, cTime.get(Calendar.HOUR_OF_DAY),
                cTime.get(Calendar.MINUTE), false).show();
    }

    private void startAlarm(int id, String title, String desc){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotifReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        dict.put(id, cTime);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cTime.getTimeInMillis(), pendingIntent);
    }
}