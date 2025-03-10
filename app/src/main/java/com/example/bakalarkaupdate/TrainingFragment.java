package com.example.bakalarkaupdate;

import android.os.Bundle;
import android.os.SystemClock;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


public class TrainingFragment extends Fragment {

    private TextView tvTime;
    private Button btnStart, btnStop;
    private boolean isTraining = false;
    private long startTime = 0L;
    private long pausedTime = 0L;
    private Handler handler = new Handler();
    private Runnable timerRunnable;


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

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (isTraining) {
                    long realPhoneTime = SystemClock.elapsedRealtime() - startTime;
                    TimerUpdate(realPhoneTime);
                    handler.postDelayed(this, 1000);
                }
            }
        };

        btnStart.setOnClickListener(v -> startTraining());
        btnStop.setOnClickListener(v -> stopTraining());

        return view;
    }

    private void TimerUpdate(long elapsedMillis) {
        int sec = (int) (elapsedMillis / 1000) % 60;
        int min = (int) ((elapsedMillis / (1000 * 60)) % 60);
        int hod = (int) ((elapsedMillis / (1000 * 60 * 60)) % 24);
        String timeFormatted = String.format("%02d:%02d:%02d", hod, min, sec);
        tvTime.setText(timeFormatted);
    }

    private void startTraining() {
        isTraining = true;
        btnStart.setVisibility(View.GONE);
        btnStop.setVisibility(View.VISIBLE);

        if (pausedTime == 0L) {
            startTime = SystemClock.elapsedRealtime();
        } else {
            startTime = SystemClock.elapsedRealtime() - pausedTime;
        }

        handler.post(timerRunnable);
        loadFragment(new TrainingCenterFragment());
    }

    private void stopTraining() {
        isTraining = false;
        btnStart.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.GONE);
        handler.removeCallbacks(timerRunnable);

        startTime = 0L;
        pausedTime = 0L;

        tvTime.setText(Long.toString(startTime));

        handler.removeCallbacks(timerRunnable);


        getChildFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_containerTraining, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacks(timerRunnable);
    }
}