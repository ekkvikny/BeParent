package com.example.beparent;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OptionMenuActivity extends AppCompatActivity {

    private TextView textProfil;
    private  TextView textAbout;
    private TextView textHapus;
    private  TextView textKeluar, textInfoanak;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private FirebaseDatabase getDatabase;
    private DatabaseReference mDatabase;
    private String GetUserID,nama;

    private static final String TAG = "OptionMenuActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_menu);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();
        GetUserID = currentUser.getUid();

        //getDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        mDatabase.child(GetUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setNama(user.getNama());
                setTitle(user.getNama());
                String password =user.getPassword();
                String email =user.getEmail();

                AuthCredential credential = EmailAuthProvider.getCredential(email,password);
                // Prompt the user to re-provide their sign-in credentials
                currentUser.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.d(TAG, "User re-authenticated.");
                                } else {
                                    // Password is incorrect
                                }
                            }
                        });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        textProfil = (TextView) findViewById(R.id.text_optionprofil);
        textAbout = (TextView) findViewById(R.id.text_optionabout);
        textKeluar = (TextView) findViewById(R.id.text_optionkeluar);
        textInfoanak = findViewById(R.id.text_informasianak);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        textProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OptionMenuActivity.this,ProfilActivity.class);
                startActivity(intent);
            }
        });
        textInfoanak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(OptionMenuActivity.this, InformasiAnakActivity.class);
                startActivity(intent);
            }
        });
        textAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //belumselesai
                Intent intent=new Intent(OptionMenuActivity.this,AboutActivity.class);
                startActivity(intent);
            }
        });
        textKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent=new Intent(OptionMenuActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


    }

}
