package com.example.bakalarkaupdate;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class RoutesBouldersAdapter extends RecyclerView.Adapter<RoutesBouldersAdapter.RoutesBouldersViewHolder>{

    Context context;
    List<RouteBoulder> routeBoulderList;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public RoutesBouldersAdapter(Context context, List<RouteBoulder> routeBoulderList) {
        this.context = context;
        this.routeBoulderList = routeBoulderList;
    }

    @NonNull
    @Override
    public RoutesBouldersAdapter.RoutesBouldersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.route_boulder_item, parent, false);
        return new RoutesBouldersAdapter.RoutesBouldersViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull RoutesBouldersAdapter.RoutesBouldersViewHolder holder, int position) {
        RouteBoulder routeBoulder = routeBoulderList.get(position);
        if (routeBoulder == null) {
            return;
        }

        holder.tvRouteBoulderName.setText(routeBoulder.getName());
        holder.tvRouteBoulderDifficulty.setText(routeBoulder.getDifficulty());
        holder.tvRouteBoulderHeight.setText(routeBoulder.getHeight() + " m");

        Glide.with(context)
                .load(getImageBasedOnColour(routeBoulder.getColour()))
                .into(holder.ivRouteBoulderHold);



        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(routeBoulder);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (longListener != null) {
                longListener.onItemLongClick(routeBoulder);
            }
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return routeBoulderList.size();
    }


    public static class RoutesBouldersViewHolder extends RecyclerView.ViewHolder {
        TextView tvRouteBoulderName, tvRouteBoulderDifficulty, tvRouteBoulderHeight;
        ImageView ivRouteBoulderHold;

        public RoutesBouldersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteBoulderName = itemView.findViewById(R.id.tvRouteBoulderName);
            tvRouteBoulderDifficulty = itemView.findViewById(R.id.tvRouteBoulderDifficulty);
            tvRouteBoulderHeight = itemView.findViewById(R.id.tvRouteBoulderHeight);
            ivRouteBoulderHold = itemView.findViewById(R.id.ivRouteBoulderHold);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RouteBoulder routeBoulder);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(RouteBoulder routeBoulder);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }

    private int getImageBasedOnColour(String colorName) {
        switch (colorName.toLowerCase()) {
            case "red": return R.drawable.red;
            case "blue": return R.drawable.blue;
            case "green": return R.drawable.green;
            case "black": return R.drawable.black;
            case "white": return R.drawable.white;
            case "yellow": return R.drawable.yellow;
            case "orange": return R.drawable.orange;
            case "purple": return R.drawable.purple;
            case "pink": return R.drawable.pink;
            case "brown": return R.drawable.brown;
            case "grey": return R.drawable.grey;
            default: return R.drawable.grey;
        }
    }



}
