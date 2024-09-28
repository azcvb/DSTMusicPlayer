package com.example.dstmusicplayer;

public class SearchResult {
    private String title;
    private String artist;
    private String album;

    public SearchResult(String title, String artist, String album) {
        this.title = title;
        this.artist = artist;
        this.album = album;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }
}
