package com.gil.mvvm;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gil.mvvm.Adapter.NoteAdapter;
import com.gil.mvvm.Adapter.ViewPagerAdapter;
import com.gil.mvvm.Fragments.CalculateActivity;
import com.gil.mvvm.Fragments.ListFragment;
import com.gil.mvvm.Fragments.StatisticClass;


public class MainActivity extends AppCompatActivity  {
    private static final String TAG = "MainActivity";
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final String CALCAULATE_PRIORITY = "com.gil.mvvm.CALCAULATE_PRIORITY";
    public static final String CALCAULATE_DEPOSIT = "com.gil.mvvm.CALCAULATE_DEPOSIT";

    private NoteViewModel mNoteViewModel;
    private NoteAdapter adapter;
    private int calculatePriority;
    private int depositCalculate;
    private Toolbar mToolbar;
    private TabLayout tabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initalWidgets();

    }

    private void initalWidgets() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mToolbar = findViewById(R.id.main_toolbar);
        mViewPager = findViewById(R.id.view_pager);
        setSupportActionBar(mToolbar);
        setUpViewPager(mViewPager);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.calc));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.statistics));

        setTitle("My Wedding App");

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.button_add_note);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        adapter.addFragment(ListFragment.getInstance());
        adapter.addFragment(CalculateActivity.getInstance());
        adapter.addFragment(StatisticClass.getInstance());
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                makeText(findViewById(R.id.coordinator_layout), "Delete all the list?? ", Snackbar.LENGTH_LONG).show();
                return true;

            case R.id.calculate_all_notes:
//
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//
//                transaction.replace(R.id.view_pager, CalculateActivity.getInstance());
//                transaction.addToBackStack(null);
//                transaction.commit();
                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), MainActivity.this);
                getSupportFragmentManager().beginTransaction().replace(R.id.view_pager , adapter.getItem(1)).commit();
//                replaceFragment();

                return true;

            case R.id.statistic:
                StatisticClass fragment = new StatisticClass();
                if(getSupportFragmentManager().findFragmentById(R.id.view_pager) != null) {
                    getSupportFragmentManager()
                            .beginTransaction().
                            remove(getSupportFragmentManager().findFragmentById(R.id.view_pager)).commit();
                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.view_pager, fragment)
                        .commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public int recieveCalculatePriority() {
        calculatePriority = ListFragment.getInstance().calculatePriority;
        return calculatePriority;
    }

    public int reciceveCalculateDeposit() {
        depositCalculate = ListFragment.getInstance().depositCalculate;
        return depositCalculate;
    }


    @SuppressLint("ResourceType")
    public Snackbar makeText(CoordinatorLayout context, String message, int duration) {
        CoordinatorLayout coordinatorLayout = context;
        ViewGroup layout;
        Snackbar snackbar = (Snackbar) Snackbar
                .make(coordinatorLayout, message, duration);
        layout = (ViewGroup) snackbar.getView();
        coordinatorLayout.setPadding(30, 10, 30, 30);
        layout.setBackground(context.getResources().getDrawable(R.drawable.background_toast));
        android.widget.TextView text = (android.widget.TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        text.setTextColor(context.getResources().getColor(R.color.white));

        snackbar.setAction("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "List deleted", Toast.LENGTH_SHORT).show();
                mNoteViewModel.deleteAll();
                return;
            }
        });
        snackbar.setActionTextColor(R.color.black);
        return snackbar;
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .addToBackStack("").commit();
    }


}
