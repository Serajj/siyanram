package com.verbosetech.weshare.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.verbosetech.weshare.R;
import com.verbosetech.weshare.network.response.ProfileResponse;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.SharedPreferenceUtil;

public class EditOptionActivity extends AppCompatActivity {

    RelativeLayout mUpdateFamily,mUpdateAbout,mUpdateDesire,infoupdate,updateedu;
    SharedPreferenceUtil sharedPreferenceUtil;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_option);
        getSupportActionBar().setTitle("Edit");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);

        mUpdateAbout=findViewById(R.id.update_about);
        mUpdateFamily=findViewById(R.id.update_family);
        mUpdateDesire=findViewById(R.id.update_desire);
        updateedu=findViewById(R.id.update_edu);
        infoupdate=findViewById(R.id.info_update);

        token=sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null);

        mUpdateDesire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditOptionActivity.this,AddDetailActivity.class);
                intent.putExtra("layout",3);
                intent.putExtra("token",token);
                startActivity(intent);

            }
        });

        updateedu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditOptionActivity.this,AddDetailActivity.class);
                intent.putExtra("layout",2);
                intent.putExtra("token",token);
                startActivity(intent);

            }
        });
        mUpdateFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditOptionActivity.this,AddDetailActivity.class);
                intent.putExtra("layout",1);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });
        mUpdateAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditOptionActivity.this,AddDetailActivity.class);
                intent.putExtra("layout",0);
                intent.putExtra("token",token);
                startActivity(intent);
            }
        });
        infoupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProfileResponse profileMe = Helper.getProfileMe(new SharedPreferenceUtil(getApplicationContext()));
                if (profileMe != null)
                    startActivity(EditProfileActivityActivity.newInstance(getApplicationContext(), profileMe, false));
                finish();



              //  Intent intent=new Intent(EditOptionActivity.this,EditProfileActivityActivity.class);
              //  intent.putExtra("token",token);
              //  startActivity(intent);
            }
        });
    }
}