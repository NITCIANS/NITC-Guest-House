package com.example.guesthousebooking;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminHome extends AppCompatActivity {


    Button showUserBill, showUserBooking, changePassword;
    EditText ET1;
    private String userid = "";
    private String password, newpassword;
    int retry = 0;
    DatabaseReference reff;
    Admin A;
    int change = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        showUserBooking = findViewById(R.id.B5);
        showUserBill = findViewById(R.id.B6);
        changePassword = findViewById(R.id.B11);


        showUserBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminHome.this);
                builder.setTitle("Enter User's Email ID:");

                final EditText input = new EditText(AdminHome.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);



                builder.setPositiveButton("Show", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        userid = input.getText().toString();
                        if(isEmpty(input))
                            Toast.makeText(AdminHome.this, "Empty Field", Toast.LENGTH_LONG).show();


                        else if(!isEmailValid(userid))
                            Toast.makeText(AdminHome.this, "Invalid Email Type", Toast.LENGTH_LONG).show();

                        else {

                            Intent intent = new Intent(AdminHome.this, ShowUserBooking.class);
                            intent.putExtra("email", userid);
                            startActivity(intent);
                        }

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


        changePassword.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {

                change = 0;
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminHome.this);
                builder.setTitle("Enter Old Password :");

                final EditText input = new EditText(AdminHome.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT |
                        InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        password = input.getText().toString();
                        if(isEmpty(input))
                            Toast.makeText(AdminHome.this, "Empty Field", Toast.LENGTH_LONG).show();
                        else
                        {
                            reff = FirebaseDatabase.getInstance().getReference("Admin");
                            reff.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    A = dataSnapshot.child("1").getValue(Admin.class);
                                    if(A.getPassword().equals(password) && change == 0)
                                    {
                                        change = 1;
                                        final AlertDialog.Builder builder1 = new AlertDialog.Builder(AdminHome.this);
                                        builder1.setTitle("Enter New Password :");

                                        final EditText input1 = new EditText(AdminHome.this);
                                        input1.setInputType(InputType.TYPE_CLASS_TEXT |
                                                InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        builder1.setView(input1);
                                        builder1.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                if(isEmpty(input1))
                                                    Toast.makeText(AdminHome.this, "Empty Field", Toast.LENGTH_LONG).show();
                                                else if(input1.getText().toString().equals(password))
                                                {
                                                    Toast.makeText(AdminHome.this, "You're using your Old Password..", Toast.LENGTH_LONG).show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                                else
                                                {
                                                        password = input1.getText().toString();
                                                        final AlertDialog.Builder builder2 = new AlertDialog.Builder(AdminHome.this);
                                                        builder2.setTitle("Confirm New Password :");

                                                        final EditText input2 = new EditText(AdminHome.this);
                                                        input2.setInputType(InputType.TYPE_CLASS_TEXT |
                                                                InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                                        builder2.setView(input2);
                                                        builder2.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if (isEmpty(input1))
                                                                    Toast.makeText(AdminHome.this, "Empty Field", Toast.LENGTH_LONG).show();
                                                                else {
                                                                    newpassword = input2.getText().toString();
                                                                    if (password.equals(newpassword)) {
                                                                        A.setPassword(password);
                                                                        reff.child("1").setValue(A);
                                                                        change = 1;
                                                                        Toast.makeText(AdminHome.this, "Password Successfully Changed..", Toast.LENGTH_LONG).show();
                                                                        finish();
                                                                        startActivity(getIntent());

                                                                    }
                                                                    else
                                                                    {
                                                                        Toast.makeText(AdminHome.this, "Password Mismatch..", Toast.LENGTH_LONG).show();
                                                                        change = 1;

                                                                    }

                                                                }


                                                            }
                                                        });
                                                        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                                        builder2.show();

                                                }

                                            }
                                        });
                                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        builder1.show();


                                    }
                                    else if(change == 0)
                                    {
                                        Toast.makeText(AdminHome.this, "Password Mismatch..", Toast.LENGTH_LONG).show();
                                        change = 1;
                                        finish();
                                        startActivity(getIntent());
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

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




        showUserBill.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminHome.this);
                builder.setTitle("Enter User's Email ID:");

                final EditText input = new EditText(AdminHome.this);

                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(input);



                builder.setPositiveButton("Show", new DialogInterface.OnClickListener() {


                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        userid = input.getText().toString();
                        if(isEmpty(input))
                            Toast.makeText(AdminHome.this, "Empty Field", Toast.LENGTH_LONG).show();


                        else if(!isEmailValid(userid))
                            Toast.makeText(AdminHome.this, "Invalid Email Type", Toast.LENGTH_LONG).show();

                        else {

                            Intent intent = new Intent(AdminHome.this, ShowUserBill.class);
                            intent.putExtra("email", userid);
                            startActivity(intent);
                        }

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


    public static boolean isEmailValid(String email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    public void checkIn(View view)
    {
        Intent intent = new Intent(this,CheckInOut.class);
        startActivity(intent);

    }

    public void regManage(View view)
    {
        Intent intent = new Intent(this,AdminManageRegistration.class);
        startActivity(intent);

    }

    public void showAllBill(View view)
    {
        Intent intent = new Intent(this,AdminShowBill.class);
        startActivity(intent);
    }

    public void showAllBoking(View view)
    {
        Intent intent = new Intent(this,AdminShowBooking.class);
        startActivity(intent);
    }

    public void manageBooking(View view)
    {
        Intent intent = new Intent(this,ManageBookings.class);
        startActivity(intent);
    }
    public void checkAvailability(View view)
    {
        Intent intent = new Intent(this, AdminCheckAvailability.class);
        startActivity(intent);
    }

    public void showRoom(View view)
    {
        Intent intent = new Intent(AdminHome.this, ShowRoom.class);
        startActivity(intent);
    }

    public void logout(View view)
    {
        SaveSharedPreference.setLoggedIn(getApplicationContext(), false);
        Intent intent = new Intent(AdminHome.this, AdminLogin.class);
        startActivity(intent);
    }

}
