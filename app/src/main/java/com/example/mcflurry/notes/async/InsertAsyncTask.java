package com.example.mcflurry.notes.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mcflurry.notes.models.Note;
import com.example.mcflurry.notes.persistence.NoteDao;

public class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
    // AsyncTask is good for executing ONE single task on a bg thread until completion then it will be destroyed.
    // Whatever u specified in the AsyncTask<THINGS> will be passed into doInBackGround(THINGS) method.

    private static final String TAG = "InsertAsyncTask";
    private NoteDao mNoteDao;

    public InsertAsyncTask(NoteDao dao){
        mNoteDao = dao;
    }


    @Override
    protected Void doInBackground(Note... notes) {
        Log.d(TAG, "doInBackground: thread: " + Thread.currentThread().getName()); //Activity will Run on MAIN thread where as Async will run on BG (different) thread.
        mNoteDao.insertNotes(notes);
        return null;
    }



}
