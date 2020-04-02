package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewRoom extends AppCompatActivity {


    private TextView roomId, price, AC, status;
    DatabaseReference ref;
    String[] details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);

        //changeStatus = findViewById(R.id.B1);
        roomId = findViewById(R.id.T2);
        price = findViewById(R.id.T4);
        AC = findViewById(R.id.T6);
        status = findViewById(R.id.T8);

        details = getIntent().getStringArrayExtra("Details");

        roomId.setText(details[0]);
        price.setText(details[1]);
        if(details[2].equals("True"))
            AC.setText("Yes");
        else
            AC.setText("No");

        if(details[3].equals("True"))
            status.setText("Occupied");
        else
            status.setText("Free");
       /* ref = FirebaseDatabase.getInstance().getReference("Room");
        changeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ViewRoom.this);
                builder.setTitle("Are You Sure ?");
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int check = 0;

                                for(DataSnapshot keys : dataSnapshot.getChildren()) {
                                    if(keys.getKey().equals(details[0])) {
                                        Room R = dataSnapshot.child(keys.getKey()).getValue(Room.class);
                                        if(check == 0) {
                                            R.changeRoomStatus();
                                            ref.child(details[0]).setValue(R);
                                            check = 1;
                                            Intent intent = new Intent(ViewRoom.this, ShowRoom.class);
                                            startActivity(intent);
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {


                            }
                        });


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
        });*/


    }
}
