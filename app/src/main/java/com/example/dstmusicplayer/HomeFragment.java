package com.example.dstmusicplayer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.example.dstmusicplayer.R;
import com.example.dstmusicplayer.bottomDialog;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Ánh xạ ImageView với id img_properties
        ImageView imgProperties = view.findViewById(R.id.img_properties);

        // Tạo đối tượng bottomDialog
        bottomDialog dialog = new bottomDialog();

        // Set sự kiện click cho ImageView để gọi showBottomDialog
        imgProperties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi showBottomDialog khi click
//                dialog.showBottomDialog(getContext());
            }
        });

        return view;
    }
}
