package com.bcs.andy.strayanimalsshelter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;
import com.google.firebase.auth.FirebaseAuth;

public class SendRemovalRequestDialog extends AppCompatDialogFragment {

    private EditText mesageEditText;
    private TextView userTextView;
    private TextView phoneTextView;
    private SendRemovalRequestDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_send_removal_request_layout, null);

        userTextView = (TextView) view.findViewById(R.id.dialogSR_usernameTextView);
        phoneTextView = (TextView) view.findViewById(R.id.dialogSR_phonenumberTextView);
        mesageEditText = (EditText) view.findViewById(R.id.dialogSR_messageEditText);

        userTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
        userTextView.setPaintFlags(userTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        phoneTextView.setText("+4 0000000000");
        phoneTextView.setPaintFlags(phoneTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);




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
                        String username = userTextView.getText().toString();
                        String message = mesageEditText.getText().toString();

                        listener.applyMessage(username, message);

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
