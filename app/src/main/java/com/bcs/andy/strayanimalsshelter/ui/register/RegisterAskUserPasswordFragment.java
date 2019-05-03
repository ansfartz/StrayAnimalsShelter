package com.bcs.andy.strayanimalsshelter.ui.register;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bcs.andy.strayanimalsshelter.R;


public class RegisterAskUserPasswordFragment extends Fragment {


    public RegisterAskUserPasswordFragment() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register_ask_user_password, container, false);
    }


}
