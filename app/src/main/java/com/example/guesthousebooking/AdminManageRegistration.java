package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class AdminManageRegistration extends AppCompatActivity {

    private List<User> list = new ArrayList<>();
    ArrayAdapter<User> adapter;
    private DatabaseReference ref;
    private ListView listView;
    //private Button view;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_registration);

        listView = findViewById(R.id.S1);
        //view = findViewById(R.id.B1);
        adapter = new ArrayAdapter<User>(this, android.R.layout.simple_list_item_1, list);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Intent intent = new Intent(AdminManageRegistration.this,AdminViewUser.class);
                User user = new User();
                user = list.get(position);
                String[] details = new String[5];
                details[0] = user.getName();
                details[1] = user.getEmail();
                details[2] = user.getContact();
                details[3] = user.getType();
                details[4] = user.getAccountStatus();
                intent.putExtra("User", details);
                startActivity(intent);
            }

        });

        ref = FirebaseDatabase.getInstance().getReference("User");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                User user = dataSnapshot.getValue(User.class);
                if(user.getAccountStatus().equals("Pending"))
                    list.add(user);
                adapter.notifyDataSetChanged();
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
