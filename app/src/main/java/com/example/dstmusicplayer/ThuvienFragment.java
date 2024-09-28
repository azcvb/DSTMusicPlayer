package com.example.dstmusicplayer;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;
import entity.Song;


public class ThuvienFragment extends Fragment {
    private SongData db;
    private ListView songListView;
    private addSong permissionManager;
    private CustomSongAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(  @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thuvien, container, false);

        db = Room.databaseBuilder(requireContext().getApplicationContext(),
                        SongData.class, "music.db")
                .allowMainThreadQueries()
                .build();

        songListView = view.findViewById(R.id.recycler_songs);
        TextView soBaiHat = view.findViewById(R.id.soBaiHat);

        boolean select = false;
        permissionManager = new addSong(requireActivity(), requireContext(), select);

        if (select) {
            permissionManager.requestPermission();
        } else {
            displaySongs();
        }

        songListView.setOnItemClickListener((adapterView, view1, position, id) -> {
            Toast.makeText(getActivity(), "Selected song: " , Toast.LENGTH_SHORT).show();
        });
        List<Song> songList = db.songDao().getAllSongs();
        int countBaiHat = songList.size();
        soBaiHat.setText(countBaiHat + " Bài Hát");

        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == addSong.REQUEST_CODE_SONG_LIST && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> selectedSongs = data.getStringArrayListExtra("selectedSongs");

            if (selectedSongs != null) {
                displaySongs();
            } else {
                Log.e("MusicFragment", "No selected songs found");
            }
        } else {
            Log.e("MusicFragment", "Request code or result code not matched");
        }
    }


    private void displaySongs() {
        List<Song> songs = db.songDao().getAllSongs();
        adapter = new CustomSongAdapter(requireContext(), songs, song -> {});
        songListView.setAdapter(adapter);
    }


}