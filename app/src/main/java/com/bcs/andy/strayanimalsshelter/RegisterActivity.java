package com.bcs.andy.strayanimalsshelter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordEditText2;
    private Button registerButton;
    private String name, email, password, password2;

    private DatabaseReference usersRef;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usersRef = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();

        nameEditText = (EditText) findViewById(R.id.registerNameEditText);
        emailEditText = (EditText) findViewById(R.id.registerEmailEditText);
        passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        passwordEditText2 = (EditText) findViewById(R.id.registerPasswordEditText2);
        registerButton = (Button) findViewById(R.id.registerBtn);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });


    }


    //TODO: upon entering AccountActivity, DisplayName is not updated, a logout and login is required. WHY? Still looking into it.
    public void startRegister() {
        init();
        if (!validate()) {

            Toast.makeText(this, "Sign up failed", Toast.LENGTH_SHORT).show();

        } else {

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Could not complete registration", Toast.LENGTH_SHORT).show();
                            } else {

                                // ----------- add used to Firebase Database ----------------------

                                String uid = task.getResult().getUser().getUid();
                                User mUser = new User(email, name);
                                usersRef.child(uid).setValue(mUser);

                                // ----------------------------------------------------------------


                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                user.updateProfile(profileUpdates);
                                Toast.makeText(RegisterActivity.this, "Update complete", Toast.LENGTH_SHORT).show();

                                startActivity(new Intent(RegisterActivity.this, AccountActivity.class).putExtra("displayName", name));
                                finish();

                            }
                        }
                    });

        }
    }


    public void init() {
        name = nameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        password2 = passwordEditText2.getText().toString().trim();
    }


    public boolean validate() {
        if (name.isEmpty() || name.length() > 40) {
            nameEditText.setError("Please enter valid name.");
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter valid email address.");
            return false;
        }
        if (password.isEmpty() || password.length() < 5) {
            passwordEditText.setError("Short passwords are easy to guess. Try one with at least 6 characters.");
            return false;
        }
        if (password2.isEmpty() || password2.compareTo(password) != 0) {
            passwordEditText2.setError("Passwords don't match.");
            return false;
        }
        return true;
    }


}
