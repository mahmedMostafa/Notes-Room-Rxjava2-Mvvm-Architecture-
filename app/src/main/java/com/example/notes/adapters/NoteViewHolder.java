package com.example.notes.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notes.databinding.NoteListItemBinding;
import com.example.notes.models.Note;

public class NoteViewHolder extends RecyclerView.ViewHolder {

    public TextView title;
    public TextView description;
    private OnNoteClickListener listener;

    private NoteListItemBinding binding;

    public NoteViewHolder(NoteListItemBinding binding,OnNoteClickListener listener) {
        super(binding.getRoot());
        this.binding = binding;
        this.listener = listener;

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if(listener != null){
                    if(position != RecyclerView.NO_POSITION){
                        listener.onClick(position);
                    }
                }
            }
        });
    }

    public void onBind(Note note) {
        binding.itemTitleText.setText(note.getTitle());
        binding.itemDescriptionText.setText(note.getDescription());
    }

}
