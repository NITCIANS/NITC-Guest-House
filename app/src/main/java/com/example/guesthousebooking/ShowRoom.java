package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ShowRoom extends AppCompatActivity {


    private Button addRoom;
    private ListView allRooms;
    private TextView totalRooms, occupiedRooms;
    int total = 0;
    int occ = 0;
    DatabaseReference ref;

    private List<Room> list = new ArrayList<>();
    ArrayAdapter<Room> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_room);


        addRoom = findViewById(R.id.B1);
        totalRooms = findViewById(R.id.T3);
        occupiedRooms = findViewById(R.id.T4);
        allRooms = findViewById(R.id.LV1);

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowRoom.this, AddRoom.class);
                startActivity(intent);

            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Room");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                total = (int) dataSnapshot.getChildrenCount();
                for(DataSnapshot keys : dataSnapshot.getChildren()) {
                    Room R = dataSnapshot.child(keys.getKey()).getValue(Room.class);
                    if (R.isRoomStatus())
                        occ = occ + 1;
                }
                totalRooms.setText(Integer.toString(total));
                occupiedRooms.setText(Integer.toString(occ));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       allRooms = findViewById(R.id.LV1);
        adapter = new ArrayAdapter<Room>(ShowRoom.this, android.R.layout.simple_list_item_1, list);
        allRooms.setAdapter(adapter);



        ref = FirebaseDatabase.getInstance().getReference("Room");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Room R = dataSnapshot.getValue(Room.class);
                list.add(R);
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

        allRooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String[] details = new String[4];
                Room R1 = list.get(position);
                details[0] = Integer.toString(R1.getRoomId());
                details[1] = Float.toString(R1.getPrice());
                if(R1.isAC())
                    details[2] = "True";
                else
                    details[2] = "False";

                if(R1.isRoomStatus())
                    details[3] = "True";
                else
                    details[3] = "False";


                Intent intent = new Intent(ShowRoom.this,ViewRoom.class);
                intent.putExtra("Details", details);
                startActivity(intent);

            }
        });

    }
}
