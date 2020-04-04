package com.example.guesthousebooking.ui.cancelbooking;

import androidx.lifecycle.ViewModelProviders;

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
import android.widget.EditText;
import android.widget.Toast;

import com.example.guesthousebooking.Bill;
import com.example.guesthousebooking.Booking;
import com.example.guesthousebooking.Home;
import com.example.guesthousebooking.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CancelBooking extends Fragment {

    private CancelBookingViewModel mViewModel;

    Button Cancel;
    EditText BID;
    private DatabaseReference ref, reff;

    public static CancelBooking newInstance() {
        return new CancelBooking();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View V = inflater.inflate(R.layout.cancel_booking_fragment, container, false);


        Cancel = V.findViewById(R.id.B2);
        BID = V.findViewById(R.id.ET1);





        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkDataEntered())
                {
                    final String ID = BID.getText().toString().trim();

                    ref = FirebaseDatabase.getInstance().getReference("Booking");

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                        {

                            int check = 0;
                            Booking booking = new Booking();
                            for(DataSnapshot keys : dataSnapshot.getChildren())
                            {
                                if(keys.getKey().equals(ID))
                                {
                                    booking = dataSnapshot.child(keys.getKey()).getValue(Booking.class);
                                    check = 1;
                                    break;
                                }
                            }
                            if(check == 0)
                                Toast.makeText(getActivity(), "Invalid Booking Id", Toast.LENGTH_LONG).show();
                            else
                            {
                                String bookingStatus = booking.getBookingStatus();

                                if(bookingStatus.equals("Cancelled"))
                                {
                                    Toast.makeText(getActivity(), "Booking is already Cancelled", Toast.LENGTH_LONG).show();
                                }
                                else if(bookingStatus.equals("Pending"))
                                {
                                    booking.setBookingStatus("Cancelled");
                                    ref.child(ID).setValue(booking);
                                    Toast.makeText(getActivity(), "Booking Cancelled", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), Home.class);
                                    startActivity(intent);
                                }
                                else
                                {
                                    String userId = booking.getUserId();
                                    float amount = (float) 0.50 * booking.calculatePrice();
                                    booking.setBookingStatus("Cancelled");
                                    Bill bill = new Bill();
                                    bill.setAmount(amount);
                                    bill.setBillId(Integer.parseInt(ID));
                                    bill.setUserId(userId);
                                    bill.setCheckInDate(booking.getCheckInDate().split(" ")[0]);
                                    reff = FirebaseDatabase.getInstance().getReference("Bill");
                                    reff.child(ID).setValue(bill);
                                    ref.child(ID).setValue(booking);
                                    Toast.makeText(getActivity(), "Booking Cancelled and 50% of your Bill has been added to your dues", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), Home.class);
                                    startActivity(intent);
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



        return V;
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered() {

        if (isEmpty(BID)) {
            BID.setError("Booking ID required");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CancelBookingViewModel.class);
        // TODO: Use the ViewModel
    }

}
