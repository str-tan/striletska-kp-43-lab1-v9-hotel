package com.hotel;

import com.hotel.model.*;
import com.hotel.service.HotelService;
import com.hotel.service.JsonService;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        HotelService hotelService = new HotelService();
        JsonService jsonService = new JsonService();
        Scanner scanner = new Scanner(System.in);

        hotelService.addRoom(new Room(101, 500.0));
        hotelService.addRoom(new Room(102, 1200.0));
        hotelService.addRoom(new Room(103, 800.0));
        hotelService.addRoom(new Room(75, 100.0));
        hotelService.addRoom(new Room(47, 200.0));

        boolean running = true;

        while (running) {
            try {
                System.out.println("\n=== ГОТЕЛЬ 'Star Rain' ===");
                showStatistics(hotelService);

                System.out.println("\n--- МЕНЮ ---");
                System.out.println("1. Забронювати номер");
                System.out.println("2. Звільнити номер");
                System.out.println("3. Переглянути всі бронювання");
                System.out.println("4. Вийти");
                System.out.println("5. Збереження");
                System.out.println("6.Завантаження");
                System.out.print("Оберіть дію: ");

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println("\nДоступні номери (відсортовано):");
                        hotelService.getAvailableRoomsSorted().forEach(r ->
                                System.out.println("Номер " + r.getNumber() + " | Ціна: $" + r.getPricePerNight() + " | Вільний: " + r.isAvailable())
                        );

                        System.out.print("\nВведіть номер кімнати: ");
                        int rNum = scanner.nextInt();
                        scanner.nextLine();

                        if (!hotelService.isRoomAvailable(rNum)) {
                            System.out.println("Помилка: Номер вже зайнято або не існує!");
                            break;
                        }

                        System.out.print("Ваш Email: ");
                        String email = scanner.nextLine();
                        Visitor visitor = new Visitor("Гість", email);

                        if (!visitor.isValidEmail()) {
                            System.out.println("Помилка: Email (" + visitor.getEmail() + ") некоректний!");
                            break;
                        }

                        System.out.print("Повне ім'я: ");
                        String name = scanner.nextLine();
                        visitor = new Visitor(name, email);

                        System.out.println("Реєструємо клієнта: " + visitor.getFullName() + " (ID: " + visitor.getId() + ")");

                        System.out.print("Скільки ночей? ");
                        int nights = scanner.nextInt();
                        scanner.nextLine();

                        Booking b = hotelService.bookRoom(visitor, rNum, nights);

                        System.out.println(" " + b.getVisitor().getFullName() + ", заброньовано на " + nights + " ночей.");
                        System.out.println("Загальна сума: $" + b.getTotalAmount());
                        break;

                    case 2:
                        System.out.print("Введіть номер кімнати для звільнення: ");
                        int roomToFree = scanner.nextInt();
                        scanner.nextLine();

                        if (hotelService.releaseRoom(roomToFree)) {
                            System.out.println("Номер " + roomToFree + " тепер знову вільний.");
                        } else {
                            System.out.println("!Увага: Номер " + roomToFree + " і так був вільним.");
                        }
                        break;

                    case 3:
                        System.out.println("\n--- ДЕТАЛЬНИЙ ЗВІТ ---");
                        List<Booking> activeBookings = hotelService.getBookingsSortedByRoom();

                        if (activeBookings.isEmpty()) {
                            System.out.println("Бронювань немає.");
                        } else {
                            for (Booking booking : activeBookings) {
                                System.out.println(
                                        "Кімната №" + booking.getRoom().getNumber() +
                                                " | Клієнт: " + booking.getVisitor().getFullName() +
                                                " | Ночей: " + booking.getNights() +
                                                " | РАЗОМ: $" + booking.getTotalAmount()
                                );
                            }

                            double totalRevenue = activeBookings.stream()
                                    .mapToDouble(Booking::getTotalAmount)
                                    .sum();
                            System.out.println("\nЗагальна виручка готелю: $" + totalRevenue);
                        }
                        break;

                    case 4:
                        running = false;
                        System.out.println("До зустрічі!");
                        break;

                    case 5: // Збереження
                        try {
                            List<Room> roomsToSave = hotelService.getAvailableRoomsSorted();
                            jsonService.saveToFile("rooms_data.json", roomsToSave);
                            System.out.println("Дані успішно збережено в rooms_data.json");
                        } catch (IOException e) {
                            System.out.println("Помилка запису: " + e.getMessage());
                        }
                        break;

                    case 6: // Завантаження
                        try {
                            List<Room> imported = jsonService.loadFromFile("rooms_data.json", Room.class);
                            imported.forEach(hotelService::addRoom);
                            System.out.println("Завантажено номерів: " + imported.size());
                        } catch (IOException e) {
                            System.out.println("Помилка завантаження: " + e.getMessage());
                        }
                        break;

                    default:
                        System.out.println("Невірний вибір. Оберіть 1-6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("\nПОМИЛКА: Вводьте лише ЧИСЛА!");
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("\nВиникла помилка: " + e.getMessage());
            }
        }
    }

    private static void showStatistics(HotelService service) {
        System.out.println("Заброньовано: " + service.getOccupiedRoomsCount());
        List<Room> free = service.getAvailableRoomsSorted();
        if (free.isEmpty()) {
            System.out.println("Вільні номери: [УСІ ЗАЙНЯТІ]");
        } else {
            System.out.print("Вільні номери: ");
            free.forEach(r -> System.out.print(r.getNumber() + " "));
            System.out.println();
        }
        System.out.println("===========================");
    }
}
