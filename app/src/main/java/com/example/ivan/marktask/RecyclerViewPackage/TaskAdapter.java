package com.example.ivan.marktask.RecyclerViewPackage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ivan.marktask.DATABase.DBHelper;
import com.example.ivan.marktask.R;
import com.example.ivan.marktask.RecyclerViewPackage.helper.ItemTouchHelperAdapter;
import com.example.ivan.marktask.RecyclerViewPackage.helper.ItemTouchHelperViewHolder;
import com.example.ivan.marktask.ShowTaskActivity;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;

import static com.example.ivan.marktask.R.id.start;

/**
 * Created by Ivan on 04.08.2017.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> implements ItemTouchHelperAdapter{

    private ArrayList<MyTask> tasks;
    private Context context;

    public TaskAdapter(ArrayList<MyTask> tasks, Context context){
        this.tasks = tasks;
        this.context = context;
    }

    public ArrayList<MyTask> getTasks(){
        return this.tasks;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        MyTask myTask = tasks.get(position);
        holder.taskName.setText(myTask.getTaskName());
        //holder.taskDescription.setText(myTask.getTaskDescripyion());
        holder.taskDeadLine.setText(myTask.getDeadLineDay() + "." + myTask.getDeadLineMonth() + "." + String.valueOf(myTask.getDeadLineYear()));
        //holder.taskGroup.setText(myTask.getTaskGroup());
        //holder.taskCheckBox.setChecked(myTask.isDone());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(view.getContext(), String.valueOf(position), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ShowTaskActivity.class);
                intent.putExtra("task_name", holder.taskName.getText().toString());
                intent.putExtra("task_date", holder.taskDeadLine.getText().toString());
                view.getContext().startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(tasks, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        Log.d("MyTag", "Moving");
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

        DBHelper dbHelper = new DBHelper(this.context);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        String taskNameToDel = tasks.get(position).getTaskName();
        String taskDay = String.valueOf(tasks.get(position).getDeadLineDay());
        String taskMonth = String.valueOf(tasks.get(position).getDeadLineMonth());
        String taskYear = String.valueOf(tasks.get(position).getDeadLineYear());

        Toast.makeText(this.context, taskNameToDel + " " + taskDay + " " + taskMonth + " " + taskYear, Toast.LENGTH_SHORT).show();

        sqLiteDatabase.delete(
                DBHelper.DATABASE_TABLE_NAME,
                "task_name = ? and task_date_day = ? and task_date_month = ? and task_date_year = ?",
                new String[]{taskNameToDel, taskDay, taskMonth, taskYear}
        );

        tasks.remove(position);
        notifyItemRemoved(position);
        Toast.makeText(this.context, "deleted " + position, Toast.LENGTH_SHORT).show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, ItemTouchHelperViewHolder{

        private ImageView taskCheckBox;
        private TextView taskName;
        //private TextView taskDescription;
        private TextView taskDeadLine;
        //private TextView taskGroup;

        private ItemClickListener itemClickListener;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            taskCheckBox = (ImageView) itemView.findViewById(R.id.taskCheckBox);
            taskName = (TextView) itemView.findViewById(R.id.taskName);
            //taskDescription = (TextView)itemView.findViewById(R.id.taskDescription);
            taskDeadLine = (TextView) itemView.findViewById(R.id.taskDeadLine);
            //taskGroup = (TextView) itemView.findViewById(R.id.taskGroup);
        }

        public void setItemClickListener(ItemClickListener itemCLickListener){
            this.itemClickListener = itemCLickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v, getAdapterPosition());
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    public void setFilter(ArrayList<MyTask> myTasks) {
        tasks = new ArrayList<>();
        tasks.addAll(myTasks);
    }
}
