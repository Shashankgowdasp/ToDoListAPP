package com.bcsc.assignment.todo;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bcsc.assignment.R;
import com.bcsc.assignment.todo.Model.ToDoModel;
import com.bcsc.assignment.todo.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;



import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    public EditText newTaskTextet,descriptionet,startdateet,enddateet,priorityet;
    private Button newTaskSaveButton;
    String task ;
    String descriptions;
    String startdates;
    String enddates;
    String prioritys;


    private DatabaseHandler db;

    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskTextet = requireView().findViewById(R.id.newTaskTextet);
        descriptionet = requireView().findViewById(R.id.descriptionet);
        startdateet = requireView().findViewById(R.id.startdateet);
        enddateet = requireView().findViewById(R.id.enddateet);
        priorityet = requireView().findViewById(R.id.priorityet);

        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
             task = bundle.getString("task");
             descriptions = bundle.getString("description");
             startdates = bundle.getString("startdate");
             enddates = bundle.getString("enddate");
             prioritys = bundle.getString("priority");



            newTaskTextet.setText(task);
            descriptionet.setText(descriptions);
            startdateet.setText(startdates);
            enddateet.setText(enddates);
            priorityet.setText(prioritys);

//            assert task != null;
//            if(task.length()>0)
//                newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
//

        }

        db = new DatabaseHandler(getActivity());
        db.openDatabase();


        final boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateTexts())
                {
                    String text = newTaskTextet.getText().toString();
                    String description = descriptionet.getText().toString();
                    String startdate = startdateet.getText().toString();
                    String enddate = enddateet.getText().toString();
                    String priority = priorityet.getText().toString();
                    if(finalIsUpdate){
                        db.updateTask(bundle.getInt("id"), text,description,startdate,enddate,priority);

                    }
                    else {
                        ToDoModel task = new ToDoModel();
                        task.setTask(text);
                        task.setDescription(description);
                        task.setStartdate(startdate);
                        task.setEnddate(enddate);
                        task.setPriority(priority);
                        db.insertTask(task);
                    }
                    dismiss();
                }
            }
        });
    }

    public boolean validateTexts()
    {
        if(newTaskTextet.getText().toString().equals("") && descriptionet.getText().toString().equals("") && startdateet.getText().toString().equals("") && enddateet.getText().toString().equals("") && priorityet.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "Please enter all fields to continue", Toast.LENGTH_SHORT).show();
            return false;
        } else if(newTaskTextet.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "Please enter Tasks name", Toast.LENGTH_SHORT).show();
            return false;
        }else if(descriptionet.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "Please enter Description", Toast.LENGTH_SHORT).show();
            return false;
        }else if(startdateet.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "Please enter Start Date", Toast.LENGTH_SHORT).show();
            return false;
        }else if(enddateet.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "Please enter End Date", Toast.LENGTH_SHORT).show();
            return false;
        }else if(priorityet.getText().toString().equals(""))
        {
            Toast.makeText(getContext(), "Please enter Priority", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
