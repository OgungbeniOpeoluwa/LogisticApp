package org.example.data.model;

public enum TypeOfVehicle {

    BIKE(400,100),CAR(800,200),TRUCK(1200,400),BUS(1000,300),
    SHIP(2000,600);
    public int ratePerKm;
    public int ratePerWeight;

    TypeOfVehicle(int value, int weightRate) {
        ratePerKm = value;
        ratePerWeight = weightRate;
    }
}
