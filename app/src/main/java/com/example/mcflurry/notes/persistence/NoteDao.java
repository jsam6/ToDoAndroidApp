package com.example.mcflurry.notes.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.mcflurry.notes.models.Note;

import java.util.List;

@Dao
public interface NoteDao {
    //Note Data Access Objects (DAO) : its an interface.

    @Insert
    long[] insertNotes(Note... notes);// elipsy '...'. telling android you will be returning a list of notes. (Note... notes) = (Note[] notes)
    // returns a long[] type.
    // Do not have to start with void(return smtg). can straght write long[] to tell android studio it returns long[] type eg. {2,3,4} . If fail it returns {-1}
    // can use void if u want.

    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getNotes(); //Live Data is a live observation Class. Part of lifecycle library on Android.

//    @Query("SELECT * FROM notes WHERE id = :id")
//    List<Note> getNotesWithCustomQuery(int id);

//    @Query("SELECT * FROM notes WHERE title LIKE :title")
//    List<Note> getNotesWithCustomQuery(String title); // getNotesWithCustomQuery("Jay S*")

    @Delete
    int delete(Note... notes);

    @Update
    int update(Note... notes);
}
