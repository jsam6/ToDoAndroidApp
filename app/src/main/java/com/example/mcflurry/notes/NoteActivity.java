package com.example.mcflurry.notes;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mcflurry.notes.models.Note;
import com.example.mcflurry.notes.persistence.NoteRepository;
import com.example.mcflurry.notes.util.Utility;

public class NoteActivity extends AppCompatActivity implements
        View.OnTouchListener,
        GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener,
        View.OnClickListener,
        TextWatcher
{
    private static final String TAG = "NoteActivity";
    private static final int EDIT_MODE_ENABLED = 1;
    private static final int EDIT_MODE_DISABLED = 0;

    // ui components
    private LineEditText mLineEditText;
    private EditText mEditTitle;
    private TextView mViewTitle;
    private RelativeLayout mCheckContainer, mBackArrowContainer;
    private ImageButton mCheck, mBackArrow;
    // vars
    private boolean mIsNewNote;
    private Note mInitialNote;
    private GestureDetector mGestureDetector;
    private int mMode;
    private NoteRepository mNoteRepository;
    private Note mFinalNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        mLineEditText = findViewById(R.id.note_text);
        mEditTitle = findViewById(R.id.note_edit_title);
        mViewTitle = findViewById(R.id.note_text_title);

        mCheckContainer = findViewById(R.id.check_container);
        mBackArrowContainer = findViewById(R.id.back_arrow_container);
        mCheck = findViewById(R.id.toolbar_check);
        mBackArrow = findViewById(R.id.toolbar_back_arrow);

        mNoteRepository = new NoteRepository(this);

        if (getIncomingIntent()) {
            // this is a new Note : EditMode
            setNewNoteProperties();
            enabledEditMode();
        } else {
            // this is NOT a new note : ViewMode
            setNewNoteProperties();
            disableContentInteraction();
        }

        setListeners();

        
    }

    private void setListeners() {
        mLineEditText.setOnTouchListener(this);
        mGestureDetector = new GestureDetector(this, this);
        mViewTitle.setOnClickListener(this);
        mCheck.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mEditTitle.addTextChangedListener(this);
    }

    private boolean getIncomingIntent(){
        if (getIntent().hasExtra("selected_note")){
            mInitialNote = getIntent().getParcelableExtra("selected_note");

            mFinalNote = new Note();
            mFinalNote.setTitle(mInitialNote.getTitle());
            mFinalNote.setContent(mInitialNote.getContent());
            mFinalNote.setTimeStamp(mInitialNote.getTimeStamp());
            mFinalNote.setId(mInitialNote.getId());

//            mFinalNote = getIntent().getParcelableExtra("selected_note");
//            Log.d(TAG, "getIncomingIntent: " + mInitialNote.toString());

            mMode = EDIT_MODE_DISABLED;
            mIsNewNote = false;
            return false;
        }
        mMode = EDIT_MODE_ENABLED;
        mIsNewNote = true;
        return true;

    }

    private void saveChanges() {
        //INSERT & UPDATE runs on ASYNC class thread.
        if (mIsNewNote) {
            // Insert if NEW note
            saveNewNote();
        }else{
            // Update if Exist.
            updateNote();
        }
    }

    private void updateNote() {
        mNoteRepository.updateNote(mFinalNote);
    }


    private void saveNewNote() {
        mNoteRepository.insertNoteTask(mFinalNote);
    }


    private void disableContentInteraction() {
        mLineEditText.setKeyListener(null);
        mLineEditText.setFocusable(false);
        mLineEditText.setFocusableInTouchMode(false);
        mLineEditText.setCursorVisible(false);
        mLineEditText.clearFocus();
    }

    private void enableContentInteraction() {
        // set default keylistener that has been set initially before going null in disableContentInteraction
        mLineEditText.setKeyListener(new EditText(this).getKeyListener());
        mLineEditText.setFocusable(true);
        mLineEditText.setFocusableInTouchMode(true);
        mLineEditText.setCursorVisible(true);
        mLineEditText.requestFocus();
    }

    private void enabledEditMode() {
        mBackArrowContainer.setVisibility(View.GONE);
        mCheckContainer.setVisibility(View.VISIBLE);

        mViewTitle.setVisibility(View.GONE);
        mEditTitle.setVisibility(View.VISIBLE);

        mMode = EDIT_MODE_ENABLED;

        enableContentInteraction();
    }

    private void disableEditMode() {
        mBackArrowContainer.setVisibility(View.VISIBLE);
        mCheckContainer.setVisibility(View.GONE);

        mViewTitle.setVisibility(View.VISIBLE);
        mEditTitle.setVisibility(View.GONE);

        mMode = EDIT_MODE_DISABLED;

        disableContentInteraction();

        String temp = mLineEditText.getText().toString();
        temp = temp.replace("\n", "");
        temp = temp.replace(" ", "");
        if (temp.length() > 0){
            mFinalNote.setTitle(mEditTitle.getText().toString());
            mFinalNote.setContent(mLineEditText.getText().toString());
            String timestamp = Utility.getCurrentTimeStamp(); // in the Utility class, u used STATIC. THat is why no need to instatiate.
            mFinalNote.setTimeStamp(timestamp);

            if (!mFinalNote.getContent().equals(mInitialNote.getContent()) || !mFinalNote.getTitle().equals(mInitialNote.getTitle())){
                saveChanges();
            }
        }

//        hideSoftKeyboard(); >>> Doesnt work when it is here.
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null){
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setNoteProperties() {
        mViewTitle.setText(mInitialNote.getTitle());
        mEditTitle.setText(mInitialNote.getTitle());
        mLineEditText.setText(mInitialNote.getContent());
    }

    private void setNewNoteProperties() {
        mViewTitle.setText("Note Title");
        mEditTitle.setText("Note Title");

        mInitialNote = new Note();
        mFinalNote = new Note();
        mInitialNote.setTitle("Note Title");
        mFinalNote.setTitle("Note Title");
    }

    //******************************************** GestureDetector.OnGestureListener ********************************************************//
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //Touch event will be intercepted by the onTouchlistener (linked to mLineEditText in setListeners()).. Then the motionEvent will be pass in the mGestureDetector.
        return mGestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
    //******************************************** GestureDetector.OnGestureListener ********************************************************//
    //******************************************** GestureDetector.OnDoubleTapListener ********************************************************//
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        enabledEditMode();
        Log.d(TAG, "onDoubleTap: Double tapped");
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }
    //******************************************** GestureDetector.OnDoubleTapListener ********************************************************//
    //******************************************** View.OnClickListener ********************************************************//
    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.toolbar_check: {

                hideSoftKeyboard();
                disableEditMode();
                break;
            }
            case R.id.note_text_title: {
                enabledEditMode();
                mEditTitle.requestFocus();
                mEditTitle.setSelection(mEditTitle.length());
                break;
            }
            case R.id.toolbar_back_arrow: {
                finish(); //Only works in an activity and not a fragment.
                break;
            }
        }
    }
    //******************************************** View.OnClickListener ********************************************************//


    @Override
    public void onBackPressed() {
        if (mMode == EDIT_MODE_ENABLED) {
            // when on EDIT MODE, IF back button in pressed, it goes back to view Mode instead of list view.
            onClick(mCheck);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Will be called when the activity is onPause. When a configuration change occurs(potrait->landscape), the activity is destroyed.
        super.onSaveInstanceState(outState);
        outState.putInt("mode", mMode); // TO identify what mode is it on when it is onPaused.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMode = savedInstanceState.getInt("mode");
        if (mMode == EDIT_MODE_ENABLED){
            enabledEditMode();
        }
    }

    //******************************************** TextWatcher ********************************************************//
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mViewTitle.setText(charSequence.toString());
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
    //******************************************** TextWatcher ********************************************************//
}
