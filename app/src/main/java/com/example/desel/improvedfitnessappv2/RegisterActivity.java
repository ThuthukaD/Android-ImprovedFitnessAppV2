package com.example.desel.improvedfitnessappv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity
{
    // VARIABLES

    // Edit Texts
    EditText etUser;
    EditText etEmail;
    EditText etPin;
    EditText etConPin;

    // Buttons
    Button btnRegister;
    Button btnLogin;
    Button btnCancel;

    // Other
    private FirebaseAuth mAuth;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // INSTANCING

        // Edit Texts
        etUser = findViewById(R.id.etUser);
        etEmail = findViewById(R.id.etEmail);
        etPin = findViewById(R.id.etPin);
        etConPin = findViewById(R.id.etConPin);

        // Buttons
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);

        // Other
        mAuth = FirebaseAuth.getInstance();
        sp = getSharedPreferences("login",MODE_PRIVATE);
    }

    public void Register (View view)
    {
        // Instance Declaration
        String user = etUser.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pin = etPin.getText().toString().trim();
        String conPin = etConPin.getText().toString().trim();

        // Checks if email and pin is empty
        if (TextUtils.isEmpty(user) && TextUtils.isEmpty(email)
                && TextUtils.isEmpty(pin) && TextUtils.isEmpty(conPin))
        {
            Toast.makeText(this, "Please fill in the required fields",
                    Toast.LENGTH_SHORT).show();
            etUser.requestFocus();
            etEmail.requestFocus();
            etPin.requestFocus();
            etConPin.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(user))
        {
            Toast.makeText(this, "Please enter your new username",
                    Toast.LENGTH_LONG).show();
            etUser.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter your new email address",
                    Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pin))
        {
            Toast.makeText(this, "Please enter your new pin",
                    Toast.LENGTH_LONG).show();
            etPin.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(conPin))
        {
            Toast.makeText(this, "Please enter your confirmation pin",
                    Toast.LENGTH_LONG).show();
            etConPin.requestFocus();
            return;
        }

        // Constraint for pin length
        if (pin.length() != 6 || conPin.length() != 6)
        {
            Toast.makeText(this, "Pin and Confirmation Pin must be 6 digits",
                    Toast.LENGTH_LONG).show();
            etPin.requestFocus();
            etConPin.requestFocus();
            return;
        }

        // Checks if pin and conPin matches
        if (conPin.equals(pin))
        {
            mAuth.createUserWithEmailAndPassword(email, pin)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                AutoLogin();
                                finish();
                                emptyInputs();
                            }
                            else
                            {
                                emptyInputs();
                                Toast.makeText(RegisterActivity.this,
                                        "Authentication Failed, please try again",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else
        {
            etPin.setText(null);
            etConPin.setText(null);
            Toast.makeText(this, "Confirmation pin does not match pin",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void Cancel(View view)
    {
        finish();
        System.exit(0);
    }

    public void Login(View view)
    {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    // Empties the text fields
    private void emptyInputs()
    {
        etUser.setText(null);
        etEmail.setText(null);
        etPin.setText(null);
        etConPin.setText(null);
    }

    // Method uses prefs to send data to LoginActivity, and also auto "logs" in the user
    public void AutoLogin()
    {

        // Initialising strings to store user data
        String uname = etUser.getText().toString();
        String email = etEmail.getText().toString();

        // creating shared preferences database
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // columns to store different values
        editor.putString("username", uname);
        editor.putString("email", email);

        // boolean to check if user created account before
        editor.putBoolean("isLogged", true);

        editor.apply();

        Intent intent = new Intent(RegisterActivity.this,
                LoginActivity.class);
        startActivity(intent);
    }
}
