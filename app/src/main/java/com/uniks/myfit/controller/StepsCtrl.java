package com.uniks.myfit.controller;

public class StepsCtrl {

    private  int stepCounter;

    public StepsCtrl() {
        stepCounter = 0;
    }

    public  void addStep() {
        setStepCounter(stepCounter++);
    }

    public int getStepCounter() {
        return stepCounter;
    }

    public void setStepCounter(int stepCounter) {
        this.stepCounter = stepCounter;
    }
}
