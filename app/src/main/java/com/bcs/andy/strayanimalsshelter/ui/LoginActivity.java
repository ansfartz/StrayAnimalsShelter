package com.bcs.andy.strayanimalsshelter.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Main Activity, this should come first when the app is started.
 * Handles Authentification and Redirects to either MainActivity or RegistrationActivity
 */
public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginBtn;
    private TextView registerTextView;

    public FirebaseAuth firebaseAuth;

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void init() {
        emailEditText = (EditText) findViewById(R.id.registerEmailEditText);
        passwordEditText = (EditText) findViewById(R.id.registerPasswordEditText);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerTextView = (TextView) findViewById(R.id.registerTextView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initFirebase();
        init();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSignIn();
            }
        });

        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

    }


    /**
     *  Check if email and password input are both filled and correct.
     *  If one or both of them is empty, or if they do not match, prompts a Toast message.
     *  <br>
     *  Continues to {@link MainActivity}
     */
    private void startSignIn() {

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {

            Toast.makeText(LoginActivity.this, "Both fields must be completed.", Toast.LENGTH_SHORT).show();

        } else {

            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                    } else {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    }

                }
            });
        }
    }


}
