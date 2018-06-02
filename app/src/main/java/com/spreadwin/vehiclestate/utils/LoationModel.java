package com.spreadwin.vehiclestate.utils;

import com.amap.api.maps2d.model.LatLng;

public class LoationModel {
     private LatLng latLng;
     private String Address;


    public LoationModel(LatLng latLng,String address) {
        super();
        this.latLng=latLng;
        this.Address=address;
    }


    public LatLng getLatLng() {
        return latLng;
    }

    public String getAddress() {
        return Address;
    }


}
