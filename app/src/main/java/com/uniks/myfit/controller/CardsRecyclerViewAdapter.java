package com.uniks.myfit.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.uniks.myfit.R;
import com.uniks.myfit.database.SportExercise;

import java.util.ArrayList;
import java.util.Date;

public class CardsRecyclerViewAdapter extends RecyclerView.Adapter<CardsRecyclerViewAdapter.DataObjectHolder> {

    private ArrayList<SportExercise> sportExercises;
    private static MyClickListener myClickListener;

    public CardsRecyclerViewAdapter(ArrayList<SportExercise> sportExercises) {
        this.sportExercises = sportExercises;
    }

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView exerciseIcon;
        TextView date;
        TextView time;
        TextView duration;

        public DataObjectHolder(View itemView) {
            super(itemView);
            exerciseIcon = itemView.findViewById(R.id.fragment_exercise_icon);
            date = itemView.findViewById(R.id.sport_exercise_fragment_date);
            time = itemView.findViewById(R.id.sport_exercise_fragment_time);
            duration = itemView.findViewById(R.id.sport_exercise_fragment_duration);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_sport_exercise, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {
//        holder.exerciseIcon.setImageIcon(); TODO: load icon here
        holder.date.setText(getDateString(sportExercises.get(position).getDate()));
        holder.time.setText(getTimeString(sportExercises.get(position).getDate()));
        holder.duration.setText(String.valueOf(sportExercises.get(position).getTripTime()));
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

        return "";

    }

    private String getTimeString(Date date) {

        return "";

    }
}
