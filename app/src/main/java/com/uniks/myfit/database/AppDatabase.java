package com.uniks.myfit.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {SportExercise.class, User.class, LocationData.class}, version = 7)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

    public abstract SportExerciseDao sportExerciseDao();

    public abstract LocationDataDao locationDataDao();

}