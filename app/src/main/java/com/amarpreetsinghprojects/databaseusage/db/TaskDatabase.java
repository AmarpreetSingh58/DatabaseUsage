package com.amarpreetsinghprojects.databaseusage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amarpreetsinghprojects.databaseusage.Task;

import java.util.ArrayList;

import static com.amarpreetsinghprojects.databaseusage.db.TableTask.*;

/**
 * Created by kulvi on 07/14/17.
 */

public class TaskDatabase extends SQLiteOpenHelper {
    String TAG = "";

    public static final String DATABSE_NAME = "Task.db";
    public static final int VERSION = 1;

    public TaskDatabase(Context context) {
        super(context, DATABSE_NAME, null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TBL = CREATE+ TABLE_NAME
                + LBR + COLUMN_ID + INT_PK_AUTOIC
                + COMMA
                + COLUMN_TASK + TYPE_TEXT + COMMA
                + COLUMN_IS_DONE + TYPE_INTEGER + RBR
                + TERMINATE;

        Log.d(TAG, "onCreate: "+CREATE_TBL);
        db.execSQL(CREATE_TBL);
    }

    public long insertTask(Task task){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK, task.getTaskName());
        cv.put(COLUMN_IS_DONE, task.isDone());
        SQLiteDatabase sqlDb = getWritableDatabase(); // provides database in writtable mode
        return sqlDb.insert(TABLE_NAME,null,cv);

    }

    public int updateTask(Task task){
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_IS_DONE,true);
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.update(TABLE_NAME,cv,COLUMN_ID +" = "+ task.getId(),null);
    }

    public ArrayList<Task> getAllTask(){
        ArrayList<Task> task = new ArrayList<>();

        SQLiteDatabase sqlDatabase = getReadableDatabase();

        Cursor c = sqlDatabase.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                COLUMN_ID + " DESC");
        String taskTiltle;
        int id;
        boolean done;
        while(c.moveToNext()){
            taskTiltle = c.getString(c.getColumnIndex(COLUMN_TASK));
            id = c.getInt(c.getColumnIndex(COLUMN_ID));
            done = (1 == c.getInt(c.getColumnIndex(COLUMN_IS_DONE)));

            task.add(new Task(taskTiltle,id,done));
        }
        c.close();
        return task;
    }
    public void insertTask(long id){


    }

    public int deleteTask(int id){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete(TABLE_NAME,COLUMN_ID + " = "+id,null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
