package com.gil.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalculateActivity extends AppCompatActivity {
    private static final String TAG = "CalculateActivity";
    private EditText edit_text_calculate;
    private TextView text_view_priority, text_view_avg;
    private int priority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);

        edit_text_calculate = findViewById(R.id.edit_text_calculate);
        text_view_priority = findViewById(R.id.text_view_priority);
        text_view_avg = findViewById(R.id.text_view_sum);

        Intent intent = getIntent();
        priority = intent.getIntExtra(MainActivity.CALCAULATE_PRIORITY, 0);
        Log.d(TAG, "onCreate: ----------" + priority);

        text_view_priority.setText(String.valueOf(priority));
        Button button = findViewById(R.id.btn_calc);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAvg();
            }
        });
    }

    private void calculateAvg() {
        String people = String.valueOf(edit_text_calculate.getText());
        if (people.matches("")) {
            Toast.makeText(this, "צריך להכניס מספר הסועדים הרצוי..", Toast.LENGTH_SHORT).show();
            return;
        }
        int numOfPeople = Integer.parseInt(people);
        int sum = priority;
        int avg = sum / numOfPeople;
        Log.d(TAG, "calculateAvg: ----" + avg);
        text_view_avg.setText(String.valueOf(avg));
    }
}
