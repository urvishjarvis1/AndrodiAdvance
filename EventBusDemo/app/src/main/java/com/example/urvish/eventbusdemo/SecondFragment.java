package com.example.urvish.eventbusdemo;

import android.content.Context;
import android.database.Observable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;



public class SecondFragment extends Fragment {
    private TextView mTextView;
    private static final String TAG="Main";
    private String data;


    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTextView=getActivity().findViewById(R.id.textview);
        mTextView.setText(data);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(!EventBus.getDefault().isRegistered(this))
        EventBus.getDefault().register(this);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
    public void onMessageEvent(NotifyEvent notifyEvent){
        data=Integer.toString(notifyEvent.mValue);
    }


}
