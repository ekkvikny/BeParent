package com.example.beparent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

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

public class InformasiAnakActivity extends AppCompatActivity  {

    //Deklarasi Variable untuk RecyclerView
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ProgressBar progressBar;
    private RecyclerView.LayoutManager layoutManager;

    //Deklarasi Variable Database Reference dan ArrayList dengan Parameter Class Model kita.
    private DatabaseReference reference;
    private ArrayList<Anak> dataAnak;

    private FirebaseAuth auth;



    private String GetUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R .layout.activity_infromasi_anak);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        GetUserID = user.getUid();

        getSupportActionBar().setTitle("Informasi Anak");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_anak);
        progressBar = findViewById(R.id.progressBar);


        MyRecyclerView();
        GetData();


    }

    //Berisi baris kode untuk mengambil data dari Database dan menampilkannya kedalam Adapter
    private void GetData(){
        Toast.makeText(getApplicationContext(),"Mohon Tunggu Sebentar...", Toast.LENGTH_LONG).show();
        //Mendapatkan Referensi Database
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("users")
                .child(GetUserID)
                .child("Anak")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataAnak = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            Anak konten = snapshot.getValue(Anak.class);

                            //Mengambil Primary Key, digunakan untuk proses Update dan Delete
                            konten.setKey(snapshot.getKey());
                            dataAnak.add(konten);
                        }

                        //Inisialisasi Adapter dan data Mahasiswa dalam bentuk Array
                        adapter = new RecyclerViewAdapter1(dataAnak, InformasiAnakActivity.this);

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
                        Log.e("ListAnakActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

    //Methode yang berisi kumpulan baris kode untuk mengatur RecyclerView
    private void MyRecyclerView(){
        //Menggunakan Layout Manager, Dan Membuat List Secara Vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);



        //Membuat Underline pada Setiap Item Didalam List
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);

        progressBar.setVisibility(View.GONE);
    }



}
