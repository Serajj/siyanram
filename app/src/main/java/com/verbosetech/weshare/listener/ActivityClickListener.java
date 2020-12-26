package com.verbosetech.weshare.listener;

import com.verbosetech.weshare.model.Activity;
import com.verbosetech.weshare.network.request.FollowRequestReview;

public interface ActivityClickListener {
    void onActivityClick(Activity activity, int pos);

    void onActivityFollowRequestClick(Activity activity, FollowRequestReview followRequestReview, int pos);
}
