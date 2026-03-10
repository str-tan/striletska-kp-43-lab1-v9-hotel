package com.hotel.model;

import java.util.Objects;

public class Room {
    private int number;
    private double pricePerNight;
    private boolean isAvailable;

    public Room() {}//для json

    public Room(int number, double pricePerNight) {
        this.number = number;
        this.pricePerNight = pricePerNight;
        this.isAvailable = true;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    public int getNumber() { return number; }
    public double getPricePerNight() { return pricePerNight; }
    public boolean isAvailable() { return isAvailable; }

    public double calculateTotal(int nights) {
        if (nights <= 0) throw new IllegalArgumentException("Кількість ночей має бути > 0");
        return pricePerNight * nights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return number == room.number;
    }

    @Override
    public int hashCode() { return Objects.hash(number); }

    @Override
    public String toString() { return "Номер " + number + " ($" + pricePerNight + ")"; }
}
