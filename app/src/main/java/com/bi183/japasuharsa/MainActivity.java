package com.bi183.japasuharsa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.bi183.japasuharsa.adapter.BukuAdapter;
import com.bi183.japasuharsa.model.Buku;
import com.bi183.japasuharsa.model.ResponseData;
import com.bi183.japasuharsa.services.ApiClient;
import com.bi183.japasuharsa.services.ApiBuku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private BukuAdapter bukuAdapter;
    private RecyclerView rvShowBuku;
    private ProgressDialog progressDialog;
    private List<Buku> dataBuku = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent openInput = new Intent(getApplicationContext(), InputActivity.class);
                openInput.putExtra("OPERATION", "insert");
                startActivity(openInput);
            }
        });

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Memuat data...");

        rvShowBuku = findViewById(R.id.rv_tampil);

        showBukuData();
    }

    private void showBukuData() {
        progressDialog.show();
        ApiBuku api = ApiClient.getRetrofitInstance().create(ApiBuku.class);
        Call<ResponseData> call = api.getData();
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                String value = response.body().getValue();

                if(value.equals("1")) {
                    dataBuku = response.body().getResult();
                    bukuAdapter = new BukuAdapter(dataBuku, getApplicationContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    rvShowBuku.setLayoutManager(layoutManager);
                    rvShowBuku.setAdapter(bukuAdapter);
                }

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    private void searchBuku(String keyword) {
        ApiBuku api = ApiClient.getRetrofitInstance().create(ApiBuku.class);
        Call<ResponseData> call = api.searchData(keyword);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                String value = response.body().getValue();

                if(value.equals("1")) {
                    dataBuku = response.body().getResult();
                    bukuAdapter = new BukuAdapter(dataBuku, getApplicationContext());
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                    rvShowBuku.setLayoutManager(layoutManager);
                    rvShowBuku.setAdapter(bukuAdapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem searchData = menu.findItem(R.id.menu_item_search);
        final SearchView searchView = (SearchView)searchData.getActionView();
        searchView.setQueryHint("Searching Buku");
        searchView.setIconified(true);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        showBukuData();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(newText.equals("")) {
            showBukuData();
        } else {
            searchBuku(newText);
        }
        return true;
    }
}
