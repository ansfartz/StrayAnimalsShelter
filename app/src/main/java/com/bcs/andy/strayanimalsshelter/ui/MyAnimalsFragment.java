package com.bcs.andy.strayanimalsshelter.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabaseListener;
import com.bcs.andy.strayanimalsshelter.dialog.GetAdoptionRequestDialog;
import com.bcs.andy.strayanimalsshelter.dialog.GetAdoptionRequestDialogListener;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.adapter.AnimalAdapter;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;

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

    private SearchView searchView;
    private RadioGroup radioGroup;
    private RadioButton observationsRadioButton;

    // vars
    private boolean adapterCreated = false;
    private List<Animal> animalList = new ArrayList<>();


    public MyAnimalsFragment() { }

    private void initFirebase() {
        user = UserUtils.getCurrentUser();
    }

    private void initUI() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");

        animalsLoadingProgressBar = (ProgressBar) CL.findViewById(R.id.loadingAnimalsProgressBar);
        animalsLoadingProgressBar.setVisibility(View.VISIBLE);

        addAnimalsBtn = (FloatingActionButton) CL.findViewById(R.id.addAnimalsFloatingActionBtn);

        searchView = (SearchView) CL.findViewById(R.id.myAnimalsSearchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // we dont need this method, our search is in RealTime, we have no "submit"
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                animalAdapter.getFilter().filter(newText);
                return false;
            }
        });

        recyclerView = (RecyclerView) CL.findViewById(R.id.myAnimalsRecyclerView);
        recyclerView.setHasFixedSize(false); // no adapter content ( card with data ) has fixed size. They may vary, depending on the length of text inside them; use this for recyclerView internal optimisation! Won't affect anything else
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        radioGroup = (RadioGroup) CL.findViewById(R.id.myAnimalsRadioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.myAnimalsDescriptionRB && adapterCreated) {
//                    Toast.makeText(getContext(), "a", Toast.LENGTH_SHORT).show();
                    animalAdapter.makeObservationsVisible();
                    animalAdapter.notifyDataSetChanged();
                }

                if (checkedId == R.id.myAnimalsAgeRB && adapterCreated) {
//                    Toast.makeText(getContext(), "b", Toast.LENGTH_SHORT).show();
                    animalAdapter.makeAgeVisible();
                    animalAdapter.notifyDataSetChanged();
                }

                if (checkedId == R.id.myAnimalsDetailsRB && adapterCreated) {
//                    Toast.makeText(getContext(), "c", Toast.LENGTH_SHORT).show();
                    animalAdapter.makeDetailsVisible();
                    animalAdapter.notifyDataSetChanged();
                }

            }
        });

        observationsRadioButton = (RadioButton) CL.findViewById(R.id.myAnimalsDescriptionRB);
        observationsRadioButton.setChecked(true);



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
                adapterCreated = true;
                recyclerView.setAdapter(animalAdapter);

            }
        });

        // code UNDER readCurrentUserAnimals() will happen first
        // because method is asynchronous


        Log.d(TAG, "AFTER: " + animalList.toString());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // use CL inside initUI()
        CL = (ConstraintLayout) inflater.inflate(R.layout.fragment_my_animals, container, false);
        initFirebase();
        initUI();

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
