package com.example.dstmusicplayer;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link playlistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class playlistFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<String> songIdList;
    private Button btnDSPNhac;
    private utf8 utf8;

    public playlistFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment playlistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static playlistFragment newInstance(String param1, String param2) {
        playlistFragment fragment = new playlistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_playlist, container, false);
//        String id1 = "L21udC9zaGFyZWQvUGljdHVyZXMvQ8O0IMSQxqFuIGdp4buvYSBjdeG7mWMgdMOsbmggdjIgLSBU SC5tcDM= ";
//        String id2 = "L21udC9zaGFyZWQvUGljdHVyZXMvTuG6v3UgLSBUaMOhaSBIb8OgbmcubXAz ";
//        String id3 = "L21udC9zaGFyZWQvUGljdHVyZXMvSOG6oW5oIFBow7pjIMSQw7MgRW0gS2jDtG5nIEPDsyAoVmVyc2lvbiAyKSAtIFRow6FpIEhvw6BuZyBSZW1peC5tcDM= ";
//
//        songIdList = new ArrayList<>();
//        songIdList.add(utf8.decodeString(id1));
//        songIdList.add(utf8.decodeString(id2));
//        songIdList.add(utf8.decodeString(id3));
//
//        btnDSPNhac = view.findViewById(R.id.btnDSPNhac);
//        btnDSPNhac.setOnClickListener(v ->{
//            Intent intent = new Intent(getContext(), MainActivity.class);
//            intent.putStringArrayListExtra("songList", songIdList);
//            getContext().startActivity(intent);
//        });

        btnDSPNhac = view.findViewById(R.id.btnDSPNhac);
        btnDSPNhac.setOnClickListener(v ->{
//            String songID = phatNhacActivity.getCurrentSongId();
//            Toast.makeText(getContext(), songID, Toast.LENGTH_SHORT).show();
        });

        return view;
    }
}