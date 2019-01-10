package com.example.urvish.volleydemo;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VolleyStringRequest {
    private RequestQueue mRequestQueue;

    public VolleyStringRequest(Context context) {
        mRequestQueue= Volley.newRequestQueue(context);
    }
    public void getDataFromApi(final onDataAvailable onDataAvailable, String url){

        StringRequest stringRequest=new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("VOLLEY", "onResponse: "+response);
                onDataAvailable.onData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", "onErrorResponse: "+error);
            }
        });
        mRequestQueue.add(stringRequest);
        mRequestQueue.start();
    }

    public void postDatatoApi(final onDataAvailable onDataAvailable, String url,final User user){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onDataAvailable.onData(response);
                Log.d("TAG", "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VOLLEY", "onErrorResponse: "+error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",user.getName());
                params.put("email",user.getEmail());
                params.put("password",user.getPassword());
                return params;
            }
        };
        mRequestQueue.add(stringRequest);
        mRequestQueue.start();

    }
    public void updateDatatoApi(final onDataAvailable onDataAvailable, String url, final User user){
        StringRequest stringRequest=new StringRequest(Request.Method.PUT, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onDataAvailable.onData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error);
            }
        }){


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",user.getName());
                params.put("email",user.getEmail());
                params.put("password",user.getPassword());
                return params;
            }
        };
        mRequestQueue.add(stringRequest);
        mRequestQueue.start();

    }
    //delete not working with parameter inside the volley
    //refer this site: https://github.com/google/volley/issues/24
    public void deleteDatatoApi(final onDataAvailable onDataAvailable, String url, final User user){
        StringRequest stringRequest=new StringRequest(Request.Method.DELETE, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                onDataAvailable.onData(response);
                Log.d("TAG", "onResponse: "+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "onErrorResponse: "+error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("name",user.getName());
                return params;
            }
        };
        mRequestQueue.add(stringRequest);
        mRequestQueue.start();
    }
    public interface onDataAvailable{
        public void onData(String Data);
    }
}
