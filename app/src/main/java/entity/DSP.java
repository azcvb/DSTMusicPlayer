package entity;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
@Entity(tableName = "DanhSachPhat",
        foreignKeys = @ForeignKey(
                entity = Song.class,
                parentColumns = "id_BaiHat",
                childColumns = "id_BaiHat",
                onDelete = ForeignKey.CASCADE
        )
)
public class DSP {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    int STT;
    String id_BaiHat;

    int phatNhac;



    public DSP(int STT, String id_BaiHat, int phatNhac) {
        this.STT = STT;
        this.id_BaiHat = id_BaiHat;
        this.phatNhac = phatNhac;
    }

    public DSP() {
        this.phatNhac = 0;
    }

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

    public int getPhatNhac() {
        return phatNhac;
    }

    public void setPhatNhac(int phatNhac) {
        this.phatNhac = phatNhac;
    }
}