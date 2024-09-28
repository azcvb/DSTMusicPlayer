package entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "PlayList")
public class Playlist {
    @NotNull
    @PrimaryKey
    String id_Playlist;

    String tenPlaylist;
    int soLuongBaiHat;
    String tongThoiGian;

    public @NotNull String getId_Playlist() {
        return id_Playlist;
    }

    public void setId_Playlist(@NotNull String id_Playlist) {
        this.id_Playlist = id_Playlist;
    }

    public String getTenPlaylist() {
        return tenPlaylist;
    }

    public void setTenPlaylist(String tenPlaylist) {
        this.tenPlaylist = tenPlaylist;
    }

    public int getSoLuongBaiHat() {
        return soLuongBaiHat;
    }

    public void setSoLuongBaiHat(int soLuongBaiHat) {
        this.soLuongBaiHat = soLuongBaiHat;
    }

    public String getTongThoiGian() {
        return tongThoiGian;
    }

    public void setTongThoiGian(String tongThoiGian) {
        this.tongThoiGian = tongThoiGian;
    }
}
