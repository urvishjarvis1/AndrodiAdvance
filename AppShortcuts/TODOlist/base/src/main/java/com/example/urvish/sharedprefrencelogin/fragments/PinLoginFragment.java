package com.example.urvish.sharedprefrencelogin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.urvish.sharedprefrencelogin.base.R;


public class PinLoginFragment extends Fragment implements View.OnClickListener {
    @Nullable
    private FragmentCommunicator mCommunicator;
    private Button mButtonLoginPin;
    private EditText mEditTextPIN;
    public PinLoginFragment(){}
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCommunicator=(FragmentCommunicator) getActivity();
        mEditTextPIN=(EditText)getActivity().findViewById(R.id.edittxt_pin);

        mButtonLoginPin=(Button) getActivity().findViewById(R.id.btn_pinlogin);
        mButtonLoginPin.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pin_login, container, false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    @Override
    public void onClick(View view) {
        int PIN;

        if(!(mEditTextPIN.getText().toString().isEmpty())) {
            PIN=Integer.parseInt(mEditTextPIN.getText().toString());
            mCommunicator.sendData(PIN);
        }else{
            mEditTextPIN.setError("please enter PIN");
        }
    }
}
