package com.uniks.myfit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.uniks.myfit.database.SportExercise;


public class DeleteExerciseDialogFragment extends DialogFragment {
    public interface DeleteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    private int position;
    DeleteDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity mainActivity = (MainActivity) getActivity();
                        int uid = mainActivity.db.userDao().getAll().get(0).getUid();
                        SportExercise sportExercise = mainActivity.db.sportExerciseDao().getAllFromUser(uid).get(position);
                        mainActivity.db.sportExerciseDao().delete(sportExercise);
                    }
                })
                .setNegativeButton(R.string.keep, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        // Create the AlertDialog object and return it
        return builder.create();
    }


}
