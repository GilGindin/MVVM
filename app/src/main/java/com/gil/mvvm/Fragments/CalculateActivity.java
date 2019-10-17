package com.gil.mvvm.Fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
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

import com.gil.mvvm.Note;
import com.gil.mvvm.NoteViewModel;
import com.gil.mvvm.R;

import java.util.List;

public class CalculateActivity extends Fragment {
    private static final String TAG = "CalculateActivity";
    private EditText edit_text_calculate;
    private TextView text_view_priority, text_view_avg;
    private int priorityCalc;
    private NoteViewModel mNoteViewModel;

    static CalculateActivity instance;

    public static CalculateActivity getInstance() {
        if (instance == null) {
            instance = new CalculateActivity();
        }
        return instance;
    }

    public CalculateActivity() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_calculate, container, false);

        initialViews(v);

        mNoteViewModel = ViewModelProviders.of(getActivity()).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(getActivity(), new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                priorityCalc = 0;
                for (int i = 0; i < notes.size(); i++) {
                    Note currentnNote = notes.get(i);
                    priorityCalc += currentnNote.getPriority();
                }
                text_view_priority.setText(String.valueOf(priorityCalc));
            }
        });

        return v;
    }

    private void initialViews(View v) {
        edit_text_calculate = v.findViewById(R.id.edit_text_calculate);
        text_view_priority = v.findViewById(R.id.text_view_priority);
        text_view_avg = v.findViewById(R.id.text_view_sum);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Calculating");

        Button button = v.findViewById(R.id.btn_calc);
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
            Toast.makeText(getActivity(), "צריך להכניס מספר הסועדים הרצוי..", Toast.LENGTH_SHORT).show();
            return;
        }
        int numOfPeople = Integer.parseInt(people);
        int sum = priorityCalc;
        int avg = sum / numOfPeople;
        text_view_avg.setText(String.valueOf(avg));
    }
}
