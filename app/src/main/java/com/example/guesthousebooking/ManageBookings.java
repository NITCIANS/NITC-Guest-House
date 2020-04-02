package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ManageBookings extends AppCompatActivity {

    private Button B1;
    private Spinner S1;
    private ListView listView;
    private List<Booking> list = new ArrayList<>();
    ArrayAdapter<Booking> adapter;
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);


        B1 = findViewById(R.id.B1);
        S1 = findViewById(R.id.S1);

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();

                final String str = S1.getSelectedItem().toString().trim();
                if(str.equals("Choose Booking Kind"))
                {
                    list.clear();
                    Toast.makeText(ManageBookings.this, "Please Select Booking Kind", Toast.LENGTH_SHORT).show();

                }

                else
                {
                    listView = findViewById(R.id.LV1);
                    adapter = new ArrayAdapter<Booking>(ManageBookings.this, android.R.layout.simple_list_item_1, list);

                    listView.setAdapter(adapter);


                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                        {

                            Booking booking = list.get(position);
                            String[] details = new String[8];
                            details[0] = Integer.toString(booking.getBookingId());
                            details[1] = booking.getUserId();
                            details[2] = booking.getBookingDate();
                            details[3] = booking.getCheckInDate();
                            details[4] = booking.getCheckOutDate();
                            details[5] = Integer.toString(booking.getNoOfRooms());
                            if(booking.isFoodServices())
                                details[6] = "True";
                            else
                                details[6] = "False";

                            details[7] = booking.getBookingStatus();
                            Intent intent = new Intent(ManageBookings.this,AdminViewBooking.class);
                            intent.putExtra("Booking", details);
                            startActivity(intent);

                        }

                    });

                    ref = FirebaseDatabase.getInstance().getReference("Booking");

                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
                        {
                            Booking booking = dataSnapshot.getValue(Booking.class);
                            if(booking.getBookingStatus().equals(str))
                                list.add(booking);
                            adapter.notifyDataSetChanged();

                        }


                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });


    }
}
