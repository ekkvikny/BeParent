package com.example.beparent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilActivity extends AppCompatActivity {

    private EditText inputNamapengguna;
    private EditText inputEmail;
    private Button btnEdit;

    private FirebaseAuth auth;
    private FirebaseDatabase getDatabase;
    private DatabaseReference mDatabase;
    private String GetUserID,nama;

    private static final String TAG = "ProfilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        setTitle("Profil");

        auth = FirebaseAuth.getInstance();

        //Mendapatkan User ID dari akun yang terautentikasi
        FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();

        //getDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");


        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

        inputNamapengguna = (EditText) findViewById(R.id.text_profilortu);
        inputEmail = (EditText) findViewById(R.id.text_profilemail);

        btnEdit = (Button) findViewById(R.id.btn_profiledit);


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProfilActivity.this,EditProfilActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabase.child(GetUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setNama(user.getNama());
                user.setEmail(user.getEmail());

                inputNamapengguna.setText(user.getNama());
                inputEmail.setText(user.getEmail());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
