package co.edu.uptc.persistence.controllers;

import java.util.List;

import co.edu.uptc.model.Booking;
import co.edu.uptc.model.Customer;
import co.edu.uptc.model.Room;
import co.edu.uptc.model.RoomType;
import co.edu.uptc.persistence.exceptions.NonexistentEntityException;

public class PersistenceController {

    CustomerJpaController customerJpaController;
    RoomJpaController roomJpaController;
    BookingJpaController bookingJpaController;

    public PersistenceController() {
        customerJpaController = new CustomerJpaController();
        roomJpaController = new RoomJpaController();
        bookingJpaController = new BookingJpaController();
    }

    // CRUD operations for Customer

    public boolean createCustomer(Customer customer) {
        customerJpaController.create(customer);
        return true;
    }

    public void updateCustomer(Customer customer) throws Exception {
        customerJpaController.edit(customer);
    }

    public Customer findCustomer(String id) {
        return customerJpaController.findCustomer(id);
    }

    public boolean deleteCustomer(String id) throws NonexistentEntityException {
        customerJpaController.destroy(id);
        return true;
    }

    public List<Customer> findAllCustomers() {
        return customerJpaController.findCustomerEntities();
    }

    public int countCustomers() {
        return customerJpaController.getCustomerCount();
    }

    public boolean existEmail(String email) {
        return customerJpaController.emailExists(email);
    }

    // CRUD operations for Room

    public boolean createRoom(Room room) {
        try {
            roomJpaController.create(room);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void updateRoom(Room room) throws Exception {
        roomJpaController.edit(room);
    }

    public Room findRoom(int id) {
        return roomJpaController.findRoom(id);
    }

    public boolean deleteRoom(int id) throws NonexistentEntityException {
        roomJpaController.destroy(id);
        return true;
    }

    public List<Room> findAllRooms() {
        return roomJpaController.findRoomEntities();
    }

    public int countRooms() {
        return roomJpaController.getRoomCount();
    }

    // CRUD operations for Booking

    public boolean createBooking(Booking booking) {
        try {
            bookingJpaController.create(booking);
            return true;
        } catch (Exception e) {

            return false;
        }
    }

    public void updateBooking(Booking booking) throws Exception {
        bookingJpaController.edit(booking);
    }

    public Booking findBooking(int id) {
        return bookingJpaController.findBooking(id);
    }

    public boolean deleteBooking(int id) throws NonexistentEntityException {
        bookingJpaController.destroy(id);
        return true;
    }

    public boolean hasActiveBookingForCustomer(String id) {
        return bookingJpaController.hasActiveBookingForCustomer(id);
    }

    public List<Booking> findAllBookings() {
        return bookingJpaController.findBookingEntities();
    }

    public List<Booking> findBookingsByRoomType(RoomType roomType) {
        return bookingJpaController.findBookingsByRoomType(roomType);
    }

    public List<Booking> findActiveBookingsWithCheckOutToday() {
        return bookingJpaController.findActiveBookingsWithCheckOutToday();
    }

    public List<Booking> findBookingsByCustomerId(String query) {
        return bookingJpaController.findBookingsByCustomerIdentificationLike(query);
    }

    public int countBookings() {
        return bookingJpaController.findMaxBookingId();
    }

}
