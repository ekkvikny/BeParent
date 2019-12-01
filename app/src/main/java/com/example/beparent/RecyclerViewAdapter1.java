package com.example.beparent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter1 extends RecyclerView.Adapter<RecyclerViewAdapter1.ViewHolder> {


    //Deklarasi Variable
    private ArrayList<Anak> listAnak;
    private Context context;
    private ProgressBar progressBar;

    //Membuat Konstruktor, untuk menerima input dari Database
    public RecyclerViewAdapter1(ArrayList<Anak> listAnak, Context context) {
        this.listAnak = listAnak;
        this.context = context;
        //listener = (InformasiAnakActivity) context;
    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder {

        private EditText NamaAnak, tglLahir;
        private TextView kelaminAnak;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            NamaAnak = itemView.findViewById(R.id.text_profilanak);
            tglLahir = itemView.findViewById(R.id.text_edittgllahir);
            kelaminAnak = itemView.findViewById(R.id.text_profilkelaminanak);
            ListItem = itemView.findViewById(R.id.list_item_anak);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_anak, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String NamaAnak = listAnak.get(position).getNamaAnak();
        final String tglLahir = listAnak.get(position).getTgllahir();
        final String jenisKelamin = listAnak.get(position).getKlaminAnak();

        //final String Jurusan = listMahasiswa.get(position).getJurusan();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.NamaAnak.setText(NamaAnak);
        holder.tglLahir.setText(tglLahir);
        holder.kelaminAnak.setText(jenisKelamin);


        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("dataNama", listAnak.get(position).getNamaAnak());
                //bundle.putString("dataKategori", listKonten.get(position).getKategoriKonten());
                bundle.putString("datatglLahir", listAnak.get(position).getTgllahir());
                bundle.putString("dataKelamin", listAnak.get(position).getKlaminAnak());
                bundle.putString("getPrimaryKey", listAnak.get(position).getKey());

                Intent intent = new Intent(v.getContext(), EditAnakActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listAnak.size();
    }
}