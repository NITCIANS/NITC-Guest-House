package com.example.guesthousebooking.ui.makebooking;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.guesthousebooking.Booking;
import com.example.guesthousebooking.GuestHouse;
import com.example.guesthousebooking.Home;
import com.example.guesthousebooking.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MakeBooking extends Fragment {

    private MakeBookingViewModel mViewModel;

    Button CIB, COB, Book;
    EditText CI, CO;
    Spinner number;

    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    int year1 = 0, year2 = 0;
    int month1, month2;
    int day1, day2;
    Calendar calendar;

    Booking booking;
    int count = 0;
    int i, rooms;
    String[] arr1, arr2;
    String child;
    int check = 0;


    private DatabaseReference ref, reff;


    public static MakeBooking newInstance() {
        return new MakeBooking();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        View V = inflater.inflate(R.layout.make_booking_fragment, container, false);


        Book= V.findViewById(R.id.B1);
        CIB = V.findViewById(R.id.B1);
        COB= V.findViewById(R.id.B1);
        CI = V.findViewById(R.id.ET1);
        CO = V.findViewById(R.id.ET2);
        number = V.findViewById(R.id.S1);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        final String personEmail = acct.getEmail();

        booking = new Booking();
        ref = FirebaseDatabase.getInstance().getReference("Booking");
        reff = FirebaseDatabase.getInstance().getReference("GuestHouse");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                count = (int) dataSnapshot.getChildrenCount();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Are You Sure ?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(checkDataEntered()) {

                            booking.setNoOfRooms(Integer.parseInt(number.getSelectedItem().toString().trim()));

                            Date date = new Date();
                            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                            String today = formatter.format(date);
                            String CIDate = CI.getText().toString().trim();
                            String CODate = CO.getText().toString().trim();

                            arr1 = CIDate.split("-");
                            arr2 = CODate.split("-");

                            CIDate = CIDate + " 10:00:00";
                            CODate = CODate + " 10:00:00";


                            booking.setCheckInDate(CIDate);
                            booking.setCheckOutDate(CODate);
                            booking.setUserId(personEmail);

                            booking.setBookingDate(today);
                            booking.setFoodServices(false);
                            booking.setBookingStatus("Pending");
                            booking.setBookingId(count + 1);



                            day1 = Integer.parseInt(arr1[0]);
                            day2 = Integer.parseInt(arr2[0]);

                            rooms = Integer.parseInt(number.getSelectedItem().toString().trim());

                            i = day1;
                            month1 = Integer.parseInt(arr1[1]);
                            month2 = Integer.parseInt(arr1[1]);
                            year1 = Integer.parseInt(arr1[2]);
                            year2 = Integer.parseInt(arr2[2]);

                            reff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    check = 0;
                                    for(DataSnapshot keys : dataSnapshot.getChildren()) {


                                        child = Integer.toString(i) + "-" + arr1[1] + "-" + arr1[2];
                                        if(keys.getKey().equals(child)) {
                                            GuestHouse G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                            if(G1.checkAvailability() < rooms) {
                                                Toast.makeText(getActivity(), "Rooms Available on : " + child + "is " + G1.checkAvailability(), Toast.LENGTH_LONG).show();
                                                check = 1;
                                            }
                                            if(i == day2 - 1)
                                                break;
                                            i = i + 1;
                                        }
                                    }

                                    if(check == 0) {
                                        ref.child(Integer.toString(count + 1)).setValue(booking);
                                        Toast.makeText(getActivity(), "You made a Booking.", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getActivity(), Home.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(getActivity(), "Rooms not available, Please check availability for all days and try again..", Toast.LENGTH_LONG).show();
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

        CIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year1 = calendar.get(Calendar.YEAR);
                month1 = calendar.get(Calendar.MONTH);
                day1 = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog1 = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                CI.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
                            }
                        }, year1,month1,day1);
                datePickerDialog1.show();
            }
        });

        COB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year2 = calendar.get(Calendar.YEAR);
                month2 = calendar.get(Calendar.MONTH);
                day2 = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog2 = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                CO.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);


                            }
                        }, year2,month2,day2);

                datePickerDialog2.show();


            }

        });



        return V;

    }


    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered()
    {

        if (isEmpty(CI)){
            CI.setError("Check In Date is required");
            return false;
        }

        if (isEmpty(CO)) {
            CO.setError("Check Out Date is required");
            return false;
        }

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        String today = formatter.format(date);
        String CID = CI.getText().toString().trim();
        String COD = CO.getText().toString().trim();

        String[] arr1 = (today.split("-"));
        String[] arr2 = (CID.split("-"));
        String[] arr3 = (COD.split("-"));

        if(Integer.parseInt(arr1[2]) > Integer.parseInt(arr2[2]) )
        {
            CI.setError("Invalid Check In Date");
            CI.setText("");
            return false;
        }
        if(Integer.parseInt(arr1[1]) > Integer.parseInt(arr2[1]) )
        {
            CI.setError("Invalid Check In Date");
            CI.setText("");
            return false;
        }

        if(Integer.parseInt(arr1[0]) > Integer.parseInt(arr2[0])  && Integer.parseInt(arr1[1]) <= Integer.parseInt(arr2[1]))
        {
            CI.setError("Invalid Check In Date");
            CI.setText("");
            return false;
        }



        if(Integer.parseInt(arr2[2]) > Integer.parseInt(arr3[2]) )
        {
            CO.setError("Invalid Check Out Date");
            CO.setText("");
            return false;
        }
        if(Integer.parseInt(arr2[1]) > Integer.parseInt(arr3[1]) )
        {
            CO.setError("Invalid Check Out Date");
            CO.setText("");
            return false;
        }


        if(Integer.parseInt(arr2[0]) > Integer.parseInt(arr3[0])  && Integer.parseInt(arr2[1]) <= Integer.parseInt(arr3[1]))
        {
            CO.setError("Invalid Check Out Date");
            CO.setText("");
            return false;
        }








        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        String checkInDate = CI.getText().toString().trim();
        String checkOutDate = CO.getText().toString().trim();
        try
        {
            if(sdf.parse(checkInDate).before(sdf.parse(checkOutDate))) {
                CO.setError("Invalid Check Out Date");
                CO.setText("");
                return false;
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }*/


        return true;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MakeBookingViewModel.class);
        // TODO: Use the ViewModel
    }


}
