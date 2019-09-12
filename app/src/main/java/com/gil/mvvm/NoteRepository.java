package com.gil.mvvm;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDataBase mNoteDataBase = NoteDataBase.getInstance(application);
        mNoteDao = mNoteDataBase.mNoteDao();
        allNotes = mNoteDao.getAllNotes();
    }

    public void insert(Note note) {
        new InsertAsyncTask(mNoteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateAsyncTask(mNoteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteAsyncTask(mNoteDao).execute(note);
    }

    public void deleteAll() {
        new DeleteAllNotesAsyncTask(mNoteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    private static class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mNoteDao;

        private InsertAsyncTask(NoteDao noteDao) {
            this.mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mNoteDao;

        private UpdateAsyncTask(NoteDao noteDao) {
            this.mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mNoteDao;

        private DeleteAsyncTask(NoteDao noteDao) {
            this.mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao mNoteDao;

        private DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mNoteDao.deleteAllNote();
            return null;
        }
    }
}
