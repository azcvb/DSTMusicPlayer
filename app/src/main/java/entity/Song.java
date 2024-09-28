package entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "BaiHat")
public class Song {
    @PrimaryKey
    @NonNull
    private String id_BaiHat;

    private String tenBaiHat;
    private String tenNgheSi;
    private String namPhatHanh;
    private String album;
    private int soLanNghe;
    private String thoiGianNghe;

    public Song(@NonNull String id_BaiHat, String tenBaiHat, String tenNgheSi, String namPhatHanh, String album, int soLanNghe, String thoiGianNghe) {
        this.id_BaiHat = id_BaiHat;
        this.tenBaiHat = tenBaiHat;
        this.tenNgheSi = tenNgheSi;
        this.namPhatHanh = namPhatHanh;
        this.album = album;
        this.soLanNghe = soLanNghe;
        this.thoiGianNghe = thoiGianNghe;
    }

    public String getId_BaiHat() {
        return id_BaiHat;
    }

    public void setId_BaiHat(String id_BaiHat) {
        this.id_BaiHat = id_BaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getTenNgheSi() {
        return tenNgheSi;
    }

    public void setTenNgheSi(String tenNgheSi) {
        this.tenNgheSi = tenNgheSi;
    }

    public String getNamPhatHanh() {
        return namPhatHanh;
    }

    public void setNamPhatHanh(String namPhatHanh) {
        this.namPhatHanh = namPhatHanh;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getSoLanNghe() {
        return soLanNghe;
    }

    public void setSoLanNghe(int soLanNghe) {
        this.soLanNghe = soLanNghe;
    }

    public String getThoiGianNghe() {
        return thoiGianNghe;
    }
    public void setThoiGianNghe(String thoiGianNghe) {
        this.thoiGianNghe = thoiGianNghe;
    }

}
