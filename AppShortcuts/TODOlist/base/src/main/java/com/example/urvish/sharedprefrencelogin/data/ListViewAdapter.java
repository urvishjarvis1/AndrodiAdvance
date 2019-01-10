package com.example.urvish.sharedprefrencelogin.data;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.urvish.sharedprefrencelogin.base.R;

import java.util.ArrayList;

/**
 * Created by urvish on 31/1/18.
 */

public class ListViewAdapter extends ArrayAdapter {
    @NonNull
    private final Activity activity;
    @NonNull
    private final ArrayList<TodoItem> data;



    public ListViewAdapter(@NonNull Activity activity, @NonNull ArrayList<TodoItem> data ) {
        super(activity, R.layout.listview_row,data);
        this.activity=activity;
        this.data=data;

    }

    @NonNull
    public View getView(int pos, View v, ViewGroup parent){
        LayoutInflater inflater=activity.getLayoutInflater();
        View rowview=inflater.inflate(R.layout.listview_row,null,true);
        TextView nameTextField = (TextView) rowview.findViewById(R.id.txt_title);
        TextView infoTextField = (TextView) rowview.findViewById(R.id.txt_desc);
        nameTextField.setText(Integer.toString(data.get(pos).getmId()));
        infoTextField.setText(data.get(pos).getmItem());
        //Returns a Single row view for parent Viewgroup.
        return rowview;
    }
}
