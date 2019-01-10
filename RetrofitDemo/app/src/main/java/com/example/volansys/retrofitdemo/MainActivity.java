package com.example.volansys.retrofitdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private List<User> mListUser;
    private User user;
    private TextView mtxtViewRes;
    private EditText mEditTxtName,mEditTxtPass,mEditTxtEmail;

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
        EditText editTxtDeleteName=findViewById(R.id.edittextnamedelete);
        String name=editTxtDeleteName.getText().toString();
        NetworkUtility.getNetworkUtility().jsonHolderApi().deleteUser(name).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(MainActivity.this, ""+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void makeCall() {
        NetworkUtility networkUtility = NetworkUtility.getNetworkUtility();

        networkUtility.jsonHolderApi().getUserwithId().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                mListUser = response.body();
                for (int i = 0; i < mListUser.size(); i++) {
                    mtxtViewRes.append("Id:"+mListUser.get(i).get_id()+"\n");
                    mtxtViewRes.append("Name:"+mListUser.get(i).getName()+"\n");
                    mtxtViewRes.append("Email:"+mListUser.get(i).getEmail()+"\n");
                    mtxtViewRes.append("Password:"+mListUser.get(i).getPassword()+"\n");
                }
                Log.e("error", "" + response.code());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });



    }



    //todo here
    public void submitDataToApi() {
        String name=mEditTxtName.getText().toString();
        String email=mEditTxtEmail.getText().toString();
        String pass=mEditTxtPass.getText().toString();
        User user=new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(pass);
       NetworkUtility.getNetworkUtility().jsonHolderApi().postUser(user).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Response:"+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
       mtxtViewRes.setText("");
        makeCall();
    }
    public void updateDatatoAPI() {
        EditText mEditTxtName=findViewById(R.id.edittextNameupdate);
        EditText mEditTxtEmail=findViewById(R.id.edittextEmailupdate);
        EditText mEditTxtPass=findViewById(R.id.edittextPassupdate);
        String name=mEditTxtName.getText().toString();
        String email=mEditTxtEmail.getText().toString();
        String pass=mEditTxtPass.getText().toString();
        NetworkUtility.getNetworkUtility().jsonHolderApi().updateUser(name,email,pass).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(getApplicationContext(), "Response:"+response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
        mtxtViewRes.setText("");
        makeCall();
    }
}
