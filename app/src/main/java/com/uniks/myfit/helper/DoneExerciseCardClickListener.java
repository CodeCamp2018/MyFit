package com.uniks.myfit.helper;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.uniks.myfit.DetailActivity;
import com.uniks.myfit.MainActivity;
import com.uniks.myfit.controller.CardsRecyclerViewAdapter;

public class DoneExerciseCardClickListener implements CardsRecyclerViewAdapter.MyClickListener {

    private MainActivity mainActivity;

    public DoneExerciseCardClickListener(MainActivity mainActivity) {

        this.mainActivity = mainActivity;
    }

    @Override
    public void onItemClick(int position, View v) {

        Log.i("ClickEvent on Card:", " Clicked on Item " + position);

        // change to DetailActivity and send index (== position) of Exercise also

        Intent showDetails = new Intent(v.getContext(), DetailActivity.class);
        showDetails.putExtra("POSITION", position);
        mainActivity.startActivity(showDetails);

    }
}
