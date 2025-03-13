package com.example.bakalarkaupdate;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrainingsAdapter extends RecyclerView.Adapter<TrainingsAdapter.TrainingsViewHolder> {



    @NonNull
    @Override
    public TrainingsAdapter.TrainingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull TrainingsAdapter.TrainingsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class TrainingsViewHolder extends RecyclerView.ViewHolder {

        public TrainingsViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
