package com.bcs.andy.strayanimalsshelter.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserAnimalsDatabase {

    private static final String TAG = "UserAnimalsDatabase";

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference userAnimalsRef;
    private List<Animal> animalList;


    // onDataChange will only happen on data changed in it's path :  users/userUid/animals
    // any other changes outside this URL will not trigger onDataChange method
    public UserAnimalsDatabase() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.userAnimalsRef = firebaseDatabase.getReference("users").child(userUid).child("animals");
        animalList = new ArrayList<>();
    }


    public void readCurrentUserAnimals(final UserAnimalsDatabaseListener userAnimalsDatabaseListener) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animalList.clear();
                Log.d(TAG, "onDataChange: HAVE CLEARED USER ANIMALS LIST");
                //dataSnapshot.getChildren() ---> iterate through entire dataSnapshot Object, to get the items names
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Animal animal;
                    animal = ds.getValue(Animal.class);
                    animalList.add(animal);
                }

                userAnimalsDatabaseListener.onCallback(animalList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }

        };

        userAnimalsRef.addValueEventListener(valueEventListener);

    }


}
