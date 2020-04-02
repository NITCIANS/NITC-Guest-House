package com.example.guesthousebooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminViewUser extends AppCompatActivity {


    private TextView TV2, TV4, TV6, TV8, TV10;
    private Button B1, B2;

    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_user);

        TV2 = findViewById(R.id.TV2);
        TV4 = findViewById(R.id.TV4);
        TV6 = findViewById(R.id.TV6);
        TV8 = findViewById(R.id.TV8);
        TV10 = findViewById(R.id.TV10);
        B1 = findViewById(R.id.B1);
        B2 = findViewById(R.id.B1);

        String[] UserDetails;
        UserDetails = getIntent().getStringArrayExtra("User");

        TV2.setText(UserDetails[0]);
        TV4.setText(UserDetails[1]);
        TV6.setText(UserDetails[2]);
        TV8.setText(UserDetails[3]);
        TV10.setText(UserDetails[4]);

        final User user = new User();
        user.setName(UserDetails[0]);
        user.setEmail(UserDetails[1]);
        user.setContact(UserDetails[2]);
        user.setType(UserDetails[3]);
        final String child = UserDetails[1].split("@")[0];
        ref = FirebaseDatabase.getInstance().getReference("User");

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setAccountStatus("Approved");
                ref.child(child).setValue(user);
                Toast.makeText(AdminViewUser.this, "User Registration Approved", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminViewUser.this,AdminManageRegistration.class);
                startActivity(intent);
            }
        });

        B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                user.setAccountStatus("Rejected");
                ref.child(child).setValue(user);
                Toast.makeText(AdminViewUser.this, "User Registration Rejected", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(AdminViewUser.this,AdminManageRegistration.class);
                startActivity(intent);
            }
        });










    }
}
