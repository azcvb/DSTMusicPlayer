package entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "YeuThich",
        foreignKeys = @ForeignKey(
                entity = Song.class,
                parentColumns = "id_BaiHat",
                childColumns = "id_BaiHat",
                onDelete = ForeignKey.CASCADE
        )
)
public class YeuThich {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int STT;

    private String id_BaiHat;

    public int getSTT() {
        return STT;
    }

    public void setSTT(int STT) {
        this.STT = STT;
    }

    public String getId_BaiHat() {
        return id_BaiHat;
    }

    public void setId_BaiHat(String id_BaiHat) {
        this.id_BaiHat = id_BaiHat;
    }
}
