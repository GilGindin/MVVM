package com.gil.mvvm;

import android.annotation.SuppressLint;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NoteAdapter.OnDeleteClickListener {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    public static final String CALCAULATE_PRIORITY = "com.gil.mvvm.CALCAULATE_PRIORITY";
    public static final String CALCAULATE_DEPOSIT = "com.gil.mvvm.CALCAULATE_DEPOSIT";

    private NoteViewModel mNoteViewModel;
    private int calculatePriority;
    private int depositCalculate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CoordinatorLayout coordinator_layout = findViewById(R.id.coordinator_layout);

        FloatingActionButton buttonAddNote = findViewById(R.id.button_add_note);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter(this, this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
                calculatePriority = 0;
                for (int i = 0; i < notes.size(); i++) {
                    Note currentnNote = notes.get(i);
                    calculatePriority += currentnNote.getPriority();
                    Log.d("", "onChanged:------------------- " + calculatePriority);
                }

                for (int i = 0; i < notes.size(); i++) {
                    Note currentnNote = notes.get(i);
                    depositCalculate += currentnNote.getDeposit();
                    Log.d("", "onChanged:------------------- " + depositCalculate);
                }
            }
        });

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, AddEditNoteActivity.class);
                intent.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DECRIPTION, note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());
                intent.putExtra(AddEditNoteActivity.EXTRA_DEPOSIT, note.getDeposit());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DECRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);
            int deposit = data.getIntExtra(AddEditNoteActivity.EXTRA_DEPOSIT, 0);

            Note note = new Note(title, description, priority, deposit);
            mNoteViewModel.insert(note);

            Toast.makeText(this, "New saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditNoteActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Note can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditNoteActivity.EXTRA_DECRIPTION);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, 0);
            int deposit = data.getIntExtra(AddEditNoteActivity.EXTRA_DEPOSIT, 0);

            Note note = new Note(title, description, priority, deposit);
            note.setId(id);
            mNoteViewModel.update(note);

            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(MainActivity.this, CalculateActivity.class);
                intent.putExtra(CALCAULATE_PRIORITY, calculatePriority);
                startActivity(intent);
                return true;

            case R.id.statistic:
                Intent intent1 = new Intent(MainActivity.this, StatisticClass.class);
                intent1.putExtra(CALCAULATE_PRIORITY, calculatePriority);
                intent1.putExtra(CALCAULATE_DEPOSIT, depositCalculate);
                startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    public void onDeleteClickListener(Note myNote) {
        mNoteViewModel.delete(myNote);
    }

}
