package com.example.bakalarkaupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.TrainingsViewHolder> {
    FirestoreHelper firestoreHelper;
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

        Timestamp startTraining = training.getStartTraining();
        Timestamp endTraining = training.getEndTraining();
        SimpleDateFormat date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        firestoreHelper = new FirestoreHelper();
        firestoreHelper.getCenterName(training.getCenterId(), holder.tvTrainingItemCenter);


        holder.tvTrainingItemDate.setText(date.format(startTraining.toDate()));
        holder.tvTrainingItemBoulders.setText(String.valueOf(training.getTotalBoulders()));
        holder.tvTrainingItemRoutes.setText(String.valueOf(training.getTotalRoutes()));
        holder.tvTrainingItemMeters.setText(training.getTotalMeters() + "m");;
        holder.tvTrainingItemTime.setText(trainingTimeRecalculation(startTraining,endTraining));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(training);
            }
        });
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


    private  String trainingTimeRecalculation(Timestamp startTime, Timestamp endTime){
        long start = startTime.toDate().getTime();
        long end= endTime.toDate().getTime();
        long time = end - start;

        long seconds = time / 1000 % 60;
        long minutes = time / (1000 * 60) % 60;
        long hours = time / (1000 * 60 * 60);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }


}
