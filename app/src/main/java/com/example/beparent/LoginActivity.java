package com.example.beparent;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText textUsername;
    private EditText textPassword;
    private TextView textLupa;
    private TextView textDaftar;
    private TextView textBelumpunya;
    private ImageView imgLogoapp;
    String email_txt,password_txt;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        btnLogin=findViewById(R.id.button_login);
        textUsername = (EditText)findViewById(R.id.text_username);
        textPassword = (EditText) findViewById(R.id.text_password);
        textLupa = findViewById(R.id.text_lupa);
        textDaftar = findViewById(R.id.text_daftar);
        textBelumpunya = findViewById(R.id.text_belumpunyaa);


        ImageView imview = (ImageView) findViewById(R.id.image_beparent);
        imview.setImageResource(0);
        Drawable draw = getResources().getDrawable(R.drawable.contoh);
        draw = (draw);
        imview.setImageDrawable(draw);


        textLupa.setPaintFlags(textLupa.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_txt = textUsername.getText().toString();
                password_txt = textPassword.getText().toString();

                if (TextUtils.isEmpty(email_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Email !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email_txt, password_txt)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login sukses, masuk ke Main Activity
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // Jika Login gagal, memberikan pesan
                                    Toast.makeText(LoginActivity.this, "Proses Login gagal : Email atau Password Salah " +  task.getException(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        textDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        textLupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,LupaSandiActivity.class);
                startActivity(intent);
            }
        });

    }
}
