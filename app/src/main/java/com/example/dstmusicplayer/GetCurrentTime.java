package com.example.dstmusicplayer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class GetCurrentTime {
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
