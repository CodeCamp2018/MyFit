package com.uniks.myfit.model;

public class AccTripleVec {

    private float x;
    private float y;
    private float z;

    public AccTripleVec() {
        x = Float.MAX_VALUE;
        y = Float.MAX_VALUE;
        z = Float.MAX_VALUE;
    }

    public AccTripleVec(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getSquaredMagnitude() {
        return x*x + y*y + z*z;
    }
}
