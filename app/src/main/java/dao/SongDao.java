package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Song;

@Dao
public interface SongDao {
    @Insert
    void insertSong(Song song);

    @Update
    void updateSong(Song song);

    @Delete
    void deleteSong(Song song);

    @Query("SELECT * FROM BaiHat")
    List<Song> getAllSongs();

    @Query("SELECT * FROM BaiHat Where id_BaiHat = :id")
    List<Song> getSongId(String id);

    @Query("UPDATE BaiHat SET thoiGianNghe = :thoiGian WHERE id_BaiHat = :id")
    void updateThoiGianNghe(String id, String thoiGian);

    @Query("UPDATE BaiHat SET soLanNghe = soLanNghe + 1 WHERE id_BaiHat = :id")
    void incrementSoLanNghe(String id);

    @Query("SELECT * FROM BaiHat WHERE tenBaiHat LIKE :query OR tenNgheSi LIKE :query OR album LIKE :query")
    List<Song> searchSongs(String query);

    @Query("UPDATE BaiHat SET tenBaiHat = :newSongName, tenNgheSi = :newArtistName, namPhatHanh = :newReleaseYear WHERE id_BaiHat = :id")
    void updateSongById(String id, String newSongName, String newArtistName, String newReleaseYear);



    //sy
    @Query("SELECT id_BaiHat FROM BaiHat")
    List<String> getAllSongId();

    @Query("SELECT album FROM BaiHat")
    List<String> getAllAlbum();

    @Query("SELECT id_BaiHat FROM BaiHat WHERE album = :album")
    List<String> getBaiHatAlbum( String album);


    @Query("SELECT tenNgheSi FROM BaiHat")
    List<String> getAllNgheSi();

    @Query("SELECT id_BaiHat FROM BaiHat WHERE tenNgheSi = :tenNgheSi")
    List<String> getBaiHatNgheSi( String tenNgheSi);

}
