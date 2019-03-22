package com.example.mcflurry.notes.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Update;
import android.content.Context;

import com.example.mcflurry.notes.async.DeleteAsyncTask;
import com.example.mcflurry.notes.async.InsertAsyncTask;
import com.example.mcflurry.notes.async.UpdateAsyncTask;
import com.example.mcflurry.notes.models.Note;

import java.util.List;

public class NoteRepository {

    private NoteDatabase mNoteDatabase;

    public NoteRepository(Context context) {
        mNoteDatabase = NoteDatabase.getInstance(context);
    }

    public void insertNoteTask(Note note){
        new InsertAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
        // in InsertAsync there is no execute method. but that is how it works. Everytime u pass in smtg into async, it will be passed to the doInBackground method.
        // eg. execute(NOTE) -> doInBackground(NOTE)
    }

    public void updateNote(Note note){
        new UpdateAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

    public LiveData<List<Note>> retrieveNotesTask() {

        return mNoteDatabase.getNoteDao().getNotes();
    }

    public void deleteNote(Note note){
        new DeleteAsyncTask(mNoteDatabase.getNoteDao()).execute(note);
    }

}
