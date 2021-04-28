package com.bubblewrap.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bubblewrap.todolist.Adapter.ToDoAdapter;
import com.bubblewrap.todolist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private Context context;
    private static final String NAME = "toDoListDB.db";
    private static final int VERSION = 1;

    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK_TITLE = "task_title";
    private static final String TASK_DESC = "task_desc";
    private static final String TASK_DATE = "task_date";
    private static final String TASK_TIME = "task_time";

    private SQLiteDatabase db;

    public DatabaseHandler(@Nullable Context context) {
        super(context, NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query =
                "CREATE TABLE " + TODO_TABLE +
                        " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        TASK_TITLE + " TEXT, " +
                        TASK_DESC + " TEXT, " +
                        TASK_DATE + " TEXT, " +
                        TASK_TIME + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null,
                    null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTaskTitle(cur.getString(cur.getColumnIndex(TASK_TITLE)));
                        task.setTaskDesc(cur.getString(cur.getColumnIndex(TASK_DESC)));
                        task.setTaskDate(cur.getString(cur.getColumnIndex(TASK_DATE)));
                        task.setTaskTime(cur.getString(cur.getColumnIndex(TASK_TIME)));
                        taskList.add(task);
                    }
                    while (cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public void insertTask(ToDoModel task){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, task.getTaskTitle());
        cv.put(TASK_DESC, task.getTaskDesc());
        cv.put(TASK_DATE, task.getTaskDate());
        cv.put(TASK_TIME, task.getTaskTime());
        db.insert(TODO_TABLE, null, cv);
    }

    public void updateTask(String id, ToDoModel task){
        db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TASK_TITLE, task.getTaskTitle());
        cv.put(TASK_DESC, task.getTaskDesc());
        cv.put(TASK_DATE, task.getTaskDate());
        cv.put(TASK_TIME, task.getTaskTime());
        long result = db.update(TODO_TABLE, cv, "id=?", new String[]{id});
        if (result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}

