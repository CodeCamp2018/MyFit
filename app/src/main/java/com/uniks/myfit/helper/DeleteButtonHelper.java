package com.uniks.myfit.helper;

import android.app.DialogFragment;
import android.view.View;

import com.uniks.myfit.DeleteExerciseDialogFragment;
import com.uniks.myfit.MainActivity;

public class DeleteButtonHelper implements View.OnClickListener {
    MainActivity mainActivity;

    public DeleteButtonHelper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        DeleteExerciseDialogFragment deleteDialog = new DeleteExerciseDialogFragment();
        deleteDialog.show(mainActivity.getSupportFragmentManager(), "deletion");
    }
}

