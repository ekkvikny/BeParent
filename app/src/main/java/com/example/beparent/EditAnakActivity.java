package com.example.beparent;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditAnakActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter;

    private RadioGroup rgGenderanak;
    private RadioButton rbGenderanak;
    private EditText textTgllahir;
    private EditText textNamaanak;
    private Button btnSimpan;
    private Button btnBatal;
    private TextView btnHapus;
    String anak_txt,tgllahir_txt,gender_txt;


    private FirebaseAuth auth;
    private FirebaseDatabase getDatabase;
    private DatabaseReference mDatabase;
    private String GetUserID,nama;
    private FirebaseUser mauth;

    private static final String TAG = "EditAnakActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_anak);
        setTitle("Edit Data Anak");

        auth = FirebaseAuth.getInstance();
        mauth =  auth.getCurrentUser();

        //Mendapatkan User ID dari akun yang terautentikasi
        FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

        //getDatabase = FirebaseDatabase.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        btnSimpan = (Button) findViewById(R.id.btn_simpananak);
        btnHapus = (TextView)findViewById(R.id.txt_hapusanak);
        btnBatal = (Button) findViewById(R.id.btn_bataledit);
        textTgllahir = (EditText) findViewById(R.id.text_edittgllahir);
        textNamaanak = (EditText) findViewById(R.id.text_editprofilanak);
        rgGenderanak = (RadioGroup) findViewById(R.id.rg_editjeniskelamin);

        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        textTgllahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateDialog();
            }
        });

        getData();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
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

                    Anak setAnak = new Anak();
                    setAnak.setNamaAnak(anak_txt);
                    setAnak.setTgllahir(tgllahir_txt);
                    setAnak.setKlaminAnak(gender_txt);
                    updateKonten(setAnak);


            }
        });
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditAnakActivity.this, InformasiAnakActivity.class);
                startActivity(intent);
            }
        });
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

    }

    private void getData(){
        //Menampilkan data dari item yang dipilih sebelumnya
        final String getNamaAnak = getIntent().getExtras().getString("dataNama");
        final String getKelamin = getIntent().getExtras().getString("dataKelamin");
        final String getTglLahir = getIntent().getExtras().getString("datatglLahir");
        textNamaanak.setText(getNamaAnak);
        textTgllahir.setText(getTglLahir);

    }

    private void updateKonten(Anak anak){
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        mDatabase.child(GetUserID)
                .child("Anak")
                .child(getKey)
                .setValue(anak)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditAnakActivity.this, "Data Berhasil diubah", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditAnakActivity.this, InformasiAnakActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private void addListenerOnButtonJawab() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
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

    private void showDateDialog() {
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

    private void showDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title dialog
        alertDialogBuilder.setTitle("Hapus Informasi Anak");

        // set pesan dari dialog
        alertDialogBuilder
                .setMessage("Yakin ingin Menghapus data anak Anda?")
                .setIcon(R.drawable.ic_delete_black_24dp)
                .setCancelable(false)
                .setPositiveButton("Hapus",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        String getKey = getIntent().getExtras().getString("getPrimaryKey");
                        if(mDatabase != null){
                            mDatabase.child(GetUserID)
                                    .child("Anak")
                                    .child(getKey)
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener() {
                                        @Override
                                        public void onSuccess(Object o) {
                                            Toast.makeText(EditAnakActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(EditAnakActivity.this, InformasiAnakActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                        }
                    }
                })
                .setNegativeButton("Batal",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // jika tombol ini diklik, akan menutup dialog
                        // dan tidak terjadi apa2
                        dialog.cancel();
                    }
                });

        // membuat alert dialog dari builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // menampilkan alert dialog
        alertDialog.show();
    }

}
