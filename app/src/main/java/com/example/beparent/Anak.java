package com.example.beparent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Anak {
    private String namaAnak, klaminAnak;
    private String Tgllahir;
    private String usia;
    private static DateFormat tanggal, bulan, tahun;
    private static Date date;
    private String key;

    public Anak() {
    }

    public Anak(String namaAnak, String klaminAnak, String tgllahir) {
        this.namaAnak = namaAnak;
        this.klaminAnak = klaminAnak;
        Tgllahir = tgllahir;
    }

    public String getNamaAnak() {
        return namaAnak;
    }

    public void setNamaAnak(String namaAnak) {
        this.namaAnak = namaAnak;
    }

    public String getKlaminAnak() {
        return klaminAnak;
    }

    public void setKlaminAnak(String klaminAnak) {
        this.klaminAnak = klaminAnak;
    }

    public String getTgllahir() {
        return Tgllahir;
    }

    public void setTgllahir(String tgllahir) {
        Tgllahir = tgllahir;
    }


    public String getUsia() {

        tanggal = new SimpleDateFormat("dd");
        bulan = new SimpleDateFormat("MM");
        tahun = new SimpleDateFormat("yyyy");
        date = new Date();

        int tanggalLahir = Integer.parseInt(Tgllahir.substring(0, 2))/30;
        int bulanLahir = Integer.parseInt(Tgllahir.substring(3, 5));
        int tahunLahir = Integer.parseInt(Tgllahir.substring(6, 10))*12;

        int tanggalHari = Integer.parseInt(tanggal.format(date))/30;
        int bulanHari = Integer.parseInt(bulan.format(date));
        int tahunHari = Integer.parseInt(tahun.format(date))*12;

        int totalTahun = (tahunHari - tahunLahir)+(bulanHari-bulanLahir);
        usia = Integer.toString(totalTahun);

        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
