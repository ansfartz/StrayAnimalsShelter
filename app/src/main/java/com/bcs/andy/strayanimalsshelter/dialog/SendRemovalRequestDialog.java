package com.bcs.andy.strayanimalsshelter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SendRemovalRequestDialog extends AppCompatDialogFragment {

    private EditText mesageEditText;
    private TextView toUserTextView, toEmailTextView, fromUserTextView, fromEmailTextView;
    private SendRemovalRequestDialogListener listener;

    private void init(View view) {
        toUserTextView = (TextView) view.findViewById(R.id.dialogSR_ToUsernameTextView);
        toEmailTextView = (TextView) view.findViewById(R.id.dialogSR_ToEmailTextView);
        fromUserTextView = (TextView) view.findViewById(R.id.dialogSR_FromUsernameTextView);
        fromEmailTextView = (TextView) view.findViewById(R.id.dialogSR_FromEmailTextView);

        mesageEditText = (EditText) view.findViewById(R.id.dialogSR_messageEditText);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_send_removal_request_layout, null);
        init(view);


//        toUserTextView.setPaintFlags(toUserTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        fromUserTextView.setText(getArguments().getString("FromUsername"));
        fromEmailTextView.setText(getArguments().getString("FromEmail"));
        toUserTextView.setText(getArguments().getString("ToUsername"));
        toEmailTextView.setText(getArguments().getString("ToEmail"));




        builder.setView(view)
                .setTitle("Send Removal Request")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String message = mesageEditText.getText().toString();
                        listener.sendRemovalRequest(message);
                    }
                });



        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SendRemovalRequestDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SendRemovalRequestDialogListener");
        }

    }
}
