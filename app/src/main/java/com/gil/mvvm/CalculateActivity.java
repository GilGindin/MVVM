package com.gil.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateActivity extends Fragment {
    private static final String TAG = "CalculateActivity";
    private EditText edit_text_calculate;
    private TextView text_view_priority, text_view_avg;
    private int priority;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_calculate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        edit_text_calculate = getView().findViewById(R.id.edit_text_calculate);
        text_view_priority = getView().findViewById(R.id.text_view_priority);
        text_view_avg = getView().findViewById(R.id.text_view_sum);

        Button button = getView().findViewById(R.id.btn_calc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAvg();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Go Back");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();



        if (getActivity().getIntent().hasExtra(MainActivity.CALCAULATE_PRIORITY)) {
            Intent intent = getActivity().getIntent();
            priority = intent.getIntExtra(MainActivity.CALCAULATE_PRIORITY, 0);
            text_view_priority.setText(String.valueOf(priority));
        }

    }

    private void calculateAvg() {
        String people = String.valueOf(edit_text_calculate.getText());
        if (people.matches("")) {
            Toast.makeText(getActivity(), "צריך להכניס מספר הסועדים הרצוי..", Toast.LENGTH_SHORT).show();
            return;
        }
        int numOfPeople = Integer.parseInt(people);
        int sum = priority;
        int avg = sum / numOfPeople;
        text_view_avg.setText(String.valueOf(avg));
    }
}
