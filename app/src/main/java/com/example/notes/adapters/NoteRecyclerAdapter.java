package com.example.notes.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.databinding.NoteListItemBinding;
import com.example.notes.models.Note;

import java.util.List;

public class NoteRecyclerAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    private List<Note> notes;
    private OnNoteClickListener listener;

    public NoteRecyclerAdapter(List<Note> notes,OnNoteClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public List<Note> getNotes(){
        return notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        NoteListItemBinding binding = NoteListItemBinding.inflate(inflater, parent, false);
        return new NoteViewHolder(binding,listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.onBind(notes.get(position));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
