package com.hotel.service;

import com.hotel.model.*;
import java.util.*;
import java.util.stream.Collectors;

public class HotelService {
    private final List<Room> rooms = new ArrayList<>();
    private final List<Booking> bookings = new ArrayList<>();

    public void addRoom(Room room) { rooms.add(room); }

    public List<Room> getAvailableRoomsSorted() {
        return rooms.stream()
                .filter(Room::isAvailable)
                .sorted(Comparator.comparingInt(Room::getNumber))
                .collect(Collectors.toList());
    }

    public List<Booking> getBookingsSortedByRoom() {
        return bookings.stream()
                // Показуємо лише ті бронювання, де кімната досі ЗАЙНЯТА
                .filter(b -> !b.getRoom().isAvailable())
                .sorted(Comparator.comparingInt(b -> b.getRoom().getNumber()))
                .collect(Collectors.toList());
    }

    public int getOccupiedRoomsCount() {
        return (int) rooms.stream().filter(r -> !r.isAvailable()).count();
    }
    public boolean releaseRoom(int roomNumber) {
        Optional<Room> roomOpt = rooms.stream()
                .filter(r -> r.getNumber() == roomNumber)
                .findFirst();

        if (roomOpt.isPresent()) {
            Room room = roomOpt.get();
            if (!room.isAvailable()) { // Якщо вона була зайнята
                room.setAvailable(true); // Робимо вільною
                // ВИДАЛЯЄМО бронювання з нашого списку
                bookings.removeIf(b -> b.getRoom().getNumber() == roomNumber);
                return true;
            }
        }
        return false;
    }

    public boolean isRoomAvailable(int roomNumber) {
        return rooms.stream().anyMatch(r -> r.getNumber() == roomNumber && r.isAvailable());
    }

    public Booking bookRoom(Visitor visitor, int roomNumber, int nights) {
        Room room = rooms.stream()
                .filter(r -> r.getNumber() == roomNumber && r.isAvailable())
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Номер зайнято!"));

        Booking booking = new Booking(visitor, room, nights);
        room.setAvailable(false);
        bookings.add(booking);
        return booking;
    }
}