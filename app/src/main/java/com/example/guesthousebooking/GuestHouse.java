package com.example.guesthousebooking;

public class GuestHouse implements AbstractGuestHouse {


    private int guestHouseId;
    private int totalRooms = 10;
    private int vacantRooms;
    private int occupiedRooms;

    public GuestHouse() {
    }

    public int getGuestHouseId() {
        return guestHouseId;
    }

    public void setGuestHouseId(int guestHouseId) {
        this.guestHouseId = guestHouseId;
    }

    public int getTotalRooms() {
        return totalRooms;
    }

    public void setTotalRooms(int totalRooms) {
        this.totalRooms = totalRooms;
    }

    public int getVacantRooms() {
        return vacantRooms;
    }

    public void setVacantRooms(int vacantRooms) {
        this.vacantRooms = vacantRooms;
    }

    public int getOccupiedRooms() {
        return occupiedRooms;
    }

    public void setOccupiedRooms(int occupiedRooms) {
        this.occupiedRooms = occupiedRooms;
    }

    public int checkAvailability()
    {
        return vacantRooms;
    }
}
