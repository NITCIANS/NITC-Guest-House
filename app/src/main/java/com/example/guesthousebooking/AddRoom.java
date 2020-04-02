package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class AddRoom extends AppCompatActivity {


    private Button addRoom;
    private EditText roomNo, price;
    private CheckBox AC;
    DatabaseReference ref;
    int count = 0;
    int check = 0;
    int roomID;

    Room R1 = new Room();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        addRoom = findViewById(R.id.B1);
        roomNo = findViewById(R.id.ET1);
        price = findViewById(R.id.ET2);
        AC = findViewById(R.id.CB);
        ref = FirebaseDatabase.getInstance().getReference("Room");


        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkDataEntered()) {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(AddRoom.this);
                    builder.setTitle("Are You Sure ?");
                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            roomID = Integer.parseInt(roomNo.getText().toString().trim());
                            float amount = Float.parseFloat(price.getText().toString().trim());
                            R1.setRoomId(roomID);
                            R1.setPrice(amount);
                            R1.setRoomStatus(false);
                            if(AC.isSelected())
                                R1.setAC(true);
                            else
                               R1.setAC(false);

                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.hasChild(Integer.toString(roomID)) && check == 0)
                                    {
                                        check = 0;
                                        Toast.makeText(AddRoom.this, "Room No. already exists..", Toast.LENGTH_SHORT).show();
                                    }
                                    else if(check == 0)
                                    {
                                        check = 1;
                                        ref.child(Integer.toString(roomID)).setValue(R1);
                                        Toast.makeText(AddRoom.this, "Room added successfully..", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddRoom.this, ShowRoom.class);
                                        startActivity(intent);
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
            }
        });

    }


    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered()
    {

        if (isEmpty(roomNo)) {
            roomNo.setError("Room No. is required!");
            return false;
        }

        if (isEmpty(price)) {
            price.setError("Price is required!");
            return false;
        }

        try {
            Double num = Double.parseDouble(price.getText().toString().trim());
            return true;
        } catch (NumberFormatException e) {
            price.setError("Invalid Price!");
            return false;
        }
    }
}