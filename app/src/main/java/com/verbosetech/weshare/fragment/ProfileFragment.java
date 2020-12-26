package com.verbosetech.weshare.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.verbosetech.weshare.activity.AddDetailActivity;
import com.verbosetech.weshare.activity.BookmarksActivity;
import com.verbosetech.weshare.activity.EditOptionActivity;
import com.verbosetech.weshare.activity.FollowerFollowingActivity;
import com.verbosetech.weshare.R;
import com.verbosetech.weshare.activity.PaymentActivity;
import com.verbosetech.weshare.activity.SettingsActivity;
import com.verbosetech.weshare.adapter.ExpandableListAdapter;
import com.verbosetech.weshare.adapter.ViewPagerAdapter;
import com.verbosetech.weshare.model.Detail;
import com.verbosetech.weshare.network.ApiError;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.ErrorUtils;
import com.verbosetech.weshare.network.response.ProfileResponse;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;
import com.verbosetech.weshare.network.ApiUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private ImageView userImage;
    private TextView userPostsCount, userFollowersCount, userFollowingCount;
    private TextView userName;
    private ProgressBar profileRefreshProgress;
    private EditText dob,fname,mname,occupation,city,state,gender;
    private DrService foxyService;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ProfileResponse profileMe;
    private UserResponse userMe;
    private FloatingActionButton  fab_bookmarks,payBtn;
    Button fab_setting,editProfile;

    List<String> listDataHeader;
    HashMap<String,List<String>> listHashMap;
    DrService drService;
    Detail detail;

    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferenceUtil = new SharedPreferenceUtil(getContext());
        foxyService = ApiUtils.getClient().create(DrService.class);
        userMe = Helper.getLoggedInUser(sharedPreferenceUtil);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        userImage = view.findViewById(R.id.userImage);
        userName = view.findViewById(R.id.fullName);
        userPostsCount = view.findViewById(R.id.userPostsCount);
        editProfile=view.findViewById(R.id.fab_edit_profile);
        userFollowersCount = view.findViewById(R.id.userFollowersCount);
        userFollowingCount = view.findViewById(R.id.userFollowingCount);
        profileRefreshProgress = view.findViewById(R.id.profileRefreshProgress);
        drService = ApiUtils.getClient().create(DrService.class);
        viewPager=view.findViewById(R.id.viewpager);
        tabLayout=view.findViewById(R.id.all_courses_tab);
        viewPager.setOffscreenPageLimit(2);
        setViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), EditOptionActivity.class));
            }
        });

      /***  fname = view.findViewById(R.id.fname);
        mname = view.findViewById(R.id.mName);
        occupation = view.findViewById(R.id.occupation);
        city = view.findViewById(R.id.city);
        state = view.findViewById(R.id.state);
        dob=view.findViewById(R.id.dob);
        gender=view.findViewById(R.id.gender);**/

        view.findViewById(R.id.followerCountContainer).setOnClickListener(view1 -> {
            if (userMe != null)
                startActivity(FollowerFollowingActivity.newInstance(getContext(), userMe.getId().toString(), "Followers"));
        });
        view.findViewById(R.id.followingCountContainer).setOnClickListener(view12 -> {
            if (userMe != null)
                startActivity(FollowerFollowingActivity.newInstance(getContext(), userMe.getId().toString(), "Followings"));
        });
//show user all feed on user profile
        /**if (userMe != null)
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.profileFrame, HomeFeedsFragment.newInstance("study_me", userMe.getId().toString(), false), "my_feed")
                    .commit();*/
        //end user feeds
        fab_setting = view.findViewById(R.id.fab_setting);
        payBtn = view.findViewById(R.id.payBtns);
        fab_bookmarks = view.findViewById(R.id.fab_bookmarks);
        fab_setting.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));
        fab_bookmarks.setOnClickListener(v -> startActivity(new Intent(getContext(), BookmarksActivity.class)));
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), PaymentActivity.class);
                getContext().startActivity(intent);
            }
        });
        return view;
    }

    private void setViewPager(ViewPager viewPager) {

            ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());

            viewPagerAdapter.addfragment(new AboutMeFragment("no"),"About");
            viewPagerAdapter.addfragment(new AboutFamilyFragment("no"),"Family");
            viewPagerAdapter.addfragment(new DesiredPartnerFragment("no"),"Partner");
            viewPager.setAdapter(viewPagerAdapter);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (profileMe != null) {
            setDetails();
        }
        if (userMe != null) {
            refreshProfile();
        }
    }

    private void refreshFeeds() {
        HomeFeedsFragment myFeedsFragment = (HomeFeedsFragment) getChildFragmentManager().findFragmentByTag("my_feed");
        if (myFeedsFragment != null) myFeedsFragment.refresh();
    }

    private void refreshProfile() {
        profileRefreshProgress.setVisibility(View.VISIBLE);
        foxyService.getProfile(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), String.valueOf(userMe.getId())).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                profileRefreshProgress.setVisibility(View.INVISIBLE);
                if (response.isSuccessful()) {
                    profileMe = response.body();

                    Helper.saveProfileMe(new SharedPreferenceUtil(getContext()),profileMe);
                    setDetails();
//                    if (TextUtils.isEmpty(profileMe.getName())) {
//                        startActivity(EditProfileActivityActivity.newInstance(getContext(), profileMe, true));
//                    }
                } else {
                    ApiError apiError = ErrorUtils.parseError(response);
                    Toast.makeText(getContext(), apiError.status() == 417 ? getString(R.string.admin_block) : TextUtils.isEmpty(apiError.message()) ? getString(R.string.something_wrong) : apiError.message(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                profileRefreshProgress.setVisibility(View.INVISIBLE);
                t.getMessage();
            }
        });
    }

    private void setDetails() {
        if (profileMe == null) profileMe = new ProfileResponse();
        Glide.with(getContext()).load(userMe.getImage()).apply(RequestOptions.bitmapTransform(new RoundedCorners(Helper.dp2px(getContext(), 8))).placeholder(R.drawable.ic_person_white_72dp)).into(userImage);
        userName.setText(userMe.getName());


        userPostsCount.setText(String.valueOf(profileMe.getPosts_count()));
        userFollowersCount.setText(String.valueOf(profileMe.getFollowers_count()));
        userFollowingCount.setText(String.valueOf(profileMe.getFollowing_count()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (userMe != null) {
            refreshProfile();
            getData(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null));

            if (sharedPreferenceUtil.getBooleanPreference(Constants.KEY_UPDATED, false)) {
                sharedPreferenceUtil.setBooleanPreference(Constants.KEY_UPDATED, false);
                userMe = Helper.getLoggedInUser(sharedPreferenceUtil);

                Glide.with(getContext()).load(userMe.getImage()).apply(RequestOptions.bitmapTransform(new RoundedCorners(Helper.dp2px(getContext(), 8))).placeholder(R.drawable.ic_person_white_72dp)).into(userImage);
                userName.setText(userMe.getName());
                new Handler().postDelayed(() -> LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(Constants.PROFILE_CHANGE_EVENT)), 200);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && userMe != null) {
            refreshProfile();
            refreshFeeds();
        }
    }


    private void getData(String token1) {
        Log.d("serajtoken",token1);
        drService.showdetail(token1,"no").enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                detail=response.body();
                Log.d("Seraj","Data fetched");

                    Log.d("Seraj","Data not null");



            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onStart() {
        getData(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null));
        super.onStart();
    }
}
