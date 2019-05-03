package com.bcs.andy.strayanimalsshelter.ui.register;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;


public class RegisterAskUserPasswordFragment extends Fragment {

    // vars
    private Boolean allFieldsSet = false;
    private boolean isPasswordShown = false;

    // UI
    private ImageView backIcon;
    private FloatingActionButton fabNextFragment;
    private EditText userET, passwordET;
    private TextView togglePasswordTV;
    private View view;


    public RegisterAskUserPasswordFragment() { }

    private void init() {
        backIcon = (ImageView) view.findViewById(R.id.register_aUP_goBack);
        fabNextFragment = (FloatingActionButton) view.findViewById(R.id.register_aUP_next);
        userET = (EditText) view.findViewById(R.id.register_aUP_userNameET);
        passwordET = (EditText) view.findViewById(R.id.register_aUP_passwordET);
        passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        togglePasswordTV = (TextView) view.findViewById(R.id.register_aUP_togglePassword);

        togglePasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordShown) {
                    isPasswordShown = false;
                    passwordET.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                else {
                    isPasswordShown = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });




    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_register_ask_user_password, container, false);
        init();

        return view;

    }


}
