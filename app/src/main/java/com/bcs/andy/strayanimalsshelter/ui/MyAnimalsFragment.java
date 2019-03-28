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

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.UserAnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.database.UserAnimalsDatabaseListener;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.adapter.AnimalAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MyAnimalsFragment extends Fragment implements AnimalAdapter.AnimalAdapterListener {

    private static final String TAG = "MyAnimalsFragment";

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;

    // UI
    private ProgressBar animalsLoadingProgressBar;
    private FloatingActionButton addAnimalsBtn;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter animalAdapter;
    private List<Animal> animalList = new ArrayList<>();
    private ConstraintLayout CL;

    // TODO: delete addDataBtn (the one in the middle of the fragment layout.
    // it's only there for having an example of Data adding to Firebase
    private Button addDataBtn;


    public MyAnimalsFragment() {
        // Required empty public constructor
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    private void init() {
        addDataBtn = (Button) CL.findViewById(R.id.addDataBtn);

        animalsLoadingProgressBar = (ProgressBar) CL.findViewById(R.id.loadingAnimalsProgressBar);
        animalsLoadingProgressBar.setVisibility(View.VISIBLE);

        addAnimalsBtn = (FloatingActionButton) CL.findViewById(R.id.addAnimalsFloatingActionBtn);

        recyclerView = (RecyclerView) CL.findViewById(R.id.myAnimalsRecyclerView);
        recyclerView.setHasFixedSize(false); // no adapter content ( card with data ) has fixed size. They may vary, depending on the length of text inside them; use this for recyclerView internal optimisation! Won't affect anything else
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UserAnimalsDatabase userAnimalsDatabase = new UserAnimalsDatabase();
        userAnimalsDatabase.readCurrentUserAnimals(new UserAnimalsDatabaseListener() {
            @Override
            public void onCallback(List<Animal> list) {
                // TODO: dont clear() and allAll, just add the new ones. for efficiency
                animalList.clear();
                animalList.addAll(list);
                animalsLoadingProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onCallBack: MY ANIMALS LIST: " + animalList.toString());

                animalAdapter = new AnimalAdapter(animalList, getActivity(), MyAnimalsFragment.this);
                recyclerView.setAdapter(animalAdapter);

            }
        });

        // code UNDER readCurrentUserAnimals() will happen first
        // because method is asynchronous


        Log.d(TAG, "AFTER: " + animalList.toString());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // use this CL inside init()
        CL = (ConstraintLayout) inflater.inflate(R.layout.fragment_my_animals, container, false);
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
                Animal a3 = new Animal("MyAnimalo", "dog", true, false, 3, "nothing");
                databaseReference.child("animals").child("ID1").setValue(a1);
                databaseReference.child("animals").child("ID2").setValue(a2);
                databaseReference.child("animals").child("ID3").setValue(a3);

            }
        });


        return CL;

    }

    @Override
    public void onAnimalClick(int position, Animal animal) {
        Intent intent = new Intent(getContext(), SelectedAnimalActivity.class);
        intent.putExtra("selectedAnimalName", animal.getAnimalName());
        intent.putExtra("selectedAnimalAge", animal.getAproxAge());
        intent.putExtra("selectedAnimalObs", animal.getObservations());
        intent.putExtra("selectedAnimalSpecies", animal.getSpecies());
        startActivity(intent);

    }
}
