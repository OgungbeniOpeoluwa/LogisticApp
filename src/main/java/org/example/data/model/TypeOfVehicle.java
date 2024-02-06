package org.example.data.model;

public enum TypeOfVehicle {

    BIKE(400,100),CAR(800,200),TRUCK(100,300);
    public int ratePerKm;
    public int ratePerWeight;

    TypeOfVehicle(int value, int weightRate) {
        ratePerKm = value;
        ratePerWeight = weightRate;
    }
}
