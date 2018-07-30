package com.uniks.myfit.model;

import android.arch.persistence.room.ColumnInfo;
import android.location.Location;

import java.util.Date;
import java.util.List;

public class Exercise {

    public String mode; // Exercising Mode e.g. hiking, cycling, ...

    public double distance; // the distance of the track

    public double speed; // the measured speed of the track

    @ColumnInfo(name = "trip_time")
    public double tripTime; // the time spend on the track

    public Date date; // the day & time of the track

    @ColumnInfo(name = "amount_of_repeats")
    public double amountOfRepeats; // for steps, push-ups and sit-ups

    public List<Location> coordinates; // list of coordinates to draw track on a map
}
