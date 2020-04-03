package com.example.guesthousebooking;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AdminLogin extends AppCompatActivity {

    private Button LogIn;
    private EditText email,password;
  // private ProgressBar = new ProgressBar();
    private FirebaseAuth admAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
            initializeUI();
            admAuth = FirebaseAuth.getInstance();
//        LogIn = findViewById(R.id.login);
//        LogIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                Intent intent = new Intent(AdminLogin.this, AdminHome.class);
//                startActivity(intent);
//            }
//        });
        LogIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void  onClick(View v)
            {
                //loginAdminAccount();
                System.out.println("123");
                loginAdminAccount();
            }
        });


    }
    //@RequiresApi(api = Build.VERSION_CODES.O)
    private void loginAdminAccount()
    {
      //  progressBar.setVisibility(View.VISIBLE);
            System.out.println("1234");
        final String email1,password1;
        email1 = email.getText().toString();
        password1 = password.getText().toString();
        System.out.println(email1);
        System.out.println(password1);
        if(TextUtils.isEmpty(email1))
        {
            //Toast toast
            Toast toast=Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG);
            View view = toast.getView();

            //To change the Background of Toast
            view.setBackgroundColor(Color.rgb(15,10,90));
            TextView text = (TextView) view.findViewById(android.R.id.message);

            //Shadow of the Of the Text Color
            text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
            text.setTextColor(Color.rgb(16,16,16));
            //text.setTextSize(Integer.valueOf(getResources().getString(R.string.text_size)));
            toast.show();
            return;
        }
       else if(TextUtils.isEmpty(password1))
        {
            Toast toast=Toast.makeText(getApplicationContext(), "Please enter Password...", Toast.LENGTH_LONG);
            View view = toast.getView();

            //To change the Background of Toast
            view.setBackgroundColor(Color.rgb(15,10,90));
            TextView text = (TextView) view.findViewById(android.R.id.message);

            //Shadow of the Of the Text Color
            text.setShadowLayer(0, 0, 0, Color.TRANSPARENT);
            text.setTextColor(Color.rgb(16,16,16));
            //text.setTextSize(Integer.valueOf(getResources().getString(R.string.text_size)));
            toast.show();
            return;
        }

//        admAuth.signInWithEmailAndPassword(email1, password1)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (email1 == "subhash" && password1=="yogesh") {
//
//                            Toast.makeText(getApplicationContext(), "Login successful!", Toast.LENGTH_LONG).show();
//                            //progressBar.setVisibility(View.GONE);
//
//                           //Intent intent = new (AdminLogin,AdminHome.class);
//                            Intent intent = new Intent(AdminLogin.this, AdminHome.class);
//                            startActivity(intent);
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(), "Login failed! Please try again later", Toast.LENGTH_LONG).show();
//                           // progressBar.setVisibility(View.GONE);
//                        }
//                    }
////                    boolean isCheck(final String email , String password)
////                    {
////
////                        admAuth.child(id).addValueEventListener(new ValueEventListener() {
////                            @Override
////                            public void onDataChange(DataSnapshot dataSnapshot) {
////
////                                Admin admin = dataSnapshot.getValue(Admin.class);
////
////                              //  Log.d("", "User name: " + admin.getEmail() + ", email " + user.getPassword());
////                                if(email == admin.getEmail() && admin.getPassword())
////                                {
////
////                                }
////                            }
////
////                            @Override
////                            public void onCancelled(DatabaseError error) {
////// Failed to read value
////                                Log.w("", "Failed to read value.", error.toException());
////                            }
////                        });
////
////
////
////                    }
//                });

    }
    private void initializeUI() {
        email = findViewById(R.id.username);
        password = findViewById(R.id.password);

        LogIn = findViewById(R.id.Login);
       // progressBar = findViewById(R.id.);
    }
}
