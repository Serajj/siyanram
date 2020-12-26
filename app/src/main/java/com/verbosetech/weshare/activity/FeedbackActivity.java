package com.verbosetech.weshare.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.verbosetech.weshare.R;
import com.verbosetech.weshare.util.Helper;

/**
 * Created by a_man on 27-12-2017.
 */

public class FeedbackActivity extends AppCompatActivity {
    private TextView tvTitle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        tvTitle = findViewById(R.id.tv_title);


        tvTitle.setTypeface(Helper.getMontserratBold(this));
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
