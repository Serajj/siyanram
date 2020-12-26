package com.verbosetech.weshare.network.request;

/**
 * Created by a_man on 05-12-2017.
 */

public class UserUpdateRequest {
    private String gender, name,fname,mname,occupation,city,state,dob,referal, fcm_registration_id, image;
    private boolean notification_on_like, notification_on_dislike, notification_on_comment;
    private int is_private = 0;

    public UserUpdateRequest(String gender, String fcm_registration_id, boolean notification_on_like, boolean notification_on_dislike, boolean notification_on_comment, int isPrivate) {
        this.gender = gender;
        this.fcm_registration_id = fcm_registration_id;
        this.notification_on_like = notification_on_like;
        this.notification_on_dislike = notification_on_dislike;
        this.notification_on_comment = notification_on_comment;
        this.is_private = isPrivate;
    }

    public UserUpdateRequest(String gender, String name, String fname, String mname, String occupation, String city, String state, String dob,String referal, String image, String fcm_registration_id, boolean notification_on_like, boolean notification_on_dislike, boolean notification_on_comment, int isPrivate) {
        this.gender = gender;
        this.name = name;
        this.fname = fname;
        this.mname = mname;
        this.occupation = occupation;
        this.city = city;
        this.state = state;
        this.dob = dob;
        this.referal=referal;
        this.image = image;
        this.fcm_registration_id = fcm_registration_id;
        this.notification_on_like = notification_on_like;
        this.notification_on_dislike = notification_on_dislike;
        this.notification_on_comment = notification_on_comment;
        this.is_private = isPrivate;
    }
}
