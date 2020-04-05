package com.example.guesthousebooking;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

public class ScheduledTask extends BroadcastReceiver {



    private List<Booking> newList = new ArrayList<>();
    int count = 0;
    String userType;
    DatabaseReference ref, reff, reference;
    boolean done = true;
    int rooms = 0, totalRooms = 0;


    @Override
    public void onReceive(Context context, Intent intent) {


        ref = FirebaseDatabase.getInstance().getReference("Booking");
        reference = FirebaseDatabase.getInstance().getReference("Rooms");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalRooms = (int) dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot keys : dataSnapshot.getChildren())
                {
                    String child = keys.getKey();
                    Booking booking = dataSnapshot.child(child).getValue(Booking.class);
                    if(booking.getBookingStatus().equals("Pending"))
                    {
                        newList.add(booking);
                        count++;
                    }
                }
                if(newList.size() > 0)
                {
                    Priority P = new Priority();
                    PriorityQueue<Booking> PQueue = P.priorTize(count, newList, 2);
                    newList.clear();
                    while (!PQueue.isEmpty()) {
                        newList.add(PQueue.poll());
                    }
                    for(Booking B : newList)
                    {
                        if(checkData(B))
                        {
                            totalRooms = B.getNoOfRooms();
                            B.setBookingStatus("Confirmed");
                            ref.child(Integer.toString(B.getBookingId())).setValue(B);
                            B.sendMail(1);
                            createEntries(B.getCheckInDate(), B.getCheckOutDate());
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }

    boolean checkData(Booking B)
    {
        userType = B.getType();
        String CID = B.getCheckInDate();
        try
        {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
            Date date1 = (Date)formatter.parse(CID);

            Date currentDate = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(currentDate);
            if(userType.equals("Faculty") || userType.equals("Staff") || userType.equals("SAC Member"))
                c.add(Calendar.DAY_OF_MONTH, 3);
            else
                c.add(Calendar.DAY_OF_MONTH, 1);
            Date newDate = c.getTime();

            String dates = formatter.format(newDate);
            newDate = (Date) formatter.parse(dates);

            if (newDate.after(date1))
                return true;
            else
                return false;
        }
        catch (Exception e)
        {

        }

        return false;
    }

    void createEntries(final String d1, final String d2)
    {

        reff = FirebaseDatabase.getInstance().getReference("GuestHouse");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (done) {

                    try {

                        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
                        Date date1 = (Date) formatter.parse(d1);
                        Date date2 = (Date) formatter.parse(d2);
                        String child = "";
                        GuestHouse G1;

                        while (date1.before(date2)) {

                            child = formatter.format(date1);
                            if (dataSnapshot.hasChild(child)) {
                                G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                G1.setOccupiedRooms(G1.getVacantRooms() - rooms);
                                G1.setVacantRooms(G1.getTotalRooms() - G1.getOccupiedRooms());
                            } else {
                                G1 = new GuestHouse();
                                G1.setGuestHouseId(1);
                                G1.setTotalRooms(totalRooms);
                                G1.setVacantRooms(totalRooms - rooms);
                                G1.setOccupiedRooms(rooms);
                            }
                            reff.child(child).setValue(G1);
                            date1 = (Date) formatter.parse(child);
                            Calendar c = Calendar.getInstance();
                            c.setTime(date1);
                            c.add(Calendar.DAY_OF_MONTH, 1);
                            date1 = c.getTime();

                        }
                        done = false;
                    } catch (Exception e) {

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
