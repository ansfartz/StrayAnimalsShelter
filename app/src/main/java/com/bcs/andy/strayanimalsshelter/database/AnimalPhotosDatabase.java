package com.bcs.andy.strayanimalsshelter.database;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

public class AnimalPhotosDatabase {

    private static final String TAG = "AnimalPhotosDatabase";

    private StorageReference storageRef;
    private StorageReference animalsPhotosStorageRef;

    public AnimalPhotosDatabase() {
        storageRef = FirebaseStorage.getInstance().getReference();
        animalsPhotosStorageRef = storageRef.child("animals");
    }

    // receive an animal with an animalID already created at this point
    public void addPhotoToAnimalGetURL(Animal animal, String pathToImageFile, AnimalPhotosDatabaseListener animalPhotosDatabaseListener) {

        Uri file = Uri.fromFile(new File(pathToImageFile));
        StorageReference currentAnimalRef = animalsPhotosStorageRef.child(animal.getAnimalID()).child(UUID.randomUUID().toString());

        UploadTask uploadTask = currentAnimalRef.putFile(file);
        Task<Uri> urlTask = uploadTask
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return currentAnimalRef.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d(TAG, "onComplete: URI: " + downloadUri.toString());

                            animalPhotosDatabaseListener.onPhotoUploadComplete(downloadUri.toString());

                        }
                    }
                });

    }

}
