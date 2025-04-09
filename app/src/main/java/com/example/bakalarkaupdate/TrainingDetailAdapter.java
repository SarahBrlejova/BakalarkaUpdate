package com.example.bakalarkaupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class TrainingDetailAdapter extends RecyclerView.Adapter<TrainingDetailAdapter.TrainingDetailViewHolder> {

    private  Context context;
    private  List<ClimbedRoutesBoulders> climbedList;

    public TrainingDetailAdapter(Context context, List<ClimbedRoutesBoulders> climbedList) {
        this.context = context;
        this.climbedList = climbedList;
    }

    @NonNull
    @Override
    public TrainingDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.climbed_routes_boulders_item, parent, false);
        return new TrainingDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingDetailViewHolder holder, int position) {
        ClimbedRoutesBoulders item = climbedList.get(position);
        holder.tvDifficulty.setText("Obtiažnosť: " + item.getDifficulty());
        holder.tvTimesClimbed.setText("Počet prelezov: " + item.getTimesClimbed());
        holder.tvClimbName.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return climbedList.size();
    }

    public static class TrainingDetailViewHolder extends RecyclerView.ViewHolder {

        TextView tvDifficulty, tvTimesClimbed, tvClimbName;

        public TrainingDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvClimbName =  itemView.findViewById(R.id.tvClimbName);
            tvDifficulty = itemView.findViewById(R.id.tvClimbedRoutesBouldersITemDifficulty);
            tvTimesClimbed = itemView.findViewById(R.id.tvClimbedRoutesBouldersITemTimes);
        }
    }

    public static String trainingTimeRecalculation(com.google.firebase.Timestamp startTime, com.google.firebase.Timestamp endTime) {
        long start = startTime.toDate().getTime();
        long end = endTime.toDate().getTime();
        long time = end - start;

        long seconds = (time / 1000) % 60;
        long minutes = (time / (1000 * 60)) % 60;
        long hours = time / (1000 * 60 * 60);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
}
