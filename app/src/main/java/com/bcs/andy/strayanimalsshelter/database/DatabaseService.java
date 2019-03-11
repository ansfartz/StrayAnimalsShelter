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

public class DatabaseService {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference firebaseRootRef;
    private DatabaseReference animalsRef;
    List<Animal> animalsList;

    public DatabaseService() {

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.firebaseRootRef = firebaseDatabase.getReference();
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.animalsRef = firebaseDatabase.getReference("users").child(userUid).child("animals");

        animalsList = new ArrayList<>();
    }


    public interface FirebaseCallback {
        void onCallback(List<Animal> list);
    }

    public void readItemsFromDatabase(final FirebaseCallback firebaseCallback) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animalsList.clear();

                //dataSnapshot.getChildren() ---> iterate through entire dataSnapshot Object, to get the items names
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Animal animal;
                    animal = ds.getValue(Animal.class);
                    animalsList.add(animal);
                }

                firebaseCallback.onCallback(animalsList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("DATABASE_TAG", databaseError.getMessage());
            }
        };

        animalsRef.addValueEventListener(valueEventListener);

    }


    public void readData() {
        readItemsFromDatabase(new FirebaseCallback() {
            @Override
            public void onCallback(List<Animal> list) {
                Log.d("DATABASE_TAG", list.toString());
            }
        });
    }

}
