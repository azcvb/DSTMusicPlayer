package dao;

import androidx.room.Dao;
import androidx.room.Insert;

import entity.Playlist;

@Dao
public interface playlistDao {
    @Insert
    Void platlist(Playlist playlist);

}
