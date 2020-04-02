package com.example.guesthousebooking.ui.checkavailability;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.guesthousebooking.AbstractGuestHouse;
import com.example.guesthousebooking.GuestHouse;
import com.example.guesthousebooking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CheckAvailability extends Fragment {

    private CheckAvailabilityViewModel mViewModel;
    DatePickerDialog datePickerDialog1;
    Button Check, Click;
    EditText AD;
    int year = 0;
    int month;
    int day;
    Calendar calendar;
    int vacant = 10;


    public static CheckAvailability newInstance() {
        return new CheckAvailability();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.check_availability_fragment, container, false);


        Click = V.findViewById(R.id.B1);
        Check = V.findViewById(R.id.B1);
        AD = V.findViewById(R.id.ET1);
        AD.setEnabled(false);

        Check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                final String text = AD.getText().toString().trim();

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
                String today = formatter.format(date);

                if(checkDate(today, text)) {


                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference ref = rootRef.child("GuestHouse");
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot keys : dataSnapshot.getChildren()) {
                                if (keys.getKey().equals(text)) {
                                    AbstractGuestHouse G1 = dataSnapshot.child(text).getValue(GuestHouse.class);
                                    vacant = G1.checkAvailability();
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Total No. Of Vacant Rooms:");

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();

                                        }

                                    });
                                    builder.setMessage(Integer.toString(vacant));
                                    builder.show();
                                } else {
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Total No. Of Vacant Rooms:");

                                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            dialog.cancel();

                                        }

                                    });
                                    builder.setMessage(Integer.toString(vacant));
                                    builder.show();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Don't ignore errors!
                        }
                    };
                    ref.addListenerForSingleValueEvent(valueEventListener);
                }
                else
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Please Select the upcoming dates");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();

                        }

                    });
                    builder.show();

                }

            }
        });

        Click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog1 = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                AD.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
                            }
                        }, year,month,day);
                datePickerDialog1.show();
            }
        });







        return V;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CheckAvailabilityViewModel.class);
        // TODO: Use the ViewModel
    }

    boolean checkDate(String date1, String date2)
    {
        String[] arr1 = (date1.split("-"));
        String[] arr2 = (date2.split("-"));
        if(Integer.parseInt(arr1[2]) > Integer.parseInt(arr2[2]) )
            return false;
        if(Integer.parseInt(arr1[1]) > Integer.parseInt(arr2[1]) )
            return false;

        if(Integer.parseInt(arr1[0]) > Integer.parseInt(arr2[0])  && Integer.parseInt(arr1[1]) <= Integer.parseInt(arr2[1]))
            return false;
        return true;


    }

}
