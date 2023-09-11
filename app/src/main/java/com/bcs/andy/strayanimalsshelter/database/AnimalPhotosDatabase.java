package com.bcs.andy.strayanimalsshelter.database;

import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

import com.bcs.andy.strayanimalsshelter.model.Animal;
import com.bcs.andy.strayanimalsshelter.utils.UUIDGenerator;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

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
        StorageReference currentAnimalRef = animalsPhotosStorageRef.child(animal.getAnimalID()).child(UUIDGenerator.createUUID());

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

    /**
     * Deleted the image of an animal from the Firebase Storage.
     * <p>Should be used when deleting an animal entirely, and updating its photoLink is not necessary.</p>
     * @param animal object whose photoLink is used for identifying and deleting the image
     */
    public void removePhotoFromStorage(Animal animal) {
        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(animal.getPhotoLink());
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "onSuccess: deleted file.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: failed to delete file. exception: " + e.getMessage());
            }
        });
    }

}
