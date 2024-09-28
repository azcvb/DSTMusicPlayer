package com.example.dstmusicplayer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ThuViennFragment extends Fragment {
    private ImageView imgTatCaBaiHat, imgAlbum, imgNgheSi;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thu_vienn, container, false);

        // Ánh xạ các ImageView
        imgTatCaBaiHat = view.findViewById(R.id.tatCaBaiHat);
        imgAlbum = view.findViewById(R.id.album);
        imgNgheSi = view.findViewById(R.id.nghesi);

        // Xử lý sự kiện nhấn cho các ImageView
        imgTatCaBaiHat.setOnClickListener(v -> replaceFragment(new ThuvienFragment()));
        imgAlbum.setOnClickListener(v -> replaceFragment(new AlbumFragment()));
        imgNgheSi.setOnClickListener(v -> replaceFragment(new NgheSiFragment()));

        return view;
    }



    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.addToBackStack(null); // Thêm vào back stack nếu cần
        fragmentTransaction.commit();
    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
