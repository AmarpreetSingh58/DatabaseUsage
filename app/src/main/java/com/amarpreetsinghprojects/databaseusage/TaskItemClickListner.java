package com.amarpreetsinghprojects.databaseusage;

/**
 * Created by kulvi on 07/15/17.
 */

public interface TaskItemClickListner {

    public void onItemClick(Task task);
    public void onItemLongClick(Task task);
    public void onCheck(Task task);
}
