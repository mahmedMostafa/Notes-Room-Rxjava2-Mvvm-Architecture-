package com.example.notes.ui.notes;


import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.transition.TransitionManager;
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
import com.example.notes.ui.MainViewModel;
import com.example.notes.util.C;
import com.example.notes.util.ViewModelFactory;
import com.google.android.material.transition.MaterialContainerTransform;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.notes.DependencyInjection.provideViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoteListFragment extends Fragment implements View.OnClickListener, OnNoteClickListener {

    private FragmentNoteListBinding binding;
    private NoteRecyclerAdapter adapter;
    private List<Note> notes;

    private MainViewModel viewModel;
    private ViewModelFactory viewModelFactory;
    private MaterialContainerTransform materialTransform;
    private MaterialContainerTransform backMaterialTransform;
    private boolean hasOpened = false;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_note_list, container, false);
        setUpViews();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpViews() {
        binding.backImage.setOnClickListener(this);
        binding.doneImage.setOnClickListener(this);
        binding.addButton.setOnClickListener(this);
        binding.addNoteButton.setOnClickListener(this);
        initToolbar();
        animate();
        setupRecyclerView();
    }

    private void setUpBackPressed() {
        //This is used to handle the back pressed in the fragment
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hasOpened) {
                    closeAddNoteView();
                    hasOpened = false;
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpBackPressed();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.getDisposables().add(viewModel.getAllNotes().doOnNext(notes -> {
            if (notes.isEmpty()) {
                binding.noNotesLayout.setVisibility(View.VISIBLE);
            } else {
                binding.noNotesLayout.setVisibility(View.GONE);
                adapter.setNotes(notes);
            }
        }).subscribe());
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(binding.titleEditText.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please Enter a Title", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(binding.descriptionEditText.getText().toString().trim())) {
            Toast.makeText(getActivity(), "Please Enter a description", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void insertNote() {
        Note note = new Note(
                String.valueOf(new Random().nextInt()),
                binding.titleEditText.getText().toString().trim(),
                binding.descriptionEditText.getText().toString().trim()
        );
        if (isValid()) {
            viewModel.insertNote(note);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void animate() {
        materialTransform = new MaterialContainerTransform(binding.addNoteButton.getContext());
        materialTransform.setStartView(binding.addNoteButton);
        materialTransform.setEndView(binding.secondView);
        //materialTransform.setPathMotion(new MaterialArcMotion());
        materialTransform.setDuration(500);
        materialTransform.setScrimColor(Color.TRANSPARENT);
        //this is for the animation when the back button is pressed
        backMaterialTransform = new MaterialContainerTransform(binding.secondView.getContext());
        backMaterialTransform.setStartView(binding.secondView);
        backMaterialTransform.setEndView(binding.addNoteButton);
        //backMaterialTransform.setPathMotion(new MaterialArcMotion());
        backMaterialTransform.setDuration(500);
        backMaterialTransform.setScrimColor(Color.TRANSPARENT);
    }

    public void closeAddNoteView() {
        TransitionManager.beginDelayedTransition(binding.secondView, backMaterialTransform);
        binding.addNoteButton.setVisibility(View.VISIBLE);
        binding.secondView.setVisibility(View.INVISIBLE);
    }

    private void initToolbar() {
        setHasOptionsMenu(true);
        binding.mainToolBar.setTitle(getResources().getString(R.string.notes_title));
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
        inflater.inflate(R.menu.note_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            viewModel.deleteAllNotes();
            adapter.getNotes().clear();
            adapter.notifyDataSetChanged();
            binding.noNotesLayout.setVisibility(View.VISIBLE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_note_button: {
                hasOpened = true;
                C.isViewOpen = true;
                //iMainActivity.addNote();
                binding.titleEditText.setText("");
                binding.descriptionEditText.setText("");
                TransitionManager.beginDelayedTransition(binding.secondView, materialTransform);
                binding.addNoteButton.setVisibility(View.INVISIBLE);
                binding.secondView.setVisibility(View.VISIBLE);
                break;
            }
            case R.id.back_image: {
                closeAddNoteView();
                break;
            }
            case R.id.add_button:
            case R.id.done_image: {
                insertNote();
                closeAddNoteView();
                break;
            }
        }
    }

    @Override
    public void onClick(int position) {
        Note note = new Note(adapter.getNotes().get(position).getId(),
                adapter.getNotes().get(position).getTitle(),
                adapter.getNotes().get(position).getDescription());
        NavHostFragment.findNavController(NoteListFragment.this).navigate(
                NoteListFragmentDirections.actionNoteListFragmentToAddNoteFragment(note)
        );

    }
}
