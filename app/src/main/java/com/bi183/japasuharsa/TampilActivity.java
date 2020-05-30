package com.bi183.japasuharsa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class TampilActivity extends AppCompatActivity {

    private TextView tvJudul, tvPenulis, tvTahun, tvPenerbit, tvJenis;
    private CircleImageView imgProfilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil);

        tvJudul = findViewById(R.id.tv_judul_buku);
        tvPenulis = findViewById(R.id.tv_penulis);
        tvTahun = findViewById(R.id.tv_tahun_terbit);
        tvPenerbit = findViewById(R.id.tv_penerbit);
        tvJenis = findViewById(R.id.tv_jenis);
        imgProfilePicture = findViewById(R.id.profile_image_display);

        Intent receivedData = getIntent();
        Bundle data = receivedData.getExtras();
        tvJudul.setText(data.getString("JUDUL"));
        tvPenulis.setText(data.getString("PENULIS"));
        tvTahun.setText(data.getString("TAHUN"));
        tvPenerbit.setText(data.getString("PENERBIT"));
        tvJenis.setText(data.getString("JENIS"));
        String imgLocation = data.getString("IMAGE");
        if (!imgLocation.equals(null)) {
            Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
            builder.downloader(new OkHttp3Downloader(getApplicationContext()));
            builder.build().load(imgLocation)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(imgProfilePicture);
        }
        imgProfilePicture.setContentDescription(imgLocation);
    }
}
