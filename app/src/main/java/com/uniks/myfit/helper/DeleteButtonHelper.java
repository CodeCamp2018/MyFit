package com.uniks.myfit.helper;

import android.os.Bundle;
import android.view.View;

import com.uniks.myfit.DeleteExerciseDialogFragment;
import com.uniks.myfit.MainActivity;

public class DeleteButtonHelper implements View.OnClickListener {
    MainActivity mainActivity;
    long exerciseId;

    public DeleteButtonHelper(MainActivity mainActivity,  long exerciseId) {
        this.mainActivity = mainActivity;
        this.exerciseId = exerciseId;
    }

    @Override
    public void onClick(View view) {
        DeleteExerciseDialogFragment deleteDialog = new DeleteExerciseDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("index", exerciseId);
        deleteDialog.setArguments(bundle);
        deleteDialog.show(mainActivity.getSupportFragmentManager(), "deletion");
    }
}

