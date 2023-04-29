package com.bcsc.assignment.todo.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcsc.assignment.R;
import com.bcsc.assignment.todo.AddNewTask;
import com.bcsc.assignment.todo.MainActivity;
import com.bcsc.assignment.todo.Model.ToDoModel;
import com.bcsc.assignment.todo.Utils.DatabaseHandler;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {

    private List<ToDoModel> todoList;
    private DatabaseHandler db;
    private MainActivity activity;

    public ToDoAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        db.openDatabase();

        final ToDoModel item = todoList.get(position);
        holder.taskname.setText(item.getTask());
        holder.description.setText(item.getDescription());
        holder.startdate.setText(item.getStartdate());
        holder.enddate.setText(item.getEnddate());
        holder.priority.setText(item.getPriority());

    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<ToDoModel> todoList) {
        this.todoList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        ToDoModel item = todoList.get(position);
        db.deleteTask(item.getId());
        todoList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = todoList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putString("description", item.getDescription());
        bundle.putString("startdate", item.getStartdate());
        bundle.putString("enddate", item.getEnddate());
        bundle.putString("priority", item.getPriority());

        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        EditText taskname,description,startdate,enddate,priority;

        ViewHolder(View view) {
            super(view);
            taskname = view.findViewById(R.id.taskname);
            description = view.findViewById(R.id.description);
            startdate = view.findViewById(R.id.startdate);
            enddate = view.findViewById(R.id.enddate);
            priority = view.findViewById(R.id.priority);

        }
    }
}
