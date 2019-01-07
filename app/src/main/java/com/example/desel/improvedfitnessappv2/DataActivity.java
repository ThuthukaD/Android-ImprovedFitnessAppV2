package com.example.desel.improvedfitnessappv2;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class DataActivity extends AppCompatActivity  implements View.OnClickListener
{
    // VARIABLES

    // Buttons
    Button btnSave;

    // Edit Texts
    EditText etName;
    EditText etSurname;
    EditText etWeight;
    EditText etHeight;
    TextView tvBmi;

    // FireStore
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        // INSTANCING

        // Buttons
        btnSave = findViewById(R.id.btnSave);

        // EditTexts
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);

        // Text Views
        tvBmi = findViewById(R.id.tvBmi);

        // Create instance of fireStore and VARIABLE
        db = FirebaseFirestore.getInstance();
    }

    private boolean hasValidationExpress
            (String s, String name, String surname, String weight, String height)
    {
        if (name.isEmpty())
        {
            etName.setError("Name Required");
            // Will focus on that text field
            etName.requestFocus();
            return true;
        }
        if (surname.isEmpty())
        {
            etSurname.setError("Surname Required");
            etSurname.requestFocus();
            return true;
        }
        if (weight.isEmpty())
        {
            etWeight.setError("Weight Required");
            etWeight.requestFocus();
            return true;
        }
        if (height.isEmpty())
        {
            etHeight.setError("Height Required");
            etHeight.requestFocus();
            return true;
        }
        return false;
    }

    public void saveUser()
    {
        double Height = Double.parseDouble(etHeight.getText().toString().trim());
        double Weight = Double.parseDouble(etWeight.getText().toString().trim());

        double BMICalc = Weight / (Height * Height);

        tvBmi.setText(String.format(Double.toString(BMICalc), "%.2f", 2));

        // Get the info the edit text, trim it, and ready to send to firebase
        String name = etName.getText().toString().trim();
        String surname = etSurname.getText().toString().trim();
        String weight = etWeight.getText().toString().trim();
        String height = etHeight.getText().toString().trim();
        String bmi = tvBmi.getText().toString().trim();

        // Only allow the user to continue if there are no validation errors
        if (!hasValidationExpress(name, surname, weight, height, bmi))
        {
            // Store to FireStore
            CollectionReference dbUser = db.collection("BMI");

            // Create a User class
            BMI BMI = new BMI(name, surname,
                    Double.parseDouble(weight),
                    Double.parseDouble(height),
                    Double.parseDouble(bmi));

            // If user succeeds or fails
            dbUser.add(BMI)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>()
                    {
                        @Override
                        public void onSuccess(DocumentReference documentReference)
                        {
                            Toast.makeText(DataActivity.this, "Data was added",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener()
                    {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            Toast.makeText(DataActivity.this, "Could not add user",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnSave:
                saveUser();
                break;
        }
    }
}