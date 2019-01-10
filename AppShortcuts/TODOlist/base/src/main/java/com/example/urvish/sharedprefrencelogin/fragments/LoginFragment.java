package com.example.urvish.sharedprefrencelogin.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.urvish.sharedprefrencelogin.base.R;

/**
 * Login Fragment for getting user data and pass it to MainActivity.
 *
 */
public class LoginFragment extends Fragment implements View.OnClickListener{
    //interface
    @Nullable
    private FragmentCommunicator mCommunicator;
    private Button mButtonLogin;
    private EditText mEditTextUname,mEditTextPass,mEditTextPin;
    public LoginFragment() {
        // Required empty public constructor
    }




    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator=(FragmentCommunicator) getActivity();
        mEditTextUname=(EditText)getActivity().findViewById(R.id.edittxt_uname);
        mEditTextPass=(EditText)getActivity().findViewById(R.id.edittxt_pass);
        mEditTextPin=(EditText)getActivity().findViewById(R.id.edittxt_pin);
        mButtonLogin=(Button) getActivity().findViewById(R.id.btn_login);
        mButtonLogin.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }




    @Override
    public void onClick(View view) {
        String uname,pass;
        int pin;
        if(mEditTextUname.getText().toString().isEmpty()){
            mEditTextUname.setError("please enter username");
        }
        if(mEditTextPass.getText().toString().isEmpty()){
            mEditTextPass.setError("please enter password");
        }
        if(mEditTextPin.getText().toString().isEmpty()){
            mEditTextPin.setError("please enter PIN");
        }


        if(!mEditTextUname.getText().toString().isEmpty() && !mEditTextPass.getText().toString().isEmpty()&&
                                !mEditTextPin.getText().toString().isEmpty()){
            uname=mEditTextUname.getText().toString();
            pass=mEditTextPass.getText().toString();
            pin=Integer.parseInt(mEditTextPin.getText().toString());
            mCommunicator.sendData(uname, pass,pin);
        }else{
            if(mEditTextUname.getText().toString().isEmpty())
                mEditTextUname.setError("please enter username");
            if(mEditTextPass.getText().toString().isEmpty())
                mEditTextPass.setError("please enter password");
            if(mEditTextPin.getText().toString().isEmpty())
                mEditTextPin.setError("please enter PIN");
        }
    }
}
