package com.example.beparent;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class LihatKontenActivity extends AppCompatActivity {

    private TextView Judul_Konten,Isi_Konten;
    private ImageView img_Konten;

    private DatabaseReference database;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_konten);

        setTitle(getIntent().getExtras().getString("dataKategori")+" Bulan");

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        Judul_Konten = findViewById(R.id.text_judulkonten);
        Isi_Konten = findViewById(R.id.text_isikonten);
        img_Konten = findViewById(R.id.img_lihat_konten);

        getData();



    }

    private void getData(){
        //Menampilkan data dari item yang dipilih sebelumnya
        final String getJudul = getIntent().getExtras().getString("dataJudul");
        //final String getKategori = getIntent().getExtras().getString("dataKategori");
        final String getIsi = getIntent().getExtras().getString("dataIsi");
        final String getImage = getIntent().getExtras().getString("dataImage");
        Judul_Konten.setText(getJudul);
        Isi_Konten.setText(getIsi);
        Picasso.with(this)
                .load(getImage)
                .into(img_Konten);

    }

}
