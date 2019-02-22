package com.bcs.andy.strayanimalsshelter;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AccountActivity extends AppCompatActivity {


    private Button signOutBtn;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                finish();
            }
        });


        TextView accountTextView = (TextView) findViewById(R.id.AccountTextView);

        //TODO: if user.getDisplayName == null, get the NAME choice from input in RegisterActivity
        if (user.getDisplayName() == null)
        {
            accountTextView.setText("xxx");
        } else {
            accountTextView.setText(user.getDisplayName());
        }


    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
