package ru.tux.moneytracker.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import ru.tux.moneytracker.R;

/**
 * Created by tux on 24/03/18.
 */

public class ConfirmationDialog extends DialogFragment {

    ConfirmationDialogListener listener;

    public void setListener(ConfirmationDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // String title = getContext().getResources().getString(R.string.app_name);
        Context context = getContext(); // ctx

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.dialog_title))
                .setMessage(context.getString(R.string.dialog_body))
                .setPositiveButton(context.getString(R.string.dialog_yes_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositiveClicked(ConfirmationDialog.this);
                    }
                })
                .setNegativeButton(context.getString(R.string.dialog_no_text), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNegativeClicked(ConfirmationDialog.this);
                    }
                })
                .create();

        return dialog;
    }
}
