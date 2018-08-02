package com.uniks.myfit.controller;


import com.uniks.myfit.TrackingViewActivity;
import com.uniks.myfit.model.ProximitySensorService;

public class PushupCtrl {

    ProximitySensorService Proximity;
    TrackingViewActivity trackingViewActivity;

    public PushupCtrl(TrackingViewActivity trackingViewActivity) {
        this.trackingViewActivity=trackingViewActivity;
        Proximity = new ProximitySensorService(trackingViewActivity);
    }

    public void proximityInit()
    {
        Proximity.initialize();
    }

    public  void calculatepushUps()
    {

    }

    public void pstop()
    {
        Proximity .stopListening();
    }
}
