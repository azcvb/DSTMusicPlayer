package com.example.dstmusicplayer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;
import entity.Playlist;
import entity.Song;

public class CustomPlaylistAdapter extends ArrayAdapter<Playlist> {
    private final Context context;
    private final List<Playlist> playlists;
    private final SongData dspData;
    private ArrayList<String> listDSP;
    private List<String> idPlayLists;
    private final OnItemClickListener listener;
    private TextView tvName, tvTime,tvCount;

    public interface OnItemClickListener {
        void onItemClick(Playlist song);
        void onItemClickMore(Playlist song);
    }

    public CustomPlaylistAdapter(@NonNull Context context, List<Playlist> songs, OnItemClickListener listener) {
        super(context, R.layout.layout_playlist, songs);
        this.context = context;
        this.playlists = songs;
        this.dspData = SongData.getInstance(context);
        this.listDSP = new ArrayList<>();
        this.idPlayLists = new ArrayList<>();
        this.listener = listener;
        fetchIdBaiHat();
    }


    private void fetchIdBaiHat() {
        new Thread(() -> {
            List<String> idPlayList = SongData.getInstance(getContext()).playlistdao().getAllId();
            if(idPlayList != null) {
                idPlayLists.addAll(idPlayList);

                ((Activity) context).runOnUiThread(() -> {
                    for (String id : idPlayLists) {
                        listDSP.add(id);
                    }
                });
            }
        }).start();

    }

    @SuppressLint("CutPasteId")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_playlist, parent, false);
        }

        Playlist playlist = getItem(position);

        if (playlist != null) {
            tvName = convertView.findViewById(R.id.edt_tenPlayList);
            tvCount = convertView.findViewById(R.id.edt_sobaihat);
            tvTime = convertView.findViewById(R.id.edt_ThoiLuong);

            tvName.setText(playlist.getTenPlaylist());
            tvCount.setText(String.valueOf(playlist.getSoLuongBaiHat()));
            tvTime.setText(String.valueOf(playlist.getTongThoiGian()));
            Log.d("VuLT", "getView: "+playlist.getSoLuongBaiHat());
            Log.d("VuLT", "getView: "+playlist.getTongThoiGian());



        }
        convertView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(playlist);
            }

        });

        return convertView;
    }

}
