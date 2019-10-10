package com.gil.mvvm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.gil.mvvm.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.gil.mvvm.EXTRA_TITLE";
    public static final String EXTRA_DECRIPTION = "com.gil.mvvm.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.gil.mvvm.EXTRA_PRIORITY";
    public static final String EXTRA_DEPOSIT = "com.gil.mvvm.EXTRA_DEPOSIT";

    private EditText edit_text_title, edit_text_description, edit_text_deposit, edit_text_priority;
    private TextView textview_left_to_pay;
    private Toolbar mToolbar;
    private int priority, deposit, overPay;
    private Animation animation1, animation2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        edit_text_title = findViewById(R.id.edit_text_title);
        edit_text_description = findViewById(R.id.edit_text_description);
        edit_text_priority = findViewById(R.id.edit_text_picker);
        edit_text_deposit = findViewById(R.id.edit_text_already_pay);
        textview_left_to_pay = findViewById(R.id.textview_left_to_pay);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            edit_text_title.setText(intent.getStringExtra(EXTRA_TITLE));
            edit_text_description.setText(intent.getStringExtra(EXTRA_DECRIPTION));
            edit_text_priority.setText("" + intent.getIntExtra(EXTRA_PRIORITY, 0));
            edit_text_deposit.setText("" + intent.getIntExtra(EXTRA_DEPOSIT, 0));

        } else {
            setTitle("Add Note");
        }
        textChangeListener();
        calculateDiffrence();
    }

    private void textChangeListener() {
        edit_text_priority.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    edit_text_priority.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    priority = Integer.parseInt(edit_text_priority.getText().toString());
                    if (priority != 0) {
                        calculateDiffrence();
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }
            }
        });

        edit_text_deposit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    edit_text_deposit.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {

                    deposit = Integer.parseInt(edit_text_priority.getText().toString());
                    if (deposit != 0) {
                        calculateDiffrence();
                    }
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                }

            }
        });

    }

    private void calculateDiffrence() {
        priority = Integer.parseInt(edit_text_priority.getText().toString());
        deposit = Integer.parseInt(edit_text_deposit.getText().toString());
        if (priority > deposit) {
            int difference = priority - deposit;
            textview_left_to_pay.setText("" + difference);
        } else if (deposit > priority) {
            overPay = deposit - priority;
            textview_left_to_pay.setText("-" + overPay);
            shotToast(overPay);
        } else
            textview_left_to_pay.setText("" + 0);
    }

    private void shotToast(int number) {
        LayoutInflater mInflater = getLayoutInflater();
        View layout = mInflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_text);

        Toast toast = new Toast(getApplicationContext());
        toastText.setText("Warning !!! Your deposit is higher than you were supposed to pay by " + number);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);

        textview_left_to_pay.setTextColor(getResources().getColor(R.color.colorPrimary));
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textview_left_to_pay, 25, 100, 5, TypedValue.COMPLEX_UNIT_SP);
        textview_left_to_pay.setGravity(Gravity.CENTER);

        createFadeInFadeOutAnimation();

        toast.show();
    }

    private void createFadeInFadeOutAnimation() {

        animation1 = new AlphaAnimation(0.0f, 1.0f);
        animation1.setDuration(250);
        animation1.setStartOffset(250);

        animation2 = new AlphaAnimation(1.0f, 0.0f);
        animation2.setDuration(250);
        animation2.setStartOffset(250);

        animation1.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation2 when animation1 ends (continue)
                textview_left_to_pay.startAnimation(animation2);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });

        animation2.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                // start animation1 when animation2 ends (repeat)
                textview_left_to_pay.startAnimation(animation1);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                // TODO Auto-generated method stub

            }

        });

        textview_left_to_pay.startAnimation(animation1);
    }


    private void saveNote() {
        String title = edit_text_title.getText().toString();
        String description = edit_text_description.getText().toString();
        int priority = Integer.parseInt(edit_text_priority.getText().toString());
        int deposit = Integer.parseInt(edit_text_deposit.getText().toString());

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

    @Override
    protected void onStop() {

        textview_left_to_pay.clearAnimation();
        super.onStop();
    }
}
