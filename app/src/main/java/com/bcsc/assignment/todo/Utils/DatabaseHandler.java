package com.bcsc.assignment.todo.Utils;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.bcsc.assignment.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String DESCRIPTION = "description";
    private static final String STARTDATE = "startdate";
    private static final String ENDDATE = "enddate";
    private static final String PRIORITY = "priority";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + DESCRIPTION + " TEXT," + STARTDATE + " TEXT," + ENDDATE + " TEXT," + PRIORITY + " TEXT)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(DESCRIPTION, task.getDescription());
        cv.put(STARTDATE, task.getStartdate());
        cv.put(ENDDATE, task.getEnddate());
        cv.put(PRIORITY, task.getPriority());

        db.insert(TODO_TABLE, null, cv);
    }

    @SuppressLint("Range")
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
          //  cur=db.rawQuery("select * from "+TODO_TABLE,null);

            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        ToDoModel task = new ToDoModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setDescription(cur.getString(cur.getColumnIndex(DESCRIPTION)));
                        task.setStartdate(cur.getString(cur.getColumnIndex(STARTDATE)));
                        task.setEnddate(cur.getString(cur.getColumnIndex(ENDDATE)));
                        task.setPriority(cur.getString(cur.getColumnIndex(PRIORITY)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
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



    public void updateTask(int id, String task,String description,String startdate,String enddate,String priority) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(DESCRIPTION,description);
        cv.put(STARTDATE, startdate);
        cv.put(ENDDATE, enddate);
        cv.put(PRIORITY, priority);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
