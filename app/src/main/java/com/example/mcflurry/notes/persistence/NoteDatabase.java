package com.example.mcflurry.notes.persistence;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.mcflurry.notes.models.Note;

@Database(entities = {Note.class}, version = 1) // IF added new tables, or entities in Note class, need to change the version. That is how Room persistence works.
public abstract class NoteDatabase extends RoomDatabase {
    // put it as abstract class so dont need to add all the other not used methods.

    public static final String DATABASE_NAME = "notes_db";

    private static NoteDatabase instance;
    // Singleton pattern.
    // Singleton pattern refers to an instance of an object.
    static NoteDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    NoteDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract NoteDao getNoteDao();

}
