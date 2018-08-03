package com.uniks.myfit.controller;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniks.myfit.MainActivity;
import com.uniks.myfit.R;
import com.uniks.myfit.database.SportExercise;
import com.uniks.myfit.helper.DeleteButtonHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CardsRecyclerViewAdapter extends RecyclerView.Adapter<CardsRecyclerViewAdapter.DataObjectHolder> {

    private ArrayList<SportExercise> sportExercises;
    private static MyClickListener myClickListener;
    private MainActivity mainActivity;

    public CardsRecyclerViewAdapter(ArrayList<SportExercise> sportExercises, MainActivity mainActivity) {
        this.sportExercises = sportExercises;
        this.mainActivity = mainActivity;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView exerciseIcon;
        TextView date;
        TextView time;
        TextView duration;
        ImageButton deleteButton;

        public DataObjectHolder(View itemView) {
            super(itemView);

            exerciseIcon = itemView.findViewById(R.id.fragment_exercise_icon);
            date = itemView.findViewById(R.id.sport_exercise_fragment_date);
            time = itemView.findViewById(R.id.sport_exercise_fragment_time);
            duration = itemView.findViewById(R.id.sport_exercise_fragment_duration);
            deleteButton = itemView.findViewById(R.id.delete_button);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_sport_exercise, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
        switch (sportExercises.get(position).getMode()) {
            case "running":
                holder.exerciseIcon.setImageDrawable(ResourcesCompat.getDrawable(mainActivity.getResources(), R.drawable.ic_run_black, null));
                break;
            case "cycling":
                holder.exerciseIcon.setImageDrawable(ResourcesCompat.getDrawable(mainActivity.getResources(), R.drawable.ic_bike_black, null));
                break;
            case "pushups":
                holder.exerciseIcon.setImageDrawable(ResourcesCompat.getDrawable(mainActivity.getResources(), R.drawable.ic_pushups_black, null));
                break;
            case "situps":
                holder.exerciseIcon.setImageDrawable(ResourcesCompat.getDrawable(mainActivity.getResources(), R.drawable.ic_situps_black, null));
                break;
        }
        holder.date.setText(getDateString(sportExercises.get(position).getDate()));
        holder.time.setText(getTimeString(sportExercises.get(position).getDate()));
        holder.duration.setText(String.valueOf(sportExercises.get(position).getTripTime()));
        holder.deleteButton.setOnClickListener(new DeleteButtonHelper());
    }

    public void addItem(SportExercise sportExercise, int index) {
        sportExercises.add(index, sportExercise);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        sportExercises.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return sportExercises.size();
    }

    public interface MyClickListener {
        void onItemClick(int position, View v);
    }

    private String getDateString(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);

        return df.format(date);

    }

    private String getTimeString(Date date) {

        SimpleDateFormat df = new SimpleDateFormat("HH:mm", Locale.GERMANY);

        return df.format(date);

    }
}
