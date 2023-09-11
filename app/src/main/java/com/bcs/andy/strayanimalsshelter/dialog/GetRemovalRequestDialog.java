package com.bcs.andy.strayanimalsshelter.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.bcs.andy.strayanimalsshelter.model.AnimalMarker;
import com.bcs.andy.strayanimalsshelter.model.RemovalRequest;

public class GetRemovalRequestDialog extends DialogFragment {
    private static final String TAG = "GetRemovalRequestDialog";

    // vars
    private AnimalMarker animalMarker;
    private RemovalRequest removalRequest;
    private GetRemovalRequestDialogListener listener;

    // UI
    private View view;
    private TextView fromUsernameTV, fromEmailTV, messageTV;
    private TextView denyTV, acceptTV, doNothingTV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.dialog_get_removal_request_layout, container, false);
        init();


        animalMarker = getArguments().getParcelable("animalMarker");
        removalRequest = getArguments().getParcelable("removalRequest");

        fromUsernameTV.setText(removalRequest.getSenderUsername());
        fromEmailTV.setText(removalRequest.getSenderUserEmail());
        messageTV.setText(removalRequest.getMessage());


        denyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: denyTV");
                listener.denyRemovalRequest(animalMarker);
                getDialog().dismiss();
            }
        });

        acceptTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: acceptTV");
                listener.resolveRemovalRequest(animalMarker, removalRequest);
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
        fromUsernameTV = (TextView) view.findViewById(R.id.dialogGR_FromUsernameTextView);
        fromEmailTV = (TextView) view.findViewById(R.id.dialogGR_FromEmailTextView);
        messageTV = (TextView) view.findViewById(R.id.dialogGR_MessageTextView);
        messageTV.setMovementMethod(new ScrollingMovementMethod());
        denyTV = (TextView) view.findViewById(R.id.dialogGR_DenyTV);
        acceptTV = (TextView) view.findViewById(R.id.dialogGR_AcceptTV);
        doNothingTV = (TextView) view.findViewById(R.id.dialogGR_DoNothingTV);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (GetRemovalRequestDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException" + e.getMessage());
        }
    }
}
