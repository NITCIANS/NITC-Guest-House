package com.example.guesthousebooking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private TextView userEmail;
    private TextView userName, Total;
    private ImageView userImage;

    private ListView LV1, LV2;

    private List<Booking> list1 = new ArrayList<>();
    private List<Booking> list2 = new ArrayList<>();

    ArrayAdapter<Booking> adapter1, adapter2;

    DatabaseReference ref;

    String personEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Home.this, "Logout Successful", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Home.this,MainActivity.class);
                startActivity(intent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_check_availability, R.id.nav_make_booking,
                R.id.nav_view_booking, R.id.nav_cancel_booking, R.id.nav_view_profile)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        userName = headerView.findViewById(R.id.userName);
        userEmail = headerView.findViewById(R.id.userEmail);
        userImage = headerView.findViewById(R.id.userImage);

        if (acct != null) {
            String personName = acct.getDisplayName();
            personEmail = acct.getEmail();
            Uri personPhoto = acct.getPhotoUrl();
            String str = acct.getPhotoUrl().toString();

            userEmail.setText(personEmail);
            userName.setText(personName);


            //Toast.makeText(HomeUser.this, "Logout Successful: " +personName + " personEmail: " + personEmail, Toast.LENGTH_LONG).show()
        }


       /* Total = findViewById(R.id.T2);
        LV1 = findViewById(R.id.LV1);
        LV2 = findViewById(R.id.LV2);

        adapter1 = new ArrayAdapter<Booking>(Home.this, android.R.layout.simple_list_item_1, list1);

        LV1.setAdapter(adapter1);

        adapter2= new ArrayAdapter<Booking>(Home.this, android.R.layout.simple_list_item_1, list2);

        LV2.setAdapter(adapter2);

        ref = FirebaseDatabase.getInstance().getReference("Booking");

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s)
            {
                Booking booking = dataSnapshot.getValue(Booking.class);
                if(booking.getUserId().equals(personEmail) && (booking.getBookingStatus().equals("Pending") || booking.getBookingStatus().equals("Confirmed")))
                    list1.add(booking);
                else if(booking.getUserId().equals(personEmail) && booking.getBookingStatus().equals("Completed"))
                    list2.add(booking);
                adapter1.notifyDataSetChanged();
                adapter2.notifyDataSetChanged();

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
*/





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
