package com.example.dstmusicplayer;

import android.util.Base64;

public class utf8 {

    public static String encodeString(String songPath) {
//        String path = removeSpaces(songPath);
        return Base64.encodeToString(songPath.getBytes(), Base64.DEFAULT);
    }

    public static String decodeString(String songPath) {
//        String path = removeSpaces(songPath);
        return new String(Base64.decode(songPath, Base64.DEFAULT));
    }

    // Hàm xóa dấu cách
    public static String removeSpaces(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll("\\s+", "");
    }
}

