package com.example.notes.util;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/*
    this is a helper class that facilitate handling the back stack for fragments
 */
public class MainFragmentManager {

    private static MainFragmentManager instance;
    private List<Fragment> fragments = new ArrayList<>();

    public static MainFragmentManager getInstance() {
        if (instance == null) {
            instance = new MainFragmentManager();
        }
        return instance;
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

    public void removeFragment(int position) {
        fragments.remove(position);
    }

    public List<Fragment> getFragments() {
        return fragments;
    }

}
