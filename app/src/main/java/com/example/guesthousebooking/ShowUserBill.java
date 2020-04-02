package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShowUserBill extends AppCompatActivity {

    private DatabaseReference ref;
    private List<Bill> list = new ArrayList<>();
    ArrayAdapter<Bill> adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user_bill);


        listView = findViewById(R.id.S1);


        adapter = new ArrayAdapter<Bill>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);

        final String email = getIntent().getStringExtra("email");


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

            }

        });

        ref = FirebaseDatabase.getInstance().getReference("Bill");

        ref.addChildEventListener(new ChildEventListener() {

            final int check = 0;
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Bill bill = dataSnapshot.getValue(Bill.class);
                if(bill.getUserId().equals(email))
                    list.add(bill);
                adapter.notifyDataSetChanged();
            }

           /*  if(check == 0)
            {
                Toast.makeText(ShowUserBill.this, "No Bills exist for this User ID", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(ShowUserBill.this,AdminHome.class);
                startActivity(intent);
            }*/

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
