package com.example.mcflurry.notes;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.mcflurry.notes.adapters.NotesRecyclerAdapter;
import com.example.mcflurry.notes.models.Note;
import com.example.mcflurry.notes.persistence.NoteRepository;
import com.example.mcflurry.notes.util.VerticalSpacingItemDecorator;

import java.util.ArrayList;
import java.util.List;

public class NotesListActivity extends AppCompatActivity implements
        NotesRecyclerAdapter.OnNoteListener,
        FloatingActionButton.OnClickListener

{
    private static final String TAG = "NotesListActivity";

    //UI .... if there's a 'm' infront if variable then its most likely a GLOBAL var.
    // elsedont have then its most likely a local or variable in a method.
    private RecyclerView mRecyclerView;
    //variable
    private ArrayList<Note> mNotes = new ArrayList<>();
    private NotesRecyclerAdapter mNoteRecyclerAdapter;
    private NoteRepository mNoteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_list);
        mRecyclerView = findViewById(R.id.recyclerView);

        findViewById(R.id.fab).setOnClickListener(this); //FloatingActionButton



        initRecyclerView();
        mNoteRepository = new NoteRepository(this);
        retrieveNotes(); // insertFakeNotes();

//        Note note = new Note("Title", "Content","timestamp")
//        Log.d(TAG, "onCreate: myNote: "+ note.getTitle());

        Log.d(TAG, "onCreate: thread: " + Thread.currentThread().getName());
        // mNoteRepository.insertNoteTask(new Note()) // this will cause the app to creash bcs it is trying to insertdata on the MAIN thread.

        setSupportActionBar((Toolbar)findViewById(R.id.notes_toolbar));
        setTitle("Notes");
    }

    private void retrieveNotes(){
        // in this method, using a method to observe if data is change LIVE
        //Anytime there is a change to the LiveData object, this method will be triggered.
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                if(mNotes.size() > 0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void insertFakeNotes(){
        for (int i=0; i<100; i++){
            Note note = new Note();
            note.setTitle("title #" + i);
            note.setContent("content #: " + i);
            note.setTimestamp("Jan 2019");
            mNotes.add(note);
        }
        mNoteRecyclerAdapter.notifyDataSetChanged(); //update list
    }

    private void initRecyclerView(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(10);
        mRecyclerView.addItemDecoration(itemDecorator);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView); // Attach swiping listener to recyclerView.
        mNoteRecyclerAdapter = new NotesRecyclerAdapter(mNotes, this);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

    @Override
    public void onNoteClick(int position) {
//        mNotes.get(position);
//        Intent intent = new Intent(this, NewActivity.class);
//        startActivity(intent);
        Log.d(TAG, "onNoteClick: " + position);
        Intent intent = new Intent(this, NoteActivity.class);
        intent.putExtra("selected_note", mNotes.get(position));
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        //View.OnClickListener override method for floatingActionButton
        Intent intent = new Intent(this, NoteActivity.class);
        startActivity(intent);
    }

    private void deleteNote(Note note) {
        mNotes.remove(note);
        mNoteRecyclerAdapter.notifyDataSetChanged();

        mNoteRepository.deleteNoteTask(note);
    }

    private ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        // Swiping functionailty.
        // '0' for onMove because we are not Moving. // if you want to swipe both RIGHT & LEFT then you can ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT
        //SimpleCallback is a simple method instead of Callback. The Callback has more method associated with it.
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            // Moving items in notes
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            // Swiping function in notes
            deleteNote(mNotes.get(viewHolder.getAdapterPosition()));
        }
    };
}
