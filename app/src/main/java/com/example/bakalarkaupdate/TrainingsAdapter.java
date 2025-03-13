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

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.TrainingsViewHolder> {

    Context context;
    List<Training> trainingList;
    private TrainingsAdapter.OnItemClickListener listener;

    public TrainingsAdapter(Context context, List<Training> trainingList) {
        this.context = context;
        this.trainingList = trainingList;
    }


    @NonNull
    @Override
    public TrainingsAdapter.TrainingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.training_item, parent, false);
        return new TrainingsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingsAdapter.TrainingsViewHolder holder, int position) {

        Training training = trainingList.get(position);
        Log.d("TrainingsAdapter", "Boulders: " + training.getTotalBoulders());
        Log.d("TrainingsAdapter", "Routes: " + training.getTotalRoutes());
        Log.d("TrainingsAdapter", "Meters: " + training.getTotalMeters());

        holder.tvTrainingItemBoulders.setText(String.valueOf(training.getTotalBoulders()));
        holder.tvTrainingItemRoutes.setText(String.valueOf(training.getTotalRoutes()));
        holder.tvTrainingItemMeters.setText(training.getTotalMeters() + "m");;

//        holder.tvTrainingItemDate.setText(training.getTotalBoulders());
//        holder.tvTrainingItemTime.setText(training.getCenterId());
//        holder.tvTrainingItemCenter.setText(training.getCenterId());


    }

    @Override
    public int getItemCount() {
        return trainingList.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Training training);
    }

    public static class TrainingsViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrainingItemTime, tvTrainingItemBoulders, tvTrainingItemRoutes, tvTrainingItemMeters, tvTrainingItemCenter, tvTrainingItemDate;

        public TrainingsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTrainingItemTime = itemView.findViewById(R.id.tvTrainingItemTime);
            tvTrainingItemBoulders = itemView.findViewById(R.id.tvTrainingItemBoulders);
            tvTrainingItemRoutes = itemView.findViewById(R.id.tvTrainingItemRoutes);
            tvTrainingItemMeters = itemView.findViewById(R.id.tvTrainingItemMeters);
            tvTrainingItemCenter = itemView.findViewById(R.id.tvTrainingItemCenter);
            tvTrainingItemDate = itemView.findViewById(R.id.tvTrainingItemDate);
        }
    }

}
