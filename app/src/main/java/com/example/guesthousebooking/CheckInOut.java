package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CheckInOut extends AppCompatActivity {

    private EditText ET1;
    private Button B1, B2;
    private DatabaseReference ref, reff;
    int check = 0;
    int roomCheck = 0;
    Booking booking;
    Bill bill;
    ArrayList<Room> list = new ArrayList<>();
    Room R1;
    int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);

        ET1 = findViewById(R.id.ET1);
        B1 = findViewById(R.id.B2);
        B2 = findViewById(R.id.B2);


        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkDataEntered()) {

                    final String bookingId = ET1.getText().toString().trim();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CheckInOut.this);
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    ref = FirebaseDatabase.getInstance().getReference("Booking");

                                    ref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                        {
                                            if(check == 0) {
                                                for (DataSnapshot keys : dataSnapshot.getChildren()) {
                                                    if (keys.getKey().equals(bookingId)) {
                                                        booking = dataSnapshot.child(keys.getKey()).getValue(Booking.class);
                                                        if (booking.getBookingStatus().equals("CheckedIn") && check == 0) {
                                                            Toast.makeText(CheckInOut.this, "Already Checked In ", Toast.LENGTH_LONG).show();
                                                            finish();
                                                            startActivity(getIntent());
                                                            check = 1;
                                                            break;
                                                        } else if (booking.getBookingStatus().equals("Completed") && check == 0) {
                                                            Toast.makeText(CheckInOut.this, "Completed Booking ", Toast.LENGTH_LONG).show();
                                                            finish();
                                                            startActivity(getIntent());
                                                            check = 1;
                                                            break;
                                                        } else if (booking.getBookingStatus().equals("Cancelled") && check == 0) {
                                                            Toast.makeText(CheckInOut.this, "Cancelled Booking", Toast.LENGTH_LONG).show();
                                                            finish();
                                                            startActivity(getIntent());
                                                            check = 1;
                                                            break;
                                                        } else if (check == 0) {
                                                            booking.setBookingStatus("CheckedIn");
                                                            ref.child(bookingId).setValue(booking);
                                                            Toast.makeText(CheckInOut.this, "Successfully Checked In..", Toast.LENGTH_LONG).show();
                                                            finish();
                                                            startActivity(getIntent());
                                                            check = 1;
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                            if(check == 0)
                                            {
                                                Toast.makeText(CheckInOut.this, "Invalid Booking Id " , Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
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

                }

        });
        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(checkDataEntered()) {

                    final String bookingId = ET1.getText().toString().trim();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CheckInOut.this);
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ref = FirebaseDatabase.getInstance().getReference("Booking");
                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    for(DataSnapshot keys : dataSnapshot.getChildren())
                                    {
                                        if(keys.getKey().equals(bookingId)) {
                                            booking = dataSnapshot.child(keys.getKey()).getValue(Booking.class);
                                            if (booking.getBookingStatus().equals("Completed") && check == 0) {
                                                Toast.makeText(CheckInOut.this, "Already Checked Out ", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                                check = 1;
                                                break;
                                            }
                                            else if (booking.getBookingStatus().equals("Pending") && check == 0) {
                                                Toast.makeText(CheckInOut.this, "Pending Booking ", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                                check = 1;
                                                break;
                                            }
                                            else if (booking.getBookingStatus().equals("Cancelled") && check == 0){
                                                Toast.makeText(CheckInOut.this, "Cancelled Booking", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                                check = 1;
                                                break;
                                            }
                                            else if(check == 0) {
                                                check = 1;
                                                number = booking.getNoOfRooms();
                                                reff = FirebaseDatabase.getInstance().getReference("Bill");
                                                bill = new Bill();
                                                bill.setUserId(booking.getUserId());
                                                bill.setBillId(booking.getBookingId());
                                                bill.setAmount(booking.getNoOfRooms() * 250);
                                                bill.setCheckInDate(booking.getCheckInDate().split(" ")[0]);
                                                reff.child(bookingId).setValue(bill);
                                                booking.setBookingStatus("Completed");
                                                ref.child(bookingId).setValue(booking);
                                                Toast.makeText(CheckInOut.this, "Checking Out Successful ", Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(getIntent());
                                                check = 1;
                                                break;
                                            }
                                        }
                                    }
                                    if(check == 0)
                                    {
                                        Toast.makeText(CheckInOut.this, "Invalid Booking Id " , Toast.LENGTH_LONG).show();
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


            }
        });


    }


    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered() {

        if (isEmpty(ET1)) {
            ET1.setError("Booking Id is required");
            return false;
        }
        else
        {
            ET1.setError(null);
        }
        return true;
    }
}
