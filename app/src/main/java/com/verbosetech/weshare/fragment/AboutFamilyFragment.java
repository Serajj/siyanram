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
public class AboutFamilyFragment extends Fragment {

    DrService drService;
    Detail detail;
    View view;
    ScrollView scrollView;
    RelativeLayout relativeLayout;
    TextView mFtype,mFincome,mFoccupation,mFstatus,mfSisters,mFbrothers;
    private SharedPreferenceUtil sharedPreferenceUtil;
    String uid;

    public AboutFamilyFragment(String uid) {
        this.uid = uid;
    }

    public AboutFamilyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.fragment_about_family, container, false);

         mFtype=view.findViewById(R.id.f_type);
        mFincome=view.findViewById(R.id.f_income);
        mFoccupation=view.findViewById(R.id.f_occupation);
        mFstatus=view.findViewById(R.id.f_status);
        mfSisters=view.findViewById(R.id.f_brother);
        mFbrothers=view.findViewById(R.id.f_sister);
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

                Log.d("Seraj","Data not null");
                if (detail!=null){
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
        mFtype.setText(detail.getFtype());
        mFstatus.setText(detail.getFstatus());
        mFincome.setText(detail.getFincome());
        mFoccupation.setText(detail.getFoccupation());
        mFbrothers.setText(detail.getFbrother());
        mfSisters.setText(detail.getFsister());
    }
}