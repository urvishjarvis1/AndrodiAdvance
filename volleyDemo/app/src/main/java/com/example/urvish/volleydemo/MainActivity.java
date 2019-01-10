package com.example.urvish.volleydemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<User> mListUser;
    private User user;
    private TextView mtxtViewRes;
    private EditText mEditTxtName,mEditTxtPass,mEditTxtEmail;
    private static final String URL="http://192.168.2.158:3000/public/";
    private VolleyStringRequest volleyStringRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListUser = new ArrayList<>();
        user = new User();
        mtxtViewRes=findViewById(R.id.ans);
        mEditTxtEmail=findViewById(R.id.edittextEmail);
        mEditTxtName=findViewById(R.id.edittextName);
        mEditTxtPass=findViewById(R.id.edittextPass);
        volleyStringRequest=new VolleyStringRequest(this);
        makeCall();

        Button btnSubmit = findViewById(R.id.btnsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitDataToApi();
            }
        });
        Button btnUpdate=findViewById(R.id.btnupdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDatatoAPI();
            }
        });
        Button btnDelete=findViewById(R.id.btndelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDataFromAPI();
            }
        });
    }

    private void deleteDataFromAPI() {
        mEditTxtName=findViewById(R.id.edittextnamedelete);
        String url=URL+"data";
        User user=new User();
        if(mEditTxtName.getText().toString().isEmpty()){
            mEditTxtName.setError("please enter name");
        }else {
            user.setName(mEditTxtName.getText().toString());
        }
        volleyStringRequest.deleteDatatoApi(new VolleyStringRequest.onDataAvailable() {
            @Override
            public void onData(String Data) {
                Toast.makeText(MainActivity.this, "delete with parameter not working with volley", Toast.LENGTH_SHORT).show();
                makeCall();
            }
        },url,user);
    }

    private void updateDatatoAPI() {
        String url=URL+"data";
        User user=new User();
        mEditTxtEmail=findViewById(R.id.edittextEmailupdate);
        mEditTxtName=findViewById(R.id.edittextNameupdate);
        mEditTxtPass=findViewById(R.id.edittextPassupdate);

        if(mEditTxtName.getText().toString().isEmpty()){
            mEditTxtName.setError("please enter name");
        }else{
            user.setName(mEditTxtName.getText().toString());
        }
        if(mEditTxtEmail.getText().toString().isEmpty()){
            mEditTxtEmail.setError("Please enter password");
        }else{
            user.setEmail(mEditTxtEmail.getText().toString());
        }
        if(mEditTxtPass.getText().toString().isEmpty()){
            mEditTxtPass.setError("please enter password");
        }else{
            user.setPassword(mEditTxtPass.getText().toString());
        }
        volleyStringRequest.updateDatatoApi(new VolleyStringRequest.onDataAvailable() {
            @Override
            public void onData(String Data) {
                Toast.makeText(MainActivity.this, "Data updated to the API", Toast.LENGTH_SHORT).show();
                makeCall();
            }
        },url,user);
    }

    private void submitDataToApi() {
        String url=URL+"data";
        User user=new User();
        mEditTxtEmail=findViewById(R.id.edittextEmail);
        mEditTxtName=findViewById(R.id.edittextName);
        mEditTxtPass=findViewById(R.id.edittextPass);
        if(mEditTxtName.getText().toString().isEmpty()){
            mEditTxtName.setError("please enter name");
        }else{
            user.setName(mEditTxtName.getText().toString());
        }
        if(mEditTxtEmail.getText().toString().isEmpty()){
            mEditTxtEmail.setError("Please enter password");
        }else{
            user.setEmail(mEditTxtEmail.getText().toString());
        }
        if(mEditTxtPass.getText().toString().isEmpty()){
            mEditTxtPass.setError("please enter password");
        }else{
            user.setPassword(mEditTxtPass.getText().toString());
        }
        volleyStringRequest.postDatatoApi(new VolleyStringRequest.onDataAvailable() {
            @Override
            public void onData(String Data) {
                Toast.makeText(MainActivity.this, "Data successfully added!", Toast.LENGTH_LONG).show();
                makeCall();
            }
        },url,user);

    }

    private void makeCall() {
        String url=URL+"data";
        mtxtViewRes.setText("");
        volleyStringRequest.getDataFromApi(new VolleyStringRequest.onDataAvailable() {
            @Override
            public void onData(String Data) {
                try{
                JSONArray jsonArray=new JSONArray(Data);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject user=jsonArray.getJSONObject(i);
                    User user1=new User();
                    user1.setName(user.getString("name"));
                    user1.set_id(user.getString("_id"));
                    user1.setEmail(user.getString("email"));
                    if(!user1.getName().isEmpty()&&!user1.getEmail().isEmpty()){
                        mtxtViewRes.append("\nid:"+user1.get_id()+"\nName:"+user1.getName()+"\nEmail:"+user1.getEmail()+"\n");
                    }else{
                        mtxtViewRes.append("error in response"+i);
                    }

                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },url);

    }

    public void openGsonActivity(View view) {
        Intent intent=new Intent(this,GsonActivity.class);
        startActivity(intent);
    }
}
