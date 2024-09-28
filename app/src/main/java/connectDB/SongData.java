package connectDB;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import dao.*;
import entity.*;

@Database(entities = {Song.class, YeuThich.class, LuuPlaylist.class, DSP.class, Playlist.class}, version = 2, exportSchema = false)
public abstract class SongData extends RoomDatabase {

    private static SongData instance;
    public abstract playlistDao playlistdao();
    public abstract SongDao songDao();
    public abstract DSPDao dspdao();
    public abstract YeuThichDao yeuThichDao();
    public abstract LuuPlaylistDao luuPlaylistDao();

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE YeuThich ADD COLUMN new_column TEXT");
        }
    };

    public static synchronized SongData getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            SongData.class, "music.db")
                    .addMigrations(MIGRATION_1_2)
                    .build();
            Log.d("SongData", "Database instance created");
        }
        return instance;
    }
}