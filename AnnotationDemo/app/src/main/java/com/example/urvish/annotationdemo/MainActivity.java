package com.example.urvish.annotationdemo;

import android.support.annotation.CallSuper;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Keep;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.annotation.StringRes;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewTitle, mTextViewName;
    private static final int MALE=0;
    private static final int FEMALE=1;
    private static final int OTHER=2;
    private static final String TAG="OnReport";
    @NonNull
    private final TextView[] containers = new TextView[2];
    private ImageView mImageProfile;
    private EditText mEditTextName, mEditTextPass;
    private Button mButtonLogin;
    private String name;
    private Child child;
    private RadioGroup mRadiogroup;
    @NonNull
    private Boolean isLoggedin = false;
    //typedef annotation
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({MALE,FEMALE,OTHER})
    @interface Gender{}
    //end of typedef annotation
    @Override
    @Status(priority=Status.Priority.HIGH,author = "Urvish",completion = 100)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditTextName = findViewById(R.id.edittextname);
        mEditTextPass = findViewById(R.id.edittextpass);
        mTextViewName = findViewById(R.id.loginname);
        mButtonLogin = findViewById(R.id.buttonlogin);
        mImageProfile = findViewById(R.id.imageprofile);
        mTextViewTitle = findViewById(R.id.title);
        containers[0] = findViewById(R.id.nameContainer);
        containers[1] = findViewById(R.id.passwordContainer);
        mRadiogroup=findViewById(R.id.radiogroup);
        child = new Child();
        mRadiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String gender=null;
                switch (checkedId){

                    case R.id.male:
                        gender=getGender(MALE);
                        break;
                        //to genrate error
                        //getGender(0);
                        //to genrate error for @VisibleForTesting
                        //gender=getGenderTest(MALE);
                    case R.id.female:
                        gender=getGender(FEMALE);
                        break;
                    case R.id.other:
                        gender=getGender(OTHER);
                        break;
                }
                Toast.makeText(MainActivity.this, "Gender:"+gender, Toast.LENGTH_SHORT).show();

            }
        });

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        computeSomething();
                    }
                }).start();
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLoggedin) {
                    name = getStringFromEdittext();
                    isLoggedin = true;
                    showTitle(name);
                    setImage(R.drawable.ic_launcher_background);
                    compeletLogin(containers);
                    Toast.makeText(MainActivity.this, "Message:" + child.toastMessage("abc"), Toast.LENGTH_SHORT).show();
                } else {
                    showTitle(R.string.app_name);
                    compeletLogout(containers);
                    isLoggedin = false;

                }
            }
        });
        getReport();
    }
    @WorkerThread
    private void computeSomething() {
        final float a=1,b=100,c;
        c=a/b;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, "Result of Background Thread computation"+c, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getReport() {
        Class parent=Parent.class;
        Log.d(TAG, "getReport: Length:"+parent.getMethods().length);
        for(Method method : parent.getMethods()){
            Status status=method.getAnnotation(Status.class);
            if(status!=null){
                Log.d(TAG, "getReport: \nMethod:"+method.getName()+"\nAuthor:"+status.author()+"\nPriority:"+status.priority()+"\nCompletion Status:"+status.completion());
            }
        }
    }

    @Status(priority = Status.Priority.LOW,author = "Urivsh",completion = 0)
    void dosomething(@Size(2)int data){
        //todo
    }
    @Status(priority = Status.Priority.LOW,author = "Urivsh",completion = 70)
    @UiThread
    private void compeletLogout(@Size(max = 2) TextView[] containers) {
        mEditTextName.setVisibility(View.VISIBLE);
        mEditTextPass.setVisibility(View.VISIBLE);
        mRadiogroup.setVisibility(View.VISIBLE);
        mButtonLogin.animate().translationY(0);
        mButtonLogin.setText(R.string.login);
        mImageProfile.setVisibility(View.GONE);
        mTextViewName.setVisibility(View.GONE);
        mTextViewName.setText(name);
        for (TextView tv : containers) {
            tv.setVisibility(View.VISIBLE);
        }
    }
    @Status(priority = Status.Priority.LOW,author = "Urivsh",completion = 50)
    @UiThread
    private void showTitle(@StringRes int app_name) {
        mTextViewTitle.setText(app_name);
    }
    @Status(priority = Status.Priority.LOW,author = "Urivsh",completion = 60)
    @MainThread
    private void compeletLogin(@Size(max=2) TextView[] containers) {
        mEditTextName.setVisibility(View.GONE);
        mEditTextPass.setVisibility(View.GONE);
        mRadiogroup.setVisibility(View.GONE);
        mButtonLogin.animate().translationY(500);
        mButtonLogin.setText(R.string.logout);
        mImageProfile.setVisibility(View.VISIBLE);
        mTextViewName.setVisibility(View.VISIBLE);
        mTextViewName.setText(name);
        for (TextView tv : containers) {
            tv.setVisibility(View.GONE);
        }
    }

    @UiThread
    private void setImage(@DrawableRes int id) {
        if (isLoggedin) mImageProfile.setImageResource(id);
    }

    @UiThread
    private void showTitle(@NonNull String name) {
        if (isLoggedin) mTextViewTitle.setText(name);
    }

    @MainThread
    private @NonNull
    String getStringFromEdittext() {
        return mEditTextName.getText().toString();
    }

    String getGender(@Gender int gender){
        if(gender==0)
            return "male";
        else if(gender==1)
            return "female";
        else
            return "not specified";
    }
    @Keep
    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    String getGenderTest(@Gender int gender){
        if(gender==0)
            return "male";
        else if(gender==1)
            return "female";
        else
            return "not specified";
    }

}