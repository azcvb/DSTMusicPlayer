package com.example.dstmusicplayer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import connectDB.SongData;

public class NgheSiFragment extends Fragment {
    private AlbumAdapter albumAdapter;
    private GridLayout gridLayout;
    private List<String> ngheSiList;
    private SongData db ;
    private ArrayList<String> DSPlist = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nghe_si, container, false);
        db = SongData.getInstance(getContext());

        gridLayout = view.findViewById(R.id.listAlbum);

        // Khởi tạo AlbumAdapter với context và gridLayout
        db = SongData.getInstance(getContext());
        new Thread(() -> {
            ngheSiList = db.songDao().getAllNgheSi();

            requireActivity().runOnUiThread(() -> {
                if (ngheSiList != null) {
                    albumAdapter = new AlbumAdapter(getContext(), gridLayout);
                    for (String album : ngheSiList) {
                        String trimmedAlbum = album.trim();
                        if (!trimmedAlbum.equals("Unknown Artist") && !trimmedAlbum.isEmpty() ) {
                            if (!DSPlist.contains(trimmedAlbum)) {
                                albumAdapter.addAlbumCard(trimmedAlbum, "NgheSi");
                            }
                        }
                    }
                } else {
                    Log.d("aqwe", "null");
                }
            });
        }).start();



        return view;
    }
}
