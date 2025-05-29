package co.edu.uptc.persistence.controllers;

public class PersistenceController {

    CustomerJpaController customerJpaController;
    RoomJpaController roomJpaController;
    BookingJpaController bookingJpaController;

    public PersistenceController() {
        customerJpaController = new CustomerJpaController();
        roomJpaController = new RoomJpaController();
        bookingJpaController = new BookingJpaController();
    }
}
