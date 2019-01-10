package com.example.urvish.sharedprefrencelogin.activities;

import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.urvish.sharedprefrencelogin.data.ListHelper;
import com.example.urvish.sharedprefrencelogin.base.R;

import java.util.Arrays;

/**
 * get data from user and set it to database
 */

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener{
    private ListHelper mListHelper;
    private EditText mEditTextAddItem;
    private Button mButtonAdd;

    @NonNull
    public static final String RETURN_REPLY="com.example.urvish.sharedprefrencelogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        this.setTitle("Add Item");
        mListHelper=new ListHelper(this);
        mEditTextAddItem=(EditText)findViewById(R.id.editxt_todoitem);
        mButtonAdd=(Button) findViewById(R.id.btn_additem);
        mButtonAdd.setOnClickListener(this);
        createDynamicShortcut();
    }
    private void createDynamicShortcut() {
        //Dynamic Shortcut code
        ShortcutManager shortcutManager=getSystemService(ShortcutManager.class);
        ShortcutInfo shortcut=new ShortcutInfo.Builder(this,"AddItemActivity")
                .setShortLabel("Add Item")
                .setLongLabel("Add new item to List")
                .setIcon(Icon.createWithResource(this,R.drawable.ic_add_black_24dp))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.EMPTY,this,AddItemActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)).build();
        try{
        shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
        }catch (NullPointerException e){
            Log.e("Nullpointer", "createDynamicShortcut: "+e.getLocalizedMessage() );
        }
    }
    @Override
    public void onClick(View view) {
            String data;
            long id;
            Intent intent=new Intent();
            data=mEditTextAddItem.getText().toString();
            if(data.isEmpty()){
                mEditTextAddItem.setError("EnterData");
                mEditTextAddItem.setFocusable(true);
            }else{

                id=mListHelper.insertData(data);
                intent.putExtra(RETURN_REPLY,id);
                setResult(RESULT_OK,intent);
                finish();
            }


    }

}
