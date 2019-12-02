package com.example.beparent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AktivitasActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private LinearLayout layoutatas;
    private LinearLayout layoutKonten;
    private TextView textUmur, textNamaAnak;
    private ImageView logoSearch,AvatarAnak;

    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;
    private RecyclerView.LayoutManager layoutManager1;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference reference;
    private ArrayList<data_Konten> dataKonten;
    private DatabaseReference mDatabase;

    private FirebaseAuth auth;

    private String GetUserID,namaAnak, usiaAnak;
    private Spinner textUrutanAnak;
    String[] status = { "Anak ke 1","Anak ke 2" };
    private String txtAnakKe;
    //final String result;

    private static final String TAG = "GiziActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktivitas);
        setTitle("Aktivitas Anak");
        textUmur = (TextView) findViewById(R.id.text_umur);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progressBar);
        textNamaAnak = (TextView) findViewById(R.id.text_namaanak) ;
        Spinner spin = (Spinner) findViewById(R.id.spinner_status);
        textUrutanAnak =findViewById(R.id.spinner_status);
        AvatarAnak = findViewById(R.id.image_anak);

        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();

        mDatabase = FirebaseDatabase.getInstance().getReference("users");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AktivitasActivity.this, android.R.layout.simple_spinner_item, status );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(adapter);
        spin.setOnItemSelectedListener(this);



    }

    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){
        String filterumur = textUmur.getText().toString().substring(0,2);
        Toast.makeText(getApplicationContext(),"Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show();
        Log.d(TAG, "Umur: "  + filterumur +", email " );
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("konten")
                .child("Aktivitas")
                .orderByChild("kategoriKonten")
                .equalTo(filterumur)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataKonten = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam objek Konten
                            data_Konten konten = snapshot.getValue(data_Konten.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            konten.setKey(snapshot.getKey());
                            dataKonten.add(konten);
                        }

                        //Inisialisasi Adapter dan data Rawat dalam bentuk Array
                        adapter = new KontenViewAdapter(dataKonten, com.example.beparent.AktivitasActivity.this);

                        //Memasang Adapter pada RecyclerView
                        recyclerView.setAdapter(adapter);

                        Toast.makeText(getApplicationContext(),"Data Berhasil Dimuat", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
              /*
                Kode ini akan dijalankan ketika ada error dan
                pengambilan data error tersebut lalu memprint error nya
                ke LogCat
               */
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat", Toast.LENGTH_LONG).show();
                        Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
        Log.d(TAG, "Umur: "  + filterumur+ ", email " );
    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private void MyRecyclerView(){
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        LinearLayoutManager  layoutManager1 = new LinearLayoutManager(com.example.beparent.AktivitasActivity.this);
        recyclerView.setLayoutManager(layoutManager1);
        recyclerView.setHasFixedSize(true);



        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);

        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (status[position].equalsIgnoreCase("Anak ke 1")){
            mDatabase.child(GetUserID).child("Anak").child("Anak1").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    Anak user = dataSnapshot.getValue(Anak.class);
                    namaAnak = user.getNamaAnak();
                    user.getTgllahir();
                    user.setTgllahir(user.getTgllahir());
                    user.setUsia(user.getUsia());
                    user.setKlaminAnak(user.getKlaminAnak());
                    usiaAnak = user.getUsia();
                    textNamaAnak.setText(namaAnak);
                    textUmur.setText(usiaAnak + " Bulan");

                    MyRecyclerView();
                    GetData();

                    if (user.getKlaminAnak().equalsIgnoreCase("Perempuan")){
                        AvatarAnak.setImageResource(R.drawable.avatarperempuan);
                    }else {
                        AvatarAnak.setImageResource(R.drawable.avatarlaki);
                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });



        }else if (status[position].equalsIgnoreCase("Anak ke 2")  ){
            mDatabase.child(GetUserID).child("Anak").child("Anak2").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {
                        //user exists, do something
                        Anak user = dataSnapshot.getValue(Anak.class);
                        namaAnak = user.getNamaAnak();
                        user.getTgllahir();
                        user.setTgllahir(user.getTgllahir());
                        user.setUsia(user.getUsia());
                        usiaAnak = user.getUsia();
                        textNamaAnak.setText(namaAnak);
                        textUmur.setText(usiaAnak + " Bulan");

                        MyRecyclerView();
                        GetData();

                        if (user.getKlaminAnak().equalsIgnoreCase("Perempuan")){
                            AvatarAnak.setImageResource(R.drawable.avatarperempuan);
                        }else {
                            AvatarAnak.setImageResource(R.drawable.avatarlaki);
                        }
                        //Anak user = dataSnapshot.getValue(Anak.class);

                    } else {
                        //user does not exist, do something else
                        Toast.makeText(getApplicationContext(),"Data Gagal Dimuat, Anda Hanya punya 1 anak, Silahkan PIlih Anak ke 1", Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                }
            });



        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
