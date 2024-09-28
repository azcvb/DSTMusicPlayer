package com.example.dstmusicplayer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;
import dao.SongDao;
import entity.Song;

public class SearchActivity extends AppCompatActivity {

    private EditText editTextSearch;
    private Button buttonSearch;
    private RecyclerView recyclerViewResults;
    private SearchResultAdapter adapter;
    private List<SearchResult> searchResults;
    private SongDao songDao;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        recyclerViewResults = findViewById(R.id.recyclerViewResults);
        songDao = SongData.getInstance(this).songDao();

        searchResults = new ArrayList<>();
        adapter = new SearchResultAdapter(searchResults);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewResults.setAdapter(adapter);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = editTextSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    searchSongs(query);
                } else {
                    Toast.makeText(SearchActivity.this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void searchSongs(String query) {
        new Thread(() -> {
            List<Song> results = songDao.searchSongs(query);
            runOnUiThread(() -> {
                searchResults.clear();
                for (Song song : results) {
                    searchResults.add(new SearchResult(song.getTenBaiHat(), song.getTenNgheSi(), song.getAlbum()));
                }
                adapter.notifyDataSetChanged();
            });
        }).start();
    }
}
