package com.uniks.myfit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.uniks.myfit.database.SportExercise;


public class DeleteExerciseDialogFragment extends DialogFragment {
    public interface DeleteDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
    }

    private long exerciseId;
    DeleteDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        super.onCreateDialog(savedInstanceState);
        this.exerciseId = getArguments().getLong("index");

        builder.setMessage(R.string.dialog_delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity mainActivity = (MainActivity) getActivity();

                        //List<SportExercise> sportExercise = mainActivity.db.sportExerciseDao().loadAllByIds(new long[]{exerciseId});
                        //Log.e("Database", "POSITION: " + exerciseId + "; ID: " + sportExercise.get(0).getId());

                        SportExercise exercise = mainActivity.db.sportExerciseDao().getExerciseById(exerciseId).get(0);
                        //getAllFromUser(mainActivity.user.getUid()).get(exerciseId);

                        Log.e("Deleting Exercise", "Id: " + exercise.getId() + "\nMode: " + exercise.getMode() + "\nduration: " + exercise.getTripTime());

                        mainActivity.db.sportExerciseDao().deleteExercise(exercise); //exercise.getId()

                        // refresh mainActivity
                        mainActivity.cardsAdapter.setSportExercises(mainActivity.getDataSet());
                        mainActivity.cardsAdapter.notifyDataSetChanged();

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
