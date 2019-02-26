package com.bcs.andy.strayanimalsshelter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.models.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AccountActivity extends AppCompatActivity {


    private Button signOutBtn;
    private Button addDataBtn;
    private Button showDataBtn;



    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    List<Animal> animals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        animals = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        TextView accountTextView = (TextView) findViewById(R.id.AccountTextView);
        signOutBtn = (Button) findViewById(R.id.signOutBtn);
        addDataBtn = (Button) findViewById(R.id.addDataBtn);
        showDataBtn = (Button) findViewById(R.id.showDataBtn);


        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(AccountActivity.this, LoginActivity.class));
                finish();
            }
        });

        addDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animal a1 = new Animal("Doggo", "dog", 2, "Needs immediate medical intervention.");
                Animal a2 = new Animal("Catto", "cat", 3, "Should lose weight.");
                databaseReference.child("animals").child("ID1").setValue(a1);
                databaseReference.child("animals").child("ID2").setValue(a2);

            }
        });

        showDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AccountActivity.this, MyAnimalsActivity.class));
            }
        });


        //TODO: if user.getDisplayName == null, get the NAME choice from input in RegisterActivity
        if (user.getDisplayName() == null)
        {
            // this happenes when coming from the Register Page. so ONLY ONCE per account, when they register and instantly enter the app
            String greeting = "Hello, " + getIntent().getExtras().getString("displayName");
            accountTextView.setText(greeting);
        } else {
            // once it's updated(after first time registration), it will always come in "else"
            String greeting = "Hello, " + user.getDisplayName();
            accountTextView.setText(greeting);
        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
