package ru.tux.moneytracker.dialog;


/**
 * Created by tux on 24/03/18.
 */

public interface ConfirmationDialogListener {
    void onPositiveClicked(ConfirmationDialog dialog);
    void onNegativeClicked(ConfirmationDialog dialog);

    // public void onNegativeClicked(DialogFragment dialog);
}
