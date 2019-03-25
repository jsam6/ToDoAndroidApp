package com.example.mcflurry.notes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcflurry.notes.R;
import com.example.mcflurry.notes.models.Note;
import com.example.mcflurry.notes.util.Utility;

import java.util.ArrayList;

public class NotesRecyclerAdapter extends RecyclerView.Adapter<NotesRecyclerAdapter.ViewHolder> {
    private static final String TAG = "NotesRecyclerAdapter";
    private ArrayList<Note> mNotes = new ArrayList<>();
    private OnNoteListener mOnNoteListener;

    public NotesRecyclerAdapter(ArrayList<Note> notes, OnNoteListener onNoteListener) {
        this.mNotes = notes;
        this.mOnNoteListener = onNoteListener;
    }
// ************************************************************
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //viewGroup is parent layout.
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_note_list_item, viewGroup, false);
        return new ViewHolder(view, mOnNoteListener );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        try{
            String month = mNotes.get(position).getTimestamp().substring(0, 2); // get first two number in MM-yyyy which is the month.
            month = Utility.getMonthFromNumber(month);
            String year = mNotes.get(position).getTimestamp().substring(3);
            String timestamp = month + " " + year;
            holder.timestamp.setText(timestamp);
            holder.title.setText(mNotes.get(position).getTitle());
        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage() );
        }

        

    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }
// ************************************************************

//    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
//        private TextView title, timestamp;
//        OnNoteListener onNoteListener;
//
//        public ViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
//            super(itemView);
//
//            title = itemView.findViewById(R.id.note_title);
//            timestamp = itemView.findViewById(R.id.note_timestamp);
//            this.onNoteListener = onNoteListener;
//
//            itemView.setOnClickListener(this);
//        }
//
//        @Override
//        public void onClick(View view) {
//            onNoteListener.onNoteClick(getAdapterPosition());
//        }
//    }
//
//    public interface OnNoteListener{
//        void onNoteClick(int position);
//
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView timestamp, title;
        OnNoteListener mOnNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            timestamp = itemView.findViewById(R.id.note_timestamp);
            title = itemView.findViewById(R.id.note_title);
            mOnNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }


}
