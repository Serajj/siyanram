package com.verbosetech.weshare.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.JsonObject;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.verbosetech.weshare.BuildConfig;
import com.verbosetech.weshare.R;
import com.verbosetech.weshare.model.ContactModel;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    Button payBtn;
    String token;
    private UserResponse userMe;
    int cash;
    FirebaseUser firebaseUser;
    SharedPreferenceUtil sharedPreferenceUtil;
    LinearLayout contactLl,profileLl;

    private DrService drService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Checkout.preload(getApplicationContext());

        sharedPreferenceUtil=new SharedPreferenceUtil(this);
        payBtn=findViewById(R.id.payBtn);
        profileLl=findViewById(R.id.profile_ll);
        contactLl=findViewById(R.id.contact_ll);
        userMe = Helper.getLoggedInUser(sharedPreferenceUtil);
         firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        firebaseUser.getPhoneNumber();

        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });

        if(sharedPreferenceUtil.getPayViewType()==1){
            contactLl.setVisibility(View.VISIBLE);
            cash=2000;
        }else{
            profileLl.setVisibility(View.VISIBLE);
            cash=20000;
        }

        drService = ApiUtils.getClient().create(DrService.class);
        token=sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null);
    }

    private void startPayment() {
        final Activity activity=this;
        final Checkout co = new Checkout();

        try {
            JSONObject options= new JSONObject();
            options.put("name", "SIYANRAM");
            options.put("description", "For profile");
            options.put("image", BuildConfig.BASE_URL+"logo.png");
            options.put("theme.color","#8E24AA");
            options.put("currency", "INR");
            options.put("amount", cash);//pass amount in currency subunits
            options.put("prefill.email", firebaseUser.getPhoneNumber()!=null ?firebaseUser.getEmail() :"No mail");
            options.put("prefill.contact",firebaseUser.getPhoneNumber()!=null ?firebaseUser.getPhoneNumber() :"+910000000000");
            co.open(activity, options);

        }catch (Exception e){

        }

    }

    @Override
    public void onPaymentSuccess(String s) {
      if (sharedPreferenceUtil.getPayViewType()==1){
          payForContact();
      }else{
          payforprofile(s);
      }
    }

    private void payForContact() {
        drService.addcontact(token,sharedPreferenceUtil.getHisUid()).enqueue(new Callback<List<ContactModel>>() {
            @Override
            public void onResponse(Call<List<ContactModel>> call, Response<List<ContactModel>> response) {
                if (response.isSuccessful()){
                    finish();
                }
            }

            @Override
            public void onFailure(Call<List<ContactModel>> call, Throwable t) {

            }
        });
    }

    private void payforprofile(String s) {
        drService.paymentUpdate(token,"yes").enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful()){
                    Log.d("Serajpay","payment success"+s);
                    Helper.setPaid(sharedPreferenceUtil,true);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, ""+s, Toast.LENGTH_SHORT).show();
        Log.d("Seraj","error payment fail: "+s);
    }
}