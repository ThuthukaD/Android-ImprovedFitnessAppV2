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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    // VARIABLES

    // Text View
    TextView tvUser;
    TextView tvEmail;
    TextView tvSwitchUser;

    // Edit Texts
    EditText etPin;
    EditText etEmail;

    // Buttons
    Button btnLogin;
    Button btnCancel;

    // Strings
    String stLogin;
    String stEmail;

    // Other
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // INSTANCING

        // Text Views
        tvUser = findViewById(R.id.tvUser);
//        tvEmail = findViewById(R.id.tvEmail);
        tvSwitchUser = findViewById(R.id.tvSwitchUser);

        // Edit Texts
        etEmail = findViewById(R.id.etEmail);
        etPin = findViewById(R.id.etPin);

        // Buttons
        btnLogin = findViewById(R.id.btnLogin);
        btnCancel = findViewById(R.id.btnCancel);

        // Other
        mAuth = FirebaseAuth.getInstance();

        // Calling text method
        try
        {
            Text();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Login Method
    public void Login(View view)
    {
        String email = etEmail.getText().toString().trim();
        String pin = etPin.getText().toString().trim();

        // Checks if email and password is empty
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Account Error, account not found",
                    Toast.LENGTH_LONG).show();
            etEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(pin))
        {
            Toast.makeText(this, "Please enter your pin",
                    Toast.LENGTH_SHORT).show();
            etPin.requestFocus();
            return;
        }

        // Constraint for password type or length
        if (pin.length() < 6)
        {
            Toast.makeText(this, "Pin must be 6 digits",
                    Toast.LENGTH_SHORT).show();
            etPin.requestFocus();
            return;
        }

        // Sign in allowed if all is good
        mAuth.signInWithEmailAndPassword(email,pin).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Passing text to main screen for the user text view
                            Intent intent = new Intent(LoginActivity.this,
                                    MainActivity.class);

                            // Initialising Keys
                            stLogin = tvUser.getText().toString();
                            intent.putExtra("UserLogin", stLogin);

//                            stEmail = tvEmail.getText().toString();
//                            intent.putExtra("EmailLogin", stEmail);

                            startActivity(intent);

                            emptyInputs();
                            //finish();
                        }
                        else
                        {
                            emptyInputs();
                            Toast.makeText(LoginActivity.this,
                                    "Unexpected Error, pleas try again",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Get text method
    public void Text()
    {
        // Calling prefs from RegisterActivity
        SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);

        // Passed string for user
        String username = prefs.getString("username", "ifaTest01");

        // Passed string for email
        String email = prefs.getString("email", "Email");

        // Setting the current fields to the stored text
        etEmail.setText(email);
        tvUser.setText(username);
    }

    // Empties pin field
    private void emptyInputs()
    {
        etPin.setText(null);
    }

    // Open Register Page Method
    public void SwitchUser(View view)
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    // Method to close the app
    public void Cancel(View view)
    {
        System.exit(0);
    }
}
