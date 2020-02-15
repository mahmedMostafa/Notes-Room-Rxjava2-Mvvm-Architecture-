package com.example.notes.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.notes.DependencyInjection;
import com.example.notes.R;
import com.example.notes.adapters.NoteRecyclerAdapter;
import com.example.notes.databinding.ActivityMainBinding;
import com.example.notes.models.Note;
import com.example.notes.ui.IMainActivity;
import com.example.notes.ui.addnote.AddNoteFragment;
import com.example.notes.ui.notes.NoteListFragment;
import com.example.notes.util.MainFragmentManager;
import com.example.notes.util.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

import static com.example.notes.DependencyInjection.provideViewModelFactory;

public class MainActivity extends AppCompatActivity implements IMainActivity {

    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        if (savedInstanceState == null)
            loadFragment(new NoteListFragment(), true);
    }

    private void loadFragment(Fragment fragment, boolean lateralMovement) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (lateralMovement) {
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left);
        }
        String tag = "";
        if (fragment instanceof NoteListFragment) {
            tag = getString(R.string.note_list_fragment);
        } else if (fragment instanceof AddNoteFragment) {
            tag = getString(R.string.add_note_fragment);
        }

        transaction.add(R.id.container, fragment, tag).commit();

        MainFragmentManager.getInstance().addFragment(fragment);
        showFragment(fragment, false);
    }

    private void showFragment(Fragment fragment, boolean backMove) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (backMove) {
            transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right);
        }
        transaction.show(fragment).commit();

        //hide any other fragment
        for (Fragment f : MainFragmentManager.getInstance().getFragments()) {
            if (f != null) {
                if (f.getTag() != null && !f.getTag().equals(fragment.getTag())) {
                    getSupportFragmentManager().beginTransaction().hide(f).commit();
                }
            }
        }

        Log.d(TAG, "showFragment: num fragments: " + MainFragmentManager.getInstance().getFragments().size());
    }

    @Override
    public void onBackPressed() {
        List<Fragment> fragments = new ArrayList<>(MainFragmentManager.getInstance().getFragments());

        if (fragments.size() > 1) {
            //remove the last fragment from the transaction
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.remove(fragments.get(fragments.size() - 1)).commit();
            //remove it from the main fragment manager
            MainFragmentManager.getInstance().removeFragment(fragments.size() - 1);
            //show the fragment before it and enable the back movement
            showFragment(fragments.get(fragments.size() - 2), true);
        }else{
            super.onBackPressed();
        }
    }
    @Override
    public void addNote() {
        loadFragment(new AddNoteFragment(), true);
    }

    @Override
    public void addNoteButton() {
        onBackPressed();
    }

    @Override
    public void updateNote(Note note) {
        loadFragment(AddNoteFragment.newInstance(note),true);
    }
}
