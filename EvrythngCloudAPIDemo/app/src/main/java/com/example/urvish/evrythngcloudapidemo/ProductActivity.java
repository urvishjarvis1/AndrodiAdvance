package com.example.urvish.evrythngcloudapidemo;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.evrythng.java.wrapper.ApiManager;
import com.evrythng.thng.resource.model.store.Product;
import com.evrythng.thng.resource.model.store.Thng;

import org.pcollections.PVector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private ApiManager mApiManager;
    private List<Thng> thngs;
    private ListView listView;
    private ThngAdapter thngAdapter;
    private FloatingActionButton addThngs;
    String productid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        mApiManager=new ApiManager(Contract.getApiKey());
        thngs=new ArrayList<>();
        Intent intent=getIntent();
        productid=intent.getStringExtra("productId");
        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
        thngs.clear();
        Log.d(MainActivity.TAG, "onResume: "+productid);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Iterator<PVector<Thng>> cloudProducts=mApiManager.thngService().iterator().execute();
                while (cloudProducts.hasNext()){
                    PVector<Thng> page=cloudProducts.next();
                    for(Thng thng:page){
                        Log.d(MainActivity.TAG, "run: "+thng.getProduct());
                        if(thng.getProduct().equals(productid)){
                            thngs.add(thng);
                        }
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thngAdapter.notifyDataSetChanged();
                        }
                    });

                }
            }
        }).start();
    }

    private void initView() {
        listView=findViewById(R.id.listView_thng);
        thngAdapter=new ThngAdapter(thngs,this);
        listView.setAdapter(thngAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(ProductActivity.this,UpdateActivity.class);
                String productId=thngAdapter.getItem(position).getId();
                intent.putExtra("thngid",productId);
                startActivity(intent);
            }
        });
        addThngs=findViewById(R.id.add);
        addThngs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductActivity.this,AddActivity.class);
                intent.putExtra("productId",productid);
                startActivity(intent);
            }
        });
    }
}
