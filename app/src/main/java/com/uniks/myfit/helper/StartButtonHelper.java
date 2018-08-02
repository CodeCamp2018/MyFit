package com.uniks.myfit.helper;

import android.content.Intent;
import android.view.View;

import com.uniks.myfit.MainActivity;
import com.uniks.myfit.TrackingViewActivity;

public class StartButtonHelper implements View.OnClickListener {

    private int modeCode;
    private MainActivity mainActivity;

    public StartButtonHelper(int modeCode, MainActivity mainActivity) {
        this.modeCode = modeCode;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onClick(View view) {
        Intent startTracking = new Intent( view.getContext() , TrackingViewActivity.class);
        startTracking.putExtra("EXERCISE", modeCode);
        mainActivity.startActivity(startTracking);
    }
}
