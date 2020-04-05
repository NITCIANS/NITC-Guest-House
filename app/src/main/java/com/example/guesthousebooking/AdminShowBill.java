package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminShowBill extends AppCompatActivity {




    DatePickerDialog datePickerDialog1;
    DatePickerDialog datePickerDialog2;

    Button Download, Show, Click1, Click2;
    EditText From, To;

    int year1, year2;
    int month1, month2;
    int day1, day2;
    Calendar calendar;

    Booking booking;
    Bill bill;
    String bookingId;

    String[] arr1, arr2;
    String F, T;

    private DatabaseReference ref;
    private List<Bill> list = new ArrayList<>();
    ArrayAdapter<Bill> adapter;
    private ListView listView;

    int child = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_show_bill);
        Download = findViewById(R.id.B3);
        Show = findViewById(R.id.B4);
        Click1 = findViewById(R.id.B1);
        Click2 = findViewById(R.id.B2);
        From = findViewById(R.id.ET1);
        To = findViewById(R.id.ET2);

        From.setEnabled(false);
        To.setEnabled(false);
        Download.setVisibility(View.GONE);


        Click1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year1 = calendar.get(Calendar.YEAR);
                month1 = calendar.get(Calendar.MONTH);
                day1 = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog1 = new DatePickerDialog(AdminShowBill.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                From.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
                            }
                        }, year1,month1,day1);
                datePickerDialog1.show();
            }
        });

        Click2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                calendar = Calendar.getInstance();
                year2 = calendar.get(Calendar.YEAR);
                month2 = calendar.get(Calendar.MONTH);
                day2 = calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog2 = new DatePickerDialog(AdminShowBill.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
                            {
                                To.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);


                            }
                        }, year2,month2,day2);

                datePickerDialog2.show();


            }

        });

        Download.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                PdfDocument myPdfDocument = new PdfDocument();
                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
                PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);

                Paint myPaint = new Paint();

                String myString = "Bills From : " + F + " To : " + T + "\n\n";
                int i = 1;
                for( Bill B : list) {

                    myString += i++ + " :- \n\n";
                    myString += B;
                    myString += "\n\n";

                }
                ActivityCompat.requestPermissions(AdminShowBill.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
                int x = 10, y=25;

                for (String line: myString.split("\n"))
                {
                    myPage.getCanvas().drawText(line, x, y, myPaint);
                    y += myPaint.descent()-myPaint.ascent();
                }
                myPdfDocument.finishPage(myPage);
                try
                {
                    File file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS );
                    if(!file.exists())
                        file.mkdir();
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date date = new Date();
                    String filename = date.toString();
                    file = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS + "/" + filename + ".pdf");
                    myPdfDocument.writeTo(new FileOutputStream(file));
                    myPdfDocument.close();
                    Toast.makeText(AdminShowBill.this, filename + " Downloaded", Toast.LENGTH_LONG).show();
                }
                catch (Exception e){

                    Toast.makeText(AdminShowBill.this, "Error name " + e, Toast.LENGTH_LONG).show();

                }



            }
        });

        Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                list.clear();
                Download.setVisibility(View.GONE);
                child = 0;
                if(checkDataEntered()) {

                    F = From.getText().toString().trim();
                    T = To.getText().toString().trim();

                    listView = findViewById(R.id.LV);

                    adapter = new ArrayAdapter<Bill>(AdminShowBill.this, android.R.layout.simple_list_item_1, list);

                    listView.setAdapter(adapter);

                    ref = FirebaseDatabase.getInstance().getReference("Bill");

                    ref.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            bill = dataSnapshot.getValue(Bill.class);
                            String arr = bill.getCheckInDate().split(" ")[0];
                            if (checkConditions(arr, F, T)) {
                                list.add(bill);
                                adapter.notifyDataSetChanged();
                                Download.setVisibility(View.VISIBLE);
                                child = 1;
                            }
                            if (child == 0) {
                                Toast.makeText(AdminShowBill.this, "Sorry No Bills found between respective dates..", Toast.LENGTH_LONG).show();
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


                }
            }
        });


    }

    boolean checkConditions(String arr, String arr1, String arr2)
    {

        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
        try {

            Date date1 = (Date)formatter.parse(arr);
            Date date2 = (Date)formatter.parse(arr1);
            Date date3 = (Date)formatter.parse(arr2);
            if((date1.after(date2) && date2.before(date3)) || date1.equals(date2) || date1.equals(date3))
                return true;

        } catch (ParseException e) {

            Toast.makeText(AdminShowBill.this, "Error name " + e, Toast.LENGTH_LONG).show();
        }
        return false;
    }

    boolean isEmpty(EditText text)
    {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    boolean checkDataEntered() {

        if (isEmpty(From)) {
            From.setError("From Date is required");
            return false;
        }
        else
        {
            From.setError(null);
        }

        if (isEmpty(To)) {
            To.setError("To Date is required");
            return false;
        }
        else
        {
            To.setError(null);
        }

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy");
            Date date1 = (Date)formatter.parse(From.getText().toString().trim());
            Date date2 = (Date)formatter.parse(To.getText().toString().trim());
            if(date2.before(date1))
            {
                To.setError("To Date should be after From Date.");
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }
}
