package com.example.dstmusicplayer;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class MusicService extends Service {

    public static final int NO_REPEAT = 0;
    public static final int REPEAT_ONE = 1;
    public static final int REPEAT_ALL = 2;
    private int repeatMode = NO_REPEAT;

    private MediaPlayer mediaPlayer;
    private final IBinder binder = new MusicBinder();
    private int currentPosition = 0;
    private String currentFilePath;
    private String currentSongTitle;
    private String currentSongArtist;
    private Bitmap currentAlbumArt;
    private String filePath;
    private ArrayList<String> playlist = new ArrayList<>();
    private int currentSongIndex = 0;
    private float[] playbackSpeeds = {0.25f, 0.5f, 0.75f, 1.0f, 1.25f, 1.5f, 1.75f, 2.0f};
    private int currentSpeedIndex = 3; //Tốc độ mặc định


    public void setPlaybackSpeed(int index) {
        if (mediaPlayer != null && index >= 0 && index < playbackSpeeds.length) {
            currentSpeedIndex = index;
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(playbackSpeeds[currentSpeedIndex]));
        }
    }

    public void setRepeatMode(int mode) {
        this.repeatMode = mode;
        if (mode == REPEAT_ONE && mediaPlayer != null) {
            mediaPlayer.setLooping(true);
        } else if (mediaPlayer != null) {
            mediaPlayer.setLooping(false);
        }
    }

    public int getRepeatMode() {
        return repeatMode;
    }


    public void setFilePath(String path) {
        this.filePath = path;
        this.currentFilePath = path;

        if (mediaPlayer != null) {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(filePath);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        updateSongInfo(filePath);
    }

    public void setPlaylist(ArrayList<String> playlist, int currentSongIndex) {
        this.playlist = playlist;
        this.currentSongIndex = currentSongIndex;
        if(mediaPlayer != null){
        mediaPlayer.reset();
        startMusic(playlist.get(0));
        }else{
            startMusic(playlist.get(0));
        }
    }

    public void clearDanhSachPhat() {
        if (playlist != null) {
            playlist.clear();
        }
        currentSongIndex = -1;
    }



    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void startMusic(String filePath) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.reset();
        }
        this.filePath = filePath;
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            currentFilePath = filePath;
            updateSongInfo(filePath);
            mediaPlayer.setLooping(repeatMode == REPEAT_ONE);
            mediaPlayer.setOnCompletionListener(mp -> {
                if (repeatMode == REPEAT_ONE) {
                    startMusic(currentFilePath);
                } else {
                    playNext(false);
                }
            });

            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playNext(boolean isManual) {
        if (playlist != null && !playlist.isEmpty()) {

            currentSongIndex++;
            // Kiểm tra chế độ lặp và hành động của người dùng
            if (currentSongIndex >= playlist.size()) {
                if (repeatMode == NO_REPEAT) {
                    if (isManual) {
                        currentSongIndex = 0;
                    } else {
                        stopMusic();
                        return;
                    }
                } else if (repeatMode == REPEAT_ALL) {
                    currentSongIndex = 0;
                }
            }
            seekTo(0);
            setFilePath(playlist.get(currentSongIndex));
            startMusic(currentFilePath);

            if (songChangeListener != null) {
                songChangeListener.onSongChanged(currentFilePath);
            }
        }
    }

    public void playPrevious() {
        if (playlist != null && !playlist.isEmpty()) {
            currentSongIndex--;
            if (currentSongIndex < 0) {
                currentSongIndex = playlist.size() - 1;
            }
            seekTo(0);
            setFilePath(playlist.get(currentSongIndex));
            startMusic(currentFilePath);
        }
    }

    private OnSongChangeListener songChangeListener;
    public interface OnSongChangeListener {
        void onSongChanged(String newFilePath);
    }
    public void setOnSongChangeListener(OnSongChangeListener listener) {
        this.songChangeListener = listener;
    }

    private void updateSongInfo(String filePath) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath);

            currentSongTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            if (currentSongTitle == null) {
                currentSongTitle = "Unknown Title";
            }

            currentSongArtist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
            if (currentSongArtist == null) {
                currentSongArtist = "Unknown Artist";
            }

            byte[] art = retriever.getEmbeddedPicture();
            if (art != null) {
                currentAlbumArt = BitmapFactory.decodeByteArray(art, 0, art.length);
            } else {
                currentAlbumArt = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCurrentSongFilePath() {
        return currentFilePath;
    }

    public String getCurrentSongTitle() {
        return currentSongTitle;
    }

    public String getCurrentSongArtist() {
        return currentSongArtist;
    }

    public Bitmap getCurrentAlbumArt() {
        return currentAlbumArt;
    }


    public void pauseMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            currentPosition = mediaPlayer.getCurrentPosition();
            mediaPlayer.pause();
        }
    }


    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }

    public void resumeMusic() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.seekTo(currentPosition);
            mediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }

    public int getCurrentPosition() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            return mediaPlayer.getCurrentPosition();
        }
        return 0;
    }


    public void seekTo(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(position);
        }
    }

    public int getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        }
        return 0;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
