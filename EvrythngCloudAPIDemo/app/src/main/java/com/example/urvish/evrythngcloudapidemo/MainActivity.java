package com.example.urvish.evrythngcloudapidemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evrythng.java.wrapper.ApiManager;
import com.evrythng.thng.resource.model.store.Product;
import com.evrythng.thng.resource.model.store.Thng;

import com.example.urvish.evrythngcloudapidemo.Contract;

import org.pcollections.PVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ApiManager mApiManager;
    private Contract mContract;
    private Product mProduct;
    private List<Product> products;
    private Thng mThng;
    private ListView listView;
    private ProductAdapter productAdapter;
    private Map<String, Object> mCustomFileds;
    public static String TAG = "evrythng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContract = Contract.getsContract();
        mApiManager = new ApiManager(Contract.getApiKey());
        products = new ArrayList<>();
        initViews();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<PVector<Product>> cloudProducts=mApiManager.productService().iterator().execute();
                while (cloudProducts.hasNext()){
                    PVector<Product> page=cloudProducts.next();
                    products.addAll(page);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            productAdapter.notifyDataSetChanged();
                        }
                    });

                }

            }
        }).start();






    }

    private void initViews() {
        listView = findViewById(R.id.listView);
        productAdapter = new ProductAdapter(products, this);
        listView.setAdapter(productAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(MainActivity.this,ProductActivity.class);//todo add activity
                String productId=productAdapter.getItem(position).getId();
                intent.putExtra("productId",productId);
                startActivity(intent);
            }
        });
    }


}
