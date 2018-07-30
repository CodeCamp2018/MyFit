package com.uniks.myfit.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface SportExerciseDao {

    @Query("SELECT * FROM sportexercise")
    List<SportExercise> getAll();

    @Query("SELECT * FROM sportexercise WHERE id IN (:ids)")
    List<SportExercise> loadAllByIds(int[] ids);



    @Insert
    void insertAll(SportExercise... sportExercises);

    @Delete
    void delete(SportExercise sportExercise);
}


/*
* @Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    List<User> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND "
           + "last_name LIKE :last LIMIT 1")
    User findByName(String first, String last);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);
}

* */