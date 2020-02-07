package com.example.notes.ui.notes;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.adapters.NoteRecyclerAdapter;
import com.example.notes.adapters.OnNoteClickListener;
import com.example.notes.databinding.FragmentNoteListBinding;
import com.example.notes.models.Note;
import com.example.notes.ui.IMainActivity;
import com.example.notes.ui.MainViewModel;
import com.example.notes.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.example.notes.DependencyInjection.provideViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment implements View.OnClickListener, OnNoteClickListener {

    private FragmentNoteListBinding binding;
    private NoteRecyclerAdapter adapter;
    private List<Note> notes;
    private IMainActivity iMainActivity;

    private MainViewModel viewModel;
    private ViewModelFactory viewModelFactory;

    public NoteListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iMainActivity = (IMainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_list, container, false);
       initToolbar();
        //set the tool bar we made to this activity

        binding.addNoteButton.setOnClickListener(this);
        setupRecyclerView();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getDisposables().add(viewModel.getAllNotes().doOnNext(new Consumer<List<Note>>() {
            @Override
            public void accept(List<Note> notes) throws Exception {
                if(notes.isEmpty()){
                    binding.noNotesLayout.setVisibility(View.VISIBLE);
                }else{
                    binding.noNotesLayout.setVisibility(View.GONE);
                    adapter.setNotes(notes);
                }
            }
        }).subscribe());
    }

    private void initToolbar() {
        setHasOptionsMenu(true);
        binding.mainToolBar.setTitle("Notes");
        binding.mainToolBar.setTitle(getString(R.string.add_note));
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.mainToolBar);
    }

    private void setupRecyclerView() {
        notes = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NoteRecyclerAdapter(notes, this);
        binding.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //it's better to be here than onCreateView or onCreate (Review the fragment lifecycle)
        viewModelFactory = provideViewModelFactory(getActivity());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.note_list_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.action_delete_all){
            viewModel.deleteAllNotes();
            adapter.getNotes().clear();
            adapter.notifyDataSetChanged();
            binding.noNotesLayout.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_note_button) {
            iMainActivity.addNote();
        }
    }

    @Override
    public void onClick(int position) {
        Note note = new Note(adapter.getNotes().get(position).getId(),
                adapter.getNotes().get(position).getTitle(),
                adapter.getNotes().get(position).getDescription());
        iMainActivity.updateNote(note);
    }
}
