package com.example.urvish.sharedprefrencelogin.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.urvish.sharedprefrencelogin.base.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class NewsActivity extends AppCompatActivity {
    static final int READ_BLOCK_SIZE = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        TextView textViewNews=(TextView) findViewById(R.id.txt_news);
        try {
            FileInputStream fin = openFileInput("News");
            InputStreamReader InputRead= new InputStreamReader(fin);

            char[] inputBuffer= new char[READ_BLOCK_SIZE];
            String s="";
            int charRead;

            while ((charRead=InputRead.read(inputBuffer))>0) {
                // char to string conversion
                String readstring=String.copyValueOf(inputBuffer,0,charRead);
                s +=readstring;
            }
            InputRead.close();
            textViewNews.setText(s);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        createDynamicShortcut();
    }
    private void createDynamicShortcut() {
        //Dynamic Shortcut code
        ShortcutManager shortcutManager=getSystemService(ShortcutManager.class);
        ShortcutInfo shortcut=new ShortcutInfo.Builder(this,"NewsActivity")
                .setShortLabel("News")
                .setLongLabel("Some Fresh News")
                .setIcon(Icon.createWithResource(this,R.drawable.ic_new_releases_black_24dp))
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.EMPTY,this,NewsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)).build();
        try{
            shortcutManager.addDynamicShortcuts(Arrays.asList(shortcut));
        }catch (NullPointerException e){
            Log.e("Nullpointer", "createDynamicShortcut: "+e.getLocalizedMessage() );
        }
        try{
        if(shortcutManager.isRequestPinShortcutSupported()){

            Intent pinnedShortcut= shortcutManager.createShortcutResultIntent(shortcut);
            PendingIntent pendingIntent=PendingIntent.getBroadcast(this,0,pinnedShortcut,0);

            shortcutManager.requestPinShortcut(shortcut,pendingIntent.getIntentSender());

        }
        }catch (NullPointerException e){
            Log.e("Nullpointer", "createDynamicShortcut: "+e.getLocalizedMessage() );
        }

    }
}
