package com.example.urvish.butterknifedemo;

import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.title)TextView mTextViewTitle;
    @BindView(R.id.subtitle)TextView mTextViewSubtitle;
    @BindView(R.id.hello)Button mBtnHello;
    @BindView(R.id.footer)TextView mTextViewFooter;
    @BindView(R.id.list_of_things)ListView mListViewThings;
    @BindString(R.string.urvish) String urvish="Urvish";
    @OnClick(R.id.hello)void  sayHello(){
        Toast.makeText(this, "Hello from ButterKnife method", Toast.LENGTH_SHORT).show();
    }
    private SimpleAdapter simpleAdapter;
    @OnItemClick(R.id.list_of_things)
    void onItemClick(int position){
        Toast.makeText(this, "Item:"+simpleAdapter.getItem(position)+" "+simpleAdapter.getItemId(position), Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        simpleAdapter=new SimpleAdapter(this);
        mTextViewTitle.setText("Butter Knife Demo!");
        mTextViewSubtitle.setText("binding using butter knife");
        mTextViewFooter.setText(urvish);
        mBtnHello.setText("Hello");
        mListViewThings.setAdapter(simpleAdapter);

    }
}
