package com.example.urvish.firebaseauthenticationdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int FIREBASE_SIGN_IN=1;
    private static final String TAG="code";
    private FirebaseUser user;

    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build()
           );

    private Button mBtnLogout;
    private Button mbtnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnLogout=findViewById(R.id.btnlogout);
        mbtnLogin=findViewById(R.id.btnsignin);
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),FIREBASE_SIGN_IN);
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthUI.getInstance().signOut(MainActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signout completed", Toast.LENGTH_SHORT).show();
                        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),FIREBASE_SIGN_IN);
                        user=null;
                    }
                });
            }
        });
        mbtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user==null) {
                    startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), FIREBASE_SIGN_IN);
                }else{
                    Toast.makeText(MainActivity.this, "Already Singed in", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FIREBASE_SIGN_IN){
            IdpResponse response=IdpResponse.fromResultIntent(data);
            if(resultCode==RESULT_OK){
                 user= FirebaseAuth.getInstance().getCurrentUser();
            }else{
                Log.e(TAG, "onActivityResult: error code"+resultCode);
                Toast.makeText(this, "Something went wrong, try again for sign-in!"+resultCode, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
