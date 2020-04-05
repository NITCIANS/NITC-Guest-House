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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdminViewBooking extends AppCompatActivity {


    private DatabaseReference ref, reff;
    private ListView listView;

    private TextView T9, T10, T11, T12, T13, T14, T15, T16;
    private Button B1, B2;
    int check = 0;
    String child;
    int stop = 0;
    ArrayList<GuestHouse> List = new ArrayList<>();
    ArrayList<String> ChildList = new ArrayList<>();
    Booking booking = new Booking();
    int total = 0;
    int once = 0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_booking);



        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Room");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                total = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        T9 = findViewById(R.id.T9);
        T10 = findViewById(R.id.T10);
        T11 = findViewById(R.id.T11);
        T12 = findViewById(R.id.T12);
        T13 = findViewById(R.id.T13);
        T14 = findViewById(R.id.T14);
        T15 = findViewById(R.id.T15);
        T16 = findViewById(R.id.T16);
        B1 = findViewById(R.id.B1);
        B2 = findViewById(R.id.B2);


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

                            final String checkInDate = BookingDetails[3];
                            final String checkOutDate = BookingDetails[4];
                            final int rooms = Integer.parseInt(BookingDetails[5]);


                            ref = FirebaseDatabase.getInstance().getReference("GuestHouse");
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if (stop == 0) {

                                            try {
                                                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
                                                Date date1 = (Date) formatter.parse(checkInDate);
                                                Date date2 = (Date) formatter.parse(checkOutDate);
                                                String child = "";

                                               while (date1.before(date2)) {

                                                    child = formatter.format(date1);

                                                    ChildList.add(child);
                                                    if (dataSnapshot.hasChild(child)) {
                                                        GuestHouse G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                                        if (G1.checkAvailability() < rooms) {

                                                            List.clear();
                                                            check = 1;
                                                            stop = 1;
                                                            break;
                                                        } else {
                                                            G1.setOccupiedRooms(G1.getVacantRooms() - rooms);
                                                            G1.setVacantRooms(G1.getTotalRooms() - G1.getOccupiedRooms());
                                                            List.add(G1);
                                                        }
                                                    } else {
                                                        GuestHouse G1 = new GuestHouse();
                                                        G1.setGuestHouseId(1);
                                                        G1.setTotalRooms(total);
                                                        G1.setOccupiedRooms(rooms);
                                                        G1.setVacantRooms(total - rooms);
                                                        List.add(G1);
                                                    }

                                                    date1 = (Date) formatter.parse(child);
                                                    Calendar c = Calendar.getInstance();
                                                    c.setTime(date1);
                                                    c.add(Calendar.DAY_OF_MONTH, 1);
                                                    date1 = c.getTime();
                                                }

                                                int k = 0;
                                                for (GuestHouse G : List) {
                                                    ref.child(ChildList.get(k++)).setValue(G);
                                                }
                                                if (check == 0) {
                                                    stop = 1;
                                                    changeBookingStatus(BookingDetails[0]);
                                                    check = 1;
                                                } else {
                                                    stop = 1;
                                                    Toast.makeText(AdminViewBooking.this, "Rooms not available", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(AdminViewBooking.this, ManageBookings.class);
                                                    startActivity(intent);

                                                }
                                            } catch (Exception e) {
                                                Toast.makeText(AdminViewBooking.this, "Error : " + e, Toast.LENGTH_LONG).show();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

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
                                booking.sendMail(AdminViewBooking.this, 3);
                            }
                            else {
                                booking.setBookingStatus("Cancelled");
                                booking.sendMail(AdminViewBooking.this, 3);
                            }

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