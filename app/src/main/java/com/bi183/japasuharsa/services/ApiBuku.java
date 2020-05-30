package com.bi183.japasuharsa.services;

import com.bi183.japasuharsa.model.ResponseData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiBuku {

    @FormUrlEncoded
    @POST("insert.php")
    Call<ResponseData> addData(
            @Field("judul") String judul,
            @Field("penulis") String penulis,
            @Field("tahun") String tahun,
            @Field("penerbit") String penerbit,
            @Field("photo") String photo,
            @Field("jenis") String jenis
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseData> editData(
            @Field("id") String id,
            @Field("judul") String judul,
            @Field("penulis") String penulis,
            @Field("tahun") String tahun,
            @Field("penerbit") String penerbit,
            @Field("photo") String photo,
            @Field("jenis") String jenis
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseData> deleteData(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("search.php")
    Call<ResponseData> searchData(
            @Field("search") String keyword
    );

    @GET("getdata.php")
    Call<ResponseData> getData();
}
