package com.example.bakalarkaupdate;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {

    private final Context context;
    private User user;

    public UsersAdapter(Context context, User user) {
        this.context = context;
        this.user = user;
    }

    public void setUser(User newUser) {
        this.user = newUser;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public UsersAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_item, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position) {
        holder.tvUserItemNick.setText(user.getNick());
        holder.tvUserItemTotalMeters.setText(String.valueOf(user.getTotal_meters()));
        holder.tvUserItemAvailableMeters.setText(String.valueOf(user.getAvailable_meters()));

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserItemTotalMeters, tvUserItemAvailableMeters, tvUserItemNick;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserItemTotalMeters = itemView.findViewById(R.id.tvUserItemTotalMeters);
            tvUserItemAvailableMeters = itemView.findViewById(R.id.tvUserItemAvailableMeters);
            tvUserItemNick = itemView.findViewById(R.id.tvUserItemNick);
        }
    }
}
