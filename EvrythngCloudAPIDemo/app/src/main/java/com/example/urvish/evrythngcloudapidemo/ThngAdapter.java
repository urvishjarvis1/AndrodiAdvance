package com.example.urvish.evrythngcloudapidemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evrythng.thng.resource.model.store.Product;
import com.evrythng.thng.resource.model.store.Thng;

import java.util.List;

public class ThngAdapter extends BaseAdapter {
    private List<Thng> thngs;
    private Context context;
    private TextView mTextViewTitle,mTextViewDesc;
    public ThngAdapter(List<Thng> thngs,Context context) {
        this.thngs = thngs;
        this.context=context;
    }

    @Override
    public int getCount() {
        return thngs.size();
    }

    @Override
    public Thng getItem(int position) {
        return thngs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;/*Long.parseLong(thngs.get(position).getId());*/
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=layoutInflater.inflate(R.layout.simple_list_view,null,false);
        mTextViewTitle=rowView.findViewById(R.id.title);
        mTextViewDesc=rowView.findViewById(R.id.productdiscrption);
        mTextViewDesc.setText(thngs.get(position).getDescription());
        mTextViewTitle.setText(thngs.get(position).getName());
        Log.d(MainActivity.TAG, "getView: "+position);
        return rowView;
    }
}
