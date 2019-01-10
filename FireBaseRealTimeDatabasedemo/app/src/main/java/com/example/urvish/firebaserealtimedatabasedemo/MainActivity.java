package com.example.urvish.firebaserealtimedatabasedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG =MainActivity.class.getName();
    private EditText mEditTextTitle,mEditTextDesc;
    private Button mBtnSubmit;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private String userId;
    private TextView mTextViewData,mTextViewAllData;
    private List<TodoItem> todoItems;
    private FirebaseAuth mAuth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextDesc=findViewById(R.id.edittxtdescription);
        mEditTextTitle=findViewById(R.id.edittxttitle);
        mBtnSubmit=findViewById(R.id.btnsubmit);
        mTextViewData=findViewById(R.id.data);
        mTextViewAllData=findViewById(R.id.dataall);
        todoItems=new ArrayList<TodoItem>();
        mAuth=FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mDatabaseReference=mFirebaseDatabase.getReference("todoitem");
        mFirebaseDatabase.getReference("app_title").setValue("DEMO");
        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=mEditTextTitle.getText().toString();
                String desc=mEditTextDesc.getText().toString();
                if(TextUtils.isEmpty(userId)){
                    createItem(title,desc);
                }else{
                    updateItem(title,desc);
                }
            }
        });
        toggleButton();
        getAll();
    }

    private void toggleButton() {
        if (TextUtils.isEmpty(userId)){
            mBtnSubmit.setText(R.string.submit);
        }else{
            mBtnSubmit.setText(R.string.update);
        }
    }

    private void createItem(String title, String desc) {
        if(TextUtils.isEmpty(userId)){
            userId=mDatabaseReference.push().getKey();
        }
        TodoItem todoItem=new TodoItem();
        todoItem.setTitle(title);
        todoItem.setDescription(desc);
        mDatabaseReference.child(userId).setValue(todoItem);
        addListener();
    }

    private void addListener() {
        mDatabaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TodoItem todoItem=dataSnapshot.getValue(TodoItem.class);
                if(todoItem==null){
                    Log.e(TAG, "onDataChange: data empty");
                    return;
                }
                Log.d(TAG, "onDataChange: data:"+todoItem.getTitle()+"  "+todoItem.getDescription());
                mTextViewData.setText(todoItem.getTitle()+"\n" +todoItem.getDescription());
                mEditTextDesc.setText("");
                mEditTextTitle.setText("");
                toggleButton();
                getAll();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: Error"+databaseError.getDetails());
            }
        });
    }

    private void updateItem(String title, String desc) {
        if(!TextUtils.isEmpty(title)){
            mDatabaseReference.child(userId).child("title").setValue(title);
        }
        if (!TextUtils.isEmpty(desc)){
            mDatabaseReference.child(userId).child("description").setValue(desc);
        }
    }
    private void getAll(){
        final DatabaseReference databaseReference=mFirebaseDatabase.getReference().child("todoitem");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                collectData((Map<String,Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void collectData(Map<String, Object> children) {
        int i=0;
        for(Map.Entry<String,Object> entry:children.entrySet()){
            Log.e(TAG, "collectData: data:"+i+":"+entry.getValue() );i++;
            Map todoitem=(Map) entry.getValue();
            todoItems.add(new TodoItem((String)todoitem.get("title"),(String)todoitem.get("description")));
        }
        for (i=0;i<todoItems.size();i++)
            mTextViewAllData.append("\n"+todoItems.get(i).getTitle()+"\t"+todoItems.get(i).getDescription());
    }
}
