package com.example.dstmusicplayer;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import connectDB.SongData;
import entity.Song;

public class Chitiet {
    public void showDialog(Context context, Song song) {
        // Tạo dialog
        final Dialog detaildialog = new Dialog(context);
        detaildialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        detaildialog.setContentView(R.layout.info_dialog); // Layout đã tạo

        // Ánh xạ các phần tử
        EditText edtSongName = detaildialog.findViewById(R.id.edtsongname);
        EditText edtArtistName = detaildialog.findViewById(R.id.edtartistname);
        EditText edtReleaseYear = detaildialog.findViewById(R.id.edtreleaseyear);
        TextView tvDuration = detaildialog.findViewById(R.id.edtduration);
        TextView tvPlayCount = detaildialog.findViewById(R.id.edtplaycount);
        Button btnEdit = detaildialog.findViewById(R.id.btnedit);
        Button btnSave = detaildialog.findViewById(R.id.btnsave);

        // Ánh xạ nút thoát
        ImageView btnClose = detaildialog.findViewById(R.id.btnClose);

        // Điền dữ liệu vào các EditText và TextView từ đối tượng song
        edtSongName.setText(song.getTenBaiHat());
        edtArtistName.setText(song.getTenNgheSi());
        edtReleaseYear.setText(song.getNamPhatHanh());
        tvDuration.setText(song.getThoiGianNghe());
        tvPlayCount.setText(String.valueOf(song.getSoLanNghe()));

        // Nút thoát đóng dialog
        btnClose.setOnClickListener(v -> detaildialog.dismiss());

        // Chỉnh sửa
        btnEdit.setOnClickListener(v -> {
            // Cho phép chỉnh sửa các trường
            edtSongName.setEnabled(true);
            edtArtistName.setEnabled(true);
            edtReleaseYear.setEnabled(true);

            // Hiển thị nút Lưu
            btnSave.setVisibility(View.VISIBLE);
        });

        // Lưu thông tin khi nhấn nút Lưu
        // Lưu thông tin khi nhấn nút Lưu
        btnSave.setOnClickListener(v -> {
            // Lấy dữ liệu từ các trường sau khi chỉnh sửa
            String newSongName = edtSongName.getText().toString();
            String newArtistName = edtArtistName.getText().toString();
            String newReleaseYear = edtReleaseYear.getText().toString();

            // Tạo biến kiểm tra xem có cập nhật thành công hay không
            boolean isUpdated = false;

            try {

                SongData db = SongData.getInstance(context);
                db.songDao().updateSongById(song.getId_BaiHat(), newSongName, newArtistName, newReleaseYear);
                isUpdated = true;
            } catch (Exception e) {
                e.printStackTrace(); // Ghi lại lỗi nếu có xảy ra
            }

            // Kiểm tra kết quả cập nhật và xử lý thông báo
            if (isUpdated) {
                Toast.makeText(context, "Cập nhật thành công", Toast.LENGTH_SHORT).show();

                // Cập nhật lại đối tượng song với dữ liệu mới
                song.setTenBaiHat(newSongName);
                song.setTenNgheSi(newArtistName);
                song.setNamPhatHanh(newReleaseYear);

                // Load lại dialog với thông tin mới
                edtSongName.setText(song.getTenBaiHat());
                edtArtistName.setText(song.getTenNgheSi());
                edtReleaseYear.setText(song.getNamPhatHanh());

                // Tắt chế độ chỉnh sửa sau khi lưu
                edtSongName.setEnabled(false);
                edtArtistName.setEnabled(false);
                edtReleaseYear.setEnabled(false);

                // Ẩn nút Lưu
                btnSave.setVisibility(View.GONE);
            } else {
                Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });


        detaildialog.show();
    }
}
