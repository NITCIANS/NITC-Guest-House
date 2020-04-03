//package com.example.guesthousebooking;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import android.Manifest;
//import android.app.DatePickerDialog;
//import android.content.pm.PackageManager;
//import android.graphics.Paint;
//import android.graphics.pdf.PdfDocument;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.List;
//import java.util.logging.FileHandler;
//
//public class AdminShowBill extends AppCompatActivity {
//
//
//
//
//    DatePickerDialog datePickerDialog1;
//    DatePickerDialog datePickerDialog2;
//
//    Button Download, Show, Click1, Click2;
//    EditText From, To;
//
//    int year1, year2;
//    int month1, month2;
//    int day1, day2;
//    Calendar calendar;
//
//    Booking booking;
//    Bill bill;
//    String bookingId;
//
//    String[] arr1, arr2;
//
//    private DatabaseReference ref;
//    private List<Bill> list = new ArrayList<>();
//    ArrayAdapter<Bill> adapter;
//    private ListView listView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_show_bill);
//        Download = findViewById(R.id.B3);
//        Show = findViewById(R.id.B4);
//        Click1 = findViewById(R.id.B1);
//        Click2 = findViewById(R.id.B2);
//        From = findViewById(R.id.ET1);
//        To = findViewById(R.id.ET2);
//
//        From.setEnabled(false);
//        To.setEnabled(false);
//
//
//        Click1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                calendar = Calendar.getInstance();
//                year1 = calendar.get(Calendar.YEAR);
//                month1 = calendar.get(Calendar.MONTH);
//                day1 = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//                datePickerDialog1 = new DatePickerDialog(AdminShowBill.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
//                            {
//                                From.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
//                            }
//                        }, year1,month1,day1);
//                datePickerDialog1.show();
//            }
//        });
//
//        Click2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                calendar = Calendar.getInstance();
//                year2 = calendar.get(Calendar.YEAR);
//                month2 = calendar.get(Calendar.MONTH);
//                day2 = calendar.get(Calendar.DAY_OF_MONTH);
//
//
//                datePickerDialog2 = new DatePickerDialog(AdminShowBill.this,
//                        new DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
//                            {
//                                To.setText(dayOfMonth+ "-" + (month + 1) + "-" + year);
//
//
//                            }
//                        }, year2,month2,day2);
//
//                datePickerDialog2.show();
//
//
//            }
//
//        });
//
//        Download.setOnClickListener(new View.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
//            @Override
//            public void onClick(View v) {
//
//                PdfDocument myPdfDocument = new PdfDocument();
//                PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(300,600,1).create();
//                PdfDocument.Page myPage = myPdfDocument.startPage(myPageInfo);
//
//                Paint myPaint = new Paint();
//                String myString = "My Name is Yogesh Kumar Adhikari";
//                int x = 10, y=25;
//
//                for (String line: myString.split("\n"))
//                {
//                    myPage.getCanvas().drawText(line, x, y, myPaint);
//                    y += myPaint.descent()-myPaint.ascent();
//                }
//                myPdfDocument.finishPage(myPage);
//
//                File file = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                    file = new File(Environment.getExternalStorageState("/Download"));
//                }
//                if (!file.exists()) {
//                    file.mkdirs();
//                }
//                String targetPdf = directory_path+"test-2.pdf";
//                File filePath = new File(targetPdf);
//
//
//                try {
//                    //ActivityCompat.requestPermissions(AdminShowBill.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                   //ActivityCompat.requestPermissions(AdminShowBill.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
//                    myPdfDocument.writeTo(new FileOutputStream(filePath));
//                }
//                catch (Exception e){
//
//                    Toast.makeText(AdminShowBill.this, "Error name " + e, Toast.LENGTH_LONG).show();
//
//                }
//
//                myPdfDocument.close();
//
//
//
//            }
//        });
//
//        Show.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                list.clear();
//
//                if(checkDataEntered()) {
//
//                    arr1 = From.getText().toString().trim().split("-");
//                    arr2 = To.getText().toString().trim().split("-");
//
//                    listView = findViewById(R.id.LV);
//
//                    adapter = new ArrayAdapter<Bill>(AdminShowBill.this, android.R.layout.simple_list_item_1, list);
//
//                    listView.setAdapter(adapter);
//
//                    ref = FirebaseDatabase.getInstance().getReference("Bill");
//
//                    ref.addChildEventListener(new ChildEventListener() {
//                        @Override
//                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                            bill = dataSnapshot.getValue(Bill.class);
//                            String[] arr = bill.getCheckInDate().split("-");
//                            if (Integer.parseInt(arr[0]) >= Integer.parseInt(arr1[0])) {
//                                list.add(bill);
//                                adapter.notifyDataSetChanged();
//
//                            }
//                        }
//
//                        @Override
//                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                        }
//
//                        @Override
//                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//                }
//            }
//        });
//
//
//    }
//
//    boolean isEmpty(EditText text)
//    {
//        CharSequence str = text.getText().toString();
//        return TextUtils.isEmpty(str);
//    }
//
//    boolean checkDataEntered() {
//
//        if (isEmpty(From)) {
//            From.setError("From Date is required");
//            return false;
//        }
//        else
//        {
//            From.setError(null);
//        }
//
//        if (isEmpty(To)) {
//            To.setError("To Date is required");
//            return false;
//        }
//        else
//        {
//            To.setError(null);
//        }
//        return true;
//    }
//}
