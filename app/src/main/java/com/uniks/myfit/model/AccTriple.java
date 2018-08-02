package com.uniks.myfit.model;

public class AccTriple {

    private float x;
    private float y;
    private float z;

    public AccTriple() {
        x = Float.MAX_VALUE;
        y = Float.MAX_VALUE;
        z = Float.MAX_VALUE;
    }

    public AccTriple(float x, float y, float z) {
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
}
