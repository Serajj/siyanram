package com.verbosetech.weshare.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.onesignal.OneSignal;
import com.verbosetech.weshare.model.PayModel;
import com.verbosetech.weshare.network.ApiError;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.ErrorUtils;
import com.verbosetech.weshare.network.request.UserUpdateRequest;
import com.verbosetech.weshare.network.response.UserResponse;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.verbosetech.weshare.R;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is used as the starting Activity to display animation
 */
public class SplashScreenActivity extends AppCompatActivity {
    private static final int RC_GOOGLE_SIGN_IN = 9001;
    private static final String TAG ="Seraj" ;

    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager facebookCallbackManager;
    private LoginButton facebookLoginButton;
    private FirebaseAuth mAuth,firebaseAuth;

    private SharedPreferenceUtil sharedPreferenceUtil;

    private LinearLayout authOptionsContainer,otpsendLayout,otpVerifyLayout;
    private SignInButton google_sign_in_button;
    private ProgressBar authProgress;
    EditText cPhone,cOtp;
    Button sendOtpBtn,verifyBtn;
    String verificationId;
    DrService drService;
    PhoneAuthProvider.ForceResendingToken token;


    private String post_id_deep_linked;
    private View titleContainer;


    @Override
    public void onStart() {
        super.onStart();
        // FirebaseDynamicLinks init
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                // Get deep link from result (may be null if no link is found)
                if (pendingDynamicLinkData != null) {
                    Uri deepLink = pendingDynamicLinkData.getLink();
                    if (deepLink.getBooleanQueryParameter("post", false)) {
                        post_id_deep_linked = deepLink.getQueryParameter("post");
                    }
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("DYNAMICLINK", "getDynamicLink:onFailure", e);
            }
        });
    }

    private void setPaymentpref(DrService drService, SharedPreferenceUtil sharedPreferenceUtil) {
        Log.d("serajpay","settitng");
        drService.paymentStatus(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null)).enqueue(new Callback<PayModel>() {
            @Override
            public void onResponse(Call<PayModel> call, Response<PayModel> response) {
                if (response.isSuccessful()){
                    Log.d("serajpayres","success");
                    if (response.body().getIsPaid().equalsIgnoreCase("yes")){
                        Helper.setPaid(sharedPreferenceUtil,true);
                        Log.d("serajpayres","done");
                    }else{
                        Helper.setPaid(sharedPreferenceUtil,false);
                    }
                }
            }

            @Override
            public void onFailure(Call<PayModel> call, Throwable t) {

            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        otpsendLayout=findViewById(R.id.sendotpLayout);
        otpVerifyLayout=findViewById(R.id.enterotpLayout);
        cPhone=findViewById(R.id.cust_phone);
        cOtp=findViewById(R.id.cust_otp);

        sendOtpBtn= findViewById(R.id.send_otp_btn);
        verifyBtn=findViewById(R.id.submit_otp_btn);
        firebaseAuth=FirebaseAuth.getInstance();

        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        drService = ApiUtils.getClient().create(DrService.class);
        setPaymentpref(drService,sharedPreferenceUtil);

        sendOtpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(cPhone.getText())&& cPhone.getText().toString().length()==10){
                    String mobile="+91"+cPhone.getText();
                    sendOtpBtn.setEnabled(false);
                    sendOtp(mobile);
                    authProgress.setVisibility(View.VISIBLE);
                }else{
                    cPhone.setError("Please Enter Valid Mobile First..");
                }
            }
        });

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(cOtp.getText())&& cOtp.getText().toString().length()==6){
                    String otp=cOtp.getText().toString();
                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(verificationId,otp);
                    verifyBtn.setEnabled(false);
                    authProgress.setVisibility(View.VISIBLE);
                    verifyAuth(credential);

                }else{
                    cPhone.setError("Please Enter Valid Mobile First..");
                }
            }
        });





        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        initUi();
        if (user != null) {
            refreshToken(user);
        } else {
            showTermandCon();
            setupAuth();
            OneSignal.provideUserConsent(true);
            OneSignal.startInit(this)
                    .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                    .init();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user == null) {
                    showAuthOptions();
                }
            }
        }, 2000);
    }

    private void showTermandCon() {
        Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_term);

        Button accept=dialog.findViewById(R.id.term_acceptBtn);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        dialog.show();
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void sendOtp(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(mobile, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks(){

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                authProgress.setVisibility(View.GONE);
                otpsendLayout.setVisibility(View.GONE);

                otpVerifyLayout.setVisibility(View.VISIBLE);
                verificationId=s;
                token=forceResendingToken;
                Log.i(TAG,"OTP sent");

            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Log.i(TAG,"Verification Failed");
            }
        });
    }

    private void verifyAuth(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.i(TAG,"OTP Verified");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    getToken(user);
                }
                else{
                    verifyBtn.setEnabled(true);
                    authProgress.setVisibility(View.GONE);
                    cPhone.setError("OTP not valid..");
                }
            }
        });
    }

    private void refreshToken(FirebaseUser user) {
        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    Log.d("Authorization", "Bearer " + idToken);
                    sharedPreferenceUtil.setStringPreference(Constants.KEY_API_KEY, "Bearer " + idToken);
                    //openActivity(isPaid ? post_id_deep_linked != null ? DetailHomeItemActivity.newIntent(SplashScreenActivity.this, post_id_deep_linked) : new Intent(SplashScreenActivity.this, MainActivity.class) : new Intent(SplashScreenActivity.this, StripePaymentActivity.class));
                    openActivity(post_id_deep_linked != null ? DetailHomeItemActivity.newIntent(SplashScreenActivity.this, post_id_deep_linked) : new Intent(SplashScreenActivity.this, MainActivity.class));
                } else {
                    Log.e(SplashScreenActivity.class.getName(), task.getException().getMessage());
                    // Handle error -> task.getException();
                    Toast.makeText(SplashScreenActivity.this, "Unable to connect, kindly retry.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupAuth() {
        mAuth = FirebaseAuth.getInstance();

        facebookCallbackManager = CallbackManager.Factory.create();
        facebookLoginButton.setPermissions(Arrays.asList("email", "public_profile"));
        facebookLoginButton.registerCallback(facebookCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                authProgress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("FacebookLogin", exception.toString());
                authProgress.setVisibility(View.INVISIBLE);
            }
        });
        facebookLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authProgress.setVisibility(View.VISIBLE);
                google_sign_in_button.setClickable(false);
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestIdToken(getString(R.string.web_client_id)).build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(mGoogleSignInClient.getSignInIntent(), RC_GOOGLE_SIGN_IN);
                authProgress.setVisibility(View.VISIBLE);
                facebookLoginButton.setClickable(false);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        Log.d("FacebookLogin", "handleFacebookAccessToken:" + accessToken);
        Toast.makeText(SplashScreenActivity.this, R.string.profile_fetch, Toast.LENGTH_SHORT).show();
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    getToken(user);
                } else {
                    authProgress.setVisibility(View.INVISIBLE);
                    if (task.getException() != null && task.getException().getMessage() != null) {
                        Toast.makeText(SplashScreenActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // If sign in fails, display a message to the user.
                    Log.w("FacebookLogin", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void initUi() {
        TextView title = findViewById(R.id.title);
        title.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Montserrat_Bold.ttf"));
        titleContainer = findViewById(R.id.titleContainer);
        authOptionsContainer = findViewById(R.id.authOptionsContainer);
        facebookLoginButton = findViewById(R.id.login_button);
        google_sign_in_button = findViewById(R.id.google_sign_in_button);
        authProgress = findViewById(R.id.authProgress);

        TextView gSignInButtonText = (TextView) google_sign_in_button.getChildAt(0);
        if (gSignInButtonText != null)
            gSignInButtonText.setText("Google");
    }

    private void showAuthOptions() {
        Animation slide_up = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slide_up.setFillAfter(true);
        authOptionsContainer.setVisibility(View.VISIBLE);
        authOptionsContainer.startAnimation(slide_up);
        titleContainer.animate().translationY(-0.5f * authOptionsContainer.getHeight()).setDuration(600).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            Log.i(TAG,"working"+data.getExtras());
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.i(TAG, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                Log.i(TAG,account.getEmail().toString());
            } catch (ApiException e) {
                authProgress.setVisibility(View.INVISIBLE);
                // Google Sign In failed, update UI appropriately
                Log.w("GoogleSignIn", "Google sign in failed", e);
                Log.i(TAG, "Google sign in failed", e);
                // ...
            }
        }
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("GoogleSignIn", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Toast.makeText(SplashScreenActivity.this, R.string.profile_fetch, Toast.LENGTH_SHORT).show();
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    getToken(user);
                } else {
                    authProgress.setVisibility(View.INVISIBLE);
                    if (task.getException() != null && task.getException().getMessage() != null) {
                        Toast.makeText(SplashScreenActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                    // If sign in fails, display a message to the user.
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.getException());
                }
            }
        });
    }

    private void getToken(FirebaseUser user) {
        user.getIdToken(false).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                String idToken = getTokenResult.getToken();
                sharedPreferenceUtil.setStringPreference(Constants.KEY_API_KEY, "Bearer " + idToken);
                Log.d("Authorization", "Bearer " + idToken);
                getUser(idToken);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                authProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(SplashScreenActivity.this, "Token retrieve error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUser(String token) {
        DrService service = ApiUtils.getClient().create(DrService.class);
        service.createUpdateUser("Bearer " + token,
                new UserUpdateRequest("m",
                        OneSignal.getPermissionSubscriptionState().getSubscriptionStatus().getUserId(),
                        true,
                        true,
                        true, 0), 0).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(@NonNull Call<UserResponse> call, @NonNull Response<UserResponse> response) {
                Log.d("createUpdateUser", response.toString());
                if (response.isSuccessful()) {
                    Helper.setLoggedInUser(sharedPreferenceUtil, response.body());
                    openActivity(post_id_deep_linked != null ? DetailHomeItemActivity.newIntent(SplashScreenActivity.this, post_id_deep_linked) : new Intent(SplashScreenActivity.this, MainActivity.class));
                } else {
                    ApiError apiError = ErrorUtils.parseError(response);
                    Toast.makeText(SplashScreenActivity.this, apiError.status() == 417 ? "BLOCKED by admin." : apiError.status() == 400 ? "Something went wrong" : (TextUtils.isEmpty(apiError.message()) ? "Login failed" : apiError.message()), Toast.LENGTH_LONG).show();
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserResponse> call, @NonNull Throwable t) {
                authProgress.setVisibility(View.INVISIBLE);
                Toast.makeText(SplashScreenActivity.this, "Error: "+t.getMessage(), Toast.LENGTH_LONG).show();
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
            }
        });
    }

    private void openActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}