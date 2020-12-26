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
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.

 * create an instance of this fragment.
 */
public class DesiredPartnerFragment extends Fragment {

    DrService drService;
    Detail detail;
    View view;
    String uid;

    ScrollView scrollView;
    RelativeLayout relativeLayout;
    private SharedPreferenceUtil sharedPreferenceUtil;
    TextView mDeducation,mDage,mDmartialStatus,mDreligion,mDmothertounge,mDcaste,mDchallenged,mDcity,mDcountry,mDearning,mDheight;

    public DesiredPartnerFragment(String uid) {
        this.uid = uid;
    }

    public DesiredPartnerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_desired_partner, container, false);
        mDheight=view.findViewById(R.id.he_should_be);
        mDage=view.findViewById(R.id.d_age);
        mDmartialStatus=view.findViewById(R.id.martial_status);
        mDreligion=view.findViewById(R.id.d_religion);
        mDmothertounge=view.findViewById(R.id.mother_tounge);
        mDcaste=view.findViewById(R.id.d_caste);
        mDchallenged=view.findViewById(R.id.d_challenged);
        mDcity=view.findViewById(R.id.d_city);
        mDcountry=view.findViewById(R.id.d_country);
        mDearning=view.findViewById(R.id.d_earning);
        scrollView=view.findViewById(R.id.main_sv);
        relativeLayout=view.findViewById(R.id.not_filled_rl);




        sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
        drService = ApiUtils.getClient().create(DrService.class);

        getData(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null));
        return view;
    }


    private void getData(String token1) {
        drService.showdetail(token1,uid).enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                detail=response.body();
                Log.d("Seraj","Data fetched");
                if (detail!=null){
                   if(alldataisAvailable(detail)){
                       relativeLayout.setVisibility(View.GONE);
                       scrollView.setVisibility(View.VISIBLE);
                       setData(detail);
                   }
                }

                Log.d("Seraj","Data not null");



            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
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

    private void setData(Detail detail) {
        mDheight.setText(detail.getDheshouldbe());
        mDage.setText(detail.getDageofaround());
        mDmartialStatus.setText(detail.getDmartialstatus());
        mDreligion.setText(detail.getDreligion());
        mDmothertounge.setText(detail.getDmothertounge());
        mDcaste.setText(detail.getDcaste());
        mDchallenged.setText(detail.getDchallenged());
        mDcity.setText(detail.getDcity());
        mDcountry.setText(detail.getDcountry());
        mDearning.setText(detail.getDeducation());
    }
}