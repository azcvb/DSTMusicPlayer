package dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import entity.YeuThich;

@Dao
public interface YeuThichDao {

    @Insert
    void addYeuThich(YeuThich yeuThich);

    @Query("DELETE FROM YeuThich WHERE id_BaiHat = :songId")
    void removeYeuThich(String songId);

    @Query("SELECT COUNT(*) FROM YeuThich WHERE id_BaiHat = :songId")
    int isFavorite(String songId);

    @Query("SELECT * FROM YeuThich")
    List<YeuThich> getAllYeuThich();
}
