package com.hotel;

import com.hotel.model.*;
import com.hotel.service.HotelService;
import com.hotel.service.JsonService; //ІМПОРТ
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HotelProjectTest {
    private HotelService service;
    private Room room101;
    private Visitor visitor;

    @BeforeEach
    void setUp() {
        // Цей метод виконується перед КОЖНИМ тестом, щоб дані були «свіжими»
        service = new HotelService();
        room101 = new Room(101, 500.0);
        visitor = new Visitor("Тетяна", "test@kpi.ua");
        service.addRoom(room101);
    }

    //ТЕСТИ ДЛЯ VISITOR

    @Test
    void testValidEmail() {
        assertTrue(visitor.isValidEmail(), "Email має бути валідним");
    }

    @Test
    void testInvalidEmail() {
        Visitor badVisitor = new Visitor("Гість", "bad@email");
        assertFalse(badVisitor.isValidEmail(), "Email без крапки не має проходити валідацію");
    }

    //ТЕСТИ ДЛЯ ROOM

    @Test
    void testCalculateTotal() {
        assertEquals(1500.0, room101.calculateTotal(3), 0.001);
    }

    @Test
    void testCalculateTotalException() {
        assertThrows(IllegalArgumentException.class, () -> room101.calculateTotal(-5));
    }

    // ТЕСТИ ДЛЯ HOTELSERVICE

    @Test
    void testSortingLogic() {
        service.addRoom(new Room(47, 200.0));
        List<Room> sorted = service.getAvailableRoomsSorted();
        assertEquals(47, sorted.get(0).getNumber(), "Першим у списку має бути номер 47");
    }

    @Test
    void testBookingProcess() {
        service.bookRoom(visitor, 101, 2);
        assertFalse(room101.isAvailable(), "Кімната має змінити статус на 'зайнята'");
    }

    @Test
    void testBookingOccupiedRoom() {
        service.bookRoom(visitor, 101, 2);
        assertThrows(NoSuchElementException.class, () -> service.bookRoom(visitor, 101, 1));
    }

    @Test
    void testReleaseRoomLogic() {
        service.bookRoom(visitor, 101, 2);
        service.releaseRoom(101);
        assertTrue(room101.isAvailable());
        assertTrue(service.getBookingsSortedByRoom().isEmpty(), "Список бронювань має стати порожнім");
    }

    @Test
    void testOccupiedCounter() {
        service.addRoom(new Room(102, 1000.0));
        service.bookRoom(visitor, 101, 1);
        assertEquals(1, service.getOccupiedRoomsCount());
    }

    //Мок-тест: взаємодію без реального запису файлу
    @Test
    void testExportWithMock() throws IOException {
        JsonService mockJson = mock(JsonService.class);
        List<Room> testRooms = List.of(new Room(99, 100.0));

        mockJson.saveToFile("test.json", testRooms);

        verify(mockJson, times(1)).saveToFile("test.json", testRooms);
    }
}