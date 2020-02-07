package com.example.notes.ui.addnote;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.databinding.FragmentAddNoteBinding;
import com.example.notes.models.Note;
import com.example.notes.ui.IMainActivity;
import com.example.notes.ui.MainViewModel;
import com.example.notes.util.ViewModelFactory;

import java.util.Random;

import static com.example.notes.DependencyInjection.provideViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddNoteFragment extends Fragment implements View.OnClickListener {

    private FragmentAddNoteBinding binding;

    private MainViewModel viewModel;
    private ViewModelFactory viewModelFactory;
    private IMainActivity iMainActivity;

    private Note editedNote;
    private boolean isUpdating = false;

    public AddNoteFragment() {
    }

    public static AddNoteFragment newInstance(Note note) {
        AddNoteFragment addNoteFragment = new AddNoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("note_key", note);
        addNoteFragment.setArguments(bundle);
        return addNoteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_note, container, false);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            binding.toolBar.setTitle(getString(R.string.update_note));
            binding.addNoteButton.setText(getString(R.string.update_note));
            isUpdating = true;
            editedNote = getArguments().getParcelable("note_key");
            binding.titleEditText.setText(editedNote.getTitle());
            binding.descriptionEditText.setText(editedNote.getDescription());
        }
        initToolbar();
        binding.addNoteButton.setOnClickListener(this);
        return binding.getRoot();
    }

    private void initToolbar() {
        binding.toolBar.setTitle(getString(R.string.add_note));
        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.toolBar);
        //this makes the back button itself
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //put the icon and the title for the activity
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //it's better to be here than onCreateView or onCreate (Review the fragment lifecycle)
        viewModelFactory = provideViewModelFactory(getActivity());
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        iMainActivity = (IMainActivity) getActivity();
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.add_note_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.home){
            //this gets us back to the main activity
            //made by android itself
            getActivity().onBackPressed();
        }else if (item.getItemId() == R.id.action_done){
            updateInsertNote();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_note_button) {
            updateInsertNote();
            //iMainActivity.addNoteButton();
        }
    }

    private void updateInsertNote(){
        if(isUpdating){
            viewModel.updateNote(new Note(editedNote.getId(),
                    binding.titleEditText.getText().toString(),
                    binding.descriptionEditText.getText().toString()));
        }else{
            insertNote();
        }
        if(isValid())
            getActivity().onBackPressed();
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
}
