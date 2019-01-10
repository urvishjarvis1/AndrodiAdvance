package com.example.urvish.evrythngcloudapidemo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evrythng.thng.resource.model.store.Product;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
    private List<Product> products;
    private Context context;
    private TextView mTextViewTitle,mTextViewDesc;
    public ProductAdapter(List<Product> products,Context context) {
        this.products = products;
        this.context=context;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;/*Long.parseLong(products.get(position).getId());*/
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=layoutInflater.inflate(R.layout.simple_list_view,null,false);
        mTextViewTitle=rowView.findViewById(R.id.title);
        mTextViewDesc=rowView.findViewById(R.id.productdiscrption);
        mTextViewDesc.setText(products.get(position).getDescription());
        mTextViewTitle.setText(products.get(position).getName());
        Log.d(MainActivity.TAG, "getView: "+position);
        return rowView;
    }
}
