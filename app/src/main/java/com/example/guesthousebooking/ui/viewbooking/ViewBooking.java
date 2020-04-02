package com.example.guesthousebooking.ui.viewbooking;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import com.example.guesthousebooking.Booking;
import com.example.guesthousebooking.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ViewBooking extends Fragment {

    private ViewBookingViewModel mViewModel;

    private List<String> list = new ArrayList<>();

    RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private DatabaseReference ref;


    public static ViewBooking newInstance() {
        return new ViewBooking();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View V =  inflater.inflate(R.layout.view_booking_fragment, container, false);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        String userId= acct.getEmail();
        //adapter = new RecyclerView.Adapter<Booking>(getActivity(), android.R.layout.simple_list_item_1, list );

        recyclerView = V.findViewById(R.id.bookings);

        recyclerView.setAdapter(adapter);

        ref = FirebaseDatabase.getInstance().getReference("Booking").child(userId);


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                for(DataSnapshot keys : dataSnapshot.getChildren()) {
                    Booking booking = dataSnapshot.getValue(Booking.class);
                    //list.add(booking);
                    adapter.notifyDataSetChanged();
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




        return V;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ViewBookingViewModel.class);
        // TODO: Use the ViewModel
    }

}
