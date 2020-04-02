package com.example.guesthousebooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class HomeUser extends AppCompatActivity {

    private Button SignOut;
    private TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        SignOut = (Button) findViewById(R.id.signOut);
        userEmail = findViewById(R.id.userEmail);

        SignOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeUser.this, "Logout Successful", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(HomeUser.this,MainActivity.class);
                startActivity(intent);

            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());

        if (acct != null)
        {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            userEmail.append(personEmail);
            //Toast.makeText(HomeUser.this, "Logout Successful: " +personName + " personEmail: " + personEmail, Toast.LENGTH_LONG).show();

        }

    }

}
