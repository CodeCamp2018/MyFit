package com.uniks.myfit.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = @ForeignKey(entity = SportExercise.class, parentColumns = "id", childColumns = "user_id", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE))
public class SportExercise {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    private String mode; // Exercising Mode e.g. hiking, cycling, ...

    private double distance; // the distance of the track

    private double speed; // the measured speed of the track

    @ColumnInfo(name = "trip_time")
    private double tripTime; // the time spend on the track

    private Date date; // the day & time of the track

    @ColumnInfo(name = "amount_of_repeats")
    private double amountOfRepeats; // for steps, push-ups and sit-ups

//    private List<Location> coordinates; // list of coordinates to draw track on a map


    // TODO: the burnt calories??

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getTripTime() {
        return tripTime;
    }

    public void setTripTime(double tripTime) {
        this.tripTime = tripTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmountOfRepeats() {
        return amountOfRepeats;
    }

    public void setAmountOfRepeats(double amountOfRepeats) {
        this.amountOfRepeats = amountOfRepeats;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

//    public List<Location> getCoordinates() {
//        return coordinates;
//    }
//
//    public void setCoordinates(List<Location> coordinates) {
//        this.coordinates = coordinates;
//    }
}