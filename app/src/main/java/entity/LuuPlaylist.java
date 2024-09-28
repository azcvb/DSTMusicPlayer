package entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "Luu",
        primaryKeys = {"id_Playlist", "id_BaiHat"},
        foreignKeys = {
                @ForeignKey(entity = Playlist.class, parentColumns = "id_Playlist", childColumns = "id_Playlist", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Song.class, parentColumns = "id_BaiHat", childColumns = "id_BaiHat", onDelete = ForeignKey.CASCADE)
        }
)
public class LuuPlaylist {

    @NonNull
    @ColumnInfo(name = "id_Playlist")
    private String id_Playlist;

    @NonNull
    @ColumnInfo(name = "id_BaiHat")
    private String id_BaiHat;

    // Getters và Setters
    public String getId_Playlist() {
        return id_Playlist;
    }

    public void setId_Playlist(String id_Playlist) {
        this.id_Playlist = id_Playlist;
    }

    public String getId_BaiHat() {
        return id_BaiHat;
    }

    public void setId_BaiHat(String id_BaiHat) {
        this.id_BaiHat = id_BaiHat;
    }
}