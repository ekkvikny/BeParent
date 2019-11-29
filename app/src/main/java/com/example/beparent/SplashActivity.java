package com.example.beparent;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseDatabase getDatabase;
    private DatabaseReference mDatabase;
    private String GetUserID,nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();

        //Mendapatkan User ID dari akun yang terautentikasi
        FirebaseUser user = auth.getCurrentUser();
        //GetUserID = user.getUid();

        //getDatabase = FirebaseDatabase.getInstance();


        //Pengecekan, jika tidak ada login. Di arahkan ke Login activity.
        if (user == null) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }else if (user != null) {
            FirebaseDatabase.getInstance().getReference().keepSynced(true);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }



        // langsung pindah ke MainActivity atau activity lain
        // begitu memasuki splash screen ini

    }
}