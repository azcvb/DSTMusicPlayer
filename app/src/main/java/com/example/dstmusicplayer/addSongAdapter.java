package com.example.dstmusicplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;

import entity.Song;

public class addSongAdapter extends ArrayAdapter<Song> {
    private final ArrayList<Song> songList;
    private final ArrayList<Song> selectedSongs = new ArrayList<>();
    private final boolean[] checkBoxState;

    public addSongAdapter(@NonNull Context context, @NonNull ArrayList<Song> objects) {
        super(context, 0, objects);
        this.songList = objects;
        this.checkBoxState = new boolean[songList.size()];
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.add_songs, parent, false);
        }

        Song song = getItem(position);
        if (song != null) {
            TextView titleTextView = convertView.findViewById(R.id.BaiHat);
            TextView artistTextView = convertView.findViewById(R.id.CaSi);
            ImageView albumArtImageView = convertView.findViewById(R.id.img);
            CheckBox selectCheckBox = convertView.findViewById(R.id.selectCheckBox);

            titleTextView.setText(song.getTenBaiHat());
            artistTextView.setText(song.getTenNgheSi());

            String songPath = song.getId_BaiHat();
            Bitmap currentAlbumArt;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(utf8.decodeString(songPath));
            byte[] art = retriever.getEmbeddedPicture();
            if (art != null) {
                currentAlbumArt = BitmapFactory.decodeByteArray(art, 0, art.length);
                albumArtImageView.setImageBitmap(currentAlbumArt);
            } else {
                currentAlbumArt = null;
                albumArtImageView.setImageResource(R.drawable.default_item);
            }

            selectCheckBox.setOnCheckedChangeListener(null);

            selectCheckBox.setChecked(checkBoxState[position]);

            selectCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                checkBoxState[position] = isChecked;
                if (isChecked) {
                    selectedSongs.add(song);
                } else {
                    selectedSongs.remove(song);
                }
            });
        }

        return convertView;
    }

    public ArrayList<Song> getSelectedSongs() {
        return selectedSongs;
    }
}
