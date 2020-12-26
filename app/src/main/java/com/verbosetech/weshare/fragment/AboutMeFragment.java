package com.verbosetech.weshare.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.verbosetech.weshare.R;
import com.verbosetech.weshare.model.Detail;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class AboutMeFragment extends Fragment {


    DrService drService;
    Detail detail;
    View view;
    String uid;

    public AboutMeFragment(String uid) {
        this.uid = uid;
    }

    ScrollView scrollView;
    RelativeLayout relativeLayout;
    TextView heightColorBody,occupation,religionCaste,income,motherTounge,heducation,city,mstatus,prifilemanagedby,about,employedin,earning
            ,hieducation,ugd,ugc,pgd,pgc;
    UserResponse profileMe;

    private SharedPreferenceUtil sharedPreferenceUtil;
    public AboutMeFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =inflater.inflate(R.layout.fragment_about_me, container, false);
        heightColorBody=view.findViewById(R.id.height_and_age);
        occupation=view.findViewById(R.id.occupation);
        religionCaste=view.findViewById(R.id.religion_and_caste);
        income=view.findViewById(R.id.income);
        motherTounge=view.findViewById(R.id.mother_tounge);
        city=view.findViewById(R.id.city);
        mstatus=view.findViewById(R.id.mstatus);
        heducation=view.findViewById(R.id.heducation);
        prifilemanagedby=view.findViewById(R.id.profilemanagedby);
        about=view.findViewById(R.id.aboutme);
        employedin=view.findViewById(R.id.c_employedin);
        earning=view.findViewById(R.id.c_annual_income);
        hieducation=view.findViewById(R.id.e_highest);
        ugd=view.findViewById(R.id.e_ugdegree);
        ugc=view.findViewById(R.id.e_ugcollege);
        pgd=view.findViewById(R.id.e_pgdegree);
        pgc=view.findViewById(R.id.e_pgcollege);
        scrollView=view.findViewById(R.id.main_sv);
        relativeLayout=view.findViewById(R.id.not_filled_rl);
        profileMe = Helper.getLoggedInUser(new SharedPreferenceUtil(getContext()));
        sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
        drService = ApiUtils.getClient().create(DrService.class);
        getData(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null));
        return view;
    }


    private boolean alldataisAvailable(Detail detail) {
        if (detail.getCaste()==null || detail.getCaste().equalsIgnoreCase("null")){
            return false;
        }
        if (detail.getFbrother()==null || detail.getFbrother().equalsIgnoreCase("Not Filled")){
            return false;
        }
        if (detail.getCannuanincome()==null || detail.getCannuanincome().equalsIgnoreCase("Not Filled")){
            return false;
        }
        if (detail.getDageofaround()==null || detail.getDageofaround().equalsIgnoreCase("Not Filled")){
            return false;
        }
        return true;
    }
    private void getData(String token1) {
        drService.showdetail(token1,uid).enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                detail=response.body();
                Log.d("Seraj","Data fetched");
                if (response.isSuccessful()){
                    Log.d("Seraj","responce suvcc");
                }

                if (detail!=null){
                    Log.d("Seraj","Data not null");
                    if(alldataisAvailable(detail)){
                        relativeLayout.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        setData(detail);
                    }
                }

            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData(Detail detail) {
        heightColorBody.setText(detail.getHeight()+","+detail.getComplexion()+","+detail.getBodytype());
        occupation.setText(detail.getCemployedin());
        religionCaste.setText(detail.getReligion()+"-"+detail.getCaste());
        income.setText(detail.getCannuanincome());
        motherTounge.setText(detail.getMotherTounge());
        city.setText(profileMe.getCity());
        mstatus.setText(detail.getMartialStatus());
        heducation.setText(detail.getEhighest());
        ugd.setText(detail.getEugdegree());
        ugc.setText(detail.getEugcollege());
        pgd.setText(detail.getEpgdegree());
        pgc.setText(detail.getEpgcollege());
        prifilemanagedby.setText("Profile managed for : "+detail.getProfilefor());
        hieducation.setText(detail.getEhighest());
        about.setText(detail.getAboutme());
        employedin.setText(detail.getCemployedin());
        earning.setText(detail.getCannuanincome());
    }
}