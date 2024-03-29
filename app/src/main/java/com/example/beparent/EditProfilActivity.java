package com.example.beparent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Objects;

public class EditProfilActivity extends AppCompatActivity  {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private RadioGroup rgGenderanak;
    private RadioButton rbGenderanak;
    private EditText textPassword, textKonfirmpassword,textPassworBaru;
    private EditText textNamapengguna;
    private EditText textEmail;
    private Button btnSimpan;
    private Button btnBatal;

    String email_txt,newpassword_txt,name_txt,konfirmsandi_txt;


    private FirebaseAuth auth;
    private FirebaseDatabase getDatabase;
    private DatabaseReference mDatabase;
    private String GetUserID,nama;
    private FirebaseUser mauth;

    private static final String TAG = "EditProfilActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        setTitle("Edit Profil");

        auth = FirebaseAuth.getInstance();
        mauth =  auth.getCurrentUser();

        //Mendapatkan User ID dari akun yang terautentikasi
         FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();

        //getDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        mDatabase.child(GetUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                user.setNama(user.getNama());
                user.setEmail(user.getEmail());
                user.setPassword(user.getPassword());

                textNamapengguna.setText(user.getNama());
                textEmail.setText(user.getEmail());
                textPassword.setText(user.getPassword());
                //textKonfirmpassword.setText(user.getPassword());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

        btnSimpan = (Button) findViewById(R.id.btn_editsimpan);
        btnBatal = (Button) findViewById(R.id.btn_editbatal);
        textPassword = (EditText) findViewById(R.id.text_editsandi);
        textKonfirmpassword = (EditText) findViewById(R.id.text_editsandikonfirm);
        textPassworBaru = (EditText)findViewById(R.id.text_editsandibaru) ;
        textEmail = (EditText) findViewById(R.id.text_editemail);
        textNamapengguna = (EditText) findViewById(R.id.text_editortu);


        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditProfilActivity.this, ProfilActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_txt = textEmail.getText().toString();
                newpassword_txt = textPassworBaru.getText().toString();
                name_txt = textNamapengguna.getText().toString();
                konfirmsandi_txt = textKonfirmpassword.getText().toString();


                if (TextUtils.isEmpty(email_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Email !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newpassword_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(konfirmsandi_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Nama !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newpassword_txt.equals(konfirmsandi_txt) ) {
                    Toast.makeText(getApplicationContext(), "Password Tidak Cocok!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mauth.updateEmail(email_txt)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    mauth.updatePassword(newpassword_txt)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mDatabase.child(GetUserID).child("password").setValue(newpassword_txt);

                                                        Log.d(TAG, "User password updated.");
                                                    }
                                                }
                                            });

                                    mDatabase.child(GetUserID).child("email").setValue(email_txt);
                                    mDatabase.child(GetUserID).child("nama").setValue(name_txt);


                                    Intent intent = new Intent(EditProfilActivity.this, ProfilActivity.class);
                                    startActivity(intent);
                                    finish();

                                    Log.d(TAG, "User Email updated.");
                                } else {

                                    try {
                                        throw Objects.requireNonNull(task.getException());
                                    }

                                    // Invalid Email
                                    catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                                        Toast.makeText(getApplicationContext(), "Invalid Email...", Toast.LENGTH_LONG).show();

                                    }
                                    // Email Already Exists
                                    catch (FirebaseAuthUserCollisionException existEmail) {
                                        Toast.makeText(getApplicationContext(), "Email Used By Someone Else, Please Give Another Email...", Toast.LENGTH_LONG).show();

                                    }
                                    // Any Other Exception
                                    catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            }});


            }
        });


    }
}
