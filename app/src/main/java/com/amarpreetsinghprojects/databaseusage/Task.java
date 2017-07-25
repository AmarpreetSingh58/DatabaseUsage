package com.amarpreetsinghprojects.databaseusage;

/**
 * Created by kulvi on 07/14/17.
 */

public class Task {

    String taskName;
    int id;
    boolean done;

    public Task(String taskName, int id, boolean done) {
        this.taskName = taskName;
        this.id = id;
        this.done = done;
    }

    public Task(String taskName, boolean done) {
        this.taskName = taskName;
        this.done = done;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
