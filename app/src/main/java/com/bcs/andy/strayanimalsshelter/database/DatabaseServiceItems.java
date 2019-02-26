package com.bcs.andy.strayanimalsshelter.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseServiceItems {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference itemsRef;
    List<String> itemList;

    public DatabaseServiceItems() {

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.firebaseRootRef = firebaseDatabase.getReference();
        this.itemsRef = firebaseDatabase.getReference("items");

        itemList = new ArrayList<>();
    }


    private interface FirebaseCallback {
        void onCallback(List<String> list);
    }

    public void readItemsFromDatabase(final FirebaseCallback firebaseCallback) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //dataSnapshot.getChildren() ---> iterate through entire dataSnapshot Object, to get the items names
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String itemName = ds.child("itemName").getValue(String.class);
                    itemList.add(itemName);
                }

                firebaseCallback.onCallback(itemList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE_TAG", databaseError.getMessage());
            }
        };

        itemsRef.addValueEventListener(valueEventListener);

    }


    public void readData() {

        readItemsFromDatabase(new FirebaseCallback() {
            @Override
            public void onCallback(List<String> list) {
                Log.d("DATABASE_TAG", list.toString());
            }
        });


    }








}
