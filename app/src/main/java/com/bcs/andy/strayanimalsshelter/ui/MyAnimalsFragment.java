package com.bcs.andy.strayanimalsshelter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.DatabaseService;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.model.AnimalAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MyAnimalsFragment extends Fragment {


    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;


    private ProgressBar myProgressBar;
    private FloatingActionButton addAnimalsBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter animalAdapter;
    private List<Animal> listAnimals = new ArrayList<>();

    private Button addDataBtn;
    private TextView accountTextView;
    private ConstraintLayout LL;

    public MyAnimalsFragment() {
        // Required empty public constructor
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void init() {
        accountTextView = (TextView) LL.findViewById(R.id.AccountTextView);
        addDataBtn = (Button) LL.findViewById(R.id.addDataBtn);

        myProgressBar = (ProgressBar) LL.findViewById(R.id.loadingProgressBar);
        myProgressBar.setVisibility(View.VISIBLE);

        addAnimalsBtn = (FloatingActionButton) LL.findViewById(R.id.addAnimalsFloatingActionBtn);

        recyclerView = (RecyclerView) LL.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(false); // no adapter content ( card with data ) has fixed size. They may vary, depending on the length of text inside them; use this for recyclerView internal optimisation! Won't affect anything else
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseService databaseService = new DatabaseService();
        databaseService.readItemsFromDatabase(new DatabaseService.FirebaseCallback() {
            @Override
            public void onCallback(List<Animal> list) {
                listAnimals.clear();
                listAnimals.addAll(list);
                myProgressBar.setVisibility(View.GONE);
                Log.d("DATABASE_TAG", "MYLIST: " + listAnimals.toString());

                animalAdapter = new AnimalAdapter(listAnimals, getActivity());
                recyclerView.setAdapter(animalAdapter);

            }
        });

        // code UNDER readItemsFromDatabase(..) will happen first, because method is asynchronous


        Log.d("DATABASE_TAG", "AFTER FOR: " + listAnimals.toString());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LL = (ConstraintLayout) inflater.inflate(R.layout.fragment_myanimals, container, false);

        initFirebase();
        init();

        addAnimalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddAnimalsPopUpActivity.class));
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


        if (user.getDisplayName() == null)
        {
            // this happenes when coming from the Register Page. so ONLY ONCE per account, when they register and instantly enter the app
            String greeting = "Hello, " + getActivity().getIntent().getExtras().getString("displayName");
            accountTextView.setText(greeting);
        } else {
            // once it's updated(after first time registration), it will always come in "else"
            String greeting = "Hello, " + user.getDisplayName();
            accountTextView.setText(greeting);
        }


        return LL;


    }



}
