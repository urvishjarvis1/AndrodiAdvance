package com.example.volansys.retrofit;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.volansys.retrofit.NetworkUtility;
import com.example.volansys.retrofit.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import static com.example.volansys.retrofit.NetworkUtility.getNetworkUtility;

public class MainViewModel {

    private List<User> mListUser;
    private Context mContext;
    private User user;
    private ActivityMainBinding binding;

    public MainViewModel(Context mContext, ActivityMainBinding binding) {
        this.mContext = mContext;
        mListUser=new ArrayList<>();
        user=new User();
        this.binding=binding;


    }

    public void makeCall(){
        NetworkUtility networkUtility=NetworkUtility.getNetworkUtility();

            networkUtility.jsonHolderApi().getUserwithId().enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                     mListUser=response.body();
                    Log.e("error",""+response.code());
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {

                }
            });

        for(int i=0;i<mListUser.size();i++){
            binding.ans.append(mListUser.get(i).getName());
            binding.ans.append(mListUser.get(i).getEmail());
            binding.ans.append(mListUser.get(i).getPassword());
        }

    }
    public View.OnClickListener submitData(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("onClick","insideOnclicke!");
                Toast.makeText(mContext, "inOnclickView", Toast.LENGTH_SHORT).show();
                    submitDataToApi();
            }
        };

    }
    //todo here
    public void submitDataToApi(){
       /* NetworkUtility.getNetworkUtility().jsonHolderApi().postUser().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(mContext, "Response:"+response.body(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
*/
    }
}
