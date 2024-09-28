package com.example.dstmusicplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;

public class AlbumAdapter {

    private Context context;
    private GridLayout gridLayout;
    private ArrayList<String> DSPlist;
    private SongData db;

    // Constructor nhận Context và GridLayout
    public AlbumAdapter(Context context, GridLayout gridLayout) {
        this.context = context;
        this.gridLayout = gridLayout;
    }

    public void addAlbumCard(String albumName, String fragmentName) {

        // Tạo một CardView
        CardView cardView = new CardView(context);
        cardView.setLayoutParams(new GridLayout.LayoutParams());

        // Đặt thuộc tính cho CardView
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.width = 0; // Width chiếm không gian còn lại
        params.height = 300; // Chiều cao
        params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Chiếm 1 cột
        params.rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f); // Chiếm 1 hàng
        params.setMargins(8, 8, 8, 8); // Margin cho CardView
        cardView.setLayoutParams(params);
        cardView.setRadius(7f); // Góc bo tròn
        cardView.setCardElevation(4f);

        // Thêm FrameLayout chứa nội dung
        FrameLayout frameLayout = new FrameLayout(context);
        cardView.addView(frameLayout);
        // Thêm ImageView (hình nền)
        if(fragmentName.equals("Album")) {
            Log.d("aqwe", "album");
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.turntable_retro_vintage_197352_3840x2160);
            frameLayout.addView(imageView);
        }else if(fragmentName == "NgheSi") {
            Log.d("aqwe", "nghesi");
            ImageView imageView = new ImageView(context);
            imageView.setLayoutParams(new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setImageResource(R.drawable.guitar_guitarist_musical_instrument_126078_3840x2160);
            frameLayout.addView(imageView);

        }

        // Thêm lớp phủ màu đen mờ
        View overlay = new View(context);
        overlay.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        overlay.setBackgroundColor(context.getResources().getColor(android.R.color.black));
        overlay.setAlpha(0.5f);
        frameLayout.addView(overlay);

        // Thêm TextView để hiển thị tên album
        TextView textView = new TextView(context);
        FrameLayout.LayoutParams textParams = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.gravity = Gravity.START | Gravity.BOTTOM; // Vị trí dưới bên trái
        textParams.setMargins(16, 16, 16, 80); // Margin dưới lớn hơn để chừa chỗ cho icon play
        textView.setLayoutParams(textParams);
        textView.setText(albumName);
        textView.setTextSize(16f);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setTypeface(null, android.graphics.Typeface.BOLD); // Thiết lập style bold
        frameLayout.addView(textView);

        ImageView playIcon = new ImageView(context);
        FrameLayout.LayoutParams playParams = new FrameLayout.LayoutParams(
                60, 60);
        playParams.gravity = Gravity.START | Gravity.BOTTOM; // Vị trí dưới bên trái
        playParams.setMargins(16, 16, 16, 16); // Khoảng cách cho icon
        playIcon.setLayoutParams(playParams);
        playIcon.setImageResource(R.drawable.baseline_play_circle_filled_24);
        playIcon.setColorFilter(context.getResources().getColor(android.R.color.white));
        frameLayout.addView(playIcon);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentName == "Album") {
                    db = SongData.getInstance(context);
                    new Thread(() -> {
                        List<String> idBaiHat = db.songDao().getBaiHatAlbum(albumName);
                        DSPlist = new ArrayList<>(idBaiHat);
                        MainActivity.getDSPList().clear();
                        MainActivity.setDSPList(DSPlist);
                        Intent intent = new Intent(context, DSPMain.class);
                        context.startActivity(intent);
                    }).start();
                }else if(fragmentName == "NgheSi") {
                    db = SongData.getInstance(context);
                    new Thread(() -> {
                        List<String> idBaiHat = db.songDao().getBaiHatNgheSi(albumName);
                        DSPlist = new ArrayList<>(idBaiHat);
                        MainActivity.getDSPList().clear();
                        MainActivity.setDSPList(DSPlist);
                        Intent intent = new Intent(context, DSPMain.class);
                        context.startActivity(intent);
                    }).start();
                }

            }
        });

        gridLayout.addView(cardView);
    }
}
