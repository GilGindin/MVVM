package com.gil.mvvm.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gil.mvvm.MainActivity;
import com.gil.mvvm.R;

public class StatisticClass extends Fragment {
    private static final String TAG = "StatisticClass";

    private TextView text_view_priority_static, textview_deposit_static, textview_left_to_pay_static;
    private int priority, deposit, leftToPay = 0;

    static StatisticClass instance;

    public static StatisticClass getInstance() {
        if (instance == null) {
            instance = new StatisticClass();
        }
        return instance;
    }

    public StatisticClass() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_statistic_class, container, false);

        initialViews(v);

        priority = ((MainActivity) getActivity()).recieveCalculatePriority();
        deposit = ((MainActivity) getActivity()).reciceveCalculateDeposit();

        if (priority >= deposit) {
            leftToPay = priority - deposit;
            text_view_priority_static.setText("" + priority);
            textview_deposit_static.setText("" + deposit);
            textview_left_to_pay_static.setText("" + leftToPay);
        }
        if (deposit > priority) {
            leftToPay = deposit - priority;
            text_view_priority_static.setText("" + priority);
            textview_deposit_static.setText("" + deposit);
            textview_left_to_pay_static.setText("-" + leftToPay);
        }

        return v;
    }

    private void initialViews(View view) {

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Statistics");

        text_view_priority_static = view.findViewById(R.id.text_view_priority_static);
        textview_deposit_static = view.findViewById(R.id.textview_deposit_static);
        textview_left_to_pay_static = view.findViewById(R.id.textview_left_to_pay_static);
    }

}
