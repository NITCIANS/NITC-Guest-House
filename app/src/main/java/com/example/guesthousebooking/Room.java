package com.example.guesthousebooking;

public class Room {

    private int roomId;
    private boolean roomStatus;
    private float price;
    private boolean AC;

    public Room() {
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public boolean isRoomStatus() {
        return roomStatus;
    }

    public void setRoomStatus(boolean roomStatus) {
        this.roomStatus = roomStatus;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean isAC() {
        return AC;
    }

    public void setAC(boolean AC) {
        this.AC = AC;
    }

    public void changeRoomStatus() {
        if(this.roomStatus)
            this.roomStatus = false;
        else
            this.roomStatus = true;
    }


    public String toString()
    {
        String status = "Free";
        if(roomStatus)
            status = "Occupied";
        return String.format("Room No. : " + roomId + "\nRoom Status : " + status);
    }
}
