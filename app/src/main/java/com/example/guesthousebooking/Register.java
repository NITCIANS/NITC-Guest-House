package com.example.guesthousebooking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;
import android.view.View;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {

    EditText Name;
    EditText Contact;
    EditText email;
    Button register;
    Spinner type;
    private DatabaseReference ref;
    private TextView userEmail;
    private EditText userName, userContact;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail = findViewById(R.id.T1);
        userName = findViewById(R.id.E1);
        userContact = findViewById(R.id.E2);

        register = findViewById(R.id.registration);
        type = findViewById(R.id.userType);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        final String personEmail = acct.getEmail();
        final String personName = acct.getDisplayName();
        //final String personContact = acct.getId();

        userEmail.append(personEmail);
        userName.setText(personName);


        user = new User();


        ref = FirebaseDatabase.getInstance().getReference("User");

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(checkDataEntered())
                {
                    user.setName(userName.getText().toString().trim());
                    user.setContact(userContact.getText().toString().trim());
                    user.setEmail(personEmail);
                    user.setType(type.getSelectedItem().toString().trim());
                    String T = type.getSelectedItem().toString();
                    if(T.equals("Student"))
                        user.setAccountStatus("Approved");
                    else
                        user.setAccountStatus("Pending");
                    String str = personEmail.split("@")[0];
                    ref.child(str).setValue(user);

                    Toast.makeText(Register.this, "Inserted Successfully", Toast.LENGTH_LONG).show();
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
        if (isEmpty(userName)) {
            userName.setError("Name is required!");
            return false;
        }

        if (isEmpty(userContact)) {
            userContact.setError("Contact No. is required!");
            return false;
        }

        String str = type.getSelectedItem().toString().trim();
        if(str.equals("Choose your Type"))
        {
            Toast t = Toast.makeText(this, "Please Select your Type", Toast.LENGTH_SHORT);
            t.show();
            return false;
        }
        return true;

    }
}

