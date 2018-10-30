package com.yksformuller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yksformuller.R;

public class BellFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bell, parent, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}

