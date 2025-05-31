package co.edu.uptc.model;

import java.util.ArrayList;
import java.util.List;

import co.edu.uptc.persistence.controllers.PersistenceController;

public class BookingSystem {
    private List<Booking> bookings;
    private List<Customer> customers;
    private PersistenceController persistence;

    public BookingSystem() {
        this.persistence = new PersistenceController();
        this.bookings = new ArrayList<>();
        this.customers = new ArrayList<>();
    }

    // Crud operations for customers

    public boolean createCustomer(Customer customer) {
        return persistence.createCustomer(customer);
    }

    public String updateCustomer(Customer customer) {
        try {
            persistence.updateCustomer(customer);
            return "Cliente actualizado correctamente";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Customer findCustomer(String id) {
        return persistence.findCustomer(id);
    }

    public List<Customer> findAllCustomers() {
        customers = persistence.findAllCustomers();
        return customers;
    }

    public List<Customer> findCustomersByIdPartial(String query) {
        List<Customer> results = new ArrayList<>();
        for (Customer customer : findAllCustomers()) {
            if (customer.getIdentification().contains(query)) {
                results.add(customer);
            }
        }
        return results;
    }

    public boolean deleteCustomer(String id) {
        try {
            persistence.deleteCustomer(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public int countCustomers() {
        return persistence.countCustomers();
    }

    // Crud operations for Rooms
    public boolean createRoom(Room room) {
        int nextId = persistence.countRooms();
        room.setId(nextId);
        return persistence.createRoom(room);
    }

    public String updateRoom(Room room) {
        try {
            persistence.updateRoom(room);
            return "Habitación actualizada correctamente";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Room findRoom(int id) {
        return persistence.findRoom(id);
    }

    public Room findRoom(RoomType roomType) {
        for (Room room : findAllRooms()) {
            if (room.getRoomType().equals(roomType)) {
                return room;
            }
        }
        return null;
    }

    public boolean deleteRoom(int id) {
        try {
            persistence.deleteRoom(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Room> findAllRooms() {

        return persistence.findAllRooms();
    }

    public int countRooms() {
        return persistence.countRooms();
    }

    // Crud operations for Bookings
    public boolean createBooking(Booking booking) {
        return persistence.createBooking(booking);
    }

    public String updateBooking(Booking booking) {
        try {
            persistence.updateBooking(booking);
            return "Reserva actualizada correctamente";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public Booking findBooking(int id) {
        return persistence.findBooking(id);
    }

    public boolean deleteBooking(int id) {
        try {
            persistence.deleteBooking(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<Booking> findAllBookings() {
        return persistence.findAllBookings();
    }

    public List<Booking> getBookingsByRoomType(RoomType roomType) {
        return persistence.findBookingsByRoomType(roomType);
    }

    public int countBookings() {
        return persistence.countBookings();
    }

    public int getNextBookingId() {
        return persistence.countBookings();
    }

}
