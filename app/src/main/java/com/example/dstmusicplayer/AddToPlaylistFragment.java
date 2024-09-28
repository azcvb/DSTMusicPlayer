package com.example.dstmusicplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.dstmusicplayer.databinding.FragmentListMusicBinding;

import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;
import entity.LuuPlaylist;
import entity.Playlist;
import entity.Song;

public class AddToPlaylistFragment extends AppCompatActivity {
    private SongData db;
    private addSongAdapter adapter;
    private FragmentListMusicBinding binding;
    private static final String ARG_PARAM1 = "param_id";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentListMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        idPlayList = getIntent().getStringExtra("ID_PLAY_LIST");
        initData();
        initAction();
    }

    private String idPlayList;
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
        }else {
            Toast.makeText(this, "DSPList Null", Toast.LENGTH_SHORT).show();
        }
        return songList;
    }
    ArrayList<Song> songHavePlayList;
    private void initDataPlayList() {
        db = Room.databaseBuilder(getApplicationContext(),
                        SongData.class, "music.db")
                .allowMainThreadQueries()
                .build();
        ArrayList<LuuPlaylist> luuPlaylists = new ArrayList<>();
        luuPlaylists.addAll(db.luuPlaylistDao().getSongsByPlaylist(idPlayList));

        songHavePlayList = new ArrayList<>();
        songHavePlayList.addAll(getSongListFromDatabase(luuPlaylists));

    }
    private void initData() {
        initDataPlayList();

        ArrayList<Song> songs = new ArrayList<>();
        songs.addAll(db.songDao().getAllSongs());
        for (int i =0 ;i<songHavePlayList.size();i++){
            int finalI = i;
            songs.removeIf(song -> song.getId_BaiHat().equals(songHavePlayList.get(finalI).getId_BaiHat()));
        }

        adapter = new addSongAdapter(this, songs);
        binding.recyclerSongs.setAdapter(adapter);
    }
    private void initAction() {
        binding.ivBack.setOnClickListener(v->{finish();});
        binding.btnAdd.setOnClickListener(v->{
            ArrayList<Song> selectedSongs = adapter.getSelectedSongs();
            if (!selectedSongs.isEmpty()) {
                saveSelectedSongsToDB(selectedSongs);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                Toast.makeText(this, "No songs selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePlayList(SongData db,ArrayList<Song> selectedSongs){
        Playlist playlist =  db.playlistdao().getPlaylistItem(idPlayList);
        playlist.setSoLuongBaiHat(playlist.getSoLuongBaiHat()+selectedSongs.size());
        db.playlistdao().update(playlist);
    }

    private void saveSelectedSongsToDB(ArrayList<Song> selectedSongs) {
        new Thread(() -> {
            SongData db = SongData.getInstance(this);
            for (Song song : selectedSongs) {
                LuuPlaylist luuPlaylist = new LuuPlaylist();
                luuPlaylist.setId_Playlist(idPlayList);
                luuPlaylist.setId_BaiHat(song.getId_BaiHat());
                db.luuPlaylistDao().insert(luuPlaylist);
            }
            updatePlayList(db,selectedSongs);
        }).start();
    }
}
