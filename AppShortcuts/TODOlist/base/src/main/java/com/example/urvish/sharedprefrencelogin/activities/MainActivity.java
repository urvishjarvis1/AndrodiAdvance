package com.example.urvish.sharedprefrencelogin.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.urvish.sharedprefrencelogin.fragments.FragmentCommunicator;
import com.example.urvish.sharedprefrencelogin.fragments.LoginFragment;
import com.example.urvish.sharedprefrencelogin.fragments.PinLoginFragment;
import com.example.urvish.sharedprefrencelogin.base.R;

import java.io.FileOutputStream;
import java.util.Arrays;

/**
 * Main Activity
 * 2 fragments - Login,PinLogin.
 * Sharedprefrence will holds users data.
 *
 */
public class MainActivity extends AppCompatActivity implements FragmentCommunicator {
    @NonNull private final String UNAME_KEY = "uname";
    @NonNull private final String PASS_KEY = "pass";
    @NonNull private final String PIN_KEY = "pin";
    @NonNull private final String mpref = "com.example.urvish.sharedprefrencelogin";
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private LoginFragment mLoginFragment;
    private PinLoginFragment mPinLoginFragment;
    private SharedPreferences mSharedPreferences;
    private Intent mIntent;
    private SharedPreferences.Editor prefrenceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fileInsert();
        //sharedprefrence for storing user's data
        checkUser();
        createDynamicShortcut(this);
        }

    private void createDynamicShortcut(MainActivity mainActivity) {
        //Dynamic Shortcut code
        ShortcutManager shortcutManager=getSystemService(ShortcutManager.class);
        ShortcutInfo shortcut=new ShortcutInfo.Builder(this,"MainActivity")
                .setShortLabel("Login")
                .setLongLabel("Login to get List")
                .setIcon(Icon.createWithResource(this,R.drawable.ic_launcher_foreground))
                .setIntent(new Intent(Intent.ACTION_MAIN, Uri.EMPTY,this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)).build();
        try{
            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
        }catch (NullPointerException e){
            Log.e("Nullpointer", "createDynamicShortcut: "+e.getLocalizedMessage() );
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkUser();
    }

    /**
     * overriden method for fragment communication
     *
     * @param uname=Username from fragment
     * @param pass=password from fragment
     * @param pin=pin from fragment
     */

    @Override
    public void sendData(String uname, String pass,int pin) {
        prefrenceEdit.putString(UNAME_KEY, uname);
        prefrenceEdit.putString(PASS_KEY, pass);
        prefrenceEdit.putInt(PIN_KEY, pin);
        prefrenceEdit.apply();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mPinLoginFragment = new PinLoginFragment();
        mFragmentTransaction.replace(R.id.frag_login, mPinLoginFragment);
        mFragmentTransaction.commit();

    }

    /**
     * overridden method for fragment communication
     * @param PIN=pin from fragment
     */

    @Override
    public void sendData(int PIN) {

        if (PIN == mSharedPreferences.getInt(PIN_KEY,0)) {
            mIntent=new Intent(this, TodoListActivity.class);
            startActivity(mIntent);
        } else {
            Toast.makeText(this, "Incorrect PIN", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * file insertion method
     */
    private void fileInsert(){
        String filename = "News";
        String string = getString(R.string.news);
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void checkUser(){
        mSharedPreferences = getSharedPreferences(mpref, MODE_PRIVATE);
        mFragmentManager = getSupportFragmentManager();
        prefrenceEdit=mSharedPreferences.edit();

        if (!mSharedPreferences.contains(UNAME_KEY) && !mSharedPreferences.contains(PASS_KEY)) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mLoginFragment = new LoginFragment();
            mFragmentTransaction.replace(R.id.frag_login, mLoginFragment);
            mFragmentTransaction.commit();
        } else {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mPinLoginFragment = new PinLoginFragment();
            mFragmentTransaction.replace(R.id.frag_login, mPinLoginFragment);
            mFragmentTransaction.commit();
        }
    }



}
