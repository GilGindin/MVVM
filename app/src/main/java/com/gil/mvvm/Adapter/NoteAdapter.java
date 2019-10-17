package com.gil.mvvm.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gil.mvvm.Note;
import com.gil.mvvm.R;

import java.util.List;


public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private Context mContext;
    private OnItemClickListener listener;
    private List<Note> notes;
    private OnDeleteClickListener mDeleteClickListener;
    private OnItemClickListener mOnItemClickListener;

    public interface OnDeleteClickListener {
        void onDeleteClickListener(Note myNote);
    }

//    public NoteAdapter(Context context) {
//        mContext = context;
//
//    }

    public NoteAdapter(Context context, OnDeleteClickListener deleteClickListener, OnItemClickListener mOnItemClickListener) {
        mContext = context;
        this.mDeleteClickListener = deleteClickListener;
        this.mOnItemClickListener = mOnItemClickListener;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item_xml, viewGroup, false);
        return new NoteHolder(itemView, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
        Note currentNote = notes.get(i);
        noteHolder.text_view_title.setText(currentNote.getTitle());
        noteHolder.text_view_description.setText(currentNote.getDescription());
        noteHolder.text_view_priority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        if (notes != null) {
            return notes.size();
        } else return 0;
    }


    public Context getContext() {
        return mContext;
    }

    public void deleteItem(int position) {

        if (mDeleteClickListener != null) {
            mDeleteClickListener.onDeleteClickListener(notes.get(position));
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, notes.size());
            notes.remove(position);
        }
    }


    public void submitList(List<Note> mNotes) {

        notes = mNotes;
        notifyDataSetChanged();
    }


    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView text_view_priority, text_view_title, text_view_description;
        private OnItemClickListener mOnItemClickListener;

        public NoteHolder(@NonNull View itemView, OnItemClickListener mOnItemClickListener) {
            super(itemView);

            text_view_title = itemView.findViewById(R.id.text_view_title);
            text_view_priority = itemView.findViewById(R.id.text_view_priority);
            text_view_description = itemView.findViewById(R.id.text_view_description);
            this.mOnItemClickListener = mOnItemClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (mOnItemClickListener != null && position != RecyclerView.NO_POSITION) {
                mOnItemClickListener.onItemClick(notes.get(position));
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

}
