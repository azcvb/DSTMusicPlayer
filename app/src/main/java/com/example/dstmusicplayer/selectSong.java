package com.example.dstmusicplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;
import entity.Song;

public class selectSong extends AppCompatActivity {

    private static final int RESULT_ADD = 29;
    private ListView listView;
    private Button sendButton;
    private ImageView img;
    private ArrayList<Song> songList = new ArrayList<>();
    private addSongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_song);

        listView = findViewById(R.id.list_add_songs);
        sendButton = findViewById(R.id.btn_add);
        img = findViewById(R.id.img);

        ArrayList<String> songPaths = getIntent().getStringArrayListExtra("songList");

        if (songPaths != null) {
            getExistingSongPaths(existingSongPaths -> {
                runOnUiThread(() -> {
                    if (existingSongPaths != null) {
                        for (String path : songPaths) {
                            try {
                                if (!existingSongPaths.contains(path)) {
                                    addSongToList(path);
                                }
                            } catch (IOException e) {
                                Log.e("SelectSong", "Error adding song: " + path, e);
                            }
                        }
                    } else {
                        Log.e("SelectSong", "existingSongPaths is null.");
                    }
                    adapter = new addSongAdapter(this, songList);
                    listView.setAdapter(adapter);
                });
            });
        }


        sendButton.setOnClickListener(v -> {
            ArrayList<Song> selectedSongs = adapter.getSelectedSongs();
            if (!selectedSongs.isEmpty()) {
                saveSelectedSongsToDB(selectedSongs);
                finishWithResult();
            } else {
                Toast.makeText(this, "No songs selected", Toast.LENGTH_SHORT).show();
            }
        });

        img.setOnClickListener(view -> finishWithResult());
    }

    private void getExistingSongPaths(final Callback callback) {
        new Thread(() -> {
            ArrayList<String> paths = new ArrayList<>();
            List<Song> existingSongs = SongData.getInstance(this).songDao().getAllSongs();
            for (Song song : existingSongs) {
                String songPath = utf8.decodeString(song.getId_BaiHat());
                paths.add(songPath);
            }
            callback.onPathsRetrieved(paths);
        }).start();
    }

    private void addSongToList(String path) throws IOException {
        File audioFile = new File(path);
        if (audioFile.exists()) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                String songPath = utf8.encodeString(path);
                retriever.setDataSource(path);
                String title = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                String album = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                String year = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR);


                Song song = new Song(songPath, title != null ? title : "Unknown Title", artist != null ? artist : "Unknown Artist",
                        year != null ? year : "Unknown Year", album != null ? album : "Unknown Album", 0, null);
                songList.add(song);
            } catch (Exception e) {
                Log.e("SongInfo", "Error retrieving metadata for: " + path, e);
            } finally {
                retriever.release();
            }
        }
    }

    private void saveSelectedSongsToDB(ArrayList<Song> selectedSongs) {
        new Thread(() -> {
            SongData db = SongData.getInstance(this);
            for (Song song : selectedSongs) {
                db.songDao().insertSong(song);
            }
        }).start();
    }

    private void finishWithResult() {
        Intent resultIntent = new Intent(this, MainActivity.class);
        setResult(RESULT_ADD, resultIntent);
        finish();
    }
}
