package com.uniks.myfit;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.uniks.myfit.database.SportExercise;

import java.util.List;


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

        super.onCreateDialog(savedInstanceState);
        this.position = getArguments().getInt("index");

        builder.setMessage(R.string.dialog_delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        MainActivity mainActivity = (MainActivity) getActivity();

                        //List<SportExercise> sportExercise = mainActivity.db.sportExerciseDao().loadAllByIds(new long[]{position});
                        //Log.e("Database", "POSITION: " + position + "; ID: " + sportExercise.get(0).getId());

                        SportExercise exercise = mainActivity.db.sportExerciseDao().getAllFromUser(mainActivity.user.getUid()).get(position);

                        mainActivity.db.sportExerciseDao().deleteExercise(exercise.getId());

                        mainActivity.finish();
                        startActivity(mainActivity.getIntent());
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
