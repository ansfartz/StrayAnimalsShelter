package com.bcs.andy.strayanimalsshelter.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserMarkersDatabase {

    private static final String TAG = "UserMarkersDatabase";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userMarkersRef;
    private List<Marker> markerList;

    public UserMarkersDatabase() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.userMarkersRef = firebaseDatabase.getReference("users").child(userUid).child("markers");
        markerList = new ArrayList<>();
    }

    public void readCurrentUserMarkers(final UserMarkersDatabaseListener userMarkersDatabaseListener) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                markerList.clear();
                Log.d(TAG, "onDataChange: HAVE CLEARED USER MARKERS LIST");

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Marker marker;
                    marker = ds.getValue(Marker.class);
                    markerList.add(marker);
                }
                userMarkersDatabaseListener.onCallBack(markerList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };

        userMarkersRef.addValueEventListener(valueEventListener);


    }



}
