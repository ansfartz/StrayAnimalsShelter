package com.bcs.andy.strayanimalsshelter.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.dialog.SendRemovalRequestDialog;
import com.bcs.andy.strayanimalsshelter.dialog.SendRemovalRequestDialogListener;
import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;
import com.bcs.andy.strayanimalsshelter.model.User;
import com.bcs.andy.strayanimalsshelter.utils.UserUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SelectedAnimalFromMapActivity extends AppCompatActivity implements SendRemovalRequestDialogListener {

    private static final String TAG = "SelectedAnimalFromMapAc";

    // UI
    private TextView animalNameTV, animalAgeTV, animalObsTV, createdByTV;
    private CheckBox animalAdultCB, animalNeuteredCB;
    private ImageView animalIconIV, animalImageView;
    private Button askToRemoveButton;

    // Firebase
    private DatabaseReference usersRef;

    // vars
    private String creatorUser = null;
    private String creatorEmail = null;
    private boolean updatedCreator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_animal_from_map);

        init();
        setUI();
    }

    private void init() {
        usersRef = UserUtils.getUsersReference();
        createdByTV = (TextView) findViewById(R.id.map_createdBy);
        animalNameTV = (TextView) findViewById(R.id.map_selectedAnimalNameTV);
        animalAgeTV = (TextView) findViewById(R.id.map_selectedAnimalAgeTV);
        animalObsTV = (TextView) findViewById(R.id.map_selectedAnimalObsTV);
        animalObsTV.setMovementMethod(new ScrollingMovementMethod());
        animalIconIV = (ImageView) findViewById(R.id.map_selectedAnimalIconIV);
        animalImageView = (ImageView) findViewById(R.id.map_selectedAnimalImageView);
        animalAdultCB = (CheckBox) findViewById(R.id.map_selectedAnimalAdultCheckBox);
        animalNeuteredCB = (CheckBox) findViewById(R.id.map_selectedAnimalNeuteredCheckBox);
        askToRemoveButton = (Button) findViewById(R.id.map_selectedAnimalRemoveRequestButton);
        askToRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!updatedCreator) {
                    Log.d(TAG, "onClick: Async task updateCreator has not finished yet.");
                    Toast.makeText(SelectedAnimalFromMapActivity.this, "Please wait a moment...", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(UserUtils.getCurrentUserName().equals(creatorUser)) {
                    Log.d(TAG, "onClick: currentUser = creatorUser");
                    Toast.makeText(SelectedAnimalFromMapActivity.this, "You can't send a removal request to yourself", Toast.LENGTH_LONG).show();
                    return;
                }

                SendRemovalRequestDialog dialog = new SendRemovalRequestDialog();
                Bundle args = new Bundle();
                args.putString("ToUsername", creatorUser);
                args.putString("ToEmail", creatorEmail);
                args.putString("FromUsername", UserUtils.getCurrentUserName());
                args.putString("FromEmail", UserUtils.getCurrentUserEmail());
                dialog.setArguments(args);

                dialog.show(getSupportFragmentManager(), "send removal request");
            }
        });

    }

    private void setUI() {
        AnimalMarker animalMarker = getIntent().getParcelableExtra("animalMarker");
        Log.d(TAG, "setUI: animalMarker: " + animalMarker);

        animalNameTV.setText(animalMarker.getAnimal().getAnimalName());
        animalAgeTV.setText(animalMarker.getAnimal().getAproxAge().toString());
        animalObsTV.setText(animalMarker.getAnimal().getObservations());
        animalAdultCB.setChecked(animalMarker.getAnimal().isAdult());
        animalNeuteredCB.setChecked(animalMarker.getAnimal().isNeutered());

        switch (animalMarker.getAnimal().getSpecies()) {
            case "Dog":
                animalIconIV.setImageResource(R.drawable.dog_icon);
                break;
            case "Cat":
                animalIconIV.setImageResource(R.drawable.cat_icon);
                break;
            default:
                animalIconIV.setImageResource(R.drawable.dog_icon);
                break;
        }

        if (animalMarker.getAnimal().getPhotoLink() != null) {
            Picasso.get().load(animalMarker.getAnimal().getPhotoLink()).fit().into(animalImageView);
        }

        Query query = usersRef.orderByChild("uuid").equalTo(animalMarker.getUserUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // because dataSnapshot is parent node (users), we go to node "users/userUID" ,
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);

                    creatorUser = user.getName();
                    creatorEmail = user.getEmail();
                    updatedCreator = true;
                    String createdBy = "Created by " + creatorUser + " (" + creatorEmail + ")";
                    createdByTV.setText(createdBy);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });


    }


    @Override
    public void applyMessage(String username, String message) {

    }
}
