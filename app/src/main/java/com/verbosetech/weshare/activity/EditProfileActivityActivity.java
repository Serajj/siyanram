package com.verbosetech.weshare.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.onesignal.OneSignal;
import com.theartofdev.edmodo.cropper.CropImage;
import com.verbosetech.weshare.R;
import com.verbosetech.weshare.model.Caste;
import com.verbosetech.weshare.model.Dropdown;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.request.UserUpdateRequest;
import com.verbosetech.weshare.network.response.ProfileResponse;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.FirebaseUploader;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.verbosetech.weshare.activity.MainActivity.dropdownData;

public class EditProfileActivityActivity extends AppCompatActivity implements ImagePickerCallback ,AdapterView.OnItemSelectedListener{
    private static String DATA_EXTRA_USER = "UserProfile";
    private static String DATA_EXTRA_FORCE_EDIT = "UserProfileForceEdit";
    private final int REQUEST_CODE_PERMISSION = 55;
    private ImageView userImage;
    private EditText userName,fname,mname,referalCode;
    Spinner occupation;
    Spinner city,state;
    ArrayAdapter<CharSequence> adapterOccupation;
    ArrayAdapter<String> adapterCity,adapterState;
    List<String> City = new ArrayList<>();
    List<String> State = new ArrayList<>();
    private ProgressBar progress;
    private Button done;
    private UserResponse profileMe;
    private DrService drService;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private String pickerPath;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private File mediaFile;
    private TextView tvTitle,dob,gender;
    private boolean updated, foreceEdit;
    private boolean updateInProgress;
    Spinner spino;
    List<String> list;
    ArrayAdapter<String> SpinnerAdapter;
    private SwitchCompat switchProfileVisibility;
    private TextView textProfileVisibility;
    String dateRegEx="[0-3]{1}[0-9]{1}/[0-1]{1}[1-2]{1}/[1-9]{1}[0-9]{3}$";
    Button mUpdateFamily,mUpdateAbout,mUpdateDesire;
    String token;
    private int year,month,day;
    String genderofuser;
    String[] courses = { "Male", "Female", "Other" };
    Dropdown dropdown;

    String [] Occupation={"Bussiness","Private","Goverment/Public","Defence","Civil Services","Other"};
    //System.out.println(Pattern.matches(dateRegEx, "01/01/1990"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_activity);
        drService = ApiUtils.getClient().create(DrService.class);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        initUi();
       // profileMe = getIntent().getParcelableExtra(DATA_EXTRA_USER);
        foreceEdit = getIntent().getBooleanExtra(DATA_EXTRA_FORCE_EDIT, false);

        profileMe = Helper.getProfileMe(new SharedPreferenceUtil(getApplicationContext()));

        if (profileMe.getReferal().contains("SNR")){
            referalCode.setEnabled(false);
        }



        spino.setOnItemSelectedListener(this);

        // Create the instance of ArrayAdapter
        // having the list of courses
        ArrayAdapter ad
                = new ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item,
                courses);

        // set simple layout resource file
        // for each item of spinner
        ad.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        // Set the ArrayAdapter (ad) data on the
        // Spinner which binds data to spinner
        spino.setAdapter(ad);












        genderofuser="m";

        Log.d("Seraj",sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null));

         token=sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null);


        if (foreceEdit) {
            Toast.makeText(this, R.string.profile_basics_setup, Toast.LENGTH_LONG).show();
        }




        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(999);
            }
        });

        getDropdownData(drService,new SharedPreferenceUtil(this));
    }


    private void getDropdownData(DrService drService, SharedPreferenceUtil sharedPreferenceUtil) {
        drService.getDropdown(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null)).enqueue(new Callback<Dropdown>() {
            @Override
            public void onResponse(Call<Dropdown> call, Response<Dropdown> response) {
                if (response.isSuccessful()){
                    dropdown=response.body();
                    Log.d("serajd","Dropdown Loaded");
                    if (dropdown!=null){
                        Log.d("serajd","d loaded");

                        Log.d("serajd", "d loaded");
                        for (int i = 0; i < dropdown.getCity().size(); i++) {
                            Log.d("serajc", "" + dropdown.getCity().get(i).getValue());
                            City.add("" + dropdown.getCity().get(i).getValue());
                        }

                        for (int i = 0; i < dropdown.getState().size(); i++) {
                            Log.d("serajc", "" + dropdown.getState().get(i).getValue());
                            State.add("" + dropdown.getState().get(i).getValue());
                        }
                    }

                    setDropDowns(dropdown);
                    setDetails();
                }
            }

            @Override
            public void onFailure(Call<Dropdown> call, Throwable t) {
                Log.d("serajd","Dropdown fail");
            }
        });
    }

    private void setDropDowns(Dropdown dropdown) {

        adapterCity=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,City);
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(adapterCity);

        adapterState=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,State);
        adapterState.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapterState);

        adapterOccupation=new ArrayAdapter<CharSequence>(this,android.R.layout.simple_spinner_item,Occupation);
        adapterOccupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        occupation.setAdapter(adapterOccupation);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };



    private void showDate(int year, int month, int day) {
        dob.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            if (requestCode == Picker.PICK_IMAGE_DEVICE) {
                if (imagePicker == null) {
                    imagePicker = new ImagePicker(this);
                    imagePicker.setImagePickerCallback(this);
                }
                imagePicker.submit(data);
            } else if (requestCode == Picker.PICK_IMAGE_CAMERA) {
                if (cameraPicker == null) {
                    cameraPicker = new CameraImagePicker(this);
                    cameraPicker.setImagePickerCallback(this);
                    cameraPicker.reinitialize(pickerPath);
                }
                cameraPicker.submit(data);
            } else if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE) {
                Uri imageUri = CropImage.getPickImageResultUri(this, data);
                CropImage.activity(imageUri).setAspectRatio(1, 1).start(this);
            } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult cropResult = data.getParcelableExtra(CropImage.CROP_IMAGE_EXTRA_RESULT);
                if (cropResult != null && cropResult.getUri() != null) {
                    mediaFile = new File(cropResult.getUri().getPath());
                    setStartUpload();
                }
            }
        }
    }

    private void setDetails() {
       if (profileMe != null){
           Log.d("Seraj", "s"+profileMe.getFname());
           userName.setText(profileMe.getName());
           fname.setText(profileMe.getFname());
           mname.setText(profileMe.getMname());
           occupation.setSelection(indexof(""+profileMe.getOccupation(),Occupation));
           city.setSelection(listindexof(""+profileMe.getCity(), City));
           state.setSelection(listindexof(""+profileMe.getState(),State));
           Log.d("serajps",""+profileMe.getCity()+""+profileMe.getState());
           dob.setText(profileMe.getDob());
           referalCode.setText(""+profileMe.getReferal());
           Glide.with(this)
                   .load(profileMe.getImage())
                   .apply(RequestOptions.bitmapTransform(new RoundedCorners(Helper.dp2px(this, 8))))
                   .into(userImage);
           switchProfileVisibility.setChecked(profileMe.getIs_private() == 0);
           textProfileVisibility.setText(getString(profileMe.getIs_private() == 0 ? R.string.profile_public : R.string.profile_private));
           textProfileVisibility.setTextColor(ContextCompat.getColor(this, profileMe.getIs_private() == 0 ? R.color.colorAccent : R.color.black));
       }
    }

    private int listindexof(String s, List<String> TYPES) {
        int index = -1;
        for (int i=0;i<TYPES.size();i++) {
            if (TYPES.get(i).equalsIgnoreCase(s)) {
                index = i;
                break;
            }
        }
        return index;
    }
    private int indexof(String s, String[] TYPES) {
        int index = -1;
        for (int i=0;i<TYPES.length;i++) {
            if (TYPES[i].equalsIgnoreCase(s)) {
                index = i;
                break;
            }
        }
        return index;
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
            actionBar.setTitle(R.string.edit_profile);
        }

        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setTypeface(Helper.getMontserratBold(this));
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        userImage = findViewById(R.id.userImage);
        userName=findViewById(R.id.fullName);
        fname = findViewById(R.id.fname);
        mname = findViewById(R.id.mName);
        occupation = findViewById(R.id.occupation);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        dob=findViewById(R.id.dob);
        referalCode=findViewById(R.id.referal);
        if(referalCode.getText().length()>5){
            referalCode.setEnabled(false);
        }
         spino = findViewById(R.id.coursesspinner);
        progress = findViewById(R.id.progress);
        switchProfileVisibility = findViewById(R.id.switchProfileVisibility);
        textProfileVisibility = findViewById(R.id.textProfileVisibility);
        switchProfileVisibility.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                profileMe.setIs_private(isChecked ? 0 : 1);
                textProfileVisibility.setText(getString(profileMe.getIs_private() == 0 ? R.string.profile_public : R.string.profile_private));
                textProfileVisibility.setTextColor(ContextCompat.getColor(EditProfileActivityActivity.this, profileMe.getIs_private() == 0 ? R.color.colorAccent : R.color.black));
            }
        });
        ImageView pickImage = findViewById(R.id.pickImage);
        pickImage.bringToFront();
        pickImage.setOnClickListener(view -> {
            if (checkStoragePermissions()) {
                CropImage.startPickImageActivity(EditProfileActivityActivity.this);
//                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditProfileActivityActivity.this);
//                    alertDialog.setMessage("Get icon_picture from");
//                    alertDialog.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//
//                            cameraPicker = new CameraImagePicker(EditProfileActivityActivity.this);
//                            cameraPicker.shouldGenerateMetadata(true);
//                            cameraPicker.shouldGenerateThumbnails(true);
//                            cameraPicker.setImagePickerCallback(EditProfileActivityActivity.this);
//                            pickerPath = cameraPicker.pickImage();
//                        }
//                    });
//                    alertDialog.setNegativeButton("Gallery", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            dialogInterface.dismiss();
//
//                            imagePicker = new ImagePicker(EditProfileActivityActivity.this);
//                            imagePicker.shouldGenerateMetadata(true);
//                            imagePicker.shouldGenerateThumbnails(true);
//                            imagePicker.setImagePickerCallback(EditProfileActivityActivity.this);
//                            imagePicker.pickImage();
//                        }
//                    });
//                    alertDialog.create().show();
            } else {
                ActivityCompat.requestPermissions(EditProfileActivityActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_PERMISSION);
            }
        });

        done = findViewById(R.id.done);
        done.setOnClickListener(view -> {
            if (TextUtils.isEmpty(userName.getText())) {
                Toast.makeText(EditProfileActivityActivity.this, R.string.err_field_name, Toast.LENGTH_SHORT).show();
                return;
            }

            updateProfile();
        });
    }

    private boolean checkStoragePermissions() {
        return
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        &&
                        ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void updateProfile() {
        setUpdateProgress(true);
        drService.createUpdateUser(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null),
                new UserUpdateRequest(genderofuser,
                        userName.getText().toString(),
                        fname.getText().toString(),
                        mname.getText().toString(),
                        occupation.getSelectedItem().toString(),
                        city.getSelectedItem().toString(),
                        state.getSelectedItem().toString(),
                        dob.getText().toString(),
                        referalCode.getText().toString(),
                        profileMe.getImage(),
                        OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId(),
                        profileMe.getNotification_on_like(),
                        profileMe.getNotification_on_dislike(),
                        profileMe.getNotification_on_comment(),
                        profileMe.getIs_private()

                ), 1).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                setUpdateProgress(false);
                if (response.isSuccessful()) {
                    profileMe.setGender(response.body().getGender());
                    profileMe.setName(response.body().getName());
                    profileMe.setImage(response.body().getImage());
                    Helper.setLoggedInUser(sharedPreferenceUtil, response.body());
                    Toast.makeText(EditProfileActivityActivity.this, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                    sharedPreferenceUtil.setBooleanPreference(Constants.KEY_UPDATED, true);
                    updated = true;
                    onBackPressed();
                }
                else{

                    try {
                        Log.d("serajimage",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(EditProfileActivityActivity.this, "Please Select an image and try again", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                setUpdateProgress(false);
                Toast.makeText(EditProfileActivityActivity.this, R.string.something_wrong+t.getMessage(), Toast.LENGTH_SHORT).show();
                t.getMessage();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (updateInProgress) {
            Toast.makeText(this, R.string.process_wait, Toast.LENGTH_LONG).show();
        } else {
            if (foreceEdit) {
                if (updated)
                    super.onBackPressed();
                else
                    Toast.makeText(this, R.string.profile_edit_msg, Toast.LENGTH_LONG).show();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onImagesChosen(List<ChosenImage> images) {
        if (images != null && !images.isEmpty()) {
            mediaFile = new File(Uri.parse(images.get(0).getOriginalPath()).getPath());
            setStartUpload();
        }
    }

    private void setStartUpload() {
        setUpdateProgress(true);
        Glide.with(this)
                .load(mediaFile)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(Helper.dp2px(this, 8))).placeholder(R.drawable.placeholder))
                .into(userImage);

        FirebaseUploader firebaseUploader = new FirebaseUploader(new FirebaseUploader.UploadListener() {
            @Override
            public void onUploadFail(String message) {
                setUpdateProgress(false);
                Toast.makeText(EditProfileActivityActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUploadSuccess(String downloadUrl) {
                profileMe.setImage(downloadUrl);
                drService.createUpdateUser(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null),
                        new UserUpdateRequest(genderofuser,
                                profileMe != null ? profileMe.getName() : userName.getText().toString(),
                                fname.getText().toString(),
                                mname.getText().toString(),
                                occupation.getSelectedItem().toString(),
                                city.getSelectedItem().toString(),
                                state.getSelectedItem().toString(),
                                dob.getText().toString(),
                                referalCode.getText().toString(),
                                downloadUrl,
                                OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId(),
                                profileMe.getNotification_on_like(),
                                profileMe.getNotification_on_dislike(),
                                profileMe.getNotification_on_comment(),
                                profileMe.getIs_private()), 0).enqueue(new Callback<UserResponse>() {
                    @Override
                    public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                        setUpdateProgress(false);
                        if (response.isSuccessful()) {
                            profileMe.setGender(response.body().getGender());
                            profileMe.setName(response.body().getName());
                            profileMe.setImage(response.body().getImage());
                            Helper.setLoggedInUser(sharedPreferenceUtil, response.body());
                            Toast.makeText(EditProfileActivityActivity.this, R.string.image_updated, Toast.LENGTH_SHORT).show();
                            sharedPreferenceUtil.setBooleanPreference(Constants.KEY_UPDATED, true);
                        } else {
                            Toast.makeText(EditProfileActivityActivity.this, R.string.something_wrong_update_image, Toast.LENGTH_SHORT).show();
                            try {
                                Log.d("serajimage",response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserResponse> call, Throwable t) {
                        setUpdateProgress(false);
                        t.getMessage();
                        Toast.makeText(EditProfileActivityActivity.this, R.string.something_wrong_update_image, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onUploadProgress(int progress) {

            }

            @Override
            public void onUploadCancelled() {

            }
        });
        firebaseUploader.setReplace(true);
        firebaseUploader.uploadImage(this, mediaFile);
    }

    private void setUpdateProgress(boolean inProgress) {
        updateInProgress = inProgress;
        progress.setVisibility(inProgress ? View.VISIBLE : View.INVISIBLE);
        done.setBackground(ContextCompat.getDrawable(this, inProgress ? R.drawable.rounded_bg_color_gray : R.drawable.rounded_bg_color_primary));
        done.setClickable(!inProgress);
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    public static Intent newInstance(Context context, UserResponse userMe, boolean forceEdit) {
        Intent intent = new Intent(context, EditProfileActivityActivity.class);
        intent.putExtra(DATA_EXTRA_USER, userMe);
        intent.putExtra(DATA_EXTRA_FORCE_EDIT, forceEdit);
        return intent;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

           if (courses[i].equalsIgnoreCase("male")){
               genderofuser="m";
           }
        if (courses[i].equalsIgnoreCase("female")){
            genderofuser="f";
        }
        if (courses[i].equalsIgnoreCase("other")){
            genderofuser="o";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
