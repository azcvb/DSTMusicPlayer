package com.example.dstmusicplayer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SongImage {
    public static Bitmap getImg(String filePath) throws IOException {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Bitmap bitmap = null;
        try {
            mmr.setDataSource(filePath);  // Cung cấp đường dẫn tới file nhạc
            byte[] artBytes = mmr.getEmbeddedPicture();  // Trích xuất ảnh bìa

            if (artBytes != null) {
                // Chuyển đổi byte array thành Bitmap
                bitmap = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mmr.release();  // Giải phóng MediaMetadataRetriever sau khi sử dụng
        }
        return bitmap;  // Trả về Bitmap hoặc null nếu không có ảnh bìa
    }
}
