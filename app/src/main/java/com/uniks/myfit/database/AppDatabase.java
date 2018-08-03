package com.uniks.myfit.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

@Database(entities = {SportExercise.class, User.class}, version = 4)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    static final String databaseName = "myFitDB";

    private static AppDatabase instance;

    public abstract UserDao userDao();

    public abstract SportExerciseDao sportExerciseDao();

    public static AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, databaseName).build();
        }

        return instance;
    }

}