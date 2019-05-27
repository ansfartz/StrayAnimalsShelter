package com.bcs.andy.strayanimalsshelter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.adapter.AnimalMarkerAdapter;
import com.bcs.andy.strayanimalsshelter.database.AnimalsDatabase;
import com.bcs.andy.strayanimalsshelter.database.MarkersDatabase;
import com.bcs.andy.strayanimalsshelter.database.MarkersDatabaseListener;
import com.bcs.andy.strayanimalsshelter.dialog.GetRemovalRequestDialog;
import com.bcs.andy.strayanimalsshelter.dialog.GetRemovalRequestDialogListener;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;
import com.bcs.andy.strayanimalsshelter.model.RemovalRequest;
import com.firebase.client.annotations.NotNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MyMarkersFragment extends Fragment implements AnimalMarkerAdapter.AnimalMarkerAdapterListener, 
        GetRemovalRequestDialogListener {


    private static final String TAG = "MyMarkersFragment";

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private MarkersDatabase markersDatabase;

    // UI
    private ProgressBar markersLoadingProgressBar;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter markerAdapter;
    private List<AnimalMarker> animalMarkerList = new ArrayList<>();

    private ConstraintLayout CL;

    public MyMarkersFragment() { }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    private void initUI() {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Markers");

        markersLoadingProgressBar = (ProgressBar) CL.findViewById(R.id.loadingMarkersProgressBar);
        markersLoadingProgressBar.setVisibility(View.VISIBLE);


        recyclerView = (RecyclerView) CL.findViewById(R.id.myMarkersRecyclerView);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        markersDatabase = new MarkersDatabase();
        markersDatabase.readCurrentUserMarkers(new MarkersDatabaseListener() {
            @Override
            public void onCurrentUserMarkersCallBack(List<AnimalMarker> list) {
                animalMarkerList.clear();
                animalMarkerList.addAll(list);
                markersLoadingProgressBar.setVisibility(View.GONE);
                Log.d(TAG, "onCurrentUserMarkersCallBack: MY MARKERS LIST: " + animalMarkerList.toString());

                markerAdapter = new AnimalMarkerAdapter(animalMarkerList, getActivity(), MyMarkersFragment.this);
                recyclerView.setAdapter(markerAdapter);

            }

            @Override
            public void onAllMarkersCallBack(List<AnimalMarker> list) {

            }
        });

        Log.d(TAG, "AFTER: " + animalMarkerList.toString());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        CL = (ConstraintLayout) inflater.inflate(R.layout.fragment_my_markers, container, false);
        initFirebase();
        initUI();


        return CL;
    }


    /**
     * Triggers when clicking the left side of an AnimalMarker in the RecyclerView list
     */
    @Override
    public void onAnimalMarkerClick(int position, AnimalMarker animalMarker) {
        Intent selectedAnimalIntent = new Intent(getContext(), SelectedAnimalFromMarkerActivity.class);
        selectedAnimalIntent.putExtra("selectedAnimal", animalMarker.getAnimal());
        startActivity(selectedAnimalIntent);
    }

    /**
     * Triggers when clicking the Warning sign of an AnimalMarker in the RecyclerView list.
     */
    @Override
    public void onWarningClick(int position, AnimalMarker animalMarker) {
        GetRemovalRequestDialog dialog = new GetRemovalRequestDialog();
        Bundle args = new Bundle();
        args.putParcelable("removalRequest", animalMarker.getRemovalRequest());
        args.putParcelable("animalMarker", animalMarker);
        dialog.setArguments(args);
        dialog.setTargetFragment(this, 1000);

        dialog.show(getFragmentManager(), "GetRemovalRequestDialog");
    }

    /**
     * Called from within the {@link GetRemovalRequestDialog}.
     * Adds an animal to the requesting user's animalList and remove the marker from the database.
     */
    @Override
    public void resolveRemovalRequest(AnimalMarker animalMarker, RemovalRequest removalRequest) {

        Animal animal = animalMarker.getAnimal();
        animal.setUserUid(removalRequest.getSenderUserId());

        markersDatabase.removeMarker(animalMarker);

        AnimalsDatabase animalsDatabase = new AnimalsDatabase();
        animalsDatabase.addAnimalToUser(animal, removalRequest.getSenderUserId());

        Log.d(TAG, "resolveRemovalRequest: resolving RR");
    }

    /**
     * Called from within the {@link GetRemovalRequestDialog}.
     * Removes RemovalRequest from the marker.
     */
    @Override
    public void denyRemovalRequest(AnimalMarker animalMarker) {
        Log.d(TAG, "denyRemovalRequest: removing RR");
        markersDatabase.removeRemovalRequestFromMarker(animalMarker);

    }
}
