package com.example.bakalarkaupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UsersBadgesAdapter extends RecyclerView.Adapter<UsersBadgesAdapter.UsersBadgesViewHolder>{

    Context context;
    List<UserBadge> userBadgeList;



    public UsersBadgesAdapter(Context context, List<UserBadge> userBadgeList) {
        this.context = context;
        this.userBadgeList = userBadgeList;
    }

    @NonNull
    @Override
    public UsersBadgesAdapter.UsersBadgesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.user_badge_item, parent, false);
        return new UsersBadgesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersBadgesAdapter.UsersBadgesViewHolder holder, int position) {
        UserBadge userBadge = userBadgeList.get(position);
        Glide.with(context)
                .load(userBadge.getImageUrl())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.ivBadgeImage);
    }

    @Override
    public int getItemCount() {
        return userBadgeList.size();
    }

    public static class UsersBadgesViewHolder extends RecyclerView.ViewHolder {

        ImageView ivBadgeImage;
        public UsersBadgesViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBadgeImage=itemView.findViewById(R.id.ivBadgeUser);

        }
    }
}
