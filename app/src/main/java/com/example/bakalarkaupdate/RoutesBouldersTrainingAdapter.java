package com.example.bakalarkaupdate;

import android.content.Context;
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

public class RoutesBouldersTrainingAdapter extends RecyclerView.Adapter<RoutesBouldersTrainingAdapter.RoutesBouldersTrainingViewHolder> {

    Context context;
    List<RouteBoulder> routeBoulderList;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public RoutesBouldersTrainingAdapter(Context context, List<RouteBoulder> routeBoulderList) {
        this.context = context;
        this.routeBoulderList = routeBoulderList;
    }

    @NonNull
    @Override
    public RoutesBouldersTrainingAdapter.RoutesBouldersTrainingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.route_boulder_training_item, parent, false);
        return new RoutesBouldersTrainingAdapter.RoutesBouldersTrainingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RoutesBouldersTrainingAdapter.RoutesBouldersTrainingViewHolder holder, int position) {
        RouteBoulder routeBoulder = routeBoulderList.get(position);

        holder.tvTrainingRouteBoulderName.setText(routeBoulder.getName());
        holder.tvRouteBoulderTrainingHeight.setText(routeBoulder.getHeight() + " m");
        holder.tvTrainingRouteBoulderDifficulty.setText(routeBoulder.getDifficulty());
        holder.tvTrainingRouteBoulderTimesClimbed.setText("Prelezy: " + routeBoulder.getClimbs());


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

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longListener = listener;
    }

    public int changeColor(Context context, String colorName) {
        int colorId = context.getResources().getIdentifier(colorName, "color", context.getPackageName());

        if (colorId == 0) {
            return context.getResources().getColor(R.color.black);
        }
        return context.getResources().getColor(colorId);
    }

    public interface OnItemClickListener {
        void onItemClick(RouteBoulder routeBoulder);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(RouteBoulder routeBoulder);
    }

    public static class RoutesBouldersTrainingViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainingRouteBoulderName, tvTrainingRouteBoulderDifficulty, tvTrainingRouteBoulderTimesClimbed, tvRouteBoulderTrainingHeight;
        ImageView ivRouteBoulderHold;
        public RoutesBouldersTrainingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainingRouteBoulderName = itemView.findViewById(R.id.tvRouteBoulderTrainingName);
            tvTrainingRouteBoulderDifficulty = itemView.findViewById(R.id.tvRouteBoulderTrainingDifficulty);
            tvTrainingRouteBoulderTimesClimbed = itemView.findViewById(R.id.tvTrainingTimesClimbed);
            tvRouteBoulderTrainingHeight = itemView.findViewById(R.id.tvRouteBoulderTrainingHeight);
            ivRouteBoulderHold = itemView.findViewById(R.id.ivRouteBoulderTrainingHold);

        }
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
