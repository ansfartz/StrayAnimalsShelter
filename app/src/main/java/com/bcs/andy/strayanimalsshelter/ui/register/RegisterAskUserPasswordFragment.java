package com.bcs.andy.strayanimalsshelter.ui.register;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.model.User;
import com.bcs.andy.strayanimalsshelter.ui.MainActivity;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;


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

    // Firebase
    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;


    public RegisterAskUserPasswordFragment() {
    }

    private void initFirebase() {
        usersRef = UserUtils.getUsersReference();
        firebaseAuth = FirebaseAuth.getInstance();
    }

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
                } else {
                    isPasswordShown = true;
                    passwordET.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
            }
        });


        userET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(userET.getText().toString()) && !TextUtils.isEmpty(passwordET.getText().toString())) {
                    fabNextFragment.show();
                } else {
                    fabNextFragment.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(userET.getText().toString()) && !TextUtils.isEmpty(passwordET.getText().toString())) {
                    fabNextFragment.show();
                } else {
                    fabNextFragment.hide();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fabNextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    String email = getArguments().getString("email");
                    String username = userET.getText().toString().trim();
                    String password = passwordET.getText().toString();

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if(task.isSuccessful()) {

                                        // ----------- add user to Firebase Database ----------------------

                                        String uid = task.getResult().getUser().getUid();
                                        User mUser = new User(uid, email, username);
                                        usersRef.child(uid).setValue(mUser);

                                        // ----------------------------------------------------------------


                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(username).build();
                                        user.updateProfile(profileUpdates);

                                        startActivity(new Intent(getActivity(), MainActivity.class).putExtra("displayName", username));
                                        getActivity().finish();
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                            Toast.makeText(getActivity(), "Email is already in use.", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.register_fragment_container, new RegisterAskEmailFragment())
                                                    .commit();
                                        }
                                        else {
                                            Toast.makeText(getActivity(), "An error occurred, please try again later.", Toast.LENGTH_SHORT).show();
                                            getActivity().getSupportFragmentManager().beginTransaction()
                                                    .replace(R.id.register_fragment_container, new RegisterAskEmailFragment())
                                                    .commit();
                                        }
                                    }


                                }
                            });
//                            .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            if (e instanceof FirebaseAuthUserCollisionException) {
//                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });


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
        view = inflater.inflate(R.layout.fragment_register_ask_user_password, container, false);
        initFirebase();
        init();

        return view;

    }


    public boolean validate() {
        if (userET.getText().toString().length() > 20 || userET.getText().toString().length() < 2) {
            userET.setError("User name should be between 2 and 20 characters");
            return false;
        }

        if (passwordET.getText().toString().length() < 5) {
            passwordET.setError("Short passwords are easy to guess. Try one with at least 6 characters");
            return false;
        }
        return true;
    }


}
