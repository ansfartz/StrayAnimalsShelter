package com.bcs.andy.strayanimalsshelter.ui.register;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatDrawableManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;


public class RegisterAskNameFragment extends Fragment {


    // vars
    private Boolean allFieldsSet = false;

    // UI
    private ImageView backIcon;
    private FloatingActionButton fabNextFragment;
    private EditText firstNameET, lastNameET;
    private View view;

    public RegisterAskNameFragment() { }

    private void init() {
        backIcon = (ImageView) view.findViewById(R.id.register_aN_goBack);
        fabNextFragment = (FloatingActionButton) view.findViewById(R.id.register_aN_next);
        firstNameET = (EditText) view.findViewById(R.id.register_aN_firstNameET);
        lastNameET = (EditText) view.findViewById(R.id.register_aN_lastNameET);
        fabNextFragment.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_right_arrow_green));

        fabNextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "allFieldsSet = " + allFieldsSet, Toast.LENGTH_SHORT).show();
                if (allFieldsSet) {
                    RegisterAskEmailFragment emailFragment = new RegisterAskEmailFragment();
                    Bundle args = new Bundle();
                    args.putString("firstName", firstNameET.getText().toString().trim());
                    args.putString("lastName", lastNameET.getText().toString().trim());
                    emailFragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.register_fragment_container, emailFragment)
                            .commit();
                } else {
                    Toast.makeText(getActivity(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firstNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(firstNameET.getText().toString()) && !TextUtils.isEmpty(lastNameET.getText().toString())) {
                    allFieldsSet = true;
                } else {
                    allFieldsSet = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(firstNameET.getText().toString()) && !TextUtils.isEmpty(lastNameET.getText().toString())) {
                    allFieldsSet = true;
                } else {
                    allFieldsSet = false;
                }
            }
        });


        lastNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!TextUtils.isEmpty(firstNameET.getText().toString()) && !TextUtils.isEmpty(lastNameET.getText().toString())) {
                    allFieldsSet = true;
                } else {
                    allFieldsSet = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(firstNameET.getText().toString()) && !TextUtils.isEmpty(lastNameET.getText().toString())) {
                    allFieldsSet = true;
                } else {
                    allFieldsSet = false;
                }
            }
        });


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register_ask_name, container, false);
        init();

        return view;
    }




}
