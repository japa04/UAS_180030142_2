package com.bi183.japasuharsa.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.bi183.japasuharsa.InputActivity;
import com.bi183.japasuharsa.R;
import com.bi183.japasuharsa.TampilActivity;
import com.bi183.japasuharsa.model.Buku;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BukuAdapter extends RecyclerView.Adapter<BukuAdapter.BukuViewHolder> {

    private List<Buku> dataBuku;
    private Context context;

    public BukuAdapter(List<Buku> dataBuku, Context context) {
        this.dataBuku = dataBuku;
        this.context = context;
    }

    @NonNull
    @Override
    public BukuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_buku, parent, false);
        return new BukuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BukuViewHolder holder, int position) {
        Buku tempBuku = dataBuku.get(position);
        holder.id = tempBuku.getId();
        holder.judul = tempBuku.getJudul();
        holder.penulis = tempBuku.getPenulis();
        holder.penerbit = tempBuku.getPenerbit();
        holder.jenis = tempBuku.getJenis();
        holder.tvJudul.setText(holder.judul);
        holder.tvTahun.setText(tempBuku.getTahun());
        String imgLocation = tempBuku.getPhoto();
        if(!imgLocation.equals(null)) {
            //Picasso.get().load(imgLocation).resize(64, 64).into(holder.imgAvatar);
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.downloader(new OkHttp3Downloader(context));
            builder.build().load(imgLocation)
                    .placeholder((R.drawable.ic_launcher_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imgAvatar);
        }
        holder.imgAvatar.setContentDescription(tempBuku.getPhoto());
    }

    @Override
    public int getItemCount() {
        return dataBuku.size();
    }

    public class BukuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private TextView tvJudul, tvTahun;
        private CircleImageView imgAvatar;
        private int id;
        private String judul, penulis, penerbit, jenis;

        public BukuViewHolder(@NonNull View itemView) {
            super(itemView);

            tvJudul = itemView.findViewById(R.id.tv_judul);
            tvTahun = itemView.findViewById(R.id.tv_tahun);
            imgAvatar = itemView.findViewById(R.id.profile_image);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent openDisplay = new Intent(context, TampilActivity.class);
            openDisplay.putExtra("JUDUL", judul );
            openDisplay.putExtra("PENULIS", penulis);
            openDisplay.putExtra("TAHUN", tvTahun.getText());
            openDisplay.putExtra("PENERBIT", penerbit);
            openDisplay.putExtra("IMAGE", imgAvatar.getContentDescription());
            openDisplay.putExtra("JENIS", jenis);
            itemView.getContext().startActivity(openDisplay);
        }

        @Override
        public boolean onLongClick(View v) {
            Intent openInput = new Intent(context, InputActivity.class);
            openInput.putExtra("OPERATION", "update");
            openInput.putExtra("ID", id);
            openInput.putExtra("JUDUL", judul);
            openInput.putExtra("PENULIS", penulis);
            openInput.putExtra("TAHUN", tvTahun.getText());
            openInput.putExtra("PENERBIT", penerbit);
            openInput.putExtra("IMAGE", imgAvatar.getContentDescription());
            openInput.putExtra("JENIS", jenis);
            itemView.getContext().startActivity(openInput);
            return true;
        }
    }
}
