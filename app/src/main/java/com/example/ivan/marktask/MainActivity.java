package com.example.ivan.marktask;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.marktask.DATABase.DBHelper;
import com.example.ivan.marktask.RecyclerViewPackage.MyTask;
import com.example.ivan.marktask.RecyclerViewPackage.TaskAdapter;
import com.example.ivan.marktask.RecyclerViewPackage.helper.OnStartDragListener;
import com.example.ivan.marktask.RecyclerViewPackage.helper.SimpleItemTouchHelperCallback;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements OnStartDragListener{

    private DrawerLayout drawerLayout;
    private NavigationView  navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private RecyclerView recyclerViewTasks;
    private TaskAdapter taskAdapter;
    private ArrayList<MyTask> tasks = new ArrayList<MyTask>();
    private Toolbar toolbar;
    private TextView numToDO, headerDescription;
    private FloatingActionButton floatingActionButton;
    private ItemTouchHelper itemTouchHelper;

    private DBHelper dbHelper;

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //numToDO = (TextView) findViewById(R.id.textViewNumberToDO);
        //headerDescription = (TextView) findViewById(R.id.textViewHeaderDescription);

        /**
         * DATABASE CREATION
         */
        dbHelper = new DBHelper(this);
        final SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        final ContentValues contentValues = new ContentValues();
        //sqLiteDatabase.delete(DBHelper.DATABASE_TABLE_NAME, null, null);
        Cursor cursor = sqLiteDatabase.query(DBHelper.DATABASE_TABLE_NAME, null, null, null, null, null, null);
        tasks.clear();
        if (cursor.moveToFirst()) {
            int tn = cursor.getColumnIndex(DBHelper.TASK_NAME);
            int td_d = cursor.getColumnIndex(DBHelper.TASK_DATE_DAY);
            int td_m = cursor.getColumnIndex(DBHelper.TASK_DATE_MONTH);
            int td_y = cursor.getColumnIndex(DBHelper.TASK_DATE_YEAR);
            do {
                tasks.add(new MyTask(
                        cursor.getString(tn),
                        "",
                        cursor.getInt(td_d),
                        cursor.getInt(td_m),
                        cursor.getInt(td_y),
                        "",
                        false
                ));
            } while (cursor.moveToNext());
        }
        /**
         * END DATABASE CREATION
         */


        /**
         * START INIT
         */
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerViewTasks = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerViewTasks.setHasFixedSize(true);
        recyclerViewTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(tasks, getApplicationContext());
        recyclerViewTasks.setAdapter(taskAdapter);
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(taskAdapter);
        itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerViewTasks);
        //numToDO.setText(String.valueOf(tasks.size()));
        //headerDescription.setText("Все задачи");
        /**
         * END INIT
         */


        /**
         * BEGIN CREATION FLOATING ACTION BUTTON
         */
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.fab) {
                    startActivity(new Intent(getApplicationContext(), AddTaskActivity.class));
                    //finish();
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();

                int s_day = new Date().getDay();
                int s_month = new Date().getMonth() + 1;
                int s_year = new Date().getYear() + 1900;

                //Toast.makeText(getApplicationContext(), s_day + "/" + s_month + "/" + s_year, Toast.LENGTH_SHORT).show();

                Cursor sCursor = sqLiteDatabase.query(DBHelper.DATABASE_TABLE_NAME, null, null, null, null, null, null);
                int s_t_name = sCursor.getColumnIndex(DBHelper.TASK_NAME);
                int s_t_date_day = sCursor.getColumnIndex(DBHelper.TASK_DATE_DAY);
                int s_t_date_month = sCursor.getColumnIndex(DBHelper.TASK_DATE_MONTH);
                int s_t_date_year = sCursor.getColumnIndex(DBHelper.TASK_DATE_YEAR);

                switch (item.getItemId()){
                    case R.id.left_nav_today:
                        //Toast.makeText(getApplicationContext(), "TODAY", Toast.LENGTH_SHORT).show();
                        getSupportActionBar().setTitle("На сегодня");
                        if(sCursor.moveToFirst()){
                            tasks.clear();
                            do {
                                String t_name = sCursor.getString(s_t_name);
                                int t_date_day = sCursor.getInt(s_t_date_day);
                                int t_date_month = sCursor.getInt(s_t_date_month);
                                int t_date_year = sCursor.getInt(s_t_date_year);
                                if(t_date_day == s_day && t_date_month == s_month && t_date_year == s_year){
                                    tasks.add(
                                            new MyTask(
                                                    t_name,
                                                    "",
                                                    t_date_day,
                                                    t_date_month,
                                                    t_date_year,
                                                    "",
                                                    false
                                            )
                                    );
                                }
                            }while (sCursor.moveToNext());
                            taskAdapter.notifyDataSetChanged();
                        }
                        headerDescription.setText("На сегодня");
                        break;
                    case R.id.left_nav_month:
                        //Toast.makeText(getApplicationContext(), "MONTH", Toast.LENGTH_SHORT).show();
                        getSupportActionBar().setTitle("На месяц");
                        if(sCursor.moveToFirst()){
                            tasks.clear();
                            do {
                                String t_name = sCursor.getString(s_t_name);
                                int t_date_day = sCursor.getInt(s_t_date_day);
                                int t_date_month = sCursor.getInt(s_t_date_month);
                                int t_date_year = sCursor.getInt(s_t_date_year);
                                if(t_date_month == s_month && t_date_year == s_year){
                                    tasks.add(
                                            new MyTask(
                                                    t_name,
                                                    "",
                                                    t_date_day,
                                                    t_date_month,
                                                    t_date_year,
                                                    "",
                                                    false
                                            )
                                    );
                                }
                            }while (sCursor.moveToNext());
                            taskAdapter.notifyDataSetChanged();
                        }
                        headerDescription.setText("На месяц");
                        break;
                    case R.id.left_nav_year:
                        //Toast.makeText(getApplicationContext(), "YEAR", Toast.LENGTH_SHORT).show();
                        getSupportActionBar().setTitle("На год");
                        if(sCursor.moveToFirst()){
                            tasks.clear();
                            do {
                                String t_name = sCursor.getString(s_t_name);
                                int t_date_day = sCursor.getInt(s_t_date_day);
                                int t_date_month = sCursor.getInt(s_t_date_month);
                                int t_date_year = sCursor.getInt(s_t_date_year);
                                if(t_date_year == s_year){
                                    tasks.add(
                                            new MyTask(
                                                    t_name,
                                                    "",
                                                    t_date_day,
                                                    t_date_month,
                                                    t_date_year,
                                                    "",
                                                    false
                                            )
                                    );
                                }
                            }while (sCursor.moveToNext());
                            taskAdapter.notifyDataSetChanged();
                        }
                        headerDescription.setText("На год");
                        break;
                    case R.id.left_nav_all:
                        //Toast.makeText(getApplicationContext(), "ALL", Toast.LENGTH_SHORT).show();
                        getSupportActionBar().setTitle("MarkTask");
                        if (sCursor.moveToFirst()) {
                            tasks.clear();
                            int tn = sCursor.getColumnIndex(DBHelper.TASK_NAME);
                            int td_d = sCursor.getColumnIndex(DBHelper.TASK_DATE_DAY);
                            int td_m = sCursor.getColumnIndex(DBHelper.TASK_DATE_MONTH);
                            int td_y = sCursor.getColumnIndex(DBHelper.TASK_DATE_YEAR);
                            do {
                                tasks.add(new MyTask(
                                        sCursor.getString(tn),
                                        "",
                                        sCursor.getInt(td_d),
                                        sCursor.getInt(td_m),
                                        sCursor.getInt(td_y),
                                        "",
                                        false
                                ));
                            } while (sCursor.moveToNext());
                            taskAdapter.notifyDataSetChanged();
                        }
                        headerDescription.setText("Все задачи");
                        break;
                    case R.id.exit:
                        finish();
                    default:
                        break;
                }
                //numToDO.setText(String.valueOf(tasks.size()));
                return true;
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_menu_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getApplicationContext(), "SUBMIT", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if(newText == ""){
                    Toast.makeText(getApplicationContext(), "ZEROO", Toast.LENGTH_SHORT).show();
                }

                SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.query(DBHelper.DATABASE_TABLE_NAME, null, null, null, null, null, null);
                tasks.clear();
                if (cursor.moveToFirst()) {
                    int tn = cursor.getColumnIndex(DBHelper.TASK_NAME);
                    int td_d = cursor.getColumnIndex(DBHelper.TASK_DATE_DAY);
                    int td_m = cursor.getColumnIndex(DBHelper.TASK_DATE_MONTH);
                    int td_y = cursor.getColumnIndex(DBHelper.TASK_DATE_YEAR);
                    do {
                        tasks.add(new MyTask(
                                cursor.getString(tn),
                                "",
                                cursor.getInt(td_d),
                                cursor.getInt(td_m),
                                cursor.getInt(td_y),
                                "",
                                false
                        ));
                    } while (cursor.moveToNext());
                }

                taskAdapter.notifyDataSetChanged();

                ArrayList<MyTask> newTasks = new ArrayList<MyTask>();
                for(int i = 0; i < tasks.size(); i++){
                    if(tasks.get(i).getTaskName().contains(newText)){
                        newTasks.add(tasks.get(i));
                    }
                }
                taskAdapter.setFilter(newTasks);
                taskAdapter.notifyDataSetChanged();
                return true;
            }
        });
        return true;
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        itemTouchHelper.startDrag(viewHolder);
    }
}
