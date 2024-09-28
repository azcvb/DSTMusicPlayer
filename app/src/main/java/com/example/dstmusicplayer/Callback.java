package com.example.dstmusicplayer;

import java.util.ArrayList;
import java.util.List;

public interface Callback {
    void onPathsRetrieved(ArrayList<String> paths);
}
