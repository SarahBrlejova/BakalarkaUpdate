package com.example.bakalarkaupdate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class TrainingFragment extends Fragment {

    private TextView tvTime;
    private Button btnStart, btnStop;
    private boolean isTraining = false;
    private int secondsElapsed = 0;


    public TrainingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);
        tvTime = view.findViewById(R.id.tvTime);
        btnStart = view.findViewById(R.id.btnStart);
        btnStop = view.findViewById(R.id.btnStop);

        btnStart.setOnClickListener(v -> startTraining());
        btnStop.setOnClickListener(v -> stopTraining());

        return view;
    }

    private void startTraining() {
        isTraining = true;
        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);

        loadFragment(new TrainingCenterFragment());
    }

    private void stopTraining() {
        isTraining = false;
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);

        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_containerTraining, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}