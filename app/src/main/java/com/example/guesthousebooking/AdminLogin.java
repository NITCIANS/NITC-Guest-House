package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class AdminLogin extends AppCompatActivity {

    private Button LogIn;
    private EditText username, password;

    DatabaseReference ref;
    Admin A;
    int attempt = 0;
    //public static final String MyPREFERENCES = "MyPrefs" ;
    //public static final String Email = "email";
    //public static final String Password = "password";

    //SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        ref = FirebaseDatabase.getInstance().getReference("Admin");

        LogIn = findViewById(R.id.login);
        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(checkDataEntered()) {

                    ref.addValueEventListener(new ValueEventListener() {
                        String email = username.getText().toString().trim();
                        String pass = password.getText().toString().trim();
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            A = snapshot.child("1").getValue(Admin.class);
                            if(A.getPassword().equals(pass) && A.getUserId().equals(email)) {

                                SaveSharedPreference.setLoggedIn(getApplicationContext(), true);
                                Intent intent = new Intent(AdminLogin.this, AdminHome.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(AdminLogin.this, "Invalid Username or Password..", Toast.LENGTH_LONG).show();
                                finish();
                                startActivity(getIntent());
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {

                        }
                    });


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
        if (isEmpty(username)) {
            username.setError("Email is required");
            return false;
        }
        else
        {
            username.setError(null);
        }

        if (isEmpty(password)) {
            password.setError("Password is required");
            return false;
        }
        else
        {
            password.setError(null);
        }
        return true;
    }
}
