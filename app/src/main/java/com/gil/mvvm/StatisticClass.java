package com.gil.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class StatisticClass extends AppCompatActivity {
    private static final String TAG = "StatisticClass";

    private TextView text_view_priority_static, textview_deposit_static, textview_left_to_pay_static;
    private int priority, deposit, leftToPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_class);

        text_view_priority_static = findViewById(R.id.text_view_priority_static);
        textview_deposit_static = findViewById(R.id.textview_deposit_static);
        textview_left_to_pay_static = findViewById(R.id.textview_left_to_pay_static);

        Intent intent = getIntent();
        priority = intent.getIntExtra(MainActivity.CALCAULATE_PRIORITY, 0);
        deposit = intent.getIntExtra(MainActivity.CALCAULATE_DEPOSIT, 0);
        leftToPay = priority - deposit;

        text_view_priority_static.setText("" + priority);
        textview_deposit_static.setText("" + deposit);
        if (deposit >= priority) {
            textview_left_to_pay_static.setText("" + 0);
        }
        textview_left_to_pay_static.setText("" + leftToPay);
    }

}
