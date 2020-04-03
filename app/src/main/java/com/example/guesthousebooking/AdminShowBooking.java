package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AdminShowBooking extends AppCompatActivity {

    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    Button Download, Show, Click1, Click2;
    EditText From, To;

    int year1, year2;
    int month1, month2;
    int day1, day2;
    Calendar calendar;

    Booking booking;
    String bookingId;

    String[] arr1, arr2;

    private DatabaseReference ref;
    private List<Booking> list = new ArrayList<>();
    ArrayAdapter<Booking> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_booking);

        Download = findViewById(R.id.B3);
        Show = findViewById(R.id.B4);
        Click1 = findViewById(R.id.B1);
        Click2 = findViewById(R.id.B2);
        From = findViewById(R.id.ET1);
        To = findViewById(R.id.ET2);

        From.setEnabled(false);
        To.setEnabled(false);
        Download.setVisibility(View.GONE);

        Click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year1 = calendar.get(Calendar.YEAR);
                month1 = calendar.get(Calendar.MONTH);
                day1 = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog1 = new DatePickerDialog(AdminShowBooking.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                From.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
                            }
                        }, year1,month1,day1);
                datePickerDialog1.show();
            }
        });

        Click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year2 = calendar.get(Calendar.YEAR);
                month2 = calendar.get(Calendar.MONTH);
                day2 = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog2 = new DatePickerDialog(AdminShowBooking.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                To.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);


                            }
                        }, year2,month2,day2);

                datePickerDialog2.show();


            }

        });


        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();

                if(checkDataEntered()) {

                    arr1 = From.getText().toString().trim().split("-");
                    arr2 = To.getText().toString().trim().split("-");

                    listView = findViewById(R.id.LV);

                    adapter = new ArrayAdapter<Booking>(AdminShowBooking.this, android.R.layout.simple_list_item_1, list);

                    listView.setAdapter(adapter);

                    ref = FirebaseDatabase.getInstance().getReference("Booking");

                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            booking = dataSnapshot.getValue(Booking.class);
                            String[] arr = booking.getCheckInDate().split("-");
                            if (Integer.parseInt(arr[0]) >= Integer.parseInt(arr1[0])) {
                                list.add(booking);
                                adapter.notifyDataSetChanged();
                                Download.setVisibility(View.VISIBLE);

                            }
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

    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered() {

        if (isEmpty(From)) {
            From.setError("From Date is required");
            return false;
        }
        else
        {
            From.setError(null);
        }

        if (isEmpty(To)) {
            To.setError("To Date is required");
            return false;
        }
        else
        {
            To.setError(null);
        }
        return true;
    }
}
