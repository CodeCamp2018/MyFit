package com.uniks.myfit.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.uniks.myfit.model.Exercise;

import java.util.List;

@Dao
public interface SportExerciseDao {

    @Query("SELECT * FROM sportexercise")
    List<SportExercise> getAll();

    @Query("SELECT * FROM sportexercise WHERE id IN (:ids)")
    List<SportExercise> loadAllByIds(int[] ids);

    @Query("SELECT mode, distance, speed, trip_time, date, amount_of_repeats, coordinates FROM sportexercise")
    List<Exercise> loadSportExercise(); // TODO?: maybe use LiveData<List<Exercise>>

    @Update
    void updateSportExercise(SportExercise sportExercise);

    @Update
    void updateSportExercises(SportExercise... sportExercises);

    @Insert
    void insertAll(SportExercise... sportExercises);

    @Delete
    void delete(SportExercise sportExercise);


}