package com.example.dstmusicplayer;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import connectDB.SongData;
import entity.Song;

public class DSPMain extends AppCompatActivity {
    private SongData db;
    private RecyclerView recyclerView;
    private DSPAdapter adapter;
    private List<Song> songList;
    private ArrayList<String> DSPList;
    private utf8 utf8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dsp);

        // Khởi tạo cơ sở dữ liệu
        db = Room.databaseBuilder(getApplicationContext(), SongData.class, "music.db")
                .allowMainThreadQueries()
                .build();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        songList = getSongListFromDatabase();
        adapter = new DSPAdapter(this, songList);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                Collections.swap(songList, fromPosition, toPosition);
                adapter.notifyItemMoved(fromPosition, toPosition);
                updateDSPList();
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Không sử dụng swipe
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder.itemView.setElevation(30);
                }
            }

            @Override
            public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                super.clearView(recyclerView, viewHolder);
                viewHolder.itemView.setElevation(0);
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG && isCurrentlyActive) {
                    viewHolder.itemView.setAlpha(0.8f);
                } else {
                    viewHolder.itemView.setAlpha(1.0f);
                }
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);


        ImageView img_more = findViewById(R.id.img_properties);
        img_more.setOnClickListener(view -> {
            ArrayList<String> listGiaiMa = new ArrayList<>();

            for (String item : DSPList) {
                String giaima = utf8.decodeString(item);
                listGiaiMa.add(giaima);
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putStringArrayListExtra("songList", listGiaiMa);
            startActivity(intent);

        });
        ImageView img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(view -> finish());
        ImageView img_shuffle = findViewById(R.id.img_shuffle);
        img_shuffle.setOnClickListener(view -> {

            DSPList = MainActivity.shuffle(DSPList);

            MainActivity.setDSPList(DSPList);

            songList = getSongListFromDatabase();

            updateDSPList();

            adapter = new DSPAdapter(this, songList);
            recyclerView.setAdapter(adapter);

            img_shuffle.setAlpha(0.5f);
            img_shuffle.setElevation(10);

            img_shuffle.postDelayed(() -> {
                img_shuffle.setAlpha(1.0f);
                img_shuffle.setElevation(0);
            }, 200);
        });





    }

    private List<Song> getSongListFromDatabase() {
        DSPList = MainActivity.getDSPList();
        List<Song> songList = new ArrayList<>();
        if (DSPList != null) {
            for (String dsp : DSPList) {
                Log.d("idBaiHat", dsp);
                List<Song> songs = db.songDao().getSongId(dsp);
                if (songs != null && !songs.isEmpty()) {
                    songList.addAll(songs);
                }
            }
        }else {
            Toast.makeText(this, "DSPList Null", Toast.LENGTH_SHORT).show();
        }
        return songList;
    }

    private void updateDSPList() {
        DSPList.clear();
        for (Song song : songList) {
            DSPList.add(song.getId_BaiHat());
        }
    }
}
