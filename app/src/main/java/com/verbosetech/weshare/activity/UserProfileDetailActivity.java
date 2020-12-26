package com.verbosetech.weshare.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.verbosetech.weshare.R;
import com.verbosetech.weshare.adapter.ExpandableListAdapter;
import com.verbosetech.weshare.adapter.ViewPagerAdapter;
import com.verbosetech.weshare.fragment.AboutFamilyFragment;
import com.verbosetech.weshare.fragment.AboutMeFragment;
import com.verbosetech.weshare.fragment.ConfirmationDialogFragment;
import com.verbosetech.weshare.fragment.DesiredPartnerFragment;
import com.verbosetech.weshare.fragment.HomeFeedsFragment;
import com.verbosetech.weshare.model.Chat;
import com.verbosetech.weshare.model.ContactModel;
import com.verbosetech.weshare.model.Detail;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.request.ReportUserRequest;
import com.verbosetech.weshare.network.response.ProfileFollowRequestResponse;
import com.verbosetech.weshare.network.response.ProfileResponse;
import com.verbosetech.weshare.network.response.ProfileFollowResponse;
import com.verbosetech.weshare.network.response.ReportUserResponse;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by a_man on 09-02-2018.
 */

public class UserProfileDetailActivity extends AppCompatActivity {
    private static String EXTRA_DATA_USER_ID = "UserResponseId";
    private static String EXTRA_DATA_USER_NAME = "UserResponseName";
    private static String EXTRA_DATA_USER_IMAGE = "UserResponseImage";
    private static String CONFIRM_TAG = "confirmtag";

    private ImageView profileImage;
    private EditText dob,fname,mname,occupation,city,state,gender;
    private TextView userPostsCount, userFollowersCount, userFollowingCount;
    private TextView profileName;
    private ProgressBar progressBar;
    private FloatingActionButton floatingActionButton;
    private View followerCountContainer, followingCountContainer;
    UserResponse userResponse;

    Detail detail;

    private DrService owhloService;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private ProfileResponse userProfile;

    private String userId;
    private String userName, userImage;
    private UserResponse userMe;
    private MenuItem menuActionReport;

    private BottomSheetBehavior sheetBehavior;
    private RadioGroup radioGroupReportReasons;
    private Button reportConfirm,addF,contactBtn;
    private Context mContext;

    ExpandableListAdapter listAdapter;
    ExpandableListView listView;
    List<String> listDataHeader;
    HashMap<String,List<String>> listHashMap;
    DrService drService;

    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);

        contactBtn=findViewById(R.id.contact_btn);



        actionProfile();
        drService = ApiUtils.getClient().create(DrService.class);

        mContext = this;
        owhloService = ApiUtils.getClient().create(DrService.class);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);





        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_DATA_USER_ID) || !intent.hasExtra(EXTRA_DATA_USER_NAME) || !intent.hasExtra(EXTRA_DATA_USER_IMAGE)) {
            finish();
        } else {
            userId = intent.getStringExtra(EXTRA_DATA_USER_ID);
            userName = intent.getStringExtra(EXTRA_DATA_USER_NAME);
            userImage = intent.getStringExtra(EXTRA_DATA_USER_IMAGE);
            userMe = Helper.getLoggedInUser(sharedPreferenceUtil);
            initUi();
            setDetails();
            loadDetails();

            viewPager=findViewById(R.id.viewpager);
            tabLayout=findViewById(R.id.all_courses_tab);
            viewPager.setOffscreenPageLimit(2);
            setViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);

            getAddress(userId);
        }

        Log.d("serajuid",userId);
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(UserProfileDetailActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                drService.getContactList(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null),userId).enqueue(new Callback<List<ContactModel>>() {
                    @Override
                    public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {
                        Log.d("serajcontact","response recieved");
                        if (response.isSuccessful()){
                            Log.d("serajcontact","response success");
                            List<ContactModel> contactModelList =response.body();
                            if (contactModelList.size()>0){
                                viewContactDialog();
                            }else{
                                askForPayDialog();
                            }
                        }else{
                            Log.d("serajcontact","response failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<ContactModel>> call, Throwable t) {
                        Log.d("serajcontact","response failure");
                    }
                });
            }
        });
    }

    private void getAddress(String userId) {
        drService.getaddress(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null),userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
               if (response.isSuccessful()){
                   userResponse=response.body();
                   Log.d("serajaddress","responce recieved success"+userResponse.getCity());
               }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void viewContactDialog() {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_pay_contact);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Button button=dialog.findViewById(R.id.pay_btn_contact);
        TextView textView=dialog.findViewById(R.id.address);

        if (userResponse!=null){
            textView.setText("City : "+userResponse.getCity()+", State : "+userResponse.getState()+", Country : India");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (dialog.isShowing()){
                   dialog.dismiss();
               }
            }
        });

        dialog.show();

    }

    private void askForPayDialog() {
        sharedPreferenceUtil.setPayViewType(1);
        sharedPreferenceUtil.setHisUid(userId);
        Intent intent=new Intent(UserProfileDetailActivity.this,PaymentActivity.class);
        startActivity(intent);
    }

    private void setViewPager(ViewPager viewPager) {

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());

        viewPagerAdapter.addfragment(new AboutMeFragment(userId),"About");
        viewPagerAdapter.addfragment(new AboutFamilyFragment(userId),"Family");
        viewPagerAdapter.addfragment(new DesiredPartnerFragment(userId),"Partner");
        viewPager.setAdapter(viewPagerAdapter);

    }





    private void loadDetails() {
        owhloService.getProfile(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), userId).enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (mContext != null) {
                    progressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()) {
                        userProfile = response.body();
                        setDetails();

                        Log.d("Seraj",userProfile.getCity() );
                        setProfileData(userProfile);

                       // refreshFeeds();
                    }
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                if (mContext != null) {
                    progressBar.setVisibility(View.GONE);
                    t.getMessage();
                }
            }
        });
    }

    private void setProfileData(ProfileResponse userProfile) {

    }

    private void setDetails() {
        Glide.with(this)
                .load(userProfile != null ? userProfile.getImage() : userImage)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(Helper.dp2px(this, 8))).placeholder(R.drawable.ic_person_white_72dp))
                .into(profileImage);
        Log.d("profileImage", userProfile != null ? userProfile.getImage() : userImage);
        profileName.setText(userProfile != null ? userProfile.getName() : userName);
        if (userProfile != null) {
            userPostsCount.setText(String.valueOf(userProfile.getPosts_count()));
            userFollowersCount.setText(String.valueOf(userProfile.getFollowers_count()));
            userFollowingCount.setText(String.valueOf(userProfile.getFollowing_count()));
            floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, userProfile.getIs_following() == 1 ? R.drawable.ic_done_white_24dp : userProfile.getIs_follow_requested() == 1 ? R.drawable.ic_person_white_24dp : R.drawable.ic_person_add_white_24dp));
            UserResponse userResponse = Helper.getLoggedInUser(sharedPreferenceUtil);
            if (menuActionReport != null)
                menuActionReport.setVisible(!(userResponse != null ? userResponse.getId().toString() : "-1").equals(userId));
        }
        followerCountContainer.setClickable(userProfile != null && userProfile.getIs_following() == 1);
        followingCountContainer.setClickable(userProfile != null && userProfile.getIs_following() == 1);
    }

    private void initUi() {
        findViewById(R.id.ll_top).setVisibility(View.GONE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
            toolbar.setTitleTextAppearance(this, R.style.MontserratBoldTextAppearance);
            actionBar.setTitle(userName);
        }
        profileImage = findViewById(R.id.userImage);
        profileName = findViewById(R.id.fullName);
        userPostsCount = findViewById(R.id.userPostsCount);
        userFollowersCount = findViewById(R.id.userFollowersCount);
        userFollowingCount = findViewById(R.id.userFollowingCount);



        sheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetReport));
        radioGroupReportReasons = findViewById(R.id.radioGroup);
        reportConfirm = findViewById(R.id.reportConfirm);
        reportConfirm.setOnClickListener(v -> {
            RadioButton radioButton = findViewById(radioGroupReportReasons.getCheckedRadioButtonId());
            if (radioButton != null) {
                reportUser(radioButton.getText().toString());
                //confirmReport();
            }
        });

        progressBar = findViewById(R.id.profileRefreshProgress);
        floatingActionButton = findViewById(R.id.fab_setting);
        UserResponse userResponse = Helper.getLoggedInUser(sharedPreferenceUtil);
        if ((userResponse != null ? userResponse.getId().toString() : "-1").equals(userId))
            floatingActionButton.hide();
        else
            floatingActionButton.show();
        //floatingActionButton.setVisibility((userResponse != null ? userResponse.getId() : -1) == userId ? View.GONE : View.VISIBLE);
        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_person_add_white_24dp));
        floatingActionButton.setOnClickListener(view -> actionProfile());
        followerCountContainer = findViewById(R.id.followerCountContainer);
        followingCountContainer = findViewById(R.id.followingCountContainer);
        followerCountContainer.setOnClickListener(v -> {
            if (userProfile != null)
                startActivity(FollowerFollowingActivity.newInstance(mContext, userProfile.getId().toString(), "Followers"));
        });
        followingCountContainer.setOnClickListener(v -> {
            if (userProfile != null)
                startActivity(FollowerFollowingActivity.newInstance(mContext, userProfile.getId().toString(), "Followings"));
        });
        followerCountContainer.setClickable(userProfile != null && userProfile.getIs_following() == 1);
        followingCountContainer.setClickable(userProfile != null && userProfile.getIs_following() == 1);
        FloatingActionButton floatingChatActionButton = findViewById(R.id.fab_bookmarks);
        floatingChatActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_message_white_24dp));
        floatingChatActionButton.setVisibility(View.GONE);
        floatingChatActionButton.setOnClickListener(v -> {
            if (userProfile != null) {
                //startActivity(MessagesActivity.newIntent(mContext, new Chat(userMe, userProfile)));
            }
        });
        if ((userResponse != null ? userResponse.getId().toString() : "-1").equals(userId))
            floatingChatActionButton.hide();
        else {
            //floatingChatActionButton.show();
        }

        //inflateFeedsView();
    }

    private void actionProfile() {
        if (userProfile != null) {
            floatingActionButton.setClickable(false);
            progressBar.setVisibility(View.VISIBLE);
            if (userProfile.getIs_following() == 1 || userProfile.getIs_private() == 0) {
                owhloService.profileFollowAction(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), userId).enqueue(new Callback<ProfileFollowResponse>() {
                    @Override
                    public void onResponse(Call<ProfileFollowResponse> call, Response<ProfileFollowResponse> response) {
                        if (mContext != null) {
                            if (response.isSuccessful())
                                userProfile.setIs_following(response.body().isFollowed() ? 1 : 0);
                            //refreshFeeds();
                            floatingActionButton.setClickable(true);
                            progressBar.setVisibility(View.GONE);
                            followerCountContainer.setClickable(response.isSuccessful() && response.body().isFollowed());
                            followingCountContainer.setClickable(response.isSuccessful() && response.body().isFollowed());
                            if (response.isSuccessful() && response.body().getSuccess() != 0) {
                                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(mContext, response.body().isFollowed() ? R.drawable.ic_done_white_24dp : R.drawable.ic_person_add_white_24dp));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowResponse> call, Throwable t) {
                        if (mContext != null) {
                            floatingActionButton.setClickable(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            } else {
                owhloService.profileFollowActionRequest(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), userId).enqueue(new Callback<ProfileFollowRequestResponse>() {
                    @Override
                    public void onResponse(Call<ProfileFollowRequestResponse> call, Response<ProfileFollowRequestResponse> response) {
                        if (mContext != null) {
                            floatingActionButton.setClickable(true);
                            progressBar.setVisibility(View.GONE);
                            if (response.isSuccessful()) {
                                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(mContext, response.body().getFollow_request() ? R.drawable.ic_person_white_24dp : R.drawable.ic_person_add_white_24dp));
                                Toast.makeText(mContext, getString(response.body().getFollow_request() ? R.string.follow_request_added : R.string.follow_request_removed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileFollowRequestResponse> call, Throwable t) {
                        if (mContext != null) {
                            floatingActionButton.setClickable(true);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }
    }
/**uncomment when you need feed in profile
    private void inflateFeedsView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.profileFrame, HomeFeedsFragment.newInstance("study", userId, false), "user_feed")
                .commit();

        new Handler().postDelayed(() -> {
            TextView empty_view_text = findViewById(R.id.empty_view_text);
            if (empty_view_text != null) {
                empty_view_text.setText(getString(R.string.need_follow) + " " + userName + " " + getString(R.string.tosee_posts));
            }
            TextView empty_view_sub_text = findViewById(R.id.empty_view_sub_text);
            if (empty_view_sub_text != null) {
                empty_view_sub_text.setVisibility(View.GONE);
            }
        }, 500);
    }

    private void refreshFeeds() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm != null) {
            HomeFeedsFragment homeFeedsFragment = (HomeFeedsFragment) fm.findFragmentByTag("user_feed");
            if (homeFeedsFragment == null)
                inflateFeedsView();
            else if (userProfile.getIs_following() == 1 || userProfile.getIs_private() == 0) {
                TextView empty_view_text = findViewById(R.id.empty_view_text);
                if (empty_view_text != null) {
                    empty_view_text.setText(getString(R.string.empty_feeds));
                }
                homeFeedsFragment.hideShowFeeds(true);
                homeFeedsFragment.refresh();
            } else {
                TextView empty_view_text = findViewById(R.id.empty_view_text);
                if (empty_view_text != null) {
                    empty_view_text.setText(getString(R.string.need_follow) + " " + userName + " " + getString(R.string.tosee_posts));
                }
                homeFeedsFragment.hideShowFeeds(false);
            }
        }
    }**/

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_item, menu);
        menuActionReport = menu.findItem(R.id.action_report);
        menuActionReport.setVisible(false);
        menu.findItem(R.id.action_delete).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_report:
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmReport() {
        FragmentManager manager = getSupportFragmentManager();
        Fragment frag = manager.findFragmentByTag(CONFIRM_TAG);
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }

        ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newInstance(getString(R.string.report_user),
                getString(R.string.report_user_confirm),
                getString(R.string.yes),
                getString(R.string.no),
                view -> {
                    RadioButton radioButton = findViewById(radioGroupReportReasons.getCheckedRadioButtonId());
                    if (radioButton != null) {
                        reportUser(radioButton.getText().toString());
                    }
                },
                view -> {
                    if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }
                });
        confirmationDialogFragment.show(manager, CONFIRM_TAG);
    }

    private void reportUser(String reason) {
        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        owhloService.reportUser(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), userId, new ReportUserRequest(reason)).enqueue(new Callback<ReportUserResponse>() {
            @Override
            public void onResponse(Call<ReportUserResponse> call, Response<ReportUserResponse> response) {
                if (mContext != null) {
                    if (response.isSuccessful()) {
                        Toast.makeText(mContext, getString(R.string.reported), Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            }

            @Override
            public void onFailure(Call<ReportUserResponse> call, Throwable t) {
                if (mContext != null) {
                    t.getMessage();
                }
            }
        });
    }

    public static Intent newInstance(Context context, String userId, String userName, String userImage) {
        Intent intent = new Intent(context, UserProfileDetailActivity.class);
        intent.putExtra(EXTRA_DATA_USER_ID, userId);
        intent.putExtra(EXTRA_DATA_USER_NAME, userName);
        intent.putExtra(EXTRA_DATA_USER_IMAGE, userImage);
        return intent;
    }

}
