package com.verbosetech.weshare.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.verbosetech.weshare.R;
import com.verbosetech.weshare.adapter.CategoryAdapter;
import com.verbosetech.weshare.model.UserByCat;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {

    String catName,sub;
    private DrService weService;
    private SharedPreferenceUtil sharedPreferenceUtil;
    TextView catshow;
    List<UserByCat> userByCatList=new ArrayList<>();
    List<UserByCat> filtereUserList=new ArrayList<>();
    RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        catshow=findViewById(R.id.catshow);
        recyclerView=findViewById(R.id.cat_rv);
        relativeLayout=findViewById(R.id.not_found_rl);
        Intent intent=getIntent();
        catName=intent.getStringExtra("category");
        sub=intent.getStringExtra("name");
        Log.d("serajds",sub);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        weService = ApiUtils.getClient().create(DrService.class);

        catshow.setText("Matched Profiles");


        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));





       weService.getuserbycat(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null),catName,sub).enqueue(new Callback<List<UserByCat>>() {
           @Override
           public void onResponse(Call<List<UserByCat>> call, Response<List<UserByCat>> response) {
               userByCatList=response.body();
               if (userByCatList!=null){
                   Log.d("Serajuserbycat","workinh");
                   filterData(userByCatList);

               }else{
                   Log.d("Serajuserbycat","sorry no user found");
                   try {
                       Log.d("Serajuserbycat","sorry no user found "+response.errorBody().string()+" " + response.isSuccessful());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
           }

           @Override
           public void onFailure(Call<List<UserByCat>> call, Throwable t) {

           }
       });

    }

    private void filterData(List<UserByCat> userByCatList) {
        for (int i=0;i<userByCatList.size();i++){
             if (catName.equalsIgnoreCase("m")){
                // Toast.makeText(this, ""+userByCatList.get(i).getGender(), Toast.LENGTH_SHORT).show();
                 if (userByCatList.get(i).getGender().equalsIgnoreCase("m")){
                     filtereUserList.add(userByCatList.get(i));
                 }
             }
            if (catName.equalsIgnoreCase("f")){
                if (userByCatList.get(i).getGender().equalsIgnoreCase("f")){
                    filtereUserList.add(userByCatList.get(i));
                }
            }
            if (catName.equalsIgnoreCase("state")){
                    filtereUserList.add(userByCatList.get(i));
            }
            if (catName.equalsIgnoreCase("divorced")){
                if (userByCatList.get(i).getMartialStatus()!=null){
                    if (userByCatList.get(i).getMartialStatus().equalsIgnoreCase("Divorce")){
                        filtereUserList.add(userByCatList.get(i));
                    }
                }
            }
            if (catName.equalsIgnoreCase("occupation")){
                if (userByCatList.get(i).getOccupation()!=null){
                    if (userByCatList.get(i).getOccupation().equalsIgnoreCase(sub)){
                        filtereUserList.add(userByCatList.get(i));
                    }
                }
            }
            if (catName.equalsIgnoreCase("religion")){
               if (userByCatList.get(i).getReligion()!=null){
                   if (userByCatList.get(i).getReligion().equalsIgnoreCase(sub)){
                       filtereUserList.add(userByCatList.get(i));
                   }
               }
            }
            if (catName.equalsIgnoreCase("city")){
                if(userByCatList.get(i).getCity()!=null) {

                    if (userByCatList.get(i).getCity().equalsIgnoreCase(sub)) {
                        filtereUserList.add(userByCatList.get(i));
                    }
                }
            }
            if (catName.equalsIgnoreCase("caste")){
                if (userByCatList.get(i).getCaste()!=null){
                    if (userByCatList.get(i).getCaste().equalsIgnoreCase(sub)){
                        filtereUserList.add(userByCatList.get(i));
                    }
                }
            }
        }

        if (filtereUserList.size()>0){
            Log.d("Serajuserbycat",""+userByCatList.get(0).getDob());
            setRecycler(filtereUserList);
        }else{
            relativeLayout.setVisibility(View.VISIBLE);
            Log.d("Serajuserbycat","No user found");
        }
    }

    private void setRecycler(List<UserByCat> filtereUserList) {
        adapter = new CategoryAdapter(this,filtereUserList);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        finish();
        //super.onBackPressed();
    }
}