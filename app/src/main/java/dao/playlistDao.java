package dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import entity.Playlist;
import entity.Song;

@Dao
public interface playlistDao {
    @Insert
    Void insert(Playlist playlist);

    @Update
    Void update(Playlist playlist);

    @Delete
    Void delete(Playlist playlist);


    @Query("SELECT * FROM PlayList")
    List<Playlist> getAllPlaylist();

    @Query("SELECT * FROM PlayList where id_Playlist = :id")
    Playlist getPlaylistItem(String id);

    @Query("SELECT id_Playlist FROM PlayList")
    List<String> getAllId();
}
