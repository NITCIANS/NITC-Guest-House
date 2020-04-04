package com.example.guesthousebooking;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdminHome extends AppCompatActivity {


    Button showUserBill, showUserBooking, B1;
    EditText ET1;
    private String userid = "";
    int occupied = 0;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Intent intent=getIntent();


        showUserBooking = findViewById(R.id.B5);
        showUserBill = findViewById(R.id.B6);


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
                          //  intent.putExtra("email", userid);
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

        showUserBill = findViewById(R.id.B6);
        getIntent();


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
      //  Intent intent = new Intent(this,AdminShowBill.class);
       // startActivity(intent);
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
        Intent intent = new Intent(AdminHome.this, AdminLogin.class);
        startActivity(intent);
    }

}
