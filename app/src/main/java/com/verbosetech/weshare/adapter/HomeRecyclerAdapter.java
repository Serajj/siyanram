package com.verbosetech.weshare.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;
import com.verbosetech.weshare.activity.AddDetailActivity;
import com.verbosetech.weshare.activity.CategoryActivity;
import com.verbosetech.weshare.activity.DetailHomeItemActivity;
import com.verbosetech.weshare.activity.EditOptionActivity;
import com.verbosetech.weshare.activity.EditProfileActivityActivity;
import com.verbosetech.weshare.activity.PaymentActivity;
import com.verbosetech.weshare.activity.StatusActivity;
import com.verbosetech.weshare.activity.UserProfileDetailActivity;
import com.verbosetech.weshare.fragment.CommentsFragment;
import com.verbosetech.weshare.fragment.HomeFeedsFragment;
import com.verbosetech.weshare.listener.OnCommentAddListener;
import com.verbosetech.weshare.listener.OnPopupMenuItemClickListener;
import com.verbosetech.weshare.model.BannerModel;
import com.verbosetech.weshare.model.Dropdown;
import com.verbosetech.weshare.model.Post;
import com.verbosetech.weshare.model.LikeDislikeScoreUpdate;
import com.verbosetech.weshare.R;
import com.verbosetech.weshare.model.UserMeta;
import com.verbosetech.weshare.network.DrService;
import com.verbosetech.weshare.network.response.UserResponse;
import com.verbosetech.weshare.util.Constants;
import com.verbosetech.weshare.util.EasyRecyclerViewAdapter;
import com.verbosetech.weshare.util.Helper;
import com.verbosetech.weshare.util.LinkTransformationMethod;
import com.verbosetech.weshare.util.SharedPreferenceUtil;
import com.verbosetech.weshare.util.SpringAnimationHelper;
import com.verbosetech.weshare.network.ApiUtils;
import com.verbosetech.weshare.network.response.LikeDislikeResponse;
import com.google.gson.JsonObject;
import com.verbosetech.weshare.view.SquareVideoView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mayank on 9/7/16.
 */
public class HomeRecyclerAdapter extends EasyRecyclerViewAdapter<Post> {
    private Fragment fragment;
    private Context context;
    UserResponse profileMe ;
    private HashMap<String, LikeDislikeScoreUpdate> likeDislikeUpdateMap;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private DrService foxyService;
    private ArrayList<UserResponse> storyUsers;

    List<BannerModel> bannerModels;

    public HomeRecyclerAdapter(Fragment fragment) {
        this.context = fragment.getContext();
        this.fragment = fragment;
        this.storyUsers = new ArrayList<>();
        likeDislikeUpdateMap = new HashMap<>();
        sharedPreferenceUtil = new SharedPreferenceUtil(fragment.getContext());
        profileMe = Helper.getLoggedInUser(new SharedPreferenceUtil(context));
        foxyService = ApiUtils.getClient().create(DrService.class);
        this.bannerModels=new ArrayList<>();
        getBanners(foxyService);
    }

    private void getBanners(DrService foxyService) {
        String token=sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null);
        foxyService.getBannerList(token).enqueue(new Callback<List<BannerModel>>() {
            @Override
            public void onResponse(Call<List<BannerModel>> call, Response<List<BannerModel>> response) {
                if (response.isSuccessful()){
                    Log.d("serajbanner","response success");
                    if (response.body().size()>0){
                        Log.d("serajbanner",response.body().size()+"");
                        bannerModels=response.body();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<BannerModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemsListSize() > position) {
            Log.d("Seraj",""+position);
            return position==1 ? 4 : position==2 ? 5 :getItem(position).getId().equalsIgnoreCase("add") ? 2 : getItem(position).getId().equalsIgnoreCase("story") || position%5==0 ? 3 : super.getItemViewType(position);
            //return getItem(position).getId().equalsIgnoreCase("add") ? 2 : getItem(position).getId().equalsIgnoreCase("story") ? 3 : super.getItemViewType(position);
        }else {
            Log.d("Seraj",""+position);
            return super.getItemViewType(position);
        }


    }

    @Override
    public RecyclerView.ViewHolder onCreateItemView(ViewGroup parent, int viewType) {
        if (viewType == 2) {
            return new AddMobViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_add, parent, false));
        } else if (viewType == 3) {
            return new AddMobViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_add, parent, false));
           // return new StoriesContainerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_story_container, parent, false));
        } else if (viewType == 4) {
            return new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.list_categoty, parent, false));
            // return new StoriesContainerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_story_container, parent, false));
        }else if (viewType == 5) {
            return new SecondAddBanner(LayoutInflater.from(context).inflate(R.layout.second_aid_banner, parent, false));
            // return new StoriesContainerViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_story_container, parent, false));
        }
        else {
            return new HomeItemViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_home, parent, false));
        }
    }

    @Override
    public void onBindItemView(RecyclerView.ViewHolder commonHolder, Post currPost, int position) {

        if (commonHolder instanceof HomeItemViewHolder && position%5!=0 && position!=1) {
            final HomeItemViewHolder holder = (HomeItemViewHolder) commonHolder;
            setPostData(holder, currPost);
            Log.d("Seraj","inside home"  + position);

            if (TextUtils.isEmpty(currPost.getTitle())) {
                holder.postTitle.setVisibility(View.GONE);
            } else {
                holder.postTitle.setVisibility(View.VISIBLE);
                holder.postTitle.setText(currPost.getTitle());
            }

            switch (currPost.getType()) {
                case "text":
                    holder.videoActionContainer.setVisibility(View.GONE);
                    holder.postText.setVisibility(View.VISIBLE);
                    holder.imageView.setVisibility(View.GONE);
                    holder.videoView.setVisibility(View.GONE);
                    holder.postText.setText(currPost.getText());
                    if (!TextUtils.isEmpty(currPost.getMedia_url())) {
                        holder.imageView.setVisibility(View.VISIBLE);
                        Glide.with(context)
                                .load(currPost.getMedia_url())
                                .apply(new RequestOptions().placeholder(R.drawable.placeholder).dontAnimate())
                                .into(holder.imageView);
                    }
                    break;
                case "image":
                    holder.videoActionContainer.setVisibility(View.GONE);
                    holder.postText.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.videoView.setVisibility(View.GONE);
                    Glide.with(context)
                            .load(currPost.getMedia_url())
                            .apply(new RequestOptions().placeholder(R.drawable.placeholder).dontAnimate())
                            .into(holder.imageView);
                    break;
                case "video":
                    holder.videoActionContainer.setVisibility(View.VISIBLE);
                    holder.postText.setVisibility(View.GONE);
                    holder.imageView.setVisibility(View.GONE);
                    holder.videoView.setVisibility(View.VISIBLE);

                    holder.videoProgress.setVisibility(View.VISIBLE);
                    holder.videoAction.setVisibility(View.GONE);

                    String videoUrl = currPost.getMedia_url();
                    holder.videoView.setVideoURI(Uri.parse(videoUrl));
                    holder.videoView.setVideoPath(videoUrl);
                    holder.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            holder.videoProgress.setVisibility(View.GONE);
                            return true;
                        }
                    });
                    holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            holder.videoProgress.setVisibility(View.GONE);
                            holder.videoAction.setVisibility(View.VISIBLE);
                            holder.videoView.seekTo(100);
                        }
                    });
                    holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            holder.videoAction.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_circle_outline_36dp));
                        }
                    });
                    holder.videoAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (holder.videoView.isPlaying()) {
                                holder.mediaStopPosition = holder.videoView.getCurrentPosition();
                                holder.videoView.pause();
                            } else {
                                holder.videoView.seekTo(holder.mediaStopPosition);
                                holder.videoView.start();
                            }
                            holder.videoAction.setImageDrawable(ContextCompat.getDrawable(context, holder.videoView.isPlaying() ? R.drawable.ic_pause_circle_outline_36dp : R.drawable.ic_play_circle_outline_36dp));
                        }
                    });
                    break;
            }
            if (currPost.getUserMetaData() != null) {
//                Glide.with(context)
//                        .load(currPost.getUserMetaData().getImage())
//                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(Helper.dp2px(context, 8))).placeholder(R.drawable.ic_person_gray_24dp))
//                        .into(holder.foxyImage);

                Glide.with(context).load(currPost.getUserMetaData().getImage())
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)).placeholder(R.drawable.placeholder))
                        .into(holder.foxyImage);
            }
        } else  {
            Log.d("Seraj","outside home"  + position);
           if (position==1){

           }else{
               // ((AddMobViewHolder) commonHolder).start();
           }
        }
    }


    @Override
    public int getItemsListSize() {
        return super.getItemsListSize();
    }

    /**
     * Binds the post data to the views in proper format.
     *
     * @param holder   {@link HomeItemViewHolder}
     * @param currPost the {@link Post} object
     */
    private void setPostData(HomeItemViewHolder holder, Post currPost) {
        holder.postText.setTransformationMethod(new LinkTransformationMethod());
        holder.postText.setMovementMethod(LinkMovementMethod.getInstance());
        holder.postTitle.setTransformationMethod(new LinkTransformationMethod());
        holder.postTitle.setMovementMethod(LinkMovementMethod.getInstance());

        String dateOfPost = currPost.getCreatedAt();

        String commentString = String.valueOf(currPost.getCommentCount()) + " " + context.getString(R.string.commented);
        String dislikeString = String.valueOf(currPost.getDislikeCount()) + " " + context.getString(R.string.find_it);
        String likeString = String.valueOf(currPost.getLikeCount()) + " " + context.getString(R.string.find_it);

        //holder.commentCount.setText(commentString);
        //holder.dislikeCount.setText(dislikeString);
        //holder.likeCount.setText(likeString);
        holder.postedTime.setText(context.getString(R.string.posted) + " " + Helper.timeDiff(dateOfPost));
        holder.userName.setText(currPost.getUserMetaData().getName());



        holder.setLikedView(currPost.getLiked() == 1);
        holder.setDislikedView(currPost.getDisliked() == 1);
    }

    public void storyShow(ArrayList<UserResponse> stories) {
        this.storyUsers.clear();
        this.storyUsers.add(new UserResponse(-1, "add", "add"));
        this.storyUsers.addAll(stories);

        if (itemsList.isEmpty() || !getItem(0).getId().equalsIgnoreCase("story")) {
            addItemOnTop(new Post("story"));
        } else {
            notifyItemChanged(0);
        }
    }

    public void storyShowMy(UserResponse userResponse) {
        if (storyUsers.isEmpty()) {
            ArrayList<UserResponse> toShow = new ArrayList<>();
            toShow.add(userResponse);
            storyShow(toShow);
        } else if (!storyUsers.contains(userResponse)) {
            storyUsers.add(1, userResponse);
            if (itemsList.isEmpty() || !getItem(0).getId().equalsIgnoreCase("story")) {
                addItemOnTop(new Post("story"));
            } else {
                notifyItemChanged(0);
            }
        }
    }

    public void storyProgress(boolean storyProgress) {
        if (!storyUsers.isEmpty()) {
            storyUsers.get(0).setStoryUpdateProgress(storyProgress);
            if (itemsList.isEmpty() || !getItem(0).getId().equalsIgnoreCase("story")) {
                addItemOnTop(new Post("story"));
            } else {
                notifyItemChanged(0);
            }
        }
    }

    class AddMobViewHolder extends RecyclerView.ViewHolder {
        private ImageSwitcher mAdView;
        SliderLayout sliderLayout;

        private int[] gallery = {R.drawable.ad_banner, R.drawable.ad_banner, R.drawable.ad_banner, R.drawable.ad_banner, R.drawable.ad_banner, R.drawable.ad_banner};


        public AddMobViewHolder(View itemView) {
            super(itemView);
            sliderLayout = itemView.findViewById(R.id.imageSlider);
            sliderLayout.setIndicatorAnimation(SliderLayout.Animations.FILL); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            sliderLayout.setScrollTimeInSec(2); //set scroll delay in seconds :
            setSliderViews(sliderLayout);
            // Set animations http://danielme.com/2013/08/18/diseno-android-transiciones-entre-activities/

        }



    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        LinearLayout male,female,divorce,caste,occupation,city,state,religion;
        Dropdown dropdown;
        List<String> Casted = new ArrayList<>();
        List<String> MotherTounge = new ArrayList<>();
        ArrayAdapter<String> adapter;
        ArrayAdapter<CharSequence> adapterc;
        String[] Occupation={"Bussiness","Private","Goverment/Public","Defence","Civil Services","Other"};
        List<String> City = new ArrayList<>();
        List<String> State = new ArrayList<>();
        String[] Religion = {"Hindu", "Muslim","Sikh","Christian","Buddhist","Jain","Parsi","Jewish","Bahai"};

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            male=itemView.findViewById(R.id.malell);
            female=itemView.findViewById(R.id.femailll);
            divorce=itemView.findViewById(R.id.divorcell);
            caste=itemView.findViewById(R.id.castell);
            occupation=itemView.findViewById(R.id.occupationll);
            city=itemView.findViewById(R.id.cityll);
            state=itemView.findViewById(R.id.statell);
            religion=itemView.findViewById(R.id.religionll);
            DrService drService=ApiUtils.getClient().create(DrService.class);
            getDropdownData(drService,new SharedPreferenceUtil(context));


            male.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(context, "male clicked", Toast.LENGTH_SHORT).show();
                  if (sharedPreferenceUtil.getIsPaid()){
                      Intent intent=new Intent(context, CategoryActivity.class);
                      intent.putExtra("category","m");
                      intent.putExtra("name","up");
                      context.startActivity(intent);
                  }else{
                      AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                      alertDialogBuilder.setTitle("Your profile details not complete...");
                      alertDialogBuilder.setMessage("Create your profile to find your partner");
                      alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              context.startActivity(new Intent(context, EditOptionActivity.class));
                          }
                      });
                      alertDialogBuilder.create().show();
                  }

                }
            });

            female.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   Toast.makeText(context, "male clicked", Toast.LENGTH_SHORT).show();
                   if (sharedPreferenceUtil.getIsPaid()){
                       Intent intent=new Intent(context, CategoryActivity.class);
                       intent.putExtra("category","f");
                       intent.putExtra("name","up");
                       context.startActivity(intent);
                   }else{
                       AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                       alertDialogBuilder.setTitle("Your profile details not complete...");
                       alertDialogBuilder.setMessage("Create your profile to find your partner");
                       alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {
                               context.startActivity(new Intent(context, EditOptionActivity.class));
                           }
                       });
                       alertDialogBuilder.create().show();
                   }

                }
            });

            divorce.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   Toast.makeText(context, "male clicked", Toast.LENGTH_SHORT).show();
                    if (sharedPreferenceUtil.getIsPaid()){
                        Intent intent=new Intent(context, CategoryActivity.class);
                        intent.putExtra("category","divorced");
                        intent.putExtra("name","up");
                        context.startActivity(intent);
                    }else{
                        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Your profile details not complete...");
                        alertDialogBuilder.setMessage("Create your profile to find your partner");
                        alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context.startActivity(new Intent(context, EditOptionActivity.class));
                            }
                        });
                        alertDialogBuilder.create().show();
                    }

                }
            });

            caste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(context, "caste clicked", Toast.LENGTH_SHORT).show();
                  if (sharedPreferenceUtil.getIsPaid()){
                      Intent intent=new Intent(context, CategoryActivity.class);

                      Dialog dialog=new Dialog(context);
                      dialog.setContentView(R.layout.additional_data_dialog);
                      TextView head=dialog.findViewById(R.id.type_head);
                      Spinner data=dialog.findViewById(R.id.data);
                      TextView done=dialog.findViewById(R.id.done);
                      TextView cancel =dialog.findViewById(R.id.cancel);

                      adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,Casted);
                      adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                      data.setAdapter(adapter);

                      done.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {

                                  String mydata=data.getSelectedItem().toString();
                                  intent.putExtra("category","caste");
                                  intent.putExtra("name",mydata);
                                  context.startActivity(intent);

                          }
                      });

                      dialog.show();

                      cancel.setOnClickListener(new View.OnClickListener() {
                          @Override
                          public void onClick(View view) {
                              if (dialog.isShowing()){
                                  dialog.dismiss();
                              }
                          }
                      });
                  }else{
                      AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                      alertDialogBuilder.setTitle("Your profile details not complete...");
                      alertDialogBuilder.setMessage("Create your profile to find your partner");
                      alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              context.startActivity(new Intent(context, EditOptionActivity.class));
                          }
                      });
                      alertDialogBuilder.create().show();
                  }


                }
            });

            religion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(context, "caste clicked", Toast.LENGTH_SHORT).show();
                 if (sharedPreferenceUtil.getIsPaid()){
                     Intent intent=new Intent(context, CategoryActivity.class);

                     Dialog dialog=new Dialog(context);
                     dialog.setContentView(R.layout.additional_data_dialog);
                     TextView head=dialog.findViewById(R.id.type_head);
                     Spinner data=dialog.findViewById(R.id.data);
                     TextView done=dialog.findViewById(R.id.done);
                     TextView cancel =dialog.findViewById(R.id.cancel);

                     adapterc=new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_item,Religion);
                     adapterc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     data.setAdapter(adapterc);

                     done.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {

                                 String mydata=data.getSelectedItem().toString();
                                 intent.putExtra("category","religion");
                                 intent.putExtra("name",mydata);
                                 context.startActivity(intent);

                         }
                     });

                     dialog.show();

                     cancel.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             if (dialog.isShowing()){
                                 dialog.dismiss();
                             }
                         }
                     });
                 }else{
                     AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                     alertDialogBuilder.setTitle("Your profile details not complete...");
                     alertDialogBuilder.setMessage("Create your profile to find your partner");
                     alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             context.startActivity(new Intent(context, EditOptionActivity.class));
                         }
                     });
                     alertDialogBuilder.create().show();
                 }


                }
            });

            city.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Toast.makeText(context, "caste clicked", Toast.LENGTH_SHORT).show();
                 if (sharedPreferenceUtil.getIsPaid()){
                     Intent intent=new Intent(context, CategoryActivity.class);

                     Dialog dialog=new Dialog(context);
                     dialog.setContentView(R.layout.additional_data_dialog);
                     TextView head=dialog.findViewById(R.id.type_head);
                     Spinner data=dialog.findViewById(R.id.data);
                     TextView done=dialog.findViewById(R.id.done);
                     TextView cancel =dialog.findViewById(R.id.cancel);
                     adapter=new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item,City);
                     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     data.setAdapter(adapter);

                     done.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                                 String mydata=data.getSelectedItem().toString();
                                 intent.putExtra("category","city");
                                 intent.putExtra("name",mydata);
                                 Log.d("serajd",mydata);
                                 context.startActivity(intent);

                         }
                     });

                     dialog.show();

                     cancel.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View view) {
                             if (dialog.isShowing()){
                                 dialog.dismiss();
                             }
                         }
                     });
                 }else{
                     AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                     alertDialogBuilder.setTitle("Your profile details not complete...");
                     alertDialogBuilder.setMessage("Create your profile to find your partner");
                     alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             context.startActivity(new Intent(context, EditOptionActivity.class));
                         }
                     });
                     alertDialogBuilder.create().show();
                 }

                }
            });

            state.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 if (sharedPreferenceUtil.getIsPaid()){
                     //  Toast.makeText(context, "caste clicked", Toast.LENGTH_SHORT).show();
                     Intent intent=new Intent(context, CategoryActivity.class);
                                 String mydata="hii";
                                 intent.putExtra("category","state");
                                 intent.putExtra("name",mydata);
                                 context.startActivity(intent);

                 }else{
                     AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                     alertDialogBuilder.setTitle("Your profile details not complete...");
                     alertDialogBuilder.setMessage("Create your profile to find your partner");
                     alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialogInterface, int i) {
                             context.startActivity(new Intent(context, EditOptionActivity.class));
                         }
                     });
                     alertDialogBuilder.create().show();
                 }

                }
            });

            occupation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  //  Toast.makeText(context, "caste clicked", Toast.LENGTH_SHORT).show();

                    if (sharedPreferenceUtil.getIsPaid()){
                        Intent intent=new Intent(context, CategoryActivity.class);

                        Dialog dialog=new Dialog(context);
                        dialog.setContentView(R.layout.additional_data_dialog);
                        TextView head=dialog.findViewById(R.id.type_head);
                        Spinner data=dialog.findViewById(R.id.data);
                        TextView done=dialog.findViewById(R.id.done);
                        TextView cancel =dialog.findViewById(R.id.cancel);

                        adapterc=new ArrayAdapter<CharSequence>(context,android.R.layout.simple_spinner_item,Occupation);
                        adapterc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        data.setAdapter(adapterc);
                        done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                    String mydata=data.getSelectedItem().toString();
                                    intent.putExtra("category","occupation");
                                    intent.putExtra("name",mydata);
                                    context.startActivity(intent);

                            }
                        });

                        dialog.show();

                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (dialog.isShowing()){
                                    dialog.dismiss();
                                }
                            }
                        });
                    }else{
                        AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Your profile details not complete...");
                        alertDialogBuilder.setMessage("Create your profile to find your partner");
                        alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                context.startActivity(new Intent(context, EditOptionActivity.class));


                            }
                        });
                        alertDialogBuilder.create().show();
                    }

                }
            });


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
                            for (int i=0;i<dropdown.getCaste().size();i++){
                                Log.d("serajc",""+dropdown.getCaste().get(i).getValue());
                                Casted.add(""+dropdown.getCaste().get(i).getValue());
                            }
                            for (int i=0;i<dropdown.getMothertounge().size();i++){
                                Log.d("serajc",""+dropdown.getCaste().get(i).getValue());
                                MotherTounge.add(""+dropdown.getMothertounge().get(i).getValue());
                            }

                            for (int i = 0; i < dropdown.getCity().size(); i++) {
                                Log.d("serajc", "" + dropdown.getCity().get(i).getValue());
                                City.add("" + dropdown.getCity().get(i).getValue());
                            }

                            for (int i = 0; i < dropdown.getState().size(); i++) {
                                Log.d("serajc", "" + dropdown.getState().get(i).getValue());
                                State.add("" + dropdown.getState().get(i).getValue());
                            }
                        }

                        //setDropDowns(dropdown);
                    }
                }

                @Override
                public void onFailure(Call<Dropdown> call, Throwable t) {
                    Log.d("serajd","Dropdown fail");
                }
            });
        }
    }

    class SecondAddBanner extends RecyclerView.ViewHolder {
        public SecondAddBanner(@NonNull View itemView) {
            super(itemView);

        }
    }

    private void setSliderViews(SliderLayout sliderLayout) {

             String image_url="https://siyaramtest.marriagemilan.in/public/images/";

        Log.d("serajbanner",image_url);

             Log.d("serajbanner",bannerModels.size()+" Bannerr Size");
            if (bannerModels.size()>3){
                for (int i = 0; i <= 3; i++) {

                    SliderView sliderView = new SliderView(context);

                    switch (i) {
                        case 0:
                            sliderView.setImageUrl(image_url+bannerModels.get(0).getBanner());
                            Log.d("serajbanner",image_url+bannerModels.get(0).getBanner());
                            break;
                        case 1:
                            sliderView.setImageUrl(image_url+bannerModels.get(1).getBanner());
                            break;
                        case 2:
                            sliderView.setImageUrl(image_url+bannerModels.get(2).getBanner());
                            break;
                        case 3:
                            sliderView.setImageUrl(image_url+bannerModels.get(3).getBanner());
                            break;
                    }

                    sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
                    sliderView.setDescription("");
                    final int finalI = i;
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                          //  Toast.makeText(context, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                        }
                    });

                    //at last add this view in your layout :
                    sliderLayout.addSliderView(sliderView);
                }
            }else{
                for (int i = 0; i <= 3; i++) {

                    SliderView sliderView = new SliderView(context);

                    switch (i) {
                        case 0:
                            sliderView.setImageDrawable(R.drawable.banner_1);
                            break;
                        case 1:
                            sliderView.setImageDrawable(R.drawable.banner_2);
                            break;
                        case 2:
                            sliderView.setImageDrawable(R.drawable.banner_3);
                            break;
                        case 3:
                            sliderView.setImageDrawable(R.drawable.banner_4);
                            break;
                    }

                    sliderView.setImageScaleType(ImageView.ScaleType.FIT_XY);
                    sliderView.setDescription("");
                    final int finalI = i;
                    sliderView.setOnSliderClickListener(new SliderView.OnSliderClickListener() {
                        @Override
                        public void onSliderClick(SliderView sliderView) {
                           // Toast.makeText(context, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT).show();
                        }
                    });

                    //at last add this view in your layout :
                    sliderLayout.addSliderView(sliderView);
                }
            }
    }

  /***  class StoriesContainerViewHolder extends RecyclerView.ViewHolder {
        private TextView playAll;
        private LinearLayout storyContainer;
        private RecyclerView recyclerStory;

        public StoriesContainerViewHolder(View itemView) {
            super(itemView);
            playAll = itemView.findViewById(R.id.playAll);
            storyContainer = itemView.findViewById(R.id.storyContainer);
            recyclerStory = itemView.findViewById(R.id.recyclerStory);
            recyclerStory.setVisibility(View.GONE);
            playAll.setOnClickListener(v -> {
                if (storyUsers != null && storyUsers.size() > 1)
                    context.startActivity(StatusActivity.newIntent(context, storyUsers, 1));
            });
        }


        public void setData() {
            recyclerStory.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            recyclerStory.setNestedScrollingEnabled(false);
            recyclerStory.setAdapter(new StoriesAdapter(storyUsers, context, new StoriesAdapter.StoryClickListener() {
                @Override
                public void showStory(int pos) {
                    context.startActivity(StatusActivity.newIntent(context, storyUsers, pos));
                }

                @Override
                public void postStory() {
                    if (storyUsers.isEmpty() || !storyUsers.get(0).isStoryUpdateProgress()) {
                        if (fragment instanceof HomeFeedsFragment) {
                            ((HomeFeedsFragment) fragment).pickMedia();
                        }
                    }
                }
            }));
            playAll.setVisibility((storyUsers != null && storyUsers.size() > 1) ? View.VISIBLE : View.GONE);
        }
    }**/

    class HomeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView foxyImage;
        CardView cardView;
        TextView postedTime;
        TextView postText;
        TextView postTitle;
        ImageView imageView;
        View videoActionContainer;
        SquareVideoView videoView;
        LinearLayout commentNow;
        TextView userName;
        ImageView dislike;
        LinearLayout like;
        ImageView likeIcon;
        ImageView videoAction;
        ProgressBar videoProgress;

        int mediaStopPosition = 0;

//        @BindView(R.id.list_item_home_video_player_progress_bar)
//        ProgressBar progressBar;

        HomeItemViewHolder(View itemView) {
            super(itemView);
            foxyImage = itemView.findViewById(R.id.list_item_home_foxy_img);
            cardView = itemView.findViewById(R.id.cardView);
            postedTime = itemView.findViewById(R.id.list_item_home_posted_txt);
            postText = itemView.findViewById(R.id.list_item_home_text);
            postTitle = itemView.findViewById(R.id.list_item_home_title);
            imageView = itemView.findViewById(R.id.list_item_home_image);
            videoActionContainer = itemView.findViewById(R.id.videoActionContainer);
            videoView = itemView.findViewById(R.id.list_item_home_video);
            commentNow = itemView.findViewById(R.id.list_item_home_comment_now);
            userName = itemView.findViewById(R.id.list_item_home_posted_name);
            dislike = itemView.findViewById(R.id.list_item_home_dislike);
            like = itemView.findViewById(R.id.list_item_home_like);
            likeIcon = itemView.findViewById(R.id.likeIcon);
            videoAction = itemView.findViewById(R.id.videoAction);
            videoProgress = itemView.findViewById(R.id.videoProgress);

            itemView.findViewById(R.id.list_item_home_menu).setOnClickListener(this);
            itemView.findViewById(R.id.userDetailContainer).setOnClickListener(this);
            itemView.findViewById(R.id.list_item_home_share).setOnClickListener(this);
            itemView.findViewById(R.id.list_item_home_txt_pic_vid_holder).setOnClickListener(this);
            commentNow.setOnClickListener(this);
            dislike.setOnClickListener(this);
            like.setOnClickListener(this);
        }

        /**
         * A function used to share the post on clicking the share button
         */
        void sharePost() {
            int pos = getLayoutPosition();
            if (pos != -1) {
                final Post post = getItem(getLayoutPosition());
                String dynamic_link_domain = context.getString(R.string.dynamic_link_domain);
                DynamicLink link = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setDomainUriPrefix(dynamic_link_domain.startsWith("https") ? dynamic_link_domain : ("https://" + dynamic_link_domain))
                        .setLink(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName() + "&post=" + post.getId()))
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().setFallbackUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName())).build())
                        .buildDynamicLink();

                FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLongLink(link.getUri())
                        .buildShortDynamicLink()
                        .addOnCompleteListener(new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();

                                    String shareText = context.getString(R.string.view_amazin_post) + " " + context.getString(R.string.app_name) + " " + shortLink.toString();
                                    Helper.openShareIntent(context, itemView, shareText);
                                    foxyService.updateSharePost(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), post.getId()).enqueue(new Callback<JsonObject>() {
                                        @Override
                                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                                            response.isSuccessful();
                                        }

                                        @Override
                                        public void onFailure(Call<JsonObject> call, Throwable t) {
                                            t.getMessage();
                                        }
                                    });
                                } else {
                                    // Error
                                    // ...
                                }
                            }
                        });
            }
        }

        /**
         * Opens the {@link CommentsFragment} for particular post on click of comment button
         */
        void commentPopUp() {
            int pos = getLayoutPosition();
            if (pos != -1) {
                final Post currPost = getItem(pos);
                String postId = currPost.getId();

                if (fragment != null && fragment.getActivity() != null) {
                    OnPopupMenuItemClickListener onPopupMenuItemClickListener = new OnPopupMenuItemClickListener() {
                        @Override
                        public void onReportNowClick() {

                        }

                        @Override
                        public void onDeleteClick() {
                            currPost.setCommentCount(currPost.getCommentCount() - 1);
                            String commentString = String.valueOf(currPost.getCommentCount()) + " " + context.getString(R.string.commented);
                            //commentCount.setText(commentString);
                        }
                    };

                    OnCommentAddListener onCommentAddListener = new OnCommentAddListener() {
                        @Override
                        public void onCommentAdded() {
                            currPost.setCommentCount(currPost.getCommentCount() + 1);
                            String commentString = String.valueOf(currPost.getCommentCount()) + " " + context.getString(R.string.commented);
                            //commentCount.setText(commentString);
                        }
                    };

                    CommentsFragment commentsFragment = CommentsFragment.newInstance(postId, onPopupMenuItemClickListener, onCommentAddListener);

                    fragment.getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down, R.anim.bottom_up, R.anim.bottom_down)
                            .add(R.id.activity_main_container, commentsFragment, CommentsFragment.class.getName())
                            .addToBackStack(null)
                            .commit();
                }
            }
        }

        void onDislikeClick() {
            final int position = getLayoutPosition();
            if (position != -1) {
                final Post currPost = getItem(position);

                boolean alreadyDisliked = currPost.getDisliked() == 1;
                currPost.setDisliked(alreadyDisliked ? 0 : 1);

                Intent postChangeEventIntent = new Intent(Constants.POST_CHANGE_EVENT);
                postChangeEventIntent.putExtra("post", currPost);
                postChangeEventIntent.putExtra("bookmark", true);
                LocalBroadcastManager.getInstance(context).sendBroadcast(postChangeEventIntent);
            }
        }

        void onLikeClick() {
            final int position = getLayoutPosition();
            if (position != -1) {
                final Post currPost = getItem(position);

                boolean alreadyLiked = currPost.getLiked() == 1;
                currPost.setLiked(alreadyLiked ? 0 : 1);
                currPost.setLikeCount(alreadyLiked ? (currPost.getLikeCount() - 1) : (currPost.getLikeCount() + 1));
                Intent postChangeEventIntent = new Intent(Constants.POST_CHANGE_EVENT);
                postChangeEventIntent.putExtra("post", currPost);
                LocalBroadcastManager.getInstance(context).sendBroadcast(postChangeEventIntent);
                if (!likeDislikeUpdateMap.containsKey(currPost.getId())) {
                    likeDislikeUpdateMap.put(currPost.getId(), new LikeDislikeScoreUpdate());
                }
                likeDislikeUpdateMap.get(currPost.getId()).setLike(alreadyLiked ? -1 : 1);
                executeLike(currPost.getId());
            }
        }

        void setDislikedView(boolean disliked) {
            //dislike.setTypeface(null, disliked ? Typeface.BOLD : Typeface.NORMAL);
            //dislike.setTextColor(ContextCompat.getColor(context, disliked ? R.color.colorAccent : R.color.colorText));
            //dislike.setCompoundDrawablesWithIntrinsicBounds(disliked ? R.drawable.ic_bookmark_blue_18dp : R.drawable.ic_bookmark_gray_18dp, 0, 0, 0);
            dislike.setImageDrawable(ContextCompat.getDrawable(context, disliked ? R.drawable.ic_bookmark_blue_18dp : R.drawable.ic_bookmark_gray_18dp));
        }

        void setLikedView(boolean liked) {
//            like.setTypeface(null, liked ? Typeface.BOLD : Typeface.NORMAL);
//            like.setTextColor(ContextCompat.getColor(context, liked ? R.color.colorPrimary : R.color.colorText));
            likeIcon.setImageResource(liked ? R.drawable.ic_like_blue_18dp : R.drawable.ic_like_gray_18dp);
        }

        @Override
        public void onClick(View view) {
           if (!TextUtils.isEmpty(profileMe.getState())){
               if (Helper.getPaid(sharedPreferenceUtil)){
                   switch (view.getId()) {
                       case R.id.userDetailContainer:
                           UserMeta userMeta = getItem(getLayoutPosition()).getUserMetaData();
                           context.startActivity(UserProfileDetailActivity.newInstance(context, userMeta.getId().toString(), userMeta.getName(), userMeta.getImage()));
                           break;
                       case R.id.list_item_home_share:
                           SpringAnimationHelper.performAnimation(view);
                           sharePost();
                           break;
                       case R.id.list_item_home_comment_now:
                           SpringAnimationHelper.performAnimation(view);
                           commentPopUp();
                           break;
                       case R.id.list_item_home_dislike:
                           SpringAnimationHelper.performAnimation(view);
                           onDislikeClick();
                           break;
                       case R.id.list_item_home_like:
                           SpringAnimationHelper.performAnimation(view);
                           onLikeClick();
                           break;
                       case R.id.list_item_home_menu:
                           final int pos = getLayoutPosition();
                           if (pos != -1) {
                               final Post post = getItem(pos);
                               UserResponse userMe = Helper.getLoggedInUser(sharedPreferenceUtil);
                               SpringAnimationHelper.performAnimation(view);
                               PopupMenu popup = new PopupMenu(context, view);
                               popup.inflate(R.menu.menu_home_item);
                               popup.getMenu().getItem(1).setVisible(post.getUserMetaData().getId().equals(userMe.getId()));
                               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                   @Override
                                   public boolean onMenuItemClick(MenuItem item) {
                                       switch (item.getItemId()) {
                                           case R.id.action_report:
                                               reportPost(post.getId());
                                               break;
                                           case R.id.action_delete:
                                               deletePost(post.getId());
                                               Toast.makeText(context, R.string.post_deleted, Toast.LENGTH_SHORT).show();
                                               removeItemAt(pos);
                                               break;
                                       }
                                       return false;
                                   }
                               });
                               //displaying the popup
                               popup.show();
                           }
                           break;
                       case R.id.list_item_home_txt_pic_vid_holder:

                           int posi = getLayoutPosition();
                           if (posi != -1)
                               context.startActivity(DetailHomeItemActivity.newIntent(context, getItem(posi)));
                           break;
                   }
               }else{
                   new SharedPreferenceUtil(context).setPayViewType(0);
                   context.startActivity(new Intent(context, PaymentActivity.class));
               }
           }else{
               AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(context);
               alertDialogBuilder.setTitle("Your profile details not complete...");
               alertDialogBuilder.setMessage("Create your profile to find your partner");
               alertDialogBuilder.setPositiveButton("Create Profile", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       context.startActivity(new Intent(context, EditOptionActivity.class));
                   }
               });
               alertDialogBuilder.create().show();
           }

        }
    }

    private void reportPost(String id) {
        foxyService.reportPost(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (context != null && response.isSuccessful()) {
                    Toast.makeText(context, R.string.post_reported, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void deletePost(String id) {
        foxyService.deletePost(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                response.isSuccessful();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                t.getMessage();
            }
        });
    }

    private void executeDislike(String id) {
        if (!likeDislikeUpdateMap.get(id).isInProgress()) {
            likeDislikeUpdateMap.get(id).setInProgress(true);
            foxyService.updatePostDislike(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), id).enqueue(new Callback<LikeDislikeResponse>() {
                @Override
                public void onResponse(Call<LikeDislikeResponse> call, Response<LikeDislikeResponse> response) {
                    if (response.isSuccessful()) {
                        likeDislikeUpdateMap.get(response.body().getId()).setInProgress(false);
                        if (likeDislikeUpdateMap.get(response.body().getId()).getDislike() != response.body().getStatus()) {
                            executeDislike(response.body().getId());
                        }
                    }
                }

                @Override
                public void onFailure(Call<LikeDislikeResponse> call, Throwable t) {

                }
            });
        }
    }

    private void executeLike(String id) {
        if (!likeDislikeUpdateMap.get(id).isInProgress()) {
            likeDislikeUpdateMap.get(id).setInProgress(true);
            foxyService.updatePostLike(sharedPreferenceUtil.getStringPreference(Constants.KEY_API_KEY, null), id).enqueue(new Callback<LikeDislikeResponse>() {
                @Override
                public void onResponse(Call<LikeDislikeResponse> call, Response<LikeDislikeResponse> response) {
                    if (response.isSuccessful()) {
                        likeDislikeUpdateMap.get(response.body().getId()).setInProgress(false);
                        if (likeDislikeUpdateMap.get(response.body().getId()).getLike() != response.body().getStatus()) {
                            executeLike(response.body().getId());
                        }
                    }
                }

                @Override
                public void onFailure(Call<LikeDislikeResponse> call, Throwable t) {
                }
            });
        }
    }


   /** public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }**/
}
