package com.example.bakalarkaupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.BadgeViewHolder> {

    private final Context context;
    private final List<Badge> badgeList;
    private OnItemClickListener listener;

    private int userMeters;

    public BadgeAdapter(Context context, List<Badge> badgeList, int userMeters) {
        this.context = context;
        this.badgeList = badgeList;
        this.userMeters = userMeters;
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

        if (userMeters >= badge.getHeight()) {
            holder.btnBadgeItemUnlock.setVisibility(View.VISIBLE);
        } else {
            holder.btnBadgeItemUnlock.setVisibility(View.GONE);
        }

    }

    public void setUserMeters(int userMeters) {
        this.userMeters = userMeters;
    }

    @Override
    public int getItemCount() {
        return badgeList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Badge badge);
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
