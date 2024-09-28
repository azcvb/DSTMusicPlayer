package com.example.dstmusicplayer;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.example.dstmusicplayer.databinding.FragmentPlaylistBinding;

import java.util.ArrayList;
import java.util.List;

import connectDB.SongData;
import entity.Playlist;
import entity.Song;

public class playlistFragment extends Fragment {

    public playlistFragment() {
        // Required empty public constructor
    }

    private FragmentPlaylistBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private CustomPlaylistAdapter adapter;
    private SongData db;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlaylistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initAction();
    }

    private void initAction() {
        binding.btnCreate.setOnClickListener(view -> {
            showDialogThem();
        });
    }
    private void initData() {
        db = Room.databaseBuilder(requireContext().getApplicationContext(),
                        SongData.class, "music.db")
                .allowMainThreadQueries()
                .build();
        ArrayList<Playlist> songs = new ArrayList<>();
        songs.addAll(db.playlistdao().getAllPlaylist());

        adapter = new CustomPlaylistAdapter(requireContext(), songs, new CustomPlaylistAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Playlist song) {
                Intent intent = new Intent(requireContext(),ActivityDetailPlaylist.class);
                intent.putExtra("ID_PLAY_LIST",song.getId_Playlist());
//                startActivity(intent);
                addToPlaylistLauncher.launch(intent);
            }

            @Override
            public void onItemClickMore(Playlist song) {
                showDialogPrPlaylist(song);
                //todo remove play list
//                showDialogEdit(song);
//                deletePlayList(song);
            }
        });
        binding.listMusicCanAdd.setAdapter(adapter);
    }
    private void showDialogThem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_create_playlist, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        EditText edtTen = view.findViewById(R.id.edt_ten_playlist);
        Button btntao = view.findViewById(R.id.btntao);

        btntao.setOnClickListener(view1 -> {
            String ten = edtTen.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên Playlist", Toast.LENGTH_SHORT).show();
                return;
            }

            // Đặt số bài hát và thời gian bằng 0
            int soBaiHat = 0;
            String thoiGian = "";
            Playlist playlist = new Playlist();
            playlist.setId_Playlist(String.valueOf(System.currentTimeMillis()));
            playlist.setTenPlaylist(ten);
            playlist.setSoLuongBaiHat(soBaiHat);
            playlist.setTongThoiGian(thoiGian);

            new Thread(()->{
                SongData.getInstance(requireContext()).playlistdao().insert(playlist);
            }).start();
            initData();
            Toast.makeText(getContext(), "Thêm thành công", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private ActivityResultLauncher<Intent>   // Đăng ký ActivityResultLauncher
            addToPlaylistLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    initData();
                }
            }
    );
    private void showDialogEdit(Playlist song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_playlist, null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();

        EditText edtTen = view.findViewById(R.id.edt_new_name);
        Button btnUpdate = view.findViewById(R.id.btn_update);
        Button btnCancel = view.findViewById(R.id.btn_cancel);

        btnCancel.setOnClickListener(v->{
            dialog.dismiss();
        });
        btnUpdate.setOnClickListener(view1 -> {
            String ten = edtTen.getText().toString().trim();

            if (ten.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập tên Playlist", Toast.LENGTH_SHORT).show();
                return;
            }

            updatePlayList(song,ten);

            initData();
            Toast.makeText(getContext(), "Sửa thành công", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }

    private void updatePlayList(Playlist playlistItem,String name){
        Playlist playlist =  db.playlistdao().getPlaylistItem(playlistItem.getId_Playlist());
        playlist.setTenPlaylist(name);
        db.playlistdao().update(playlist);
    }

    private void confirmDeletePlayList(Playlist playlistItem) {
        // Tạo hộp thoại xác nhận
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xóa playlist");
        builder.setMessage("Bạn có chắc chắn muốn xóa playlist này không?");

        // Thiết lập nút "Xóa"
        builder.setPositiveButton("Xóa", (dialog, which) -> {
            // Nếu người dùng chọn "Xóa", tiến hành xóa playlist
            deletePlayList(playlistItem);
        });

        // Thiết lập nút "Hủy"
        builder.setNegativeButton("Hủy", (dialog, which) -> {
            // Đóng hộp thoại nếu người dùng chọn "Hủy"
            dialog.dismiss();
        });

        // Hiển thị hộp thoại
        builder.show();
    }

    private void deletePlayList(Playlist playlistItem) {
        Playlist playlist = db.playlistdao().getPlaylistItem(playlistItem.getId_Playlist());

        // Chạy thao tác xóa trong một luồng riêng
        new Thread(() -> {
            SongData.getInstance(requireContext()).playlistdao().delete(playlist);
            // Cập nhật UI hoặc danh sách sau khi xóa
            getActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                initData(); // Cập nhật lại danh sách playlist sau khi xóa
            });
        }).start();
    }

    private void showDialogPrPlaylist(Playlist song) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String[] options = {"Đổi tên", "Xóa"};

        builder.setItems(options, (dialog, which) -> {
            if (which == 0) {
                showDialogEdit(song);
            } else if (which == 1) {
                confirmDeletePlayList(song);
            }
        });

        builder.show();
    }
}