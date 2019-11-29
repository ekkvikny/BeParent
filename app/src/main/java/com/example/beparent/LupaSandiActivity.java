package com.example.beparent;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LupaSandiActivity extends AppCompatActivity {

    private TextView textLogin;
    private Button SubmitSandi;
    private FirebaseAuth user;
    private EditText txt_Email;
    private String getEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lupa_sandi);
        setTitle("Lupa Sandi");

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

        txt_Email = findViewById(R.id.text_emaillupa);
        textLogin = (TextView) findViewById(R.id.text_login);
        textLogin.setPaintFlags(textLogin.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        SubmitSandi = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        user = FirebaseAuth.getInstance();

        SubmitSandi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                getEmail = txt_Email.getText().toString().trim();

                //Melakukan Proses Reset Password, dengan memasukan alamat email pengguna
                user.sendPasswordResetEmail(getEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Mengecek status keberhasilan saat proses reset Password
                                if(task.isSuccessful()){
                                    Toast.makeText(LupaSandiActivity.this, "Silakan Cek Inbox pada Email Anda", Toast.LENGTH_SHORT).show();
                                    finish();
                                }else {
                                    Toast.makeText(LupaSandiActivity.this, "Terjadi Kesalahan, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LupaSandiActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


    }
}
