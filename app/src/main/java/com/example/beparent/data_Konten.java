package com.example.beparent;


public class data_Konten {

    private String image_url;
    private String judulKonten;
    private String isiKOnten;
    private String kategoriKonten;
    private String key;

    //Konstruktor kosong, untuk data snapshot pada Firebase Realtime Database
    public data_Konten() {}

    public data_Konten(String image_url) {
        this.image_url = image_url;
    }

    public data_Konten(String image_url, String judulKonten, String isiKOnten, String kategoriKonten) {
        this.image_url = image_url;
        this.judulKonten = judulKonten;
        this.isiKOnten = isiKOnten;
        this.kategoriKonten = kategoriKonten;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getJudulKonten() {
        return judulKonten;
    }

    public String getIsiKOnten() {
        return isiKOnten;
    }

    public String getKategoriKonten() {
        return kategoriKonten;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setJudulKonten(String judulKonten) {
        this.judulKonten = judulKonten;
    }

    public void setIsiKOnten(String isiKOnten) {
        this.isiKOnten = isiKOnten;
    }

    public void setKategoriKonten(String kategoriKonten) {
        this.kategoriKonten = kategoriKonten;
    }
}