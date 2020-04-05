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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.guesthousebooking.AbstractGuestHouse;
import com.example.guesthousebooking.Booking;
import com.example.guesthousebooking.GuestHouse;
import com.example.guesthousebooking.Home;
import com.example.guesthousebooking.R;
import com.example.guesthousebooking.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MakeBooking extends Fragment {

    private MakeBookingViewModel mViewModel;

    private Button CIB, COB, Book;
    private EditText CID, COD;
    private Spinner number;
    private CheckBox Food;

    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    int year1 = 0, year2 = 0;
    int month1, month2;
    int day1, day2;
    Calendar calendar;

    Booking booking;
    int count = 0;
    int i, rooms;
    String userType;
    int totalRooms = 0;
    int check = 0;
    int done = 0;
    boolean entered = true;


    private DatabaseReference ref, reff, reference, getRef;


    public static MakeBooking newInstance() {
        return new MakeBooking();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {

        View V = inflater.inflate(R.layout.make_booking_fragment, container, false);



        CIB = V.findViewById(R.id.B1);
        COB= V.findViewById(R.id.B2);
        Book= V.findViewById(R.id.B3);


        CID = V.findViewById(R.id.ET1);
        COD = V.findViewById(R.id.ET2);
        CID.setEnabled(false);
        COD.setEnabled(false);

        number = V.findViewById(R.id.S1);
        Food = V.findViewById(R.id.CB);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        final String personEmail = acct.getEmail();



        booking = new Booking();
        ref = FirebaseDatabase.getInstance().getReference("Booking");
        reff = FirebaseDatabase.getInstance().getReference("GuestHouse");
        reference = FirebaseDatabase.getInstance().getReference("User");
        getRef = FirebaseDatabase.getInstance().getReference("Room");
        getRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                totalRooms = count;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.hasChild(personEmail.split("@")[0]))
                {
                    User U = dataSnapshot.child(personEmail.split("@")[0]).getValue(User.class);
                    userType = U.getType();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                            final String CIDate = CID.getText().toString().trim();
                            final String CODate = COD.getText().toString().trim();

                            booking.setCheckInDate(CIDate);
                            booking.setCheckOutDate(CODate);
                            booking.setUserId(personEmail);
                            booking.setType(userType);

                            booking.setBookingDate(today);

                            if(Food.isChecked())
                                booking.setFoodServices(true);
                            else
                                booking.setFoodServices(false);

                            booking.setBookingStatus("Pending");
                            booking.setBookingId(count + 1);
                            rooms = Integer.parseInt(number.getSelectedItem().toString().trim());


                            reff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (entered) {
                                        check = 0;
                                        for (DataSnapshot keys : dataSnapshot.getChildren()) {

                                            String child = keys.getKey();
                                            if (checkDate(child, CIDate, CODate)) {
                                                GuestHouse G1 = dataSnapshot.child(child).getValue(GuestHouse.class);
                                                if (G1.checkAvailability() < rooms)
                                                    check = 1;
                                            }

                                        }
                                        if (check == 0) {
                                            if (userType.equals("Director") || userType.equals("Registrar")) {
                                                booking.setBookingStatus("Confirmed");
                                                booking.sendMail(getActivity(), 1);
                                                Toast.makeText(getActivity(), "You made a Booking.. Check Email for more details..", Toast.LENGTH_LONG).show();
                                                ref.child(Integer.toString(count + 1)).setValue(booking);
                                                entered = false;
                                                createEntries(CIDate, CODate);
                                            } else if (userType.equals("Faculty") || userType.equals("Staff") || userType.equals("SAC Member")) {
                                                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
                                                Date currentDate = new Date();
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(currentDate);
                                                c.add(Calendar.DAY_OF_MONTH, 3);
                                                Date currentDatePlusThree = c.getTime();

                                                try {
                                                    Date date1 = (Date) formatter.parse(CIDate);
                                                    String dates = formatter.format(currentDatePlusThree);
                                                    Date newDate = (Date) formatter.parse(dates);

                                                    if (newDate.after(date1)) {
                                                        booking.setBookingStatus("Confirmed");
                                                        booking.sendMail(getActivity(), 1);
                                                        Toast.makeText(getActivity(), "You made a Booking.. Check Email for more details", Toast.LENGTH_LONG).show();
                                                        ref.child(Integer.toString(count + 1)).setValue(booking);
                                                        entered = false;
                                                        createEntries(CIDate, CODate);
                                                    } else {
                                                        booking.setBookingStatus("Pending");
                                                        booking.sendMail(getActivity(), 3);
                                                        Toast.makeText(getActivity(), "You made a Booking.. Check Email for more details..", Toast.LENGTH_LONG).show();
                                                        ref.child(Integer.toString(count + 1)).setValue(booking);
                                                        entered = false;
                                                        Intent intent = new Intent(getActivity(), Home.class);
                                                        startActivity(intent);
                                                    }
                                                } catch (ParseException e) {
                                                    Toast.makeText(getActivity(), "Error : " + e, Toast.LENGTH_LONG).show();


                                                }


                                            } else {

                                                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
                                                Date currentDate = new Date();
                                                Calendar c = Calendar.getInstance();
                                                c.setTime(currentDate);
                                                c.add(Calendar.DAY_OF_MONTH, 1);
                                                Date currentDatePlusOne = c.getTime();

                                                try {
                                                    Date date1 = (Date) formatter.parse(CIDate);
                                                    String dates = formatter.format(currentDatePlusOne);
                                                    Date newDate = (Date) formatter.parse(dates);

                                                    if (newDate.after(date1)) {
                                                        booking.setBookingStatus("Confirmed");
                                                        booking.sendMail(getActivity(), 1);
                                                        Toast.makeText(getActivity(), "You made a Booking.. Check Email for more details", Toast.LENGTH_LONG).show();
                                                        ref.child(Integer.toString(count + 1)).setValue(booking);
                                                        createEntries(CIDate, CODate);
                                                        entered = false;
                                                    } else {
                                                        booking.setBookingStatus("Pending");
                                                        booking.sendMail(getActivity(), 3);
                                                        Toast.makeText(getActivity(), "You made a Booking.. Check Email for more details..", Toast.LENGTH_LONG).show();
                                                        ref.child(Integer.toString(count + 1)).setValue(booking);
                                                        entered = false;
                                                        Intent intent = new Intent(getActivity(), Home.class);
                                                        startActivity(intent);
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                        } else {
                                            Toast.makeText(getActivity(), "Rooms not available, Please check availability for your query first..", Toast.LENGTH_LONG).show();
                                        }

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
                                CID.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
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
                                COD.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);


                            }
                        }, year2,month2,day2);

                datePickerDialog2.show();


            }

        });
        return V;

    }

    void createEntries(final String d1, final String d2)
    {

        reff = FirebaseDatabase.getInstance().getReference("GuestHouse");
        reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (done == 0) {

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
                                done = 1;
                                Intent intent = new Intent(getActivity(), Home.class);
                                startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Error : " + e, Toast.LENGTH_LONG).show();
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

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



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MakeBookingViewModel.class);
        // TODO: Use the ViewModel
    }


}
