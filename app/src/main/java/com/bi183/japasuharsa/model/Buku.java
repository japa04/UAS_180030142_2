package com.bi183.japasuharsa.model;

import com.google.gson.annotations.SerializedName;

public class Buku {

    @SerializedName("id")
    private int id;
    @SerializedName("judul")
    private String judul;
    @SerializedName("penulis")
    private String penulis;
    @SerializedName("tahun")
    private String tahun;
    @SerializedName("penerbit")
    private String penerbit;
    @SerializedName("photo")
    private String photo;
    @SerializedName("jenis")
    private String jenis;

    public Buku(int id, String judul, String penulis, String tahun, String penerbit, String photo, String jenis) {
        this.id = id;
        this.judul = judul;
        this.penulis = penulis;
        this.tahun = tahun;
        this.penerbit = penerbit;
        this.photo = photo;
        this.jenis = jenis;
    }

    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getPenulis() {
        return penulis;
    }

    public String getTahun() {
        return tahun;
    }

    public String getPenerbit() {
        return penerbit;
    }

    public String getPhoto() {
        return photo;
    }

    public String getJenis() {
        return jenis;
    }
}
