package com.verbosetech.weshare.network;

import com.verbosetech.weshare.model.Activity;
import com.verbosetech.weshare.model.BannerModel;
import com.verbosetech.weshare.model.Comment;
import com.verbosetech.weshare.model.ContactModel;
import com.verbosetech.weshare.model.Detail;
import com.verbosetech.weshare.model.Dropdown;
import com.verbosetech.weshare.model.PayModel;
import com.verbosetech.weshare.model.Post;
import com.verbosetech.weshare.model.UserByCat;
import com.verbosetech.weshare.network.request.CreateCommentRequest;
import com.verbosetech.weshare.network.request.CreatePostRequest;
import com.verbosetech.weshare.network.request.FollowRequestReview;
import com.verbosetech.weshare.network.request.PaymentRequest;
import com.verbosetech.weshare.network.request.ReportUserRequest;
import com.verbosetech.weshare.network.request.UserUpdateRequest;
import com.verbosetech.weshare.network.response.BaseListModel;
import com.verbosetech.weshare.network.response.CreatePostResponse;
import com.verbosetech.weshare.network.response.FollowRequest;
import com.verbosetech.weshare.network.response.LikeDislikeResponse;
import com.verbosetech.weshare.network.response.ProfileFollowRequestResponse;
import com.verbosetech.weshare.network.response.ProfileFollowRequestReviewResponse;
import com.verbosetech.weshare.network.response.ProfileResponse;
import com.verbosetech.weshare.network.response.ReportUserResponse;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.network.response.PaymentResponse;
import com.verbosetech.weshare.network.response.ProfileFollowResponse;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by a_man on 05-12-2017.
 */

public interface DrService {

    @Headers("Accept: application/json")
    @POST("api/profile")
    Call<UserResponse> createUpdateUser(@Header("Authorization") String token, @Body UserUpdateRequest userRegisterResponse, @Query("update") int update);



    @Headers("Accept: application/json")
    @POST("api/profile/updt")
    Call<UserResponse> updatedetail(@Header("Authorization") String token, @Body UserUpdateRequest userRegisterResponse, @Query("update") int update);


    @Headers("Accept: application/json")
    @POST("api/profile/imageupdate")
    Call<UserResponse> imageUpdate(@Header("Authorization") String token, @Query("image") String image);

    @Headers("Accept: application/json")
    @POST("api/profile/detail")
    Call<Detail> detail(@Header("Authorization") String token,
                        @Query("dheshouldbe") String dheshouldbe, @Query("dageofaround") String dageofaround, @Query("dmartialstatus") String dmartialstatus, @Query("dchallenged") String dchallenged, @Query("dreligion") String dreligion, @Query("dmothertounge") String dmothertounge, @Query("dcaste") String dcaste, @Query("dcity") String dcity, @Query("dcountry") String dcountry, @Query("deducation") String deducation, @Query("ehighest") String ehighest, @Query("eugdegree") String eugdegree, @Query("eugcollege") String eugcollege, @Query("epgcollege") String epgcollege, @Query("epgdegree") String epgdegree, @Query("chighesteducation") String chighesteducation, @Query("cemployedin") String cemployedin, @Query("cannuanincome") String cannuanincome, @Query("ftype") String ftype, @Query("fvalue") String fvalue, @Query("fstatus") String fstatus, @Query("fincome") String fincome, @Query("foccupation") String foccupation, @Query("fbrother") String fbrother, @Query("fsister") String fsister,
                        @Query("caste") String caste,@Query("religion") String religion,@Query("height") String height,@Query("martial_status") String martial,@Query("mother_tounge") String mothertong,@Query("profilefor") String profile,@Query("complexion") String complexion,@Query("bodytype") String bodytype,@Query("aboutme") String about,@Query("selfg") String selfg,@Query("nanig") String nanig,@Query("mamag") String mamag,@Query("manglik") String manglik,@Query("type") String type);


    @Headers("Accept: application/json")
    @POST("api/profile/showdetail")
    Call<Detail> showdetail(@Header("Authorization") String token,@Query("uid") String uid);


    @Headers("Accept: application/json")
    @GET("api/profile/following/{id}")
    Call<BaseListModel<UserResponse>> getFollowings(@Header("Authorization") String token, @Path("id") String profileId, @Query("page") int page);

    @Headers("Accept: application/json")
    @GET("api/profile/followers/{id}")
    Call<BaseListModel<UserResponse>> getFollowers(@Header("Authorization") String token, @Path("id") String profileId, @Query("page") int page);

    @Headers("Accept: application/json")
    @GET("api/stories/users")
    Call<ArrayList<UserResponse>> getStoryUsers(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @GET("api/stories/users/{id}")
    Call<ArrayList<Post>> getStory(@Header("Authorization") String token, @Path("id") String profileId);

    @Headers("Accept: application/json")
    @GET("api/profile/{id}")
    Call<ProfileResponse> getProfile(@Header("Authorization") String token, @Path("id") String profileId);

    @Headers("Accept: application/json")
    @GET("api/activities")
    Call<BaseListModel<Activity>> getActivities(@Header("Authorization") String token, @Query("page") int page);

    @Headers("Accept: application/json")
    @POST("api/report/{id}")
    Call<ReportUserResponse> reportUser(@Header("Authorization") String token, @Path("id") String profileId, @Body ReportUserRequest reportUserRequest);

    @Headers("Accept: application/json")
    @POST("api/posts")
    Call<CreatePostResponse> createPost(@Header("Authorization") String token, @Body CreatePostRequest createPostRequest);

    @Headers("Accept: application/json")
    @GET("api/posts")
    Call<BaseListModel<Post>> getPosts(@Header("Authorization") String token, @Query("treding") int type, @Query("page") int page);

    @Headers("Accept: application/json")
    @GET("api/posts")
    Call<BaseListModel<Post>> getPostsByUserId(@Header("Authorization") String token, @Query("user_profile_id") String userProfileId, @Query("treding") int type, @Query("page") int page);

    @Headers("Accept: application/json")
    @GET("api/posts/me")
    Call<BaseListModel<Post>> getPostsMy(@Header("Authorization") String token, @Query("page") int page);

    @Headers("Accept: application/json")
    @GET("api/posts/{id}/show")
    Call<Post> getPostById(@Header("Authorization") String token, @Path("id") String postId);

    @Headers("Accept: application/json")
    @DELETE("api/posts/{id}/delete")
    Call<JsonObject> deletePost(@Header("Authorization") String token, @Path("id") String postId);

    @Headers("Accept: application/json")
    @GET("api/posts/{id}/report")
    Call<JsonObject> reportPost(@Header("Authorization") String token, @Path("id") String postId);

    @Headers("Accept: application/json")
    @POST("api/posts/{id}/share")
    Call<JsonObject> updateSharePost(@Header("Authorization") String token, @Path("id") String postId);

    @Headers("Accept: application/json")
    @POST("api/posts/{id}/like")
    Call<LikeDislikeResponse> updatePostLike(@Header("Authorization") String token, @Path("id") String postId);

    @Headers("Accept: application/json")
    @POST("api/posts/{id}/dislike")
    Call<LikeDislikeResponse> updatePostDislike(@Header("Authorization") String token, @Path("id") String postId);

    @Headers("Accept: application/json")
    @GET("api/posts/{id}/comments")
    Call<BaseListModel<Comment>> getComments(@Header("Authorization") String token, @Path("id") String postId, @Query("page") int page);

    @Headers("Accept: application/json")
    @POST("api/posts/{id}/comments")
    Call<Comment> createComment(@Header("Authorization") String token, @Path("id") String postId, @Body CreateCommentRequest comment);

    @Headers("Accept: application/json")
    @POST("api/comments/{id}/like")
    Call<LikeDislikeResponse> updateCommentLike(@Header("Authorization") String token, @Path("id") String commentId);

    @Headers("Accept: application/json")
    @POST("api/comments/{id}/dislike")
    Call<LikeDislikeResponse> updateCommentDislike(@Header("Authorization") String token, @Path("id") String commentId);

    @Headers("Accept: application/json")
    @POST("api/profile/search")
    Call<BaseListModel<UserResponse>> profileSearch(@Header("Authorization") String token, @Body HashMap<String, String> request, @Query("page") int page);

    @Headers("Accept: application/json")
    @POST("api/profile/follow/{id}")
    Call<ProfileFollowResponse> profileFollowAction(@Header("Authorization") String token, @Path("id") String profileId);

    @Headers("Accept: application/json")
    @GET("api/profile/follow-requests/follow/{id}")
    Call<ProfileFollowRequestResponse> profileFollowActionRequest(@Header("Authorization") String token, @Path("id") String profileId);

    @Headers("Accept: application/json")
    @GET("api/profile/follow-requests")
    Call<ArrayList<FollowRequest>> profileFollowRequests(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @POST("api/profile/follow-requests/{id}/review")
    Call<ProfileFollowRequestReviewResponse> profileFollowActionReview(@Header("Authorization") String token, @Path("id") String requestId, @Body FollowRequestReview request);

    @Headers("Accept: application/json")
    @POST("api/profile/payment")
    Call<PaymentResponse> payment(@Header("Authorization") String token, @Body PaymentRequest paymentRequest);



    //get user by category
    @Headers("Accept: application/json")
    @POST("api/profile/getUserByCat")
    Call<List<UserByCat>> getuserbycat(@Header("Authorization") String token, @Query("category") String category, @Query("subcat") String subcat);


    @Headers("Accept: application/json")
    @POST("api/profile/isPaid")
    Call<PayModel> paymentStatus(@Header("Authorization") String token);

    @Headers("Accept: application/json")
    @POST("api/profile/updatePaid")
    Call<JSONObject> paymentUpdate(@Header("Authorization") String token,@Query("paid") String pay);

    @Headers("Accept: application/json")
    @POST("api/profile/contact")
    Call<List<ContactModel>> addcontact(@Header("Authorization") String token, @Query("hid") String hisUid);

    @Headers("Accept: application/json")
    @POST("api/profile/getcontactlist")
    Call<List<ContactModel>> getContactList(@Header("Authorization") String token, @Query("hid") String hisUid);


    @Headers("Accept: application/json")
    @POST("api/profile/getaddress")
    Call<UserResponse> getaddress(@Header("Authorization") String token, @Query("hid") String hisUid);


    @Headers("Accept: application/json")
    @GET("api/getbanners")
    Call<List<BannerModel>> getBannerList(@Header("Authorization") String token);


    @Headers("Accept: application/json")
    @POST("api/profile/getdropdown")
    Call<Dropdown> getDropdown(@Header("Authorization") String token);
}
