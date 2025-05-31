package co.edu.uptc.presenter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import co.edu.uptc.model.BookingSystem;
import co.edu.uptc.model.Customer;
import co.edu.uptc.model.Room;
import co.edu.uptc.model.RoomType;
import co.edu.uptc.view.MainView;

public class Presenter {

    private BookingSystem bookingSystem;
    private MainView mainView;

    public Presenter(MainView mainView) {
        this.mainView = mainView;
        this.bookingSystem = new BookingSystem();
    }

    public boolean createCustomer(String[] data) {
        if (data.length < 6) {
            return false;
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

}
