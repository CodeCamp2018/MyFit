package com.uniks.myfit.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

/**
 * the apps database, which stores user-, exercise- and location data.
 */
@Database(entities = {SportExercise.class, User.class, LocationData.class}, version = 11)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract SportExerciseDao sportExerciseDao();

    public abstract LocationDataDao locationDataDao();

}