package com.bubblewrap.todolist;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bubblewrap.todolist.Model.ToDoModel;
import com.bubblewrap.todolist.Utils.DatabaseHandler;

import java.util.List;

import static com.bubblewrap.todolist.App.CHANNEL_1_ID;

public class NotifReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {

        int id = intent.getIntExtra("id", 0);
        String title = intent.getStringExtra("title");
        String desc = intent.getStringExtra("desc");

        Notification builder = new NotificationCompat.Builder(context, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(title)
                .setContentText(desc)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder);
    }

}
