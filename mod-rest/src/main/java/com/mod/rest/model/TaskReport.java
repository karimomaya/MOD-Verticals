package com.mod.rest.model;

import com.mod.rest.annotation.ColumnName;
import com.mod.rest.repository.UserRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * Created by karim.omaya on 12/27/2019.
 */
public class TaskReport {

    Task task;
    UserRepository userRepository;

    public TaskReport(Task task, UserRepository userRepository) {
        this.task = task;
        this.userRepository = userRepository;
    }


    @ColumnName(key = "اسم")
    public String getTaskName(){
        return removeNullValue(this.task.getTaskName());
    }

    @ColumnName(key = "منشأ")
    public String getCreatedByName(){
        if (this.task.getUserCreatedBy() == null) {
            Optional<User> createdBy =  userRepository.findById(task.getCreatedBy());
            if (createdBy.isPresent()){
                return createdBy.get().getDisplayName();
            }
        }
        return this.task.getUserCreatedBy().getDisplayName();
    }

    @ColumnName(key = "مالك")
    public String getOwnerName(){
        if (this.task.getUserOwner() == null) {
            Optional<User> userOwner =  userRepository.findById(task.getOwner());
            if (userOwner.isPresent()){
                return userOwner.get().getDisplayName();
            }
        }
        return this.task.getUserOwner().getDisplayName();
    }

    @ColumnName(key = "وصف")
    public String getDescription(){
        return removeNullValue(this.task.getDescription());
    }

    @ColumnName(key = "حالة")
    public String getTaskStatus() {
        switch (this.task.getStatus()){
            case 0:
                return "متوقفة";
            case 1:
                return "لم تبدأ";
            case 2:
                return "بدء";
            case 3:
                return "منتهية";
            default:
                return "";
        }
    }
    @ColumnName(key = "تاريخ البدء")
    public String getTaskStartDate(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        return dateFormat.format(this.task.getStartDate());
    }

    @ColumnName(key = "تاريخ الانتهاء")
    public String getTaskDueDate(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        return dateFormat.format(task.getDueDate());
    }

    private String removeNullValue(String value){
        if (value.equals("null")){
            return "";
        }
        return value;
    }


}
