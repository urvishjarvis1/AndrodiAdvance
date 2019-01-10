package com.example.urvish.sharedprefrencelogin.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.urvish.sharedprefrencelogin.data.ListHelper;
import com.example.urvish.sharedprefrencelogin.base.R;

import java.util.Arrays;

/**
 * Detail activity
 * Detail view
 * toolbar
 */
public class DetailActivity extends AppCompatActivity {
    private ListHelper mListHelper;
    private Intent mIntent;
    private int mId;
    private SharedPreferences.Editor prefrenceEdit;
    private SharedPreferences mSharedPreferences;
    private Button mBtnUpdate,mBtnUpdateData;
    private EditText mEditxtUpdate;
    @NonNull
    private final String mpref = "com.example.urvish.sharedprefrencelogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mBtnUpdate=findViewById(R.id.btn_update);
        mBtnUpdateData=findViewById(R.id.btn_updatedata);
        mEditxtUpdate=findViewById(R.id.edittxt_update);
        mSharedPreferences = getSharedPreferences(mpref, MODE_PRIVATE);
        prefrenceEdit=mSharedPreferences.edit();
        this.setTitle("Details");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TextView mTextViewTitle=(TextView) findViewById(R.id.detail_title);

        mListHelper=new ListHelper(this);
        mIntent= getIntent();
        mId=mIntent.getIntExtra(TodoListActivity.ID_DETAIL,-1);
        if(mId!=-1) {
            String titleText=mListHelper.getdata(mId);
            mTextViewTitle.setText(titleText);
        }else{

        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent= getIntent();
                mId=mIntent.getIntExtra(TodoListActivity.ID_DETAIL,-1);
                Log.d("count",Integer.toString(mId));
                if(mId!=-1) {
                    int id=mListHelper.delete(mId);
                    Toast.makeText(DetailActivity.this, "Deleted "+id+" no. row "+mId, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,mIntent);
                    finish();
                }else{
                    Toast.makeText(DetailActivity.this, "can't be deleted!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //update with view
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEditxtUpdate.setVisibility(View.VISIBLE);
                mEditxtUpdate.setText(mTextViewTitle.getText());
                mBtnUpdateData.setVisibility(View.VISIBLE);
                mBtnUpdate.setVisibility(View.GONE);
                mTextViewTitle.setVisibility(View.GONE);

            }
        });
        //update database
        mBtnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent= getIntent();
                mId=mIntent.getIntExtra(TodoListActivity.ID_DETAIL,-1);
                Log.d("count",Integer.toString(mId));
                if(mId!=-1) {
                    int id=mListHelper.update(mEditxtUpdate.getText().toString(),mId);
                    Toast.makeText(DetailActivity.this, "Updated "+id+" no. row "+mId, Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK,mIntent);
                    finish();
                }else{
                    Toast.makeText(DetailActivity.this, "can't be Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        createDynamicShortcut();
    }
    private void createDynamicShortcut() {
        //Dynamic Shortcut code
        ShortcutManager shortcutManager=getSystemService(ShortcutManager.class);
        ShortcutInfo shortcut=new ShortcutInfo.Builder(this,"DetailActivity")
                .setShortLabel("Detail")
                .setLongLabel("Detail of List")
                .setIcon(Icon.createWithResource(this,R.drawable.ic_logout_24dp))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.EMPTY,
                        this,DetailActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)).build();
        try{
            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
        }catch (NullPointerException e){
            Log.e("Nullpointer", "createDynamicShortcut: "+e.getLocalizedMessage() );
        }
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
            mIntent=new Intent(this,MainActivity.class);
            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mIntent);
            finish();
            return true;
        }else if(id==R.id.action_file){
            mIntent=new Intent(this,NewsActivity.class);
            startActivity(mIntent);

        }

        return super.onOptionsItemSelected(item);
    }
    

}
