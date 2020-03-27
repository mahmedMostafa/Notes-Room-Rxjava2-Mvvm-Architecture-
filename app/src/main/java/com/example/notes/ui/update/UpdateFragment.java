package com.example.notes.ui.update;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.notes.R;
import com.example.notes.databinding.FragmentUpdateNoteBinding;
import com.example.notes.models.Note;
import com.example.notes.ui.MainViewModel;
import com.example.notes.util.ViewModelFactory;

import static com.example.notes.DependencyInjection.provideViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateFragment extends Fragment implements View.OnClickListener {

    private FragmentUpdateNoteBinding binding;

    private MainViewModel viewModel;
    private ViewModelFactory viewModelFactory;
    private Note editedNote;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_update_note, container, false);
        NavigationUI.setupWithNavController(binding.toolBar,NavHostFragment.findNavController(this));
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            UpdateFragmentArgs args = UpdateFragmentArgs.fromBundle(getArguments());
            editedNote = args.getNote();
            binding.titleEditText.setText(editedNote.getTitle());
            binding.descriptionEditText.setText(editedNote.getDescription());
        }
        binding.addNoteButton.setOnClickListener(this);
        return binding.getRoot();
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
        inflater.inflate(R.menu.add_note_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getActivity().onBackPressed();
        } else if (item.getItemId() == R.id.action_done) {
            updateNote();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_note_button) {
            updateNote();
        }
    }

    private void updateNote() {
        viewModel.updateNote(new Note(editedNote.getId(),
                binding.titleEditText.getText().toString(),
                binding.descriptionEditText.getText().toString()));
        if (isValid()) getActivity().onBackPressed();
    }
}
