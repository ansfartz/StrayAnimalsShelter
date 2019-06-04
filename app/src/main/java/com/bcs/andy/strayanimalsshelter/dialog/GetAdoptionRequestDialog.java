package com.bcs.andy.strayanimalsshelter.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.model.AdoptionRequest;
import com.bcs.andy.strayanimalsshelter.model.Animal;

public class GetAdoptionRequestDialog extends DialogFragment {

    private static final String TAG = "GetAdoptionRequestDialog";

    // vars
    private GetAdoptionRequestDialogListener listener;
    private Animal animal;
    private AdoptionRequest adoptionRequest;

    // UI
    private View view;
    private TextView fromUsernameTV, fromEmailTV;
    private TextView denyTV, acceptTV, doNothingTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_get_adoption_request_layout, container, false);
        init();

        animal = getArguments().getParcelable("animal");
        adoptionRequest = animal.getAdoptionRequest();

        fromUsernameTV.setText(adoptionRequest.getSenderUsername());
        fromEmailTV.setText(adoptionRequest.getSenderUserEmail());

        denyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: denyTV");
                listener.denyAdoptionRequest(animal);
                getDialog().dismiss();
            }
        });

        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: acceptTV");
                listener.resolveAdoptionRequest(animal);
                getDialog().dismiss();
            }
        });

        doNothingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: doNothingTV");
                getDialog().dismiss();
            }
        });

        return view;

    }

    private void init() {
        fromUsernameTV = (TextView) view.findViewById(R.id.dialogGA_FromUsernameTextView);
        fromEmailTV = (TextView) view.findViewById(R.id.dialogGA_FromEmailTextView);
        denyTV = (TextView) view.findViewById(R.id.dialogGA_DenyTV);
        acceptTV = (TextView) view.findViewById(R.id.dialogGA_AcceptTV);
        doNothingTV = (TextView) view.findViewById(R.id.dialogGA_DoNothingTV);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (GetAdoptionRequestDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException" + e.getMessage());
        }
    }
}
