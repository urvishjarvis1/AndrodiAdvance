package com.example.urvish.butterknifedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SimpleAdapter extends BaseAdapter {
    private static final int[] ITEM=new int[]{1,2,3,4,5,6,7,8,8,9,0,0,0,0,00};
    private LayoutInflater mLayoutInflater;

    public SimpleAdapter(Context context) {
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ITEM.length;
    }

    @Override
    public Object getItem(int position) {
        return ITEM[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView!=null){
            viewHolder=(ViewHolder) convertView.getTag();
        }else{
            convertView=mLayoutInflater.inflate(R.layout.simple_list,parent,false);
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        int item=(int)getItem(position);
        viewHolder.item.setText("Item:"+item);
        return convertView;
    }
    public static class ViewHolder{
        @BindView(R.id.item)TextView item;

        public ViewHolder(View view) {
            ButterKnife.bind(this,view);
        }
    }
}
