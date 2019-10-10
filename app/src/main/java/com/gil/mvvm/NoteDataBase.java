package com.gil.mvvm;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = Note.class, version = 2, exportSchema = false)
public abstract class NoteDataBase extends RoomDatabase {

    private static NoteDataBase instance;

    public abstract NoteDao mNoteDao();

    public static synchronized NoteDataBase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDataBase.class, "onte_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao mNoteDao;

        public PopulateDbAsyncTask(NoteDataBase db) {
            mNoteDao = db.mNoteDao();
        }


        @Override
        protected Void doInBackground(Void... voids) {
            mNoteDao.insert(new Note(" אולם", "הכנס שם", 1 , 0));
            mNoteDao.insert(new Note("צלם", "הכנס שם", 1 , 0));
            mNoteDao.insert(new Note("דיג'יי", "הכנס שם", 1 , 0 ));
            return null;
        }
    }
}
