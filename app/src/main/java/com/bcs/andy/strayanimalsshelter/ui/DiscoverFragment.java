package com.bcs.andy.strayanimalsshelter.ui;

import android.content.Context;
import android.net.Uri;
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
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.adapter.AnimalAdapter;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabaseListener;
import com.bcs.andy.strayanimalsshelter.model.Animal;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment implements AnimalAdapter.AnimalAdapterListener {


    private static final String TAG = "DiscoverFragment";

    // Firebase
    private AnimalsDatabase animalsDatabase;

    // UI
    private ConstraintLayout CL;
    private ProgressBar animalsDiscoverProgressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter animalAdapter;

    // vars
    private List<Animal> discoverAnimalList = new ArrayList<>();


    public DiscoverFragment() {

    }

    private void initFirebase() {
    }

    private void initUI() {
        animalsDiscoverProgressBar = (ProgressBar) CL.findViewById(R.id.loadingDiscoverAnimalsProgressBar);

        recyclerView = (RecyclerView) CL.findViewById(R.id.discoverAnimalsRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        animalsDatabase = new AnimalsDatabase();
        animalsDatabase.readAllAnimals(new AnimalsDatabaseListener() {
            @Override
            public void onCallback(List<Animal> list) {
                discoverAnimalList.clear();
                discoverAnimalList.addAll(list);
                animalsDiscoverProgressBar.setVisibility(View.GONE);

                animalAdapter = new AnimalAdapter(discoverAnimalList, getActivity(), DiscoverFragment.this);
                recyclerView.setAdapter(animalAdapter);
            }
        });

        Log.d(TAG, "AFTER: " + discoverAnimalList.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CL = (ConstraintLayout) inflater.inflate(R.layout.fragment_discover, container, false);
        initFirebase();
        initUI();

        return CL;
    }


    @Override
    public void onAnimalClick(int position, Animal animal) {
        Log.d(TAG, "onAnimalClick: clicked an animal in discover");
        Toast.makeText(getActivity(), "clicked pos: " + position, Toast.LENGTH_SHORT).show();
    }
}
