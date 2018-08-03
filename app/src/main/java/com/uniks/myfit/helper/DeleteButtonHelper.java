package com.uniks.myfit.helper;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.uniks.myfit.DeleteExerciseDialogFragment;
import com.uniks.myfit.MainActivity;
import com.uniks.myfit.database.SportExercise;

public class DeleteButtonHelper implements View.OnClickListener {
    MainActivity mainActivity;
    long position;

    public DeleteButtonHelper(MainActivity mainActivity,  long position) {
        this.mainActivity = mainActivity;
        this.position = position;
    }

    @Override
    public void onClick(View view) {
        DeleteExerciseDialogFragment deleteDialog = new DeleteExerciseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("index", position);
        deleteDialog.setArguments(bundle);
        deleteDialog.show(mainActivity.getSupportFragmentManager(), "deletion");
    }
}

