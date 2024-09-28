package com.example.dstmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.dstmusicplayer.databinding.ActivityDetailPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;
import entity.LuuPlaylist;
import entity.Song;

public class ActivityDetailPlaylist extends AppCompatActivity {
    private SongData db;
    private ActivityDetailPlaylistBinding binding;
    private CustomSongAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initData();
        binding.ivBack.setOnClickListener(v -> {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });
        binding.btnSelect.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddToPlaylistFragment.class);
            intent.putExtra("ID_PLAY_LIST", idPlayList);
            addToPlaylistLauncher.launch(intent);
        });
    }

    private ActivityResultLauncher<Intent>
            addToPlaylistLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    initData();
                }
            }
    );

    private String idPlayList;
    private ArrayList<Song> songs;

    private List<Song> getSongListFromDatabase(List<LuuPlaylist> data) {
        List<Song> songList = new ArrayList<>();
        if (data != null) {
            for (LuuPlaylist dsp : data) {
                Log.d("idBaiHat", dsp.getId_BaiHat());
                List<Song> songs = db.songDao().getSongId(dsp.getId_BaiHat());
                if (songs != null && !songs.isEmpty()) {
                    songList.addAll(songs);
                }
            }
        } else {

        }
        return songList;
    }

    private void initData() {
        idPlayList = getIntent().getStringExtra("ID_PLAY_LIST");
        db = Room.databaseBuilder(getApplicationContext(),
                        SongData.class, "music.db")
                .allowMainThreadQueries()
                .build();
        ArrayList<LuuPlaylist> luuPlaylists = new ArrayList<>();
        luuPlaylists.addAll(db.luuPlaylistDao().getSongsByPlaylist(idPlayList));
        songs = new ArrayList<>();
        new Thread(() -> {
            songs.addAll(getSongListFromDatabase(luuPlaylists));
        }).start();

        adapter = new CustomSongAdapter(this, songs, song -> {

        });
        binding.recyclerSongs.setAdapter(adapter);
    }
}
