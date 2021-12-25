package com.nbakh.blooddonation;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText registerFullName,nationalID, registerDoB,
            registerPhoneNumber, registerHomeAddress, registerPostalCode,
            registerEmail, registerPassword;

    private CircleImageView profile_image;

    private Spinner bloodGroupsSpinner, genderSpinner;

    private Button registerButton;

    private TextView backButton;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseRef;

    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        profile_image = findViewById(R.id.profile_image);
        registerFullName = findViewById(R.id.registerFullName);
        nationalID = findViewById(R.id.registerNationalID);
        registerDoB = findViewById(R.id.registerDoB);
        registerPhoneNumber = findViewById(R.id.registerPhoneNumber);
        registerHomeAddress = findViewById(R.id.registerHomeAddress);
        registerPostalCode = findViewById(R.id.registerPostalCode);
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        bloodGroupsSpinner = findViewById(R.id.bloodGroupsSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                //startActivity(intent);
                startActivityForResult(intent, 1);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = registerEmail.getText().toString().trim();
                final  String password = registerPassword.getText().toString().trim();
                final String fullName = registerFullName.getText().toString().trim();
                final String nationalid = nationalID.getText().toString().trim();
                final String registerdob = registerDoB.getText().toString().trim();
                final String phoneNumber = registerPhoneNumber.getText().toString().trim();
                final String homeAddress = registerHomeAddress.getText().toString().trim();
                final String postalCode = registerPostalCode.getText().toString().trim();
                final String bloodGroup = bloodGroupsSpinner.getSelectedItem().toString();
                final String gender = genderSpinner.getSelectedItem().toString();

                if (TextUtils.isEmpty(email)){
                    registerEmail.setError("Email required!");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    registerPassword.setError("Password required!");
                    return;
                }
                if (TextUtils.isEmpty(fullName)){
                    registerFullName.setError("Full name required!");
                    return;
                }
                if (TextUtils.isEmpty(nationalid)){
                    nationalID.setError("National ID required!");
                    return;
                }
                if (TextUtils.isEmpty(registerdob)){
                    registerDoB.setError("Date of Birth required!");
                    return;
                }
                if (TextUtils.isEmpty(phoneNumber)){
                    registerPhoneNumber.setError("Phone Number required!");
                    return;
                }
                if (TextUtils.isEmpty(homeAddress)){
                    registerEmail.setError("Home address required!");
                    return;
                }
                if (TextUtils.isEmpty(postalCode)){
                    registerEmail.setError("Post code required!");
                    return;
                }
                if (gender.equals("Select gender")){
                    Toast.makeText(RegistrationActivity.this, "Select gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (bloodGroup.equals("Select blood group")){
                    Toast.makeText(RegistrationActivity.this, "Select Blood group", Toast.LENGTH_SHORT).show();
                    return;
                }

                else {
                    loader.setMessage("Signing up");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (!task.isSuccessful()){
                                Toast.makeText(RegistrationActivity.this, "Error: " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String currentUserId = mAuth.getCurrentUser().getUid();
                                userDatabaseRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(currentUserId);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUserId);
                                userInfo.put("name", fullName);
                                userInfo.put("email", email);
                                userInfo.put("nid", nationalid);
                                userInfo.put("dateofbirth", registerdob);
                                userInfo.put("phonenumber", phoneNumber);
                                userInfo.put("homeaddress", homeAddress);
                                userInfo.put("postalcode", postalCode);
                                userInfo.put("gender", gender);
                                userInfo.put("bloodgroup", bloodGroup);
                                userInfo.put("type", "user");
                                userInfo.put("search", "user"+bloodGroup);

                                userDatabaseRef.updateChildren(userInfo).addOnCompleteListener(
                                        new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (!task.isSuccessful()){
                                            Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }

                                        finish();
                                    }
                                });

                                if (resultUri !=null){
                                    final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                            .child("profilepictures").child(currentUserId);
                                    Bitmap bitmap = null;

                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), resultUri);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }

                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data  = byteArrayOutputStream.toByteArray();
                                    UploadTask uploadTask = filePath.putBytes(data);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            if (taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference() !=null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String profilePictureUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profilepictureurl", profilePictureUrl);

                                                        userDatabaseRef.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if (!task.isSuccessful()){
                                                                    Toast.makeText(RegistrationActivity.this, "Error: "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                        finish();
                                                    }
                                                });
                                            }

                                        }
                                    });

                                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();
                                }
                            }

                        }
                    });
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==1 && resultCode == RESULT_OK && data !=null){
            resultUri = data.getData();
            profile_image.setImageURI(resultUri);
        }
    }
}