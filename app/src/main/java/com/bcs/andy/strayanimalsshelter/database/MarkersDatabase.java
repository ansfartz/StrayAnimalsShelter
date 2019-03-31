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

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference allMarkersRef;

    private String userUid;
    private List<AnimalMarker> myAnimalMarkerList;


    public MarkersDatabase() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.allMarkersRef = firebaseDatabase.getReference("markers");
        this.myAnimalMarkerList = new ArrayList<>();
    }

    /**
     * Triggers when an update on a currently existing AnimalMarker object occurs or when a AnimalMarker is added or removed.
     * <p>
     *     Note: it will trigger even when a AnimalMarker that does not belong to the currently logged in user is added.
     * </p>
     * @param markersDatabaseListener interface with methods for logic/code that should happen after the async method {@link ValueEventListener#onDataChange(DataSnapshot)} completes
     */
    public void readCurrentUserMarkers(final MarkersDatabaseListener markersDatabaseListener) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myAnimalMarkerList.clear();
                Log.d(TAG, "onDataChange: HAVE CLEARED USER MARKERS LIST");

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    AnimalMarker animalMarker;
                    animalMarker = ds.getValue(AnimalMarker.class);
                    if (animalMarker.getUserUid().equals(userUid)) {
                        myAnimalMarkerList.add(animalMarker);
                    }
                }

                markersDatabaseListener.onCallBack(myAnimalMarkerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };

        allMarkersRef.addValueEventListener(valueEventListener);

    }

    /**
     * Add a AnimalMarker object to Firebase Database.
     * Will also trigger the valueEventListener inside {@link #readCurrentUserMarkers(MarkersDatabaseListener)}
     * @param animalMarker
     */
    public void addMarker(AnimalMarker animalMarker) {
        String markerUuid = UUID.randomUUID().toString().replace("-", "");
        allMarkersRef.child(markerUuid).setValue(animalMarker);
    }





}
