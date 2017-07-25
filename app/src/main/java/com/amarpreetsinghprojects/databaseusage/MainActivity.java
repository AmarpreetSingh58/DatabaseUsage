package com.amarpreetsinghprojects.databaseusage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.amarpreetsinghprojects.databaseusage.db.TaskDatabase;

import java.util.ArrayList;
import java.util.Calendar;

import static android.app.AlarmManager.RTC_WAKEUP;

public class MainActivity extends AppCompatActivity {

    EditText taskETV;
    RecyclerView recyclerViewTask;
    ArrayList<Task> taskArrayList = new ArrayList<>();
    TaskAdapter taskadapter;
    int taskHour,taskMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerViewTask = (RecyclerView)findViewById(R.id.recyclerViewTasks);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewTask.setLayoutManager(layoutManager);
        taskadapter = new TaskAdapter(new TaskItemClickListner() {

            @Override
            public void onItemClick(Task task) {
                TaskDatabase tb = new TaskDatabase(MainActivity.this);
                if (tb.updateTask(task)==1){
                    Toast.makeText(MainActivity.this,"One Item Updated",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this,"Operation Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(Task task) {
                TaskDatabase tb = new TaskDatabase(MainActivity.this);
                if (tb.deleteTask(task.getId())==1){
                    Toast.makeText(MainActivity.this,"One Item deleted \n "+ task.getTaskName(),Toast.LENGTH_SHORT).show();
                    taskArrayList.clear();
                    taskArrayList.addAll(tb.getAllTask());
                    taskadapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(MainActivity.this,"Operation Unsuccessful",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCheck(final Task task) {
                CardView alarmCardView = (CardView) getLayoutInflater().inflate(R.layout.set_alarm_dialog,null);
                final TimePicker addTaskTime = (TimePicker)alarmCardView.findViewById(R.id.addTaskTimePicker);
                AlertDialog alarmDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Set Time")
                        .setView(R.layout.set_alarm_dialog)
                        .setPositiveButton("SET", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                addTaskTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                                    @Override
                                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                                        Log.d("", "onTimeChanged: "+hourOfDay + "\n"+minute);
                                        taskHour = hourOfDay;
                                        taskMinute = minute;
                                    }
                                });

                                Calendar c = Calendar.getInstance();
                                int currentHour = c.get(Calendar.HOUR_OF_DAY),currentMinute = c.get(Calendar.MINUTE);

                                int taskTimeMilliseconds = ((taskHour*3600 + taskMinute*60) - (currentHour*3600 + currentMinute*60))*1000;
                                Log.d("time millseconds", "onClick: "+taskTimeMilliseconds);

                                Intent i = new Intent(MainActivity.this, TaskReciever.class);
                                i.putExtra("taskTitle",task.getTaskName());
                                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,123,i,PendingIntent.FLAG_UPDATE_CURRENT);

                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                                alarmManager.set(RTC_WAKEUP,System.currentTimeMillis()+taskTimeMilliseconds, pi);
                            }
                        })
                        .show();
            }
        });
        recyclerViewTask.setAdapter(taskadapter);



        FloatingActionButton addFab = (FloatingActionButton)findViewById(R.id.addFab);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout dialogLayout = (LinearLayout)getLayoutInflater().inflate(R.layout.add_dialog,null);
                taskETV = (EditText)dialogLayout.findViewById(R.id.addTaskTitleETV);





                AlertDialog addDialog = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Add Task")
                        .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                Task newtask = new Task(taskETV.getText().toString(),false);
                                TaskDatabase tb = new TaskDatabase(MainActivity.this);
                                long id = tb.insertTask(newtask);
                                taskArrayList.clear();
                                // taskArrayList.add(tb.getTask(id));
                                taskArrayList.addAll(tb.getAllTask());
                                taskadapter.notifyDataSetChanged();

                            }
                        })
                        .setView(dialogLayout)
                        .show();


            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TaskDatabase tb = new TaskDatabase(MainActivity.this);

        taskArrayList.clear();
        // taskArrayList.add(tb.getTask(id));
        taskArrayList.addAll(tb.getAllTask());
        taskadapter.notifyDataSetChanged();

    }

    public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {


        TaskItemClickListner listner;

        public TaskAdapter(TaskItemClickListner listner) {
            this.listner = listner;
        }

        @Override
        public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.single_item_task,parent,false);
            return new TaskViewHolder(v);
        }

        @Override
        public void onBindViewHolder(TaskViewHolder holder, int position) {
            holder.onBind(taskArrayList.get(position));

        }

        @Override
        public int getItemCount() {
            return taskArrayList.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            TextView taskTitle,isDone;
            CheckBox doneStatus;
            public TaskViewHolder(View itemView) {
                super(itemView);
                taskTitle = (TextView) itemView.findViewById(R.id.taskTitle);
                isDone = (TextView)itemView.findViewById(R.id.doneStatus);
                doneStatus = (CheckBox)itemView.findViewById(R.id.taskStatuscheckBox);
            }

            public void onBind(final Task task){

                taskTitle.setText(task.getTaskName());
                isDone.setText(String.valueOf(task.isDone()));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listner.onItemClick(task);
                        task.setDone(true);
                        isDone.setText(String.valueOf(task.isDone()));
                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        listner.onItemLongClick(task);
                        return false;
                    }
                });
                doneStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked == true) {
                            if (task.isDone() == true) {
                                listner.onCheck(task);
                            } else {
                                listner.onItemClick(task);
                                task.setDone(true);
                                isDone.setText(String.valueOf(task.isDone()));
                                listner.onCheck(task);
                            }
                        }
                    }
                });
            }
        }
    }
}
