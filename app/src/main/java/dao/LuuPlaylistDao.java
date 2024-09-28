package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import entity.LuuPlaylist;

@Dao
public interface LuuPlaylistDao {

    @Insert
    void insert(LuuPlaylist luuPlaylist);

    @Delete
    void delete(LuuPlaylist luuPlaylist);

    @Query("SELECT * FROM Luu WHERE id_Playlist = :playlistId")
    List<LuuPlaylist> getSongsByPlaylist(String playlistId);

    @Query("DELETE FROM Luu WHERE id_Playlist = :playlistId AND id_BaiHat = :songId")
    void deleteSongFromPlaylist(String playlistId, String songId);

    @Query("SELECT COUNT(*) FROM Luu WHERE id_Playlist = :playlistId AND id_BaiHat = :songId")
    int countSongInPlaylist(String playlistId, String songId);
}