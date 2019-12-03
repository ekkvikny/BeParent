package com.example.beparent;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TambahAnakActivity extends AppCompatActivity {

    private RadioGroup rgGenderanak;
    private RadioButton rbGenderanak;
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;
    private EditText textTgllahir;
    private EditText textNamaanak;
    private Button btnTambahAnak, btnBatal;
    private FirebaseAuth mAuth;
    String anak_txt,tgllahir_txt,gender_txt,status_txt;
    private DatabaseReference mDatabase;
    private String GetUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_anak);
        mAuth = FirebaseAuth.getInstance();
        setTitle("Tambah Informasi Anak");

        btnTambahAnak = (Button) findViewById(R.id.btn_tmbhanak);
        btnBatal = (Button)findViewById(R.id.btn_editbatal);
        textTgllahir = (EditText) findViewById(R.id.text_tglanak);
        textNamaanak = (EditText) findViewById(R.id.intext_namaanak);
        rgGenderanak = (RadioGroup) findViewById(R.id.rg_jeniskelamin);

        addListenerOnButtonJawab();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        textTgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        FirebaseUser user = mAuth.getCurrentUser();
        GetUserID = user.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TambahAnakActivity.this, InformasiAnakActivity.class);
                startActivity(intent);
            }
        });

        btnTambahAnak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                anak_txt = textNamaanak.getText().toString();
                tgllahir_txt = textTgllahir.getText().toString();

                if (TextUtils.isEmpty(anak_txt)) {
                    Toast.makeText(getApplicationContext(), "Tolong isi nama Anak Anda", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(tgllahir_txt)) {
                    Toast.makeText(getApplicationContext(), "Tolong isi tanggal lahir Anak Anda", Toast.LENGTH_SHORT).show();
                    return;
                }

                int selectedId = rgGenderanak.getCheckedRadioButtonId();
                View rbGenderanak = (RadioButton) findViewById(selectedId);
                if (selectedId == -1){
                    Toast.makeText(getApplicationContext(), "Pilih jenis kelamin anak Anda terlebih dahulu", Toast.LENGTH_SHORT).show();
                }else {
                    int idx = rgGenderanak.indexOfChild(rbGenderanak);
                    RadioButton rb = (RadioButton) rgGenderanak.getChildAt(idx);
                    gender_txt = rb.getText().toString();
                }

                mDatabase.child(GetUserID).child("Anak").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild("Anak2")){
                                //user exists, do something
                                Toast.makeText(getApplicationContext(),"Data Gagal di Tambah, Maksimal Jumlah Anak adalah 2", Toast.LENGTH_LONG).show();
                            } else {
                                //user does not exist, do something else
                                Anak anak = new Anak(anak_txt,gender_txt,tgllahir_txt);

                                FirebaseUser user = mAuth.getCurrentUser();
                                GetUserID = user.getUid();
                                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                mDatabase.child(GetUserID).child("Anak").child("Anak2").setValue(anak);

                                Toast.makeText(TambahAnakActivity.this, "Tambah Anak Berhasil", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(TambahAnakActivity.this, InformasiAnakActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                        }
                    });


            }

        });
    }

    private void addListenerOnButtonJawab() {
        btnTambahAnak.setOnClickListener(new View.OnClickListener() {
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