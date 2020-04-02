package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminViewBooking extends AppCompatActivity {


    private DatabaseReference ref, reff;
    private ListView listView;

    private TextView T9, T10, T11, T12, T13, T14, T15, T16;
    private Button B1, B2;
    int check = 0;
    String child;
    GuestHouse G1 = new GuestHouse();
    int i = 0;
    int stop = 0;
    String[] arr1, arr2;
    int day1, day2, year1, year2, month1, month2;
    ArrayList<GuestHouse> List = new ArrayList<>();
    ArrayList<String> ChildList = new ArrayList<>();
    Booking booking = new Booking();
    int free = 0;
    int once = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_booking);


        T9 = findViewById(R.id.T9);
        T10 = findViewById(R.id.T10);
        T11 = findViewById(R.id.T11);
        T12 = findViewById(R.id.T12);
        T13 = findViewById(R.id.T13);
        T14 = findViewById(R.id.T14);
        T15 = findViewById(R.id.T15);
        T16 = findViewById(R.id.T16);
        B1 = findViewById(R.id.B1);
        B2 = findViewById(R.id.B1);


        final String[] BookingDetails;
        BookingDetails = getIntent().getStringArrayExtra("Booking");


        T9.setText(BookingDetails[0]);
        T10.setText(BookingDetails[1]);
        T11.setText(BookingDetails[2]);
        T12.setText(BookingDetails[3]);
        T13.setText(BookingDetails[4]);
        T14.setText(BookingDetails[5]);
        T15.setText(BookingDetails[6]);
        T16.setText(BookingDetails[7]);

        booking.setBookingId(Integer.parseInt(BookingDetails[0]));
        booking.setUserId(BookingDetails[1]);
        booking.setBookingDate(BookingDetails[2]);
        booking.setCheckInDate(BookingDetails[3]);
        booking.setCheckOutDate(BookingDetails[4]);
        booking.setNoOfRooms(Integer.parseInt(BookingDetails[5]));


        if (BookingDetails[6].equals("True"))
            booking.setFoodServices(true);
        else
            booking.setFoodServices(false);

        if(BookingDetails[7].equals("Pending"))
            B2.setText("REJECT");
        else if(BookingDetails[7].equals("Confirmed"))
            B2.setText("CANCEL");


        if (BookingDetails[7].equals("Completed") || BookingDetails[7].equals("CheckedIn" ) || BookingDetails[7].equals("Rejected") ) {
            B1.setVisibility(View.GONE);
            B2.setVisibility(View.GONE);
        }
        else {

            B1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewBooking.this);
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            final String checkInDate = BookingDetails[3].split(" ")[0];
                            final String checkOutDate = BookingDetails[4].split(" ")[0];




                            arr1 = checkInDate.split("-");
                            arr2 = checkOutDate.split("-");
                            day1 = Integer.parseInt(arr1[0]);
                            day2 = Integer.parseInt(arr2[0]);
                            month1 = Integer.parseInt(arr1[1]);
                            month2 = Integer.parseInt(arr2[1]);
                            year1 = Integer.parseInt(arr1[2]);
                            year2 = Integer.parseInt(arr2[2]);

                            final int rooms = Integer.parseInt(BookingDetails[5]);


                            ref = FirebaseDatabase.getInstance().getReference("GuestHouse");

                            if(Integer.parseInt(arr1[1]) - Integer.parseInt(arr2[1]) == 0)
                            {
                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                            if(free == 0) {
                                                for (i = day1; i < day2; i++) {

                                                    child = Integer.toString(i) + "-" + arr1[1] + "-" + arr1[2];
                                                    ChildList.add(child);

                                                    if (dataSnapshot.hasChild(child)) {

                                                        G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                                        int vacant = G1.getVacantRooms();
                                                        if (vacant < rooms) {

                                                            Toast.makeText(AdminViewBooking.this, "Rooms not available", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(AdminViewBooking.this, ManageBookings.class);
                                                            startActivity(intent);
                                                            check = 1;
                                                            free = 1;
                                                            break;
                                                        } else {
                                                            int occupied = G1.getOccupiedRooms();
                                                            G1.setOccupiedRooms(occupied + Integer.parseInt(BookingDetails[5]));
                                                            G1.setVacantRooms(vacant - Integer.parseInt(BookingDetails[5]));
                                                            List.add(G1);
                                                        }

                                                    }
                                                    else {

                                                        G1.setVacantRooms(G1.getTotalRooms() - rooms);
                                                        G1.setOccupiedRooms(rooms);
                                                        G1.setGuestHouseId(1);
                                                        List.add(G1);
                                                    }
                                                }
                                                if(check == 0) {
                                                    int k = 0;
                                                    for (GuestHouse G : List) {
                                                        ref.child(ChildList.get(k++)).setValue(G);
                                                    }
                                                    changeBookingStatus(BookingDetails[0]);
                                                    check = 1;
                                                }
                                                free = 1;
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                            }
                            else {

                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                                        if(free == 0) {
                                            for (i = day1; i != day2; i++) {

                                                child = Integer.toString(i) + "-" + arr1[1] + "-" + arr1[2];
                                                ChildList.add(child);

                                                if (dataSnapshot.hasChild(child)) {

                                                    G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                                    int vacant = G1.getVacantRooms();
                                                    if (vacant < rooms) {

                                                        Toast.makeText(AdminViewBooking.this, "Rooms not available", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(AdminViewBooking.this, ManageBookings.class);
                                                        startActivity(intent);
                                                        check = 1;
                                                        free = 1;
                                                        break;
                                                    } else {
                                                        int occupied = G1.getOccupiedRooms();
                                                        G1.setOccupiedRooms(occupied + Integer.parseInt(BookingDetails[5]));
                                                        G1.setVacantRooms(vacant - Integer.parseInt(BookingDetails[5]));
                                                        List.add(G1);
                                                    }

                                                }
                                                else {

                                                    G1.setVacantRooms(G1.getTotalRooms() - rooms);
                                                    G1.setOccupiedRooms(rooms);
                                                    G1.setGuestHouseId(1);
                                                    List.add(G1);
                                                }

                                                if (i + 1 == 32) {
                                                    i = 0;
                                                    arr1[1] = arr2[1];
                                                }

                                                if (i + 1 == 31 && month1 != 1 && month1 != 3 && month1 != 5 && month1 != 7 && month1 != 8 && month1 != 10 && month1 != 12)
                                                {
                                                    i = 0;
                                                    arr1[1] = arr2[1];
                                                }
                                                if (year1 != year2 && i == 31) {
                                                    arr1[2] = arr2[2];
                                                    arr1[1] = arr2[1];
                                                    i = 0;
                                                }

                                                if (year1 % 4 != 0 && month1 == 2 && i == 28) {
                                                    arr1[1] = arr2[1];
                                                    i = 0;
                                                }
                                                if (year1 % 4 == 0 && i == 29 && month1 == 2) {
                                                    arr1[1] = arr2[1];
                                                    i = 0;
                                                }

                                            }
                                            if(check == 0) {
                                                int k = 0;
                                                for (GuestHouse G : List) {
                                                    ref.child(ChildList.get(k++)).setValue(G);
                                                }
                                                changeBookingStatus(BookingDetails[0]);
                                                check = 1;
                                            }
                                            free = 1;
                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }

                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });

            B2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(AdminViewBooking.this);
                    builder.setTitle("Are You Sure ?");

                    builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            if(BookingDetails[7].equals("Pending")) {
                                booking.setBookingStatus("Rejected");
                                booking.sendMail(AdminViewBooking.this, 2);
                            }
                            else
                                booking.setBookingStatus("Cancelled");

                            ref = FirebaseDatabase.getInstance().getReference("Booking");
                            ref.child(BookingDetails[0]).setValue(booking);
                            if(BookingDetails[7].equals("Pending"))
                                Toast.makeText(AdminViewBooking.this, "Booking Rejected", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(AdminViewBooking.this, "Booking Cancelled", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(AdminViewBooking.this, ManageBookings.class);
                            startActivity(intent);

                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });



        }

    }

    void checkAvailability(final String cidate, final String codate, final int num)
    {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = rootRef.child("GuestHouse");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot keys : dataSnapshot.getChildren()) {
                    if (keys.getKey().equals(cidate)) {
                        AbstractGuestHouse G1 = dataSnapshot.child(cidate).getValue(GuestHouse.class);
                        if(G1.checkAvailability() < num) {

                            Toast.makeText(AdminViewBooking.this, "Function called", Toast.LENGTH_SHORT).show();
                            free = 1;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(valueEventListener);
    }

    void changeBookingStatus(final String bookId)
    {
        once = 0;
        reff = FirebaseDatabase.getInstance().getReference("Booking");
        reff.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(once == 0) {
                    Booking booking = dataSnapshot.child(bookId).getValue(Booking.class);
                    booking.setBookingStatus("Confirmed");
                    reff.child(bookId).setValue(booking);
                    booking.sendMail(AdminViewBooking.this, 1);
                    Toast.makeText(AdminViewBooking.this, "Booking Confirmed..", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AdminViewBooking.this, ManageBookings.class);
                    startActivity(intent);
                    once = 1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}