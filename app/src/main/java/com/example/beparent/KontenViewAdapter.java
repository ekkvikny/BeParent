package com.example.beparent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class KontenViewAdapter extends RecyclerView.Adapter<KontenViewAdapter.ViewHolder>{


    //Deklarasi Variable
    private ArrayList<data_Konten> listKonten;
    private Context context;
    private ProgressBar progressBar;

    //Membuat Konstruktor, untuk menerima input dari Database
    public KontenViewAdapter(ArrayList<data_Konten> listKonten, Context context) {
        this.listKonten = listKonten;
        this.context = context;

    }

    //ViewHolder Digunakan Untuk Menyimpan Referensi Dari View-View
    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView Judul, isiKonten;
        private  ImageView image;
        private LinearLayout ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            //Menginisialisasi View-View yang terpasang pada layout RecyclerView kita
            image = itemView.findViewById(R.id.gambar);
            Judul = itemView.findViewById(R.id.text_kontenjudul);
            isiKonten = itemView.findViewById(R.id.text_viewkonten);
            ListItem = itemView.findViewById(R.id.list_item);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Membuat View untuk Menyiapkan dan Memasang Layout yang Akan digunakan pada RecyclerView
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Mengambil Nilai/Value yenag terdapat pada RecyclerView berdasarkan Posisi Tertentu
        final String Judul = listKonten.get(position).getJudulKonten();
        final String isiKonten = listKonten.get(position).getIsiKOnten();
        //final String Jurusan = listMahasiswa.get(position).getJurusan();

        //Memasukan Nilai/Value kedalam View (TextView: NIM, Nama, Jurusan)
        holder.Judul.setText(Judul);
        String isilite= isiKonten.substring(0,70)+ "....lihat detail";
        holder.isiKonten.setText(isilite);
        String currentUrl = listKonten.get(position).getImage_url();

        Uri uri = Uri.parse(currentUrl);

        Glide.with(context).load(uri).into(holder.image);

        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("dataJudul", listKonten.get(position).getJudulKonten());
                bundle.putString("dataKategori", listKonten.get(position).getKategoriKonten());
                bundle.putString("dataIsi", listKonten.get(position).getIsiKOnten());
                bundle.putString("dataImage", listKonten.get(position).getImage_url());
                //bundle.putString("getPrimaryKey", listKonten.get(position).getKey());

                Intent intent = new Intent(v.getContext(), LihatKontenActivity.class);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });


    }



    @Override
    public int getItemCount() {
        //Menghitung Ukuran/Jumlah Data Yang Akan Ditampilkan Pada RecyclerView
        return listKonten.size();
    }

}