package com.example.bakalarkaupdate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            Log.e("RoutesBouldersAdapter", "RouteBoulder is null at position: " + position);
            return;
        }
        Log.d("RoutesBouldersAdapter", "Binding route: " + routeBoulder.getName() + ", Active: " + routeBoulder.isActive());

        holder.tvRouteBoulderName.setText(routeBoulder.getName());
        holder.tvRouteBoulderDifficulty.setText(routeBoulder.getDifficulty());
        holder.TVRouteBoulderID.setText(routeBoulder.getId());

        int color = changeColor(context, routeBoulder.getColour());
        holder.tvRouteBoulderName.setTextColor(color);
        holder.tvRouteBoulderDifficulty.setTextColor(color);

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
        TextView tvRouteBoulderName, tvRouteBoulderDifficulty, TVRouteBoulderID;

        public RoutesBouldersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRouteBoulderName = itemView.findViewById(R.id.tvRouteBoulderName);
            tvRouteBoulderDifficulty = itemView.findViewById(R.id.tvRouteBoulderDifficulty);
            TVRouteBoulderID = itemView.findViewById(R.id.TVRouteBoulderID);
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

    public int changeColor(Context context, String colorName){
        int colorId = context.getResources().getIdentifier(colorName, "color", context.getPackageName());

        if (colorId == 0) {
            return context.getResources().getColor(R.color.black);
        }
        return context.getResources().getColor(colorId);
    }


}
