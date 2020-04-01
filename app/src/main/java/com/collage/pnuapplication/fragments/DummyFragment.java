package com.collage.pnuapplication.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.collage.pnuapplication.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DummyFragment extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frament_dummy, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
