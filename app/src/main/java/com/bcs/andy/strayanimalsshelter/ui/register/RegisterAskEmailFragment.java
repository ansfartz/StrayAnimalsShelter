package com.bcs.andy.strayanimalsshelter.ui.register;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bcs.andy.strayanimalsshelter.R;

public class RegisterAskEmailFragment extends Fragment {

    // vars

    // UI
    private ImageView backIcon;
    private FloatingActionButton fabNextFragment;
    private EditText emailET;
    private View view;

    public RegisterAskEmailFragment() { }

    private void init() {
        backIcon = (ImageView) view.findViewById(R.id.register_aE_goBack);
        fabNextFragment = (FloatingActionButton) view.findViewById(R.id.register_aE_next);
        emailET = (EditText) view.findViewById(R.id.register_aE_emailET);


        fabNextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validateEmail(emailET.getText().toString())) {
                    RegisterAskUserPasswordFragment userAndPasswordFragment = new RegisterAskUserPasswordFragment();
                    Bundle args = new Bundle();
                    args.putString("email", emailET.getText().toString().trim());
                    userAndPasswordFragment.setArguments(args);

                    getFragmentManager().beginTransaction()
                            .replace(R.id.register_fragment_container, userAndPasswordFragment)
                            .commit();

                } else {
                    emailET.setError("Please fill a valid email address");
                }

            }
        });


        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(emailET.getText().toString().trim())) {
                    fabNextFragment.show();
                } else {
                    fabNextFragment.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private boolean validateEmail(String email) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailET.setError("Please enter a valid email address");
            return false;
        }
        return true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_register_ask_email, container, false);
        init();



        return view;
    }


}
