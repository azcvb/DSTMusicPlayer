package com.example.dstmusicplayer;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MiniPlayerFragment extends Fragment {

    private TextView tvMiniSongTitle, tvMiniArtist;
    private ImageButton btnMiniPlayPause, btnMiniExpand, btnMiniNext, btnMiniPrevious;
    private MusicService musicService;
    private boolean isServiceBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isServiceBound = true;
            updateMiniPlayerUI();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isServiceBound = false;
        }
    };

    public void updateUI(String fileNhac) {
        if (musicService != null) {
            musicService.setFilePath(fileNhac);
            updateMiniPlayerUI(); // Cập nhật lại UI
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mini_player, container, false);
        tvMiniArtist = view.findViewById(R.id.tenNgheSimini);
        tvMiniSongTitle = view.findViewById(R.id.tenBaiHatmini);
        btnMiniPlayPause = view.findViewById(R.id.btnPlayPausemini);
        btnMiniExpand = view.findViewById(R.id.btnExpandmini);
        btnMiniNext = view.findViewById(R.id.btnNextmini);
        btnMiniPrevious = view.findViewById(R.id.btnPreviousmini);
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        tvMiniSongTitle.setSelected(true);
        tvMiniArtist.setSelected(true);

        btnMiniPlayPause.setOnClickListener(v -> {
            if (musicService != null) {
                if (musicService.isPlaying()) {
                    musicService.pauseMusic();
                    btnMiniPlayPause.setImageResource(R.drawable.play);
                } else {
                    musicService.resumeMusic();
                    btnMiniPlayPause.setImageResource(R.drawable.pause);
                }
                updateMiniPlayerUI();
            }
        });

        btnMiniNext.setOnClickListener(v ->{
            if(musicService != null){
                musicService.playNext(true);
                updateMiniPlayerUI();
            }
        });

        btnMiniPrevious.setOnClickListener(v ->{
            if(musicService != null){
                musicService.playPrevious();
                updateMiniPlayerUI();
            }
        });


        btnMiniExpand.setOnClickListener(v -> {
            if (musicService != null) {
                Intent expandIntent = new Intent(getActivity(), PhatNhacActivity.class);
                expandIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(expandIntent);
            }
        });

        return view;
    }

    private void updateMiniPlayerUI() {
        if (musicService != null) {
            tvMiniSongTitle.setText(musicService.getCurrentSongTitle());
            tvMiniArtist.setText(musicService.getCurrentSongArtist());
            btnMiniPlayPause.setImageResource(musicService.isPlaying() ? R.drawable.pause : R.drawable.play);
            Bitmap albumArt = musicService.getCurrentAlbumArt();
            if (albumArt != null) {
                btnMiniExpand.setImageBitmap(albumArt);
            } else {
                btnMiniExpand.setImageResource(R.drawable.default_item);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isServiceBound && musicService != null) {
            updateMiniPlayerUI(); // Cập nhật UI khi fragment quay lại
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isServiceBound) {
            getActivity().unbindService(serviceConnection);
            isServiceBound = false;
        }
    }
}
