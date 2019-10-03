package com.gil.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.gil.mvvm.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.gil.mvvm.EXTRA_TITLE";
    public static final String EXTRA_DECRIPTION = "com.gil.mvvm.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.gil.mvvm.EXTRA_PRIORITY";
    public static final String EXTRA_DEPOSIT = "com.gil.mvvm.EXTRA_DEPOSIT";

    private EditText edit_text_title, edit_text_description, edit_text_already_pay, edit_text_picker;
    private TextView textview_left_to_pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edit_text_title = findViewById(R.id.edit_text_title);
        edit_text_description = findViewById(R.id.edit_text_description);
        edit_text_picker = findViewById(R.id.edit_text_picker);
        edit_text_already_pay = findViewById(R.id.edit_text_already_pay);
        textview_left_to_pay = findViewById(R.id.textview_left_to_pay);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE));
            edit_text_description.setText(intent.getStringExtra(EXTRA_DECRIPTION));
            edit_text_picker.setText("" + intent.getIntExtra(EXTRA_PRIORITY, 0));
            edit_text_already_pay.setText("" + intent.getIntExtra(EXTRA_DEPOSIT, 0));

        } else {
            setTitle("Add Note");
        }

        calculateDiffrence();

    }

    private void calculateDiffrence() {
        int priority = Integer.parseInt(edit_text_picker.getText().toString());
        int deposit = Integer.parseInt(edit_text_already_pay.getText().toString());
        if (priority > deposit) {
            int difference = priority - deposit;
            textview_left_to_pay.setText("" + difference);
        } else if (deposit > priority) {
            int overPay = deposit - priority;
            textview_left_to_pay.setText("-" + overPay);
            shotToast("Warning!!! you deposit more than you need.. by " + overPay);
        } else
            textview_left_to_pay.setText("" + 0);

    }

    private void shotToast(String text) {
        LayoutInflater mInflater = getLayoutInflater();
        View layout = mInflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);

        Toast toast = new Toast(getApplicationContext());
        toastText.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void saveNote() {
        String title = edit_text_title.getText().toString();
        String description = edit_text_description.getText().toString();
        int priority = Integer.parseInt(edit_text_picker.getText().toString());
        int deposit = Integer.parseInt(edit_text_already_pay.getText().toString());

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a Title and Description", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DECRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);
        data.putExtra(EXTRA_DEPOSIT, deposit);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
