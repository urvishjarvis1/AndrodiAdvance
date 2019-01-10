package com.example.urvish.evrythngcloudapidemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.evrythng.java.wrapper.ApiManager;
import com.evrythng.thng.resource.model.store.Thng;

import java.util.Arrays;

public class AddActivity extends AppCompatActivity {
    private EditText mEditextColor,mEditextModel,mEditextDesc;
    private Button mBtnAdd;
    private ApiManager manager;
    private Thng thng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        thng=new Thng();
        manager=new ApiManager(Contract.getApiKey());
    }

    private void initView() {
        mEditextDesc=findViewById(R.id.edittextdesc);
        mEditextColor=findViewById(R.id.edittextcolor);
        mEditextModel=findViewById(R.id.edittextModel);
        mBtnAdd=findViewById(R.id.addItems);
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mEditextDesc.getText().toString().isEmpty()){
                    mEditextDesc.setError("Enter Desc");
                    mEditextDesc.requestFocus();
                    return;
                }

                if(mEditextColor.getText().toString().isEmpty()){
                    mEditextColor.setError("Enter color");
                    mEditextColor.requestFocus();
                    return;
                }if(mEditextModel.getText().toString().isEmpty()){
                    mEditextModel.setError("Enter model");
                    mEditextModel.requestFocus();
                    return;
                }
                thng.setName("item");
                thng.setProduct(getIntent().getStringExtra("productId"));
                thng.setDescription(mEditextDesc.getText().toString());
                thng.addCustomFields("color", mEditextColor.getText().toString());
                thng.addCustomFields("license_plate", mEditextModel.getText().toString());
                thng.setTags(Arrays.asList("yellow", "NY", "US"));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                      final Thng thng1=manager.thngService().thngCreator(thng).execute();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(thng1!=null){
                                    Toast.makeText(AddActivity.this, "Item posted", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                    }
                }).start();

                mEditextDesc.setText("");
                mEditextColor.setText("");
                mEditextModel.setText("");


            }
        });

    }
}
