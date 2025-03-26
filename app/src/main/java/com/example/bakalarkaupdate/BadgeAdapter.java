package com.example.bakalarkaupdate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {

    private final Context context;
    private final List<Badge> badgeList;

    private FirestoreHelper firestoreHelper;

    private String collectionId;
    private int userMeters;

    public BadgeAdapter(Context context, List<Badge> badgeList, int userMeters, String collectionId) {
        this.context = context;
        this.badgeList = badgeList;
        this.userMeters = userMeters;
        this.collectionId = collectionId;
        firestoreHelper = new FirestoreHelper();
    }

    @NonNull
    @Override
    public BadgeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.badge_item, parent, false);
        return new BadgeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BadgeViewHolder holder, int position) {
        Badge badge = badgeList.get(position);

        holder.tvBadgeName.setText(badge.getName());
        holder.tvBadgeHeight.setText("Height: " + badge.getHeight() + "m");

        Glide.with(context)
                .load(badge.getImageUrl())
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.ivBadgeImage);

        if (badge.isUnlocked()) {
            holder.btnBadgeItemUnlock.setVisibility(View.GONE);
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.green));

        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            if (userMeters >= badge.getHeight()) {
                holder.btnBadgeItemUnlock.setVisibility(View.VISIBLE);
            } else {
                holder.btnBadgeItemUnlock.setVisibility(View.GONE);
            }
        }

        holder.btnBadgeItemUnlock.setOnClickListener(v -> {
            firestoreHelper.unlockBadge(badge.getId(), collectionId, badge.getImageUrl());
            badge.setUnlocked(true);
            notifyItemChanged(position);
        });

    }

    public void setUserMeters(int userMeters) {
        this.userMeters = userMeters;
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }



    public static class BadgeViewHolder extends RecyclerView.ViewHolder {
        TextView tvBadgeName, tvBadgeHeight;
        ImageView ivBadgeImage;
        Button btnBadgeItemUnlock;

        public BadgeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBadgeName = itemView.findViewById(R.id.tvBadgeItemName);
            tvBadgeHeight = itemView.findViewById(R.id.tvBadgeItemHeight);
            ivBadgeImage = itemView.findViewById(R.id.ivBadgeItemImage);
            btnBadgeItemUnlock = itemView.findViewById(R.id.btnBadgeItemUnlock);
        }
    }


}
