package com.bubblewrap.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
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

import static com.bubblewrap.todolist.App.CHANNEL_1_ID;

public class EditTask extends AppCompatActivity {

    private ToDoAdapter adapter;
    EditText title, desc;
    TextView date, time;
    String id, titleTXT, descTXT, dateTXT, timeTXT;
    Button btnSaveTask, btnCancelUpdate, btnDate2, btnTime2;
    NotificationManagerCompat notificationManager;
    Calendar timeAfter, timeBefore;


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
        notificationManager = NotificationManagerCompat.from(this);
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
                updateAlarm(Integer.parseInt(id), title2, desc2);
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
        timeAfter = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeAfter.set(Calendar.HOUR_OF_DAY, hourOfDay);
                timeAfter.set(Calendar.MINUTE, minute);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                time.setText(simpleDateFormat.format(timeAfter.getTime()));
            }
        };

        new TimePickerDialog(EditTask.this, R.style.TimePicker ,timeSetListener, timeAfter.get(Calendar.HOUR_OF_DAY),
                timeAfter.get(Calendar.MINUTE), false).show();
    }

    private void updateAlarm(int id, String title, String desc){
        Calendar timeBefore = NewTaskAct.dict.get(id);
        System.out.println(timeBefore);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotifReceiver.class);
        intent.putExtra("id", id);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (timeAfter == null){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeBefore.getTimeInMillis(), pendingIntent);
        } else {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeAfter.getTimeInMillis(), pendingIntent);
        }
    }
}