package com.example.beparent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView imgLogoapp;
    String[] status = { "Orang Tua","Calon Orang Tua" };
    //private Button btnTambahanak ;
    private RadioGroup rgGenderanak;
    private RadioButton rbGenderanak;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private Spinner textStatus;
    private EditText textPassword;
    private EditText textNamapengguna;
    private EditText textEmail;
    private EditText textTgllahir;
    private EditText textNamaanak;
    private Button btnDaftar;
    private FirebaseAuth mAuth;
    String email_txt,password_txt,name_txt,anak_txt,tgllahir_txt,gender_txt,status_txt;
    private DatabaseReference mDatabase;
    private String GetUserID;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        btnDaftar = (Button) findViewById(R.id.btn_daftar);
        //btDatePicker = (Button) findViewById(R.id.bt_datepicker);
        textTgllahir = (EditText) findViewById(R.id.text_tglanak);
        textNamaanak = (EditText) findViewById(R.id.intext_namaanak);
        textPassword = (EditText) findViewById(R.id.text_password);
        textNamapengguna = (EditText) findViewById(R.id.text_username);
        textEmail = (EditText) findViewById(R.id.text_email);
        ImageView imview = (ImageView) findViewById(R.id.image_beparent);
        rgGenderanak = (RadioGroup) findViewById(R.id.rg_jeniskelamin);
       // btnTambahanak = (Button) findViewById(R.id.btn_tmbhanak);


        addListenerOnButtonJawab();

        imview.setImageResource(0);
        Drawable draw = getResources().getDrawable(R.drawable.contoh);
        draw = (draw);
        imview.setImageDrawable(draw);


        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        textTgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email_txt = textEmail.getText().toString();
                password_txt = textPassword.getText().toString();
                name_txt = textNamapengguna.getText().toString();
                anak_txt = textNamaanak.getText().toString();
                tgllahir_txt = textTgllahir.getText().toString();


                int selectedId = rgGenderanak.getCheckedRadioButtonId();
                View rbGenderanak = (RadioButton) findViewById(selectedId);
                int idx = rgGenderanak.indexOfChild(rbGenderanak);
                RadioButton rb = (RadioButton) rgGenderanak.getChildAt(idx);
                gender_txt = rb.getText().toString();

                if (TextUtils.isEmpty(email_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Email !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Password !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Nama Anda !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(gender_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Gender anak !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(anak_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Nama Anak !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tgllahir_txt)) {
                    Toast.makeText(getApplicationContext(), "Masukan Tanggal Lahir Anak !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email_txt, password_txt)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Daftar sukses, masuk ke Main Activity
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    GetUserID = user.getUid();

                                    //write data di database
                                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                    User pengguna = new User(name_txt, email_txt,password_txt);
                                    Anak anak = new Anak(anak_txt,gender_txt,tgllahir_txt);
                                    mDatabase.child(GetUserID).setValue(pengguna);
                                    mDatabase.child(GetUserID).child("Anak").child("Anak1").setValue(anak);

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    
                                } else {
                                    // Jika daftar gagal, memberikan pesan
                                    Toast.makeText(RegisterActivity.this, "Proses Pendaftaran gagal : " +  task.getException(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

    }

    private void addListenerOnButtonJawab() {
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 //get selected radio button from radioGroup
                int selectedId = rgGenderanak.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbGenderanak = (RadioButton) findViewById(selectedId);
                Toast.makeText(getBaseContext(),
                       "Anda Memilih Jenis Kelamin " + rbGenderanak.getText(),
                        Toast.LENGTH_SHORT).show();
                gender_txt = rbGenderanak.getText().toString();
            }

        });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), "Selected User: "+status[position] ,Toast.LENGTH_SHORT).show();
        status_txt = status[position];

    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO - Custom Code
    }

    private void showDateDialog(){

        /**
         * Calendar untuk mendapatkan tanggal sekarang
         */
        Calendar newCalendar = Calendar.getInstance();

        /**
         * Initiate DatePicker dialog
         */
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                /**
                 * Method ini dipanggil saat kita selesai memilih tanggal di DatePicker
                 */

                /**
                 * Set Calendar untuk menampung tanggal yang dipilih
                 */
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                /**
                 * Update TextView dengan tanggal yang kita pilih
                 */
                textTgllahir.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        /**
         * Tampilkan DatePicker dialog
         */
        datePickerDialog.show();
    }


}
