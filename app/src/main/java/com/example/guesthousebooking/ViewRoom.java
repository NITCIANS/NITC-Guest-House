package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewRoom extends AppCompatActivity {


    private TextView roomId, price, AC, status;
    DatabaseReference ref;
    String[] details;
    private Button changeStatus;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_room);

        //changeStatus = findViewById(R.id.B1);
        roomId = findViewById(R.id.T3);
        price = findViewById(R.id.T4);
        AC = findViewById(R.id.T6);
        status = findViewById(R.id.T8);
        changeStatus = findViewById(R.id.B2);

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

        ref = FirebaseDatabase.getInstance().getReference("Room");
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

                                    if (dataSnapshot.hasChild(details[0]) && check == 0)
                                    {
                                        Room R = dataSnapshot.child(details[0]).getValue(Room.class);
                                        R.changeRoomStatus();
                                        ref.child(details[0]).setValue(R);
                                        Toast.makeText(ViewRoom.this, "Status Changed Successfully.." , Toast.LENGTH_LONG).show();
                                        finish();
                                        Intent intent = new Intent(ViewRoom.this, ShowRoom.class);
                                        startActivity(intent);
                                        check = 1;
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
        });


    }
}
