package com.id1720430.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    protected NavigationView navigationView;

    private CircleImageView nav_profile_image;
    private TextView nav_fullName, nav_email, nav_phoneNumber, nav_homeAddress, nav_bloodGroup;

    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Blood Donation App");

        navigationView = findViewById(R.id.navigationView);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        nav_profile_image = navigationView.getHeaderView(0).findViewById(R.id.nav_user_image);
        nav_fullName = navigationView.getHeaderView(0).findViewById(R.id.nav_user_fullname);
        nav_email = navigationView.getHeaderView(0).findViewById(R.id.nav_user_email);
        nav_phoneNumber = navigationView.getHeaderView(0).findViewById(R.id.nav_user_phonenumber);
        nav_homeAddress = navigationView.getHeaderView(0).findViewById(R.id.nav_user_homeaddress);
        nav_bloodGroup = navigationView.getHeaderView(0).findViewById(R.id.nav_user_bloodgroup);

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        );

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue().toString();
                    nav_fullName.setText(name);
                    String email = snapshot.child("email").getValue().toString();
                    nav_email.setText(email);
                    String phonenumber = snapshot.child("phonenumber").getValue().toString();
                    nav_phoneNumber.setText(phonenumber);
                    String homeaddress = snapshot.child("homeaddress").getValue().toString();
                    nav_homeAddress.setText(homeaddress);
                    String bloodgroup = snapshot.child("bloodgroup").getValue().toString();
                    nav_bloodGroup.setText(bloodgroup);

                    String profilepictureurl = snapshot.child("profilepictureurl").getValue().toString();
                    Glide.with(getApplicationContext()).load(profilepictureurl).into(nav_profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}