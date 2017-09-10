package com.example.ivan.marktask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.marktask.DATABase.DBHelper;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    private ImageButton buttonPickDate;
    private FloatingActionButton buttonAddTask;
    private TextView taskDate;
    private EditText taskName;
    private EditText taskDescription;
    private Toolbar toolbar;
    private int year_x = new Date().getYear() + 1900,
            month_x = new Date().getMonth(),
            day_x = new Date().getDay();
    static final int DIALOG_ID = 0;

    private DatePickerDialog.OnDateSetListener dpListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            year_x = year;
            month_x = month + 1;
            day_x = dayOfMonth;
            taskDate.setText(day_x + "." + month_x + "." + year_x);
        }
    };

    @Override
    protected Dialog onCreateDialog(int id) {
        if(id == DIALOG_ID){
            return new DatePickerDialog(this, dpListener, year_x, month_x, day_x);
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.action_menu_clean:
                taskName.setText("");
                taskDescription.setText("");
            default:
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        toolbar = (Toolbar) findViewById(R.id.add_task_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Добавление");


        buttonPickDate = (ImageButton) findViewById(R.id.add_task_deadline_date_pick);
        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.add_task_deadline_date_pick){
                    showDialog(DIALOG_ID);
                }
            }
        });

        taskDate = (TextView) findViewById(R.id.add_task_deadline_date_date);
        taskName = (EditText) findViewById(R.id.add_task_name_value);
        taskDescription = (EditText) findViewById(R.id.add_task_description);

        taskDate.setText(day_x + "." + String.valueOf(month_x + 1) + "." + year_x);

        buttonAddTask = (FloatingActionButton) findViewById(R.id.add_task_fab_add);
        buttonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.add_task_fab_add){
                    /*Toast.makeText(getApplicationContext(),
                            "Task Name: " + taskName.getText() +
                            "Task Description: " + taskDescription.getText() +
                            "Task Date: " + taskDate.getText(),
                            Toast.LENGTH_SHORT)
                            .show();
                    */
                    DBHelper dbHelper = new DBHelper(getApplicationContext());
                    SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                    boolean toAdd = true;
                    Cursor cursorCheck = sqLiteDatabase.query(DBHelper.DATABASE_TABLE_NAME, null, null, null, null, null, null, null);
                    if(cursorCheck.moveToFirst()){
                        String checkName = "";
                        int checkNameIndex = cursorCheck.getColumnIndex(DBHelper.TASK_NAME);
                        do{
                            checkName = cursorCheck.getString(checkNameIndex);
                            if (checkName.equals(taskName.getText().toString())){
                                toAdd = false;
                            }
                        }while (cursorCheck.moveToNext());
                    }

                    if(toAdd) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DBHelper.TASK_NAME, taskName.getText().toString());
                        contentValues.put(DBHelper.TASK_DESCRIPTION, taskDescription.getText().toString());
                        contentValues.put(DBHelper.TASK_DATE_DAY, day_x);
                        contentValues.put(DBHelper.TASK_DATE_MONTH, month_x + 1);
                        contentValues.put(DBHelper.TASK_DATE_YEAR, year_x);
                        contentValues.put(DBHelper.TASK_GROUP, "");
                        contentValues.put(DBHelper.TASK_DONE, "false");
                        sqLiteDatabase.insert(DBHelper.DATABASE_TABLE_NAME, null, contentValues);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Задача с таким именем уже существует", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

}
