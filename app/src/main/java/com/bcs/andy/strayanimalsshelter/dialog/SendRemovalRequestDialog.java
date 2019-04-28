package com.bcs.andy.strayanimalsshelter.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bcs.andy.strayanimalsshelter.R;

public class SendRemovalRequestDialog extends AppCompatDialogFragment {

    private EditText messageET;
    private TextView toUsernameTV, toEmailTV, fromUsernameTV, fromEmailTV;
    private TextView sendTV, cancelTV;
    private SendRemovalRequestDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), android.R.style.Theme_Holo_Dialog_NoActionBar);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_send_removal_request_layout, null);
        init(view);


//        toUsernameTV.setPaintFlags(toUsernameTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        fromUsernameTV.setText(getArguments().getString("FromUsername"));
        fromEmailTV.setText(getArguments().getString("FromEmail"));
        toUsernameTV.setText(getArguments().getString("ToUsername"));
        toEmailTV.setText(getArguments().getString("ToEmail"));
        sendTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageET.getText().toString();
                listener.sendRemovalRequest(message);
                dismiss();
            }
        });
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setView(view).setTitle("Send Removal Request");
        return builder.create();
    }

    private void init(View view) {
        toUsernameTV = (TextView) view.findViewById(R.id.dialogSR_ToUsernameTextView);
        toEmailTV = (TextView) view.findViewById(R.id.dialogSR_ToEmailTextView);
        fromUsernameTV = (TextView) view.findViewById(R.id.dialogSR_FromUsernameTextView);
        fromEmailTV = (TextView) view.findViewById(R.id.dialogSR_FromEmailTextView);
        messageET = (EditText) view.findViewById(R.id.dialogSR_messageEditText);
        messageET.setMovementMethod(new ScrollingMovementMethod());
        sendTV = (TextView) view.findViewById(R.id.SendTV);
        cancelTV = (TextView) view.findViewById(R.id.CancelTV);
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
