package com.example.volansys.mvvmdemo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MyClickHandlers {
    private Context context;
    public MyClickHandlers(Context context) {
        this.context=context;
    }

    public void onFabClicked(View view){
        Toast.makeText(context, "FabClicked!", Toast.LENGTH_SHORT).show();
        Log.d("Onclick","onFabMethod Called");
    }
}

