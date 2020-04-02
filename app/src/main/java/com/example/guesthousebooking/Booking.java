package com.example.guesthousebooking;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Date;


public class Booking
{
    private int bookingId;
    private int noOfRooms;
    public  String bookingDate;
    private String checkInDate;
    private String checkOutDate;
    private boolean foodServices;
    private String bookingStatus;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getBookingId(){
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getNoOfRooms() {
        return noOfRooms;
    }

    public void setNoOfRooms(int noOfRooms) {
        this.noOfRooms = noOfRooms;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public boolean isFoodServices() {
        return foodServices;
    }

    public void setFoodServices(boolean foodServices) {
        this.foodServices = foodServices;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }


    public float calculatePrice()
    {
        int totalRooms = getNoOfRooms();
        return 250 * totalRooms;
    }


    public String toString()
    {
        return String.format("Booking Id : " + bookingId + "\nUser Id : " + userId + "\nBooking Status : " + bookingStatus);
    }

    public void sendMail(Context page, int type)
    {
        String mail = this.userId;
        String details = "Your Booking Details: \n Booking ID : " + this.bookingId + "\nBooking Date : " + this.bookingDate + "\n No. of Rooms : " + this.noOfRooms + "\n Check In Date : " + this.checkInDate + "\n Check Out Date : " + this.checkOutDate;

        String subject = "NITC Guesthoust Booking";
        String message = "";
        if(type == 1)
            message = "Your Booking is Confirmed";
        else if(type == 2)
            message = "Your Booking has been Rejected due to certain Conditions";
        message = message + "\n" + details;
        JavaMailAPI javaMailAPI = new JavaMailAPI(page, mail, subject, message);
        javaMailAPI.execute();

    }
}
