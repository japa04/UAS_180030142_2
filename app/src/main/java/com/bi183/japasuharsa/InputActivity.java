package com.bi183.japasuharsa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.bi183.japasuharsa.model.ResponseData;
import com.bi183.japasuharsa.services.ApiClient;
import com.bi183.japasuharsa.services.ApiBuku;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InputActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private EditText editJudul, editPenulis, editTahun, editPenerbit;
    private Button btnSave;
    private RadioButton rbNovel, rbPengetahuan;
    private CircleImageView imgProfilePicture;
    private String imgLocation;
    private boolean updateOperation = false;
    private String jenis;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        progressDialog = new ProgressDialog(InputActivity.this);

        editJudul = findViewById(R.id.edit_judul);
        editPenulis = findViewById(R.id.edit_penulis);
        editTahun = findViewById(R.id.edit_tahun);
        editPenerbit = findViewById(R.id.edit_penerbit);
        btnSave = findViewById(R.id.btn_simpan);
        imgProfilePicture = findViewById(R.id.profile_image);
        rbNovel = findViewById(R.id.jenis_novel);
        rbPengetahuan = findViewById(R.id.jenis_pengetahuan);

        Intent receivedData = getIntent();
        Bundle data = receivedData.getExtras();
        if(data.getString("OPERATION").equals("insert")) {
            updateOperation = false;
        } else {
            updateOperation = true;
            id = data.getInt("ID");
            editJudul.setText(data.getString("JUDUL"));
            editPenulis.setText(data.getString("PENULIS"));
            editTahun.setText(data.getString("TAHUN"));
            editPenerbit.setText(data.getString("PENERBIT"));
            imgLocation = data.getString("IMAGE");
            jenis = data.getString("JENIS");
            if(jenis.equals(getString(R.string.jenis_novel)))
                rbNovel.setChecked(true);
            else
                rbPengetahuan.setChecked(true);
            if(!imgLocation.equals(null)){
                Picasso.Builder builder = new Picasso.Builder(getApplicationContext());
                builder.downloader(new OkHttp3Downloader(getApplicationContext()));
                builder.build().load(imgLocation)
                        .placeholder((R.drawable.ic_launcher_background))
                        .error(R.drawable.ic_launcher_background)
                        .into(imgProfilePicture);
            }
            imgProfilePicture.setContentDescription(imgLocation);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_input, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.item_menu_delete) {
            deleteData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem item = menu.findItem(R.id.item_menu_delete);

        if(updateOperation==true) {
            item.setEnabled(true);
            item.getIcon().setAlpha(255);
        } else{
            item.setEnabled(false);
            item.getIcon().setAlpha(130);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.jenis_novel:
                if (checked) {
                    jenis = getString(R.string.jenis_novel);
                    imgLocation = ApiClient.IMAGE_URL + "Novel.png";
                    break;
                }
            case R.id.jenis_pengetahuan:
                if (checked) {
                    jenis = getString(R.string.jenis_pengetahuan);
                    imgLocation = ApiClient.IMAGE_URL + "BukuPengetahuan.png";
                    break;
                }
        }
    }

    private void saveData() {
        progressDialog.setMessage("Saving...");
        progressDialog.show();

        String judul = editJudul.getText().toString();
        String penulis = editPenulis.getText().toString();
        String tahun = editTahun.getText().toString();
        String penerbit = editPenerbit.getText().toString();
        if(!(judul.equals("") && penulis.equals("") && tahun.equals(""))) {
            ApiBuku api = ApiClient.getRetrofitInstance().create(ApiBuku.class);
            Call<ResponseData> call;
            if(updateOperation == false) {
                call = api.addData(judul, penulis, tahun, penerbit, imgLocation, jenis);
            } else {
                call = api.editData(String.valueOf(id), judul, penulis, tahun, penerbit, imgLocation, jenis);
                updateOperation = false;
            }
            call.enqueue(new Callback<ResponseData>() {
                @Override
                public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                    String value = response.body().getValue();
                    String message = response.body().getMessage();
                    progressDialog.dismiss();
                    if(value.equals("1")) {
                        Toast.makeText(InputActivity.this, "SUKSES: " + message, Toast.LENGTH_LONG).show();
                        finish();
                    } else{
                        Toast.makeText(InputActivity.this, "GAGAL: " + message, Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(InputActivity.this, "Gagal menghubungi server...", Toast.LENGTH_LONG).show();
                    t.printStackTrace();
                    Log.d("Input Data Error", t.toString());

                }
            });
        } else {
            Toast.makeText(this, "Data judul dan tahun harus diberikan", Toast.LENGTH_LONG).show();
        }

    }

    private void deleteData() {
        AlertDialog.Builder builder =new AlertDialog.Builder(this);
        builder.setTitle("Hapus Data");
        builder.setMessage("Apakah anda yakin ingin menghapus data?")
                .setCancelable(false)
                .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.setMessage("Deleting...");
                        progressDialog.show();
                        ApiBuku api = ApiClient.getRetrofitInstance().create(ApiBuku.class);
                        Call<ResponseData> call = api.deleteData(String.valueOf(id));
                        call.enqueue(new Callback<ResponseData>() {
                            @Override
                            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                                String value = response.body().getValue();
                                String message = response.body().getMessage();
                                progressDialog.dismiss();

                                if(value.equals("1")) {
                                    Toast.makeText(InputActivity.this, "SUKSES: " + message, Toast.LENGTH_LONG).show();
                                } else{
                                    Toast.makeText(InputActivity.this, "GAGAL: " + message, Toast.LENGTH_LONG).show();
                                }

                                finish();
                            }

                            @Override
                            public void onFailure(Call<ResponseData> call, Throwable t) {
                                progressDialog.dismiss();
                                Toast.makeText(InputActivity.this, "Gagal menghubungi server...", Toast.LENGTH_SHORT).show();
                                t.printStackTrace();
                                Log.d("Detele Data Error", t.toString());
                            }
                        });
                    }
                }).setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}