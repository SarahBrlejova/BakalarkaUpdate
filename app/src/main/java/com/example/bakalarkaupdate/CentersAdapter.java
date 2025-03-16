package com.example.bakalarkaupdate;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.CentersViewHolder> {

    Context context;
    List<Center> centersList;
    private OnItemClickListener listener;

    public CentersAdapter(Context context, List<Center> centersList) {
        this.context = context;
        this.centersList = centersList;
    }

    @NonNull
    @Override
    public CentersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.center_item, parent, false);
        return new CentersViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CentersAdapter.CentersViewHolder holder, int position) {
        Center center = centersList.get(position);
        holder.tvCenterName.setText(center.getName());
        holder.tvCenterAddress.setText(center.getAddress());
        holder.tvCenterID.setText(center.getId());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(center);
            }
        });
    }

    @Override
    public int getItemCount() {
        return centersList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Center center);
    }

    public static class CentersViewHolder extends RecyclerView.ViewHolder {
        TextView tvCenterName, tvCenterAddress, tvCenterID;

        public CentersViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCenterName = itemView.findViewById(R.id.TVCenterItemCenterName);
            tvCenterAddress = itemView.findViewById(R.id.TVCenterItemCenterAddress);
            tvCenterID = itemView.findViewById(R.id.TVCenterItemCenterID);
        }
    }
}
