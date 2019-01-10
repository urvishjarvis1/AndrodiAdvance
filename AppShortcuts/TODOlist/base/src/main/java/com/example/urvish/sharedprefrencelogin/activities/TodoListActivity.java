package com.example.urvish.sharedprefrencelogin.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.urvish.sharedprefrencelogin.fragments.FragmentCommunicator;
import com.example.urvish.sharedprefrencelogin.data.ListHelper;
import com.example.urvish.sharedprefrencelogin.data.ListViewAdapter;
import com.example.urvish.sharedprefrencelogin.base.R;
import com.example.urvish.sharedprefrencelogin.data.TodoItem;

import java.util.ArrayList;

/**
 * ListView
 * prefrencedata
 * fab button for adding data
 */

public class TodoListActivity extends AppCompatActivity {
    public static final int TEXT_REQUEST = 1;
    private ListHelper mListHelper;
    private TodoItem mTodoItem;
    private Intent mIntent;
    public ListViewAdapter listViewAdapter;
    private ListView listview;
    private ArrayList<TodoItem> todoItems;
    @NonNull
    private final String UNAME_KEY = "uname";
    private SharedPreferences mSharedPreferences;
    @NonNull
    private final String mpref = "com.example.urvish.sharedprefrencelogin";
    @NonNull
    public static final String ID_DETAIL="com.example.urvish.sharedprefrencelogin";
    private static final String TAG=TodoListActivity.class.getSimpleName();
    private SharedPreferences.Editor prefrenceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        mSharedPreferences = getSharedPreferences(mpref, MODE_PRIVATE);
        prefrenceEdit=mSharedPreferences.edit();
        this.setTitle(mSharedPreferences.getString(UNAME_KEY,"Dummy User"));
        mListHelper = new ListHelper(this);
        todoItems = mListHelper.query();
        listViewAdapter = new ListViewAdapter(this, todoItems);
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(listViewAdapter);
        setOnclick();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivityForResult(mIntent, TEXT_REQUEST);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        todoItems = mListHelper.query();
        listViewAdapter = new ListViewAdapter(this,todoItems);
        listview.setAdapter(listViewAdapter);
    }

    /**
     *listview onclick method
     */

    private void setOnclick(){
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, @NonNull View view, int pos, long l) {
                mIntent=new Intent(getApplication(),DetailActivity.class);
                TextView textView=view.findViewById(R.id.txt_title);
                Log.d("Count",textView.getText().toString());
                int id=Integer.valueOf(textView.getText().toString());
                mIntent.putExtra(ID_DETAIL,id);
                startActivityForResult(mIntent,TEXT_REQUEST);
            }
        });
    }

    /**
     * when activity gets result from another activity
     * @param requestCode= TEXT_CODE having value 1
     * @param resultCode= RESULT_OK return from callee activity
     * @param data=intent
     */
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    /**
     * inflate menu for toolbar
     * @param menu= Menu Object
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * on item selected in menu
     * @param item = MenuItem object
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            prefrenceEdit.clear();
            prefrenceEdit.commit();
            //Finish the Activity
            finish();
            return true;
        }else if(id==R.id.action_file){
            mIntent=new Intent(this,NewsActivity.class);
            startActivity(mIntent);

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * for exiting activity when user presses back button
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
    }
}




