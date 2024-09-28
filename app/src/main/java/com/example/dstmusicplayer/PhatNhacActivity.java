package com.example.dstmusicplayer;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import connectDB.SongData;
import dao.SongDao;
import dao.YeuThichDao;
import entity.YeuThich;

public class PhatNhacActivity extends AppCompatActivity {

    private ImageButton btnLove, btnVolume, btnBack, btnPlayPause, btnTimer, btnPlayList, btnRepeat, btnNextSong, btnPreviousSong;
    private SeekBar seekBarVolume, seekBarMusic;
    private TextView tvCurrentTime, tvTotalTime, tenNgheSi, tenBaiHat;
    private AudioManager audioManager;
    private final Handler handler = new Handler();
    private ImageView imageVinyl;
    private MusicService musicService;
    private boolean isServiceBound = false;
    private int currentPosition;
    private boolean isUpdatingSeekBar = false;
    private String currentSongId, thoiGianNghe;
    private YeuThichDao yeuThichDao;
    private SongDao songDao;
    private boolean isFavorite;
    private Runnable timerRunnable;
    private Handler timerHandler = new Handler();
    private utf8 utf8;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            musicService = binder.getService();
            isServiceBound = true;
            musicService.setOnSongChangeListener(new MusicService.OnSongChangeListener() {
                @Override
                public void onSongChanged(String newFilePath) {
                    setupUI(); // Cập nhật UI với bài hát mới
                }
            });
            handler.postDelayed(() -> {
                if (musicService != null) {
                    int totalTime = musicService.getDuration();
                    seekBarMusic.setMax(totalTime);
                    tvTotalTime.setText(formatTime(totalTime));
                    updateSeekBarMusic();
                }
            }, 1000);
            setupUI();
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isServiceBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phatnhac);

        // Khởi tạo các thành phần giao diện
        btnTimer = findViewById(R.id.btnTimer);
        btnTimer.setOnClickListener(v -> showTimerDialog());
        ImageButton btnChangeSpeed = findViewById(R.id.btnChangeSpeed);
        btnChangeSpeed.setOnClickListener(v -> showPlaybackSpeedDialog());
        btnPlayPause = findViewById(R.id.btnPause);
        seekBarMusic = findViewById(R.id.seekBarMusic);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tenBaiHat = findViewById(R.id.tenBaiHat);
        tenNgheSi = findViewById(R.id.tenNgheSi);
        imageVinyl = findViewById(R.id.imageVinyl);
        btnBack = findViewById(R.id.btnBack);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        seekBarVolume = findViewById(R.id.seekBarVolume);
        btnVolume = findViewById(R.id.btnVolume);
        btnLove = findViewById(R.id.btnLove);
        btnNextSong = findViewById(R.id.btnNext);
        btnPreviousSong = findViewById(R.id.btnPrevious);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnPlayList = findViewById(R.id.btnPlaylist);

        Intent serviceIntent = new Intent(this, MusicService.class);
        startService(serviceIntent);
        bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);

        songDao = SongData.getInstance(this).songDao();
        yeuThichDao = SongData.getInstance(this).yeuThichDao();

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (isFinishing() && isServiceBound) {
            unbindService(serviceConnection);
            isServiceBound = false;
        }
        MainActivity.setIdPhatNhac(currentSongId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicService != null) {
            currentPosition = musicService.getCurrentPosition();
            seekBarMusic.setProgress(currentPosition);
            tvCurrentTime.setText(formatTime(currentPosition));
            updateSeekBarMusic();
        }
    }

    private void setupUI() {
        if (musicService != null) {
            tenBaiHat.setSelected(true);
            tenNgheSi.setSelected(true);
            tenBaiHat.setText(musicService.getCurrentSongTitle());
            tenNgheSi.setText(musicService.getCurrentSongArtist());
            if(musicService.getCurrentAlbumArt() != null){
                imageVinyl.setImageBitmap(musicService.getCurrentAlbumArt());
            }else{
                imageVinyl.setImageResource(R.drawable.default_item);
            }
            currentPosition = musicService.getCurrentPosition();
            seekBarMusic.setProgress(currentPosition);
            tvCurrentTime.setText(formatTime(currentPosition));
            tvTotalTime = findViewById(R.id.tvTotalTime);
            int totalTime = musicService.getDuration();
            tvTotalTime.setText(formatTime(totalTime));
            seekBarMusic.setMax(totalTime);

            currentSongId = utf8.encodeString(musicService.getCurrentSongFilePath());
            checkFavoriteStatus(currentSongId);

            if(!musicService.isPlaying()){
                musicService.resumeMusic();
                btnPlayPause.setImageResource(R.drawable.pause);
                updateSeekBarMusic();
            }else {
                btnPlayPause.setImageResource(R.drawable.pause);
            }

            btnPlayPause.setOnClickListener(v -> {
                if (musicService != null) {
                    if (!musicService.isPlaying()) {
                        musicService.resumeMusic();
                        btnPlayPause.setImageResource(R.drawable.pause);
                        updateSeekBarMusic();
                    } else {
                        musicService.pauseMusic();
                        btnPlayPause.setImageResource(R.drawable.play);
                        isUpdatingSeekBar = false;
                    }
                }
            });

            btnPlayList.setOnClickListener(v ->{
                MainActivity.setIdPhatNhac(currentSongId);
                Intent intent = new Intent(this, DSPMain.class);
                startActivity(intent);

            });

            AsyncTask.execute(() -> {
                thoiGianNghe = GetCurrentTime.getCurrentTime();
                songDao.updateThoiGianNghe(currentSongId, thoiGianNghe);

            });
            new Thread(() -> {
                songDao.incrementSoLanNghe(currentSongId);
            }).start();
            btnNextSong.setOnClickListener(v -> {
                if (musicService != null) {
                    if (musicService.getRepeatMode() == MusicService.REPEAT_ONE) {
                        musicService.setRepeatMode(MusicService.NO_REPEAT);
                        Toast.makeText(PhatNhacActivity.this, "Tắt lặp lại bài hát hiện tại", Toast.LENGTH_SHORT).show();
                        btnRepeat.setImageResource(R.drawable.repeat); // Cập nhật UI
                    }
                    musicService.playNext(true);
                    setupUI();
                }
            });

            btnPreviousSong.setOnClickListener(v -> {
                if (musicService != null) {
                    if (musicService.getRepeatMode() == MusicService.REPEAT_ONE) {
                        musicService.setRepeatMode(MusicService.NO_REPEAT);
                        Toast.makeText(PhatNhacActivity.this, "Tắt lặp lại bài hát hiện tại", Toast.LENGTH_SHORT).show();
                        btnRepeat.setImageResource(R.drawable.repeat); // Cập nhật UI
                    }
                    musicService.playPrevious();
                    setupUI();
                }
            });

            btnRepeat.setOnClickListener(v -> {
                if (musicService != null) {
                    int currentMode = musicService.getRepeatMode();
                    if (currentMode == MusicService.NO_REPEAT) {
                        musicService.setRepeatMode(MusicService.REPEAT_ONE);
                        Toast.makeText(PhatNhacActivity.this, "Lặp lại bài hát hiện tại: BẬT", Toast.LENGTH_SHORT).show();
                        btnRepeat.setImageResource(R.drawable.repeat_1);
                    } else if (currentMode == MusicService.REPEAT_ONE) {
                        musicService.setRepeatMode(MusicService.REPEAT_ALL);
                        Toast.makeText(PhatNhacActivity.this, "Lặp lại toàn bộ Playlist: BẬT", Toast.LENGTH_SHORT).show();
                        btnRepeat.setImageResource(R.drawable.repeat_playlist);
                    } else {
                        musicService.setRepeatMode(MusicService.NO_REPEAT);
                        Toast.makeText(PhatNhacActivity.this, "Lặp lại: TẮT", Toast.LENGTH_SHORT).show();
                        btnRepeat.setImageResource(R.drawable.repeat);
                    }
                }
            });
            //Volume
            btnVolume.setOnClickListener(v -> {
                int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
                int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                if (currentVolume > 0) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                    seekBarVolume.setProgress(0);
                } else {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 10, 0);
                    seekBarVolume.setProgress(10);
                }
                updateVolumeIcon(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
            });
            btnBack.setOnClickListener(v -> {
                finish();
            });
            setupVolumeControl();
            btnLove.setOnClickListener(view -> {
                if (isFavorite) {
                    removeFavorite(currentSongId);
                } else {
                    addFavorite(currentSongId);
                }
                checkFavoriteStatus(currentSongId);
            });
            seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && musicService != null) {
                    musicService.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }
    }


    private void checkFavoriteStatus(String checkIDlove) {
        AsyncTask.execute(() -> {
            isFavorite = yeuThichDao.isFavorite(checkIDlove) > 0;
            runOnUiThread(() -> {
                if (isFavorite) {
                    btnLove.setImageResource(R.drawable.loved);
                } else {
                    btnLove.setImageResource(R.drawable.unlove);
                }
            });
        });
    }

    private void addFavorite(String songId) {
        AsyncTask.execute(() -> {
            YeuThich yeuThich = new YeuThich();
            yeuThich.setId_BaiHat(songId);
            yeuThichDao.addYeuThich(yeuThich);

            runOnUiThread(() -> {
                isFavorite = true;
                btnLove.setImageResource(R.drawable.loved);
                Toast.makeText(PhatNhacActivity.this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void removeFavorite(String songId) {
        AsyncTask.execute(() -> {
            yeuThichDao.removeYeuThich(songId);
            runOnUiThread(() -> {
                isFavorite = false;
                btnLove.setImageResource(R.drawable.unlove);
                Toast.makeText(PhatNhacActivity.this, "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void updateSeekBarMusic() {
        if (musicService != null) {
            isUpdatingSeekBar = true;
            int currentPosition = musicService.getCurrentPosition();
            seekBarMusic.setProgress(currentPosition);
            tvCurrentTime.setText(formatTime(currentPosition));

            handler.postDelayed(() -> {
                if (isUpdatingSeekBar) {
                    updateSeekBarMusic();
                }
            }, 1000);
        }
    }

    private void setupVolumeControl() {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setMax(maxVolume);
        seekBarVolume.setProgress(currentVolume);
        seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                updateVolumeIcon(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
        VolumeReceiver volumeReceiver = new VolumeReceiver(audioManager, seekBarVolume, btnVolume);
        IntentFilter filter = new IntentFilter("android.media.VOLUME_CHANGED_ACTION");
        registerReceiver(volumeReceiver, filter);
    }

    private void updateVolumeIcon(int volume) {
        if (volume == 0) {
            btnVolume.setImageResource(R.drawable.volume_mute);
        } else if (volume > 0 && volume <= audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2) {
            btnVolume.setImageResource(R.drawable.volume_low);
        } else {
            btnVolume.setImageResource(R.drawable.volume_high);
        }
    }

    private void showPlaybackSpeedDialog() {
        String[] speeds = {"0.25", "0.5", "0.75", "Chuẩn", "1.25", "1.5", "1.75", "2"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn tốc độ phát nhạc")
                .setItems(speeds, (dialog, which) -> {
                    musicService.setPlaybackSpeed(which);
                    Toast.makeText(PhatNhacActivity.this, "Tốc độ phát: " + speeds[which], Toast.LENGTH_SHORT).show();
                });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }

    private void setTimer(int minutes) {
        long delayMillis = minutes * 60 * 1000;

        timerRunnable = new Runnable() {
            @Override
            public void run() {
                if (musicService != null && musicService.isPlaying()) {
                    musicService.pauseMusic();
                    Toast.makeText(PhatNhacActivity.this, "Nhạc đã tắt sau " + minutes + " phút", Toast.LENGTH_SHORT).show();
                    btnTimer.setImageResource(R.drawable.clock);
                }
            }
        };

        timerHandler.postDelayed(timerRunnable, delayMillis);
    }

    private void cancelTimer() {
        if (timerRunnable != null) {
            timerHandler.removeCallbacks(timerRunnable);
            timerRunnable = null;
        }
    }

    private void showTimerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn thời gian tắt nhạc");
        String[] timerOptions = {"1 phút", "5 phút","10 phút", "20 phút", "30 phút", "45 phút", "1 giờ", "2 giờ", "Hủy hẹn giờ"};
        builder.setItems(timerOptions, (dialog, which) -> {
            int minutes = 0;
            String selectedTime = "";
            switch (which) {
                case 0:
                    minutes = 1;
                    selectedTime = "1 phút";
                    break;
                case 1:
                    minutes = 5;
                    selectedTime = "5 phút";
                    break;
                case 2:
                    minutes = 10;
                    selectedTime = "10 phút";
                    break;
                case 3:
                    minutes = 20;
                    selectedTime = "20 phút";
                    break;
                case 4:
                    minutes = 30;
                    selectedTime = "30 phút";
                    break;
                case 5:
                    minutes = 45;
                    selectedTime = "45 phút";
                    break;
                case 6:
                    minutes = 60;
                    selectedTime = "1 giờ";
                    break;
                case 7:
                    minutes = 120;
                    selectedTime = "2 giờ";
                    break;
                case 8:
                    minutes = -100;
                    break;
            }
            if(minutes == -100){
                Toast.makeText(this, "Đã hủy hẹn giờ" , Toast.LENGTH_SHORT).show();
                btnTimer.setImageResource(R.drawable.clock);
                cancelTimer();
            }else{
                Toast.makeText(this, "Nhạc sẽ tắt sau: " + selectedTime, Toast.LENGTH_SHORT).show();
                btnTimer.setImageResource(R.drawable.clock_change);
                setTimer(minutes);
            }
        });
        builder.show();
    }


    @SuppressLint("DefaultLocale")
    private String formatTime(int milliseconds) {
        int minutes = (milliseconds / 1000) / 60;
        int seconds = (milliseconds / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

}