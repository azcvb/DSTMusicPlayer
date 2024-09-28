package com .example.dstmusicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class addSong {
    private static final int PERMISSION_REQUEST_CODE = 1;
    public static final int REQUEST_CODE_SONG_LIST = 2;

    private Activity activity;
    private Context context;
    private Boolean select;
    public addSong(Activity activity, Context context, boolean select) {
        this.activity = activity;
        this.context = context;
        this.select = select;
    }

    public void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            openSong();
        } else {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
            } else {
                openSong();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openSong();
            } else {
                Log.e("PermissionManager", "Permission denied");
            }
        }
    }

    private ArrayList<String> getAllAudioFromDevice() {
        ArrayList<String> songList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.Media.DATA};

        try (Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    songList.add(path);
                }
            }
        }
        return songList;
    }

    public void openSong() {
        ArrayList<String> songPaths = getAllAudioFromDevice();
        if (songPaths != null && !songPaths.isEmpty() && select == true) {
            Intent intent = new Intent(context, selectSong.class);
            intent.putStringArrayListExtra("songList", songPaths);
            activity.startActivityForResult(intent, REQUEST_CODE_SONG_LIST);
        } else {
            Log.d("PermissionManager", "No songs found on device");
        }
    }
}
