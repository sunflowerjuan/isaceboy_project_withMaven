package co.edu.uptc.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Entity
public class Room implements Serializable {
    @Id
    private int id;
    @Enumerated(EnumType.STRING)
    private RoomType roomType;
    private int numberOfRooms;
    private double pricePerNight;

    public Room(int id, RoomType roomType, int numberOfRooms, double pricePerNight) {
        this.id = id;
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.pricePerNight = pricePerNight;
    }

    public Room() {
    }

    public Room(RoomType roomType, int numberOfRooms, double pricePerNight) {
        this.roomType = roomType;
        this.numberOfRooms = numberOfRooms;
        this.pricePerNight = pricePerNight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

}
