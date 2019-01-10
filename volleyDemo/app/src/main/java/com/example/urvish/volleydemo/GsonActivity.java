package com.example.urvish.volleydemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.urvish.volleydemo.Model.Result;

import java.util.ArrayList;
import java.util.List;

public class GsonActivity extends AppCompatActivity {


    private List<User> mListUser;
    private User user;
    private TextView mtxtViewRes;
    private EditText mEditTxtName;
    private static final String URL="https://www.googleapis.com/books/v1/volumes?q=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson);
        mListUser = new ArrayList<>();
        user = new User();
        mtxtViewRes=findViewById(R.id.ans);

        mEditTxtName=findViewById(R.id.edittextquery);



        Button btnSubmit = findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mEditTxtName.getText().toString().isEmpty())
                    submitDataToApi(mEditTxtName.getText().toString());
                else
                    mEditTxtName.setError("please enter data!");
            }
        });

    }


    private void submitDataToApi(String query) {
        //todo
        String url=URL+query+"/1/books";
        GsonRequest gsonRequest=new GsonRequest(url, Result.class, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                Log.d("TAG", "onResponse: "+response.getClass());
                Result result=(Result)response;
                for(int i=0;i<result.getBooks().size();i++)
                mtxtViewRes.append("\nTitle:"+result.getBooks().get(i).getVolumeInfo().getTitle()+
                        "\nAuthors:"+result.getBooks().get(i).getVolumeInfo().getAuthors()
                        +"\nPublisher and Date:"+result.getBooks().get(i).getVolumeInfo().getPublishername()
                        +" "+result.getBooks().get(i).getVolumeInfo().getPublishedDate()+"\n");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error);
            }
        });
        RequestQueue mRequestQueue= Volley.newRequestQueue(this);
        mRequestQueue.add(gsonRequest);
    }


}
