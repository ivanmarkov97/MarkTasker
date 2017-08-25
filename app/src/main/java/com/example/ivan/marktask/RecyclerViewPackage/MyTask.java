package com.example.ivan.marktask.RecyclerViewPackage;

import java.util.Date;

/**
 * Created by Ivan on 04.08.2017.
 */

public class MyTask {
    private String taskName;
    private String taskDescripyion;
    private int deadLineDay;
    private int deadLineMonth;
    private int deadLineYear;
    private String taskGroup;
    private boolean isDone;

    public MyTask() {
        this.taskName = "Задача";
        this.taskDescripyion = "Описание";
        this.deadLineDay = new Date().getDay();
        this.deadLineMonth = new Date().getMonth();
        this.deadLineYear = new Date().getYear();
        this.taskGroup = "";
        this.isDone = false;
    }

    public MyTask(String taskName, String taskDescripyion, int deadLineDay, int deadLineMonth, int deadLineYear, String taskGroup, boolean isDone) {

        this.taskName = taskName;
        this.taskDescripyion = taskDescripyion;
        this.deadLineDay = deadLineDay;
        this.deadLineMonth = deadLineMonth;
        this.deadLineYear = deadLineYear;
        this.taskGroup = taskGroup;
        this.isDone = isDone;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTaskGroup() {
        return taskGroup;
    }

    public void setTaskGroup(String taskGroup) {
        this.taskGroup = taskGroup;
    }

    public int getDeadLineYear() {
        return deadLineYear;
    }

    public void setDeadLineYear(int deadLineYear) {
        this.deadLineYear = deadLineYear;
    }

    public int getDeadLineMonth() {
        return deadLineMonth;
    }

    public void setDeadLineMonth(int deadLineMonth) {
        this.deadLineMonth = deadLineMonth;
    }

    public int getDeadLineDay() {
        return deadLineDay;
    }

    public void setDeadLineDay(int deadLineDay) {
        this.deadLineDay = deadLineDay;
    }

    public String getTaskDescripyion() {
        return taskDescripyion;
    }

    public void setTaskDescripyion(String taskDescripyion) {
        this.taskDescripyion = taskDescripyion;
    }
}