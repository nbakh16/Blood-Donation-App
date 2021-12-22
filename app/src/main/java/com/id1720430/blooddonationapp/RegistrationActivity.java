package com.id1720430.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText registerFullName, registerDOB, registerNationalID, registerHomeAddress
            , registerPostalCode, registerPhoneNumber, registerEmail, registerPassword;

    private Spinner genderSpinner, bloodGroupSpinner;

    private Button signupButton;

    private TextView backButton;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerFullName = findViewById(R.id.registerFullName);
        registerDOB = findViewById(R.id.registerDOB);
        registerNationalID = findViewById(R.id.registerNationalID);
        registerHomeAddress = findViewById(R.id.registerHomeAddress);
        registerPostalCode = findViewById(R.id.registerPostalCode);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        genderSpinner = findViewById(R.id.genderSpinner);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        signupButton = findViewById(R.id.signupButton);
        backButton = findViewById(R.id.backButton);

        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fullName = registerFullName.getText().toString().trim();
                final String dob = registerDOB.getText().toString().trim();
                final String nationalID = registerNationalID.getText().toString().trim();
                final String homeAddress = registerHomeAddress.getText().toString().trim();
                final String postalCode = registerPostalCode.getText().toString().trim();
                final String phoneNumber = registerPhoneNumber.getText().toString().trim();
                final String gender = genderSpinner.getSelectedItem().toString();
                final String bloodGroup = bloodGroupSpinner.getSelectedItem().toString();
                final String email = registerEmail.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();


                if(TextUtils.isEmpty(fullName)){
                    registerFullName.setError("Full name required");
                    return;
                }
                if(TextUtils.isEmpty(dob)){
                    registerDOB.setError("Date of Birth required");
                    return;
                }
                if(TextUtils.isEmpty(nationalID)){
                    registerNationalID.setError("National ID number required");
                    return;
                }
                if(TextUtils.isEmpty(homeAddress)){
                    registerHomeAddress.setError("Home address required");
                    return;
                }
                if(TextUtils.isEmpty(postalCode)){
                    registerPostalCode.setError("Postal code required");
                    return;
                }
                if(TextUtils.isEmpty(phoneNumber)){
                    registerPhoneNumber.setError("Phone number required");
                    return;
                }
                if(gender.equals("Select gender")){
                    Toast.makeText(RegistrationActivity.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(bloodGroup.equals("Select blood group")){
                    Toast.makeText(RegistrationActivity.this, "Please select your blood group", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    registerEmail.setError("Email address required");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    registerPassword.setError("Password required");
                    return;
                }

                else {
                    loader.setMessage("Signing up ....");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(RegistrationActivity.this, "ERROR: " + error, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String currentUserID = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserID);

                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserID);
                                userInfo.put("name", fullName);
                                userInfo.put("dob", dob);
                                userInfo.put("nationalid", nationalID);
                                userInfo.put("homeaddress", homeAddress);
                                userInfo.put("postalcode", postalCode);
                                userInfo.put("phonenumber", phoneNumber);
                                userInfo.put("gender", gender);
                                userInfo.put("bloodgroup", bloodGroup);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            String error = task.getException().toString();
                                            Toast.makeText(RegistrationActivity.this, "ERROR: " + error, Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                        //loader.dismiss();

                                    }
                                });

                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                loader.dismiss();
                            }
                        }
                    });

                }
            }
        });
    }

}