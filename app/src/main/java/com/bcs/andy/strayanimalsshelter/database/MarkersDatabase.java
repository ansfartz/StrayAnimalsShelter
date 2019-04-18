package com.bcs.andy.strayanimalsshelter.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;
import com.bcs.andy.strayanimalsshelter.model.RemovalRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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
     * Add an AnimalMarker object to the Firebase Realtime Database.
     * Will trigger the valueEventListeners that are listening to the "markers" reference
     *
     * @param animalMarker the marker that will be added to the database
     */
    public void addMarker(AnimalMarker animalMarker) {
        markersRef.child(animalMarker.getMarkerID()).setValue(animalMarker);
    }


    /**
     * Remove the {@link AnimalMarker} object from the database, without removing the animal photo from the Firebase Storage
     * @param animalMarker used to identify the marker that will be removed from the database
     */
    // TODO: Remember the original user who created this marker (the userUid field in the animalMarkers) and add a "from: / source:" field in the animals
    public void removeMarker(AnimalMarker animalMarker) {
        markersRef.child(animalMarker.getMarkerID()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "removeMarker - onSuccess: animalMarker: " + animalMarker.getMarkerID() + " has been removed from the database");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "removeMarker - onFailure: animalMarker: "  + animalMarker.getMarkerID() + "could not be removed. Exception: " + e.getMessage());
                    }
                });
    }

    /**
     * Destroy the {@link AnimalMarker} object from the database, along with the photo of the {@linl Animal} nested in it.
     * Will trigger the valueEventListeners that are listening to the "markers" reference
     *
     * @param animalMarker used to identify the marker that will be destroyed from the database
     */
    public void destroyMarker(AnimalMarker animalMarker) {
        Animal animal = animalMarker.getAnimal();
        if (animal.getPhotoLink() != null) {
            AnimalPhotosDatabase animalPhotosDatabase = new AnimalPhotosDatabase();
            animalPhotosDatabase.removePhotoFromStorage(animal);
        }

        markersRef.child(animalMarker.getMarkerID()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "destroyMarker - onSuccess: marker " + animalMarker.getMarkerID() + " successfully removed.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "destroyMarker - onFailure: marker " + animalMarker.getMarkerID() + " failed to be removed. Exception: " + e.getMessage());
                    }
                });


//        Query markerQuery = markersRef.child(animalMarker.getMarkerID());
//        markerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                dataSnapshot.getRef().removeValue();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: " + databaseError.toException());
//            }
//        });

    }

    /**
     * Add a {@link RemovalRequest} object to an {@link AnimalMarker}.
     * @param animalMarker the marker in which the removalRequest is being added
     * @param removalRequest the request that is added in the animalMarker
     */
    public void addRemovalRequestToMarker(AnimalMarker animalMarker, RemovalRequest removalRequest) {
        markersRef.child(animalMarker.getMarkerID()).child("removalRequest").setValue(removalRequest);
    }

    /**
     * Remove the {@link RemovalRequest} object from the {@link AnimalMarker}.
     * @param animalMarker used for identifying the markerID in the database.
     */
    public void removeRemovalRequestFromMarker(AnimalMarker animalMarker) {
        markersRef.child(animalMarker.getMarkerID()).child("removalRequest").removeValue();
    }


}
