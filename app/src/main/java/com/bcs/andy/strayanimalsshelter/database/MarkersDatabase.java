package com.bcs.andy.strayanimalsshelter.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarkersDatabase {

    private static final String TAG = "MarkersDatabase";

    // Firebase
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference markersRef;

    // vars
    private String userUid;
    private List<AnimalMarker> myAnimalMarkerList;
    private List<AnimalMarker> allAnimalMarkerList;


    public MarkersDatabase() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.markersRef = firebaseDatabase.getReference("markers");
        this.userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        this.myAnimalMarkerList = new ArrayList<>();
        this.allAnimalMarkerList = new ArrayList<>();
    }

    /**
     * Creates a list of the users markers.
     * Is called when an update on a currently existing AnimalMarker object occurs or when a AnimalMarker is added or removed from the "markers" reference.
     *
     * <p>
     * Note: will trigger even when a AnimalMarker that does not belong to the currently logged in user is added.
     * </p>
     *
     * @param markersDatabaseListener interface with methods for logic/code that should happen after the async method {@link ValueEventListener#onDataChange(DataSnapshot)} completes
     */
    public void readCurrentUserMarkers(final MarkersDatabaseListener markersDatabaseListener) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAnimalMarkerList.clear();
                Log.d(TAG, "readCurrentUserMarkers - onDataChange: HAVE CLEARED USER MARKERS LIST");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AnimalMarker animalMarker;
                    animalMarker = ds.getValue(AnimalMarker.class);
                    if (animalMarker.getUserUid().equals(userUid)) {
                        myAnimalMarkerList.add(animalMarker);
                    }
                }

                markersDatabaseListener.onCurrentUserMarkersCallBack(myAnimalMarkerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "readCurrentUserMarkers - " + databaseError.getMessage());
            }
        };

        markersRef.addValueEventListener(valueEventListener);

    }

    /**
     * Creates a list of all markers.
     * Is called when an update on a currently existing AnimalMarker object occurs or when a AnimalMarker is added or removed from the "markers" reference.
     *
     * @param markersDatabaseListener interface with methods for logic/code that should happen after the async method {@link ValueEventListener#onDataChange(DataSnapshot)} completes
     */
    public void readAllMarkers(final MarkersDatabaseListener markersDatabaseListener) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allAnimalMarkerList.clear();
                Log.d(TAG, "readAllMarkers - onDataChange: HAVE CLEARED ALL MARKERS LIST");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AnimalMarker animalMarker;
                    animalMarker = ds.getValue(AnimalMarker.class);
                    Log.d(TAG, "readAllMarkers - onDataChange: checking: " + animalMarker.getUserUid() + " at location: " + animalMarker.getLocation());
                    if (!animalMarker.getUserUid().equals(userUid)) {
                        Log.d(TAG, "readAllMarkers - onDataChange: this one passed");
                        allAnimalMarkerList.add(animalMarker);
                    }
                }

                markersDatabaseListener.onAllMarkersCallBack(allAnimalMarkerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "readAllMarkers - " + databaseError.getMessage());
            }
        };

        markersRef.addValueEventListener(valueEventListener);

    }

    /**
     * Add a AnimalMarker object to Firebase Database.
     * Will also trigger the valueEventListener inside {@link #readCurrentUserMarkers(MarkersDatabaseListener)}
     *
     * @param animalMarker the marker that will be added to the database
     */
    public void addMarker(AnimalMarker animalMarker) {
        markersRef.child(animalMarker.getMarkerID()).setValue(animalMarker);
    }


}
