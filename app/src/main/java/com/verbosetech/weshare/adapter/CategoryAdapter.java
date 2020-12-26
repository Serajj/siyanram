package com.verbosetech.weshare.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.verbosetech.weshare.R;
import com.verbosetech.weshare.activity.UserProfileDetailActivity;
import com.verbosetech.weshare.model.UserByCat;
import com.verbosetech.weshare.model.UserMeta;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyHolder> {

    Context context;
    List<UserByCat> userByCatList;

    public CategoryAdapter(Context context, List<UserByCat> userByCatList) {
        this.context = context;
        this.userByCatList = userByCatList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filtered_user_by_cat,parent,false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        holder.textView.setText(userByCatList.get(position).getName());
        Glide.with(context)
                .load(userByCatList.get(position).getImage())
                .apply(new RequestOptions().placeholder(R.drawable.placeholder).dontAnimate())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(UserProfileDetailActivity.newInstance(context,userByCatList.get(position).getUserId(), userByCatList.get(position).getName(), userByCatList.get(position).getImage()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return userByCatList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.cat_user_image);
            textView=itemView.findViewById(R.id.cat_user_name);

        }
    }
}
