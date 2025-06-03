package co.edu.uptc.presenter;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.uptc.model.Booking;
import co.edu.uptc.model.BookingSystem;
import co.edu.uptc.model.Customer;
import co.edu.uptc.model.Room;
import co.edu.uptc.model.RoomType;
import co.edu.uptc.utils.DateUtil;
import co.edu.uptc.view.MainView;

public class Presenter {

    private BookingSystem bookingSystem;
    private MainView mainView;

    public Presenter(MainView mainView) {
        this.mainView = mainView;
        this.bookingSystem = new BookingSystem();
    }

    public String createCustomer(String[] data) {
        if (data.length < 6) {
            return "Informacion Incompleta";
        }
        String id = data[0];
        String name = data[1];
        String lastName = data[2];
        String address = data[3];
        String email = data[4];
        String phone = data[5];

        return bookingSystem.createCustomer(new Customer(id, name, lastName, address, email, phone, 0.0));
    }

    public String updateCustomer(String[] data) {
        if (data.length < 6) {
            return "Datos incompletos para actualizar el cliente.";
        }
        String id = data[0];
        String name = data[1];
        String lastName = data[2];
        String address = data[3];
        String email = data[4];
        String phone = data[5];

        Customer customer = new Customer(id, name, lastName, address, email, phone, 0.0);
        return bookingSystem.updateCustomer(customer);
    }

    public boolean deleteCustomer(String id) {
        return bookingSystem.deleteCustomer(id);
    }

    public List<String[]> findCustomersByIdPartial(String query) {
        List<String[]> results = new ArrayList<>();
        for (Customer customer : bookingSystem.findCustomersByIdPartial(query)) {
            results.add(new String[] {
                    customer.getIdentification(),
                    customer.getName(),
                    customer.getLastName(),
                    customer.getAddress(),
                    customer.getEmail(),
                    customer.getPhoneNumber()
            });
        }
        return results;
    }

    public List<String[]> getAllCustomers() {
        List<String[]> results = new ArrayList<>();
        for (Customer customer : bookingSystem.findAllCustomers()) {
            results.add(new String[] {
                    customer.getIdentification(),
                    customer.getName(),
                    customer.getLastName(),
                    customer.getAddress(),
                    customer.getEmail(),
                    customer.getPhoneNumber()
            });
        }
        return results;
    }

    public String[] getRoomInfo(RoomType roomType) {
        Room room = bookingSystem.findRoom(roomType);
        if (room != null) {
            String[] roomInfo = new String[2];
            DecimalFormat df = new DecimalFormat("0");
            df.setGroupingUsed(false); // evita comas o puntos como separadores de miles
            roomInfo[0] = String.valueOf(room.getNumberOfRooms());
            roomInfo[1] = df.format(room.getPricePerNight());
            System.out.println(roomInfo[1]);
            return roomInfo;
        }
        return null;
    }

    public List<RoomType> getRoomTypes() {
        return Arrays.asList(RoomType.values());
    }

    public void updateRoomPrice(String strPrice, RoomType roomType) {
        double price = Double.parseDouble(strPrice);
        Room room = bookingSystem.findRoom(roomType);
        room.setPricePerNight(price);
        bookingSystem.updateRoom(room);
    }

    public List<LocalDate> getUnavailableDates(RoomType roomType) {
        List<Booking> bookings = bookingSystem.getBookingsByRoomType(roomType);
        System.out.println(bookings.size());
        Map<LocalDate, Integer> dateCountMap = new HashMap<>();

        Room room = bookingSystem.findRoom(roomType);
        int totalRooms = 0;
        if (room != null) {
            totalRooms = room.getNumberOfRooms();
        }

        for (Booking booking : bookings) {
            if (!booking.isActive())
                continue;

            LocalDate start = DateUtil.toLocalDate(booking.getStartDate());
            LocalDate end = DateUtil.toLocalDate(booking.getEndDate());

            for (LocalDate date = start; !date.isAfter(end.minusDays(1)); date = date.plusDays(1)) {
                dateCountMap.put(date, dateCountMap.getOrDefault(date, 0) + 1);
            }
        }

        List<LocalDate> unavailableDates = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : dateCountMap.entrySet()) {
            if (entry.getValue() >= totalRooms) {
                unavailableDates.add(entry.getKey());
            }
        }

        return unavailableDates;
    }

    public List<LocalDate> getUnavailableDates(RoomType roomType, int excludeBookingId) {
        List<Booking> bookings = bookingSystem.getBookingsByRoomType(roomType);
        Map<LocalDate, Integer> dateCountMap = new HashMap<>();

        Room room = bookingSystem.findRoom(roomType);
        int totalRooms = room != null ? room.getNumberOfRooms() : 0;

        for (Booking booking : bookings) {
            if (!booking.isActive() || booking.getBookingId() == excludeBookingId) {
                continue;
            }

            LocalDate start = DateUtil.toLocalDate(booking.getStartDate());
            LocalDate end = DateUtil.toLocalDate(booking.getEndDate());

            for (LocalDate date = start; !date.isAfter(end.minusDays(1)); date = date.plusDays(1)) {
                dateCountMap.put(date, dateCountMap.getOrDefault(date, 0) + 1);
            }
        }

        List<LocalDate> unavailableDates = new ArrayList<>();
        for (Map.Entry<LocalDate, Integer> entry : dateCountMap.entrySet()) {
            if (entry.getValue() >= totalRooms) {
                unavailableDates.add(entry.getKey());
            }
        }

        return unavailableDates;
    }

    public String[] getCustomerDataById(String selected) {
        Customer customer = bookingSystem.findCustomer(selected);
        if (customer != null) {
            return new String[] {
                    customer.getIdentification(),
                    customer.getName(),
                    customer.getLastName(),
                    customer.getAddress(),
                    customer.getEmail(),
                    customer.getPhoneNumber()
            };
        }
        return null;
    }

    public double calculateBookingPrice(RoomType value, long nights) {
        Room room = bookingSystem.findRoom(value);
        if (room != null) {
            return room.getPricePerNight() * nights;
        }
        return 0.0;
    }

    public boolean createBooking(String customerId, RoomType roomType, LocalDate checkIn, LocalDate checkOut,
            String value) {
        Customer customer = bookingSystem.findCustomer(customerId);
        Room room = bookingSystem.findRoom(roomType);
        Double price = Double.parseDouble(value);
        int id = bookingSystem.getNextBookingId();
        if (customer == null || room == null) {
            return false;
        }
        Booking booking = new Booking(id, customer, room,
                Date.from(checkIn.atStartOfDay(ZoneId.systemDefault()).toInstant()),
                Date.from(checkOut.atStartOfDay(ZoneId.systemDefault()).toInstant()), price, true);
        return bookingSystem.createBooking(booking);
    }

    public void showCustomerForm(String identification) {
        mainView.getCustomerPane().setFromReservation(true);
        mainView.getCustomerPane().getIdField().setText(identification);
        mainView.notifyChange(mainView.getCustomerPane());
    }

    public void goToBookingPane(String customerId) {
        mainView.notifyChange(mainView.getBookingPane());
        mainView.getBookingPane().loadCustomerData();
        mainView.getBookingPane().updateSuggestions(customerId);
    }

    public boolean deleteBooking(int bookingId) {
        return bookingSystem.deleteBooking(bookingId);
    }

    public List<Booking> getBookingsByDate(LocalDate selectedDate) {
        return bookingSystem.findAllBookings();
    }

    public List<Booking> findBookingsByCustomerId(String query) {
        return bookingSystem.findBookingsByCustomerId(query);
    }

    public List<String[]> getAllBookings() {
        List<String[]> results = new ArrayList<>();
        for (Booking booking : bookingSystem.findAllBookings()) {
            String[] bookingData = new String[6];
            bookingData[0] = String.valueOf(booking.getBookingId());
            bookingData[1] = booking.getCustomer().getIdentification();
            bookingData[2] = booking.getRoom().getRoomType().toString();
            bookingData[3] = DateUtil.toLocalDate(booking.getStartDate()).toString();
            bookingData[4] = DateUtil.toLocalDate(booking.getEndDate()).toString();
            bookingData[5] = String.valueOf(booking.getTotalPrice());
            results.add(bookingData);
        }
        return results;
    }

    public String updateBooking(String[] strings) {
        if (strings.length < 4) {
            return "Datos incompletos para actualizar la reserva.";
        }
        int bookingId = Integer.parseInt(strings[0]);
        RoomType roomType = RoomType.valueOf(strings[1]);
        LocalDate checkIn = LocalDate.parse(strings[2]);
        LocalDate checkOut = LocalDate.parse(strings[3]);

        Booking booking = bookingSystem.findBooking(bookingId);
        if (booking == null) {
            return "Reserva no encontrada.";
        }

        Room room = bookingSystem.findRoom(roomType);

        booking.setRoom(room);
        booking.setStartDate(Date.from(checkIn.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        booking.setEndDate(Date.from(checkOut.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        booking.setTotalPrice(calculateBookingPrice(roomType, DateUtil.getDaysBetween(checkIn, checkOut)));

        return bookingSystem.updateBooking(booking);
    }

    public boolean cancelBooking(String currentBookingId) {
        return bookingSystem.deleteBooking(Integer.parseInt(currentBookingId));
    }

    public RoomType roomTypeFromString(String roomType) {
        switch (roomType) {
            case "DOBLE":
                return RoomType.DOBLE;
            case "TRIPLE":
                return RoomType.TRIPLE;
            case "CUADRUPLE":
                return RoomType.CUADRUPLE;
            case "QUINTUPLE":
                return RoomType.QUINTUPLE;
            case "CABAÑA":
                return RoomType.CABAÑA;
            default:
                throw new IllegalArgumentException("Tipo de habitación desconocido: " + roomType);
        }
    }
}
