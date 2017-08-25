package com.example.ivan.marktask;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.marktask.DATABase.DBHelper;

public class ShowTaskActivity extends AppCompatActivity {

    private ImageButton buttonPickDate;
    private Button buttonSaveChangesTask;
    private TextView taskDate;
    private EditText taskName;
    private EditText taskDescription;
    private int year_x = 2017, month_x = 1, day_x = 1;
    String day_X = "", month_X = "", year_X = "";
    static final int DIALOG_ID = 0;

    private Toolbar toolbar;

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
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                break;
            default:break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_task);

        toolbar = (Toolbar) findViewById(R.id.show_task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Intent my_intent = getIntent();

        Toast.makeText(getApplicationContext(), my_intent.getStringExtra("task_name"), Toast.LENGTH_SHORT).show();

        buttonPickDate = (ImageButton) findViewById(R.id.show_task_deadline_date_change);
        buttonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.show_task_deadline_date_change){
                    showDialog(DIALOG_ID);
                }
            }
        });

        taskDate = (TextView) findViewById(R.id.show_task_deadline_date_date);
        taskName = (EditText) findViewById(R.id.show_task_name_value);
        taskDescription = (EditText) findViewById(R.id.show_task_description);

        taskName.setText(my_intent.getStringExtra("task_name"));
        taskDate.setText(my_intent.getStringExtra("task_date"));

        DBHelper dbHelper = new DBHelper(this);
        final SQLiteDatabase sqlDateBase = dbHelper.getWritableDatabase();

        String date = my_intent.getStringExtra("task_date");

        int pos = 0;
        for(int i = 0; i < date.length(); i++){
            if(date.charAt(i) != '.'){
                if(pos == 0){
                    day_X += date.charAt(i);
                }
                if(pos == 1){
                    month_X += date.charAt(i);
                }
                if(pos == 2){
                    year_X += date.charAt(i);
                }
            }
            else {
                pos ++;
            }
        }

        Cursor cursor = sqlDateBase.query(DBHelper.DATABASE_TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            //Toast.makeText(getApplicationContext(), "INTO CURSOR", Toast.LENGTH_SHORT).show();
            int t_name = cursor.getColumnIndex(DBHelper.TASK_NAME);
            int t_dscr = cursor.getColumnIndex(DBHelper.TASK_DESCRIPTION);
            int t_day = cursor.getColumnIndex(DBHelper.TASK_DATE_DAY);
            int t_mon = cursor.getColumnIndex(DBHelper.TASK_DATE_MONTH);
            int t_year = cursor.getColumnIndex(DBHelper.TASK_DATE_YEAR);

            //Toast.makeText(getApplicationContext(), c_name + "/" + c_d + "/" + c_m + "/" + c_y, Toast.LENGTH_SHORT).show();

            do{
                String c_name = cursor.getString(t_name);
                String c_d = String.valueOf(cursor.getInt(t_day));
                String c_m = String.valueOf(cursor.getInt(t_mon));
                String c_y = String.valueOf(cursor.getInt(t_year));

                //Toast.makeText(getApplicationContext(), c_name + "/" + c_d + "/" + c_m + "/" + c_y, Toast.LENGTH_SHORT).show();
                if(taskName.getText().toString().equals(c_name)
                        && day_X.equals(c_d)
                        && month_X.equals(c_m)
                        && year_X.equals(c_y)
                        ){
                    taskDescription.setText(cursor.getString(t_dscr));
                    //break;
                }

            }while (cursor.moveToNext());
        }

        buttonSaveChangesTask = (Button) findViewById(R.id.show_task_button_save_changes);
        buttonSaveChangesTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.show_task_button_save_changes){
                    ContentValues newContentValues = new ContentValues();

                    newContentValues.put(DBHelper.TASK_NAME, taskName.getText().toString());
                    newContentValues.put(DBHelper.TASK_DESCRIPTION, taskDescription.getText().toString());
                    newContentValues.put(DBHelper.TASK_DATE_DAY, day_x);
                    newContentValues.put(DBHelper.TASK_DATE_MONTH, month_x);
                    newContentValues.put(DBHelper.TASK_DATE_YEAR, year_x);

                    sqlDateBase.update(
                            DBHelper.DATABASE_TABLE_NAME,
                            newContentValues,
                            "task_name = ? and task_date_day = ? and task_date_month = ? and task_date_year = ?",
                            new String[]{my_intent.getStringExtra("task_name"), String.valueOf(day_X), String.valueOf(month_X), String.valueOf(year_X)}
                    );
                    Toast.makeText(getApplicationContext(), "UPDATED", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });
    }
}
