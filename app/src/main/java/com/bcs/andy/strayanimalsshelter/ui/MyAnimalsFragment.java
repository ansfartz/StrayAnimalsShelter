package com.bcs.andy.strayanimalsshelter.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabaseListener;
import com.bcs.andy.strayanimalsshelter.dialog.GetAdoptionRequestDialog;
import com.bcs.andy.strayanimalsshelter.dialog.GetAdoptionRequestDialogListener;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.adapter.AnimalAdapter;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MyAnimalsFragment extends Fragment implements AnimalAdapter.AnimalAdapterListener,
        GetAdoptionRequestDialogListener {

    private static final String TAG = "MyAnimalsFragment";

    // Firebase
    private FirebaseUser user;
    private AnimalsDatabase animalsDatabase;

    // UI
    private ConstraintLayout CL;
    private ProgressBar animalsLoadingProgressBar;
    private FloatingActionButton addAnimalsBtn;
    private RecyclerView recyclerView;
    private AnimalAdapter animalAdapter;

    // vars
    private List<Animal> animalList = new ArrayList<>();


    public MyAnimalsFragment() {

    }

    private void initFirebase() {
        user = UserUtils.getCurrentUser();
    }

    private void init() {

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

        animalsDatabase = new AnimalsDatabase();
        animalsDatabase.readCurrentUserAnimals(new AnimalsDatabaseListener() {
            @Override
            public void onCallback(List<Animal> list) {
                animalList.clear();
                animalList.addAll(list);
                animalsLoadingProgressBar.setVisibility(View.GONE);

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
        // use CL inside init()
        CL = (ConstraintLayout) inflater.inflate(R.layout.fragment_my_animals, container, false);
        initFirebase();
        init();

        addAnimalsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddAnimalToMyselfActivity.class));
            }
        });

        return CL;

    }

    @Override
    public void onAnimalClick(int position, Animal animal) {
        Intent selectedAnimalIntent = new Intent(getContext(), SelectedAnimalFromListActivity.class);
        selectedAnimalIntent.putExtra("selectedAnimal", animal);
        startActivity(selectedAnimalIntent);
    }

    @Override
    public void onHelpingHandClick(Animal animal) {
        GetAdoptionRequestDialog dialog = new GetAdoptionRequestDialog();
        Bundle args = new Bundle();
        args.putParcelable("animal", animal);
        dialog.setArguments(args);
        dialog.setTargetFragment(this, 1000);

        dialog.show(getFragmentManager(), "GetAdoptionRequestDialog");
    }


    /**
     * Sends the animal from the current user to the one requesting the adoption.
     * @param animal object that will be moved between users. Currently has the owner userUid, and the
     *               new userUid inside it's AdoptionRequest.
     */
    @Override
    public void resolveAdoptionRequest(Animal animal) {
        animalsDatabase.transferAnimalToRequestingUser(animal);
    }

    /**
     * Removes the adoption request from the animal and doesn't move it from the current user.
     * @param animal object that will have it's AdoptionRequest removed ( made null ).
     */
    @Override
    public void denyAdoptionRequest(Animal animal) {
        animalsDatabase.removeAdoptionRequestFromAnimal(animal);
    }
}
