package com.bcs.andy.strayanimalsshelter.database;

import android.support.annotation.NonNull;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.model.AdoptionRequest;
import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.model.User;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AnimalsDatabase {

    private static final String TAG = "AnimalsDatabase";

    private DatabaseReference usersRef;
    private DatabaseReference currentUserAnimalsRef;
    private List<Animal> animalList;


    public AnimalsDatabase() {

        this.usersRef = UserUtils.getUsersReference();
//        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        this.currentUserAnimalsRef = usersRef.child(userUid).child("animals");
        this.currentUserAnimalsRef = usersRef.child(UserUtils.getCurrentUserId()).child("animals");
        animalList = new ArrayList<>();
    }


    public void readCurrentUserAnimals(final AnimalsDatabaseListener animalsDatabaseListener) {

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animalList.clear();
                Log.d(TAG, "onDataChange: HAVE CLEARED USER ANIMALS LIST");
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Animal animal;
                    animal = ds.getValue(Animal.class);
                    animalList.add(animal);
                }

                animalsDatabaseListener.onCallback(animalList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };

        currentUserAnimalsRef.addValueEventListener(valueEventListener);
    }

    public void addAnimalToUser(Animal animal, String userID) {
//        DatabaseReference userTarget = firebaseDatabase.getReference("users").child(userID).child("animals");
        DatabaseReference userTarget = usersRef.child(userID)
                .child("animals");

        userTarget.child(animal.getAnimalID()).setValue(animal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "addAnimalToUser - onSuccess: added animal " + animal.getAnimalID() + " to user " + userID);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "addAnimalToUser - onFailure: couldn't add animal " + animal.getAnimalID() + " to user " + userID +
                                "\nException: " + e.getMessage());
                    }
                });
    }


    /**
     * Actually reads all users, and then the animals inside each.
     */
    public void readAllAnimals(final AnimalsDatabaseListener animalsDatabaseListener) {

        List<Animal> animals = new ArrayList<>();
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                animals.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User someUser = ds.getValue(User.class);
                    if (!someUser.getUuid().equals(UserUtils.getCurrentUserId())) {
                        // found a user that is not currently logged in user in database

                        if (someUser.getAnimals() != null) {
                            someUser.getAnimals().values()
                                    .stream()
                                    .filter(Animal::isAdoptable)
                                    .forEach(animals::add);
                        }
                    }
                }

                animalsDatabaseListener.onCallback(animals);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        };

        usersRef.addValueEventListener(valueEventListener);

    }


    public void addAdoptionRequestToAnimal(Animal animal, String userUid, AdoptionRequest adoptionRequest, final AnimalsDatabaseListener animalsDatabaseListener) {

        DatabaseReference targetAnimalRef = usersRef.child(userUid).child("animals")
                .child(animal.getAnimalID());

        targetAnimalRef.child("adoptionRequest").setValue(adoptionRequest)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: added adoptionRequest " + adoptionRequest + " to Animal " + animal.getAnimalID() + " in user " + userUid);
                        animalsDatabaseListener.onCallback(null);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: couldn't add Adoption Request.\nException: " + e.getMessage());
                    }
                });

    }


    /**
     * Changes the owner userUid inside the Animal to the new userUid inside the AdoptionRequest,
     * while removing the AdoptionRequest
     *
     * @param animal that will be moved from one user to the other
     */
    public void transferAnimalToRequestingUser(Animal animal) {

        AdoptionRequest adoptionRequest = animal.getAdoptionRequest();

        DatabaseReference ownerUserAnimalRef = UserUtils.getUsersReference()
                .child(animal.getUserUid())
                .child("animals")
                .child(animal.getAnimalID());

        ownerUserAnimalRef.child("adoptionRequest").removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Removed adoptionRequest from animal " + animal.getAnimalID());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Could not remove adoptionRequest from animal " + animal.getAnimalID());
                    }
                });

        ownerUserAnimalRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Removed animal " + animal.getAnimalID() + " from user " + animal.getUserUid());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Could not remove animal " + animal.getAnimalID() + " from user " + animal.getUserUid());
                    }
                });


        animal.setAdoptionRequest(null);
        animal.setUserUid(adoptionRequest.getSenderUserId());


        DatabaseReference newOwnerUser = UserUtils.getUsersReference()
                .child(animal.getUserUid())
                .child("animals");

        newOwnerUser.child(animal.getAnimalID()).setValue(animal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: added animal " + animal.getAnimalID() + " to user " + adoptionRequest.getSenderUserId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: could not add animal " + animal.getAnimalID() + " to user " + adoptionRequest.getSenderUserId());
                    }
                });


    }

    /**
     * Removes the AdoptionRequest from the Animal
     *
     * @param animal that will have it's AdoptionRequest set to null
     */
    public void removeAdoptionRequestFromAnimal(Animal animal) {
        DatabaseReference targetAnimal = UserUtils.getUsersReference()
                .child(animal.getUserUid())
                .child("animals")
                .child(animal.getAnimalID());

        targetAnimal.child("adoptionRequest").removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Removed adoptionRequest from animal " + animal.getAnimalID());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: Could not remove adoptionRequest from animal " + animal.getAnimalID());
                    }
                });

    }
}
