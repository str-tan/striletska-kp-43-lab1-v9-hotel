package com.hotel.model;

public class Booking {
    private Visitor visitor;
    private Room room;
    private int nights;
    private double totalAmount;

    public Booking() {}//для json

    public Booking(Visitor visitor, Room room, int nights) {
        this.visitor = visitor;
        this.room = room;
        this.nights = nights;

        this.totalAmount = room.calculateTotal(nights);
    }
    public int getNights() {
        return nights;
    }

    public Visitor getVisitor() { return visitor; }
    public Room getRoom() { return room; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Бронювання: " + visitor.getFullName() + " у номері " + room.getNumber() +
                ". На " + nights + " ночей. До сплати: $" + totalAmount;
    }
}