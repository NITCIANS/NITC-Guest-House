package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminCheckAvailability extends AppCompatActivity {

    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    Button Check, Click1, Click2;
    EditText CID, COD;

    int year1, year2;
    int month1, month2;
    int day1, day2;
    Calendar calendar;
    int totalRooms;
    int count = 0;
    String child, text1, text2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_availability);

        Check = findViewById(R.id.B3);
        Click1 = findViewById(R.id.B2);
        Click2 = findViewById(R.id.B2);
        CID = findViewById(R.id.ET1);
        COD = findViewById(R.id.ET2);

        CID.setEnabled(false);
        COD.setEnabled(false);


        DatabaseReference reff = FirebaseDatabase.getInstance().getReference("Room");
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                count = (int) dataSnapshot.getChildrenCount();
                totalRooms = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year1 = calendar.get(Calendar.YEAR);
                month1 = calendar.get(Calendar.MONTH);
                day1 = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog1 = new DatePickerDialog(AdminCheckAvailability.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                CID.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
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


                datePickerDialog2 = new DatePickerDialog(AdminCheckAvailability.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                COD.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);


                            }
                        }, year2,month2,day2);

                datePickerDialog2.show();


            }

        });


        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                text1 = CID.getText().toString().trim();
                text2 = COD.getText().toString().trim();

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
                String today = formatter.format(date);

                if(checkDataEntered()){

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GuestHouse");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot keys : dataSnapshot.getChildren()) {

                                    String child = keys.getKey();
                                    if (checkDate(child, text1, text2)) {
                                        GuestHouse G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                        if (G1.checkAvailability() < totalRooms)
                                            totalRooms = G1.checkAvailability();
                                    }

                                }
                                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckAvailability.this);
                                builder.setTitle("Total No. Of Vacant Rooms : ");
                                builder.setMessage(Integer.toString(totalRooms));
                                totalRooms = count;
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
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


        String d1 = CID.getText().toString().trim();
        String d2 = COD.getText().toString().trim();
        Date date1 = new Date(), date2 = new Date();

        Date today = new Date();

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
            date1 = (Date)formatter.parse(d1);
            date2 = (Date)formatter.parse(d2);
            today = formatter.parse(today.toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (isEmpty(CID)) {
            CID.setError("Check In Date is required");
            return false;
        }
        else {

            CID.setError(null);
            if(date1.before(today) && date1.after(today) != false)
            {
                CID.setError("Check In Date should be at least today's date");
                CID.setText("");
                return false;
            }
        }

        if (isEmpty(COD)) {
            COD.setError("Check Out Date is required");
            return false;
        }
        else
            COD.setError(null);
        if(date2.after(date1) == false )
        {
            COD.setError("Check Out Date should be after Check In Date");
            COD.setText("");
            return false;
        }

        return true;
    }

    boolean checkDate(String d1, String d2, String d3)
    {

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
            Date date1 = (Date)formatter.parse(d1);
            Date date2 = (Date)formatter.parse(d2);
            Date date3 = (Date)formatter.parse(d3);

            if((date1.after(date2) && date1.before(date3)) || date1.equals(date2))
                return true;
            else
                return false;

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }
}
