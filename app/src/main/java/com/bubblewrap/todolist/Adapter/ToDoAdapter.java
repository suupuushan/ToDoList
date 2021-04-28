package com.bubblewrap.todolist.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bubblewrap.todolist.EditTask;
import com.bubblewrap.todolist.MainActivity;
import com.bubblewrap.todolist.Model.ToDoModel;
import com.bubblewrap.todolist.NewTaskAct;
import com.bubblewrap.todolist.R;
import com.bubblewrap.todolist.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private Context context;
    private List<ToDoModel> todoList;
    private DatabaseHandler db;
    Activity activity;


    public ToDoAdapter(Context context, DatabaseHandler db, Activity activity) {
        this.context = context;
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_does, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final ToDoModel item = todoList.get(position);
        holder.taskTitleTxt.setText(item.getTaskTitle());
        holder.taskDescTxt.setText(item.getTaskDesc());
        holder.taskDateTxt.setText(item.getTaskDate());
        holder.taskTimeTxt.setText(item.getTaskTime());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(context, EditTask.class);
                ToDoModel b = todoList.get(position);
                a.putExtra("item_id", String.valueOf(b.getId()));
                a.putExtra("addTitle2", b.getTaskTitle());
                a.putExtra("addDesc2", b.getTaskDesc());
                a.putExtra("addDate2", b.getTaskDate());
                a.putExtra("addTime2", b.getTaskTime());
                activity.startActivityForResult(a,1);
                activity.finish();
            }
        });
    }



    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
    }

    public void deleteItem(int position){
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView taskTitleTxt;
        private TextView taskDescTxt;
        private TextView taskDateTxt;
        private TextView taskTimeTxt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitleTxt = itemView.findViewById(R.id.taskTitle);
            taskDescTxt = itemView.findViewById(R.id.taskDesc);
            taskDateTxt = itemView.findViewById(R.id.taskDate);
            taskTimeTxt = itemView.findViewById(R.id.taskTime);
        }

    }
}