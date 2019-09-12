package com.gil.mvvm;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends ListAdapter<Note , NoteAdapter.NoteHolder> {


    private OnItemClickListener listener;

    public NoteAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Note> DIFF_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getTitle().equals(newItem.getTitle()) && oldItem.getDescription().equals(newItem.getDescription()) && oldItem.getPriority() == newItem.getPriority();
        }
    };

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item_xml, viewGroup, false);

        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
        Note currentNote = getItem(i);
        noteHolder.text_view_title.setText(currentNote.getTitle());
        noteHolder.text_view_description.setText(currentNote.getDescription());
        noteHolder.text_view_priority.setText(String.valueOf(currentNote.getPriority()));
    }

    public Note getNote(int position) {
        return getItem(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder {

        private TextView text_view_priority, text_view_title, text_view_description;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);

            text_view_title = itemView.findViewById(R.id.text_view_title);
            text_view_priority = itemView.findViewById(R.id.text_view_priority);
            text_view_description = itemView.findViewById(R.id.text_view_description);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
