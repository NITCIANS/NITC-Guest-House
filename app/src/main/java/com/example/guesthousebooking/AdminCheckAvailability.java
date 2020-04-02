package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    int vacant = 10;
    int min = 10;
    int show = 0;
    int i;
    String child, text1, text2;

    String[] arr1, arr2;

    GuestHouse G1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_check_availability);

        Check = findViewById(R.id.B3);
        Click1 = findViewById(R.id.B1);
        Click2 = findViewById(R.id.B2);
        CID = findViewById(R.id.ET1);
        COD = findViewById(R.id.ET2);

        CID.setEnabled(false);
        COD.setEnabled(false);










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

                if(checkDate(today, text1 ) && checkDate(text1, text2)){


                    arr1 = text1.split("-");
                    arr2 = text2.split("-");
                    day1 = Integer.parseInt(arr1[0]);
                    day2 = Integer.parseInt(arr2[0]);
                    month1 = Integer.parseInt(arr1[1]);
                    month2 = Integer.parseInt(arr2[1]);
                    year1 = Integer.parseInt(arr1[2]);
                    year2 = Integer.parseInt(arr2[2]);

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("GuestHouse");
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (i = day1; i < day2; i++) {

                                    child = Integer.toString(i) + "-" + arr1[1] + "-" + arr1[2];

                                    if (dataSnapshot.hasChild(child)) {

                                        G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                        vacant = G1.checkAvailability();
                                        if (vacant < min)
                                            min = vacant;

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

                            final AlertDialog.Builder builder = new AlertDialog.Builder(AdminCheckAvailability.this);
                            builder.setTitle("Total No. Of Vacant Rooms:");
                            builder.setMessage(Integer.toString(min));

                            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    min = 10;

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
                else
                {
                    Toast.makeText(AdminCheckAvailability.this, "Invalid Dates, Please retry..", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    boolean checkDate(String date1, String date2)
    {
       /* String[] arr1 = (date1.split("-"));
        String[] arr2 = (date2.split("-"));
        if(Integer.parseInt(arr1[2]) > Integer.parseInt(arr2[2]) )
            return false;
        if(Integer.parseInt(arr1[1]) > Integer.parseInt(arr2[1]) )
            return false;

        if(Integer.parseInt(arr1[0]) > Integer.parseInt(arr2[0])  && Integer.parseInt(arr1[1]) <= Integer.parseInt(arr2[1]))
            return false;*/
        return true;


    }
}
