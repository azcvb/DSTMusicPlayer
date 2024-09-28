package com.example.dstmusicplayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.ImageButton;
import android.widget.SeekBar;

public class VolumeReceiver extends BroadcastReceiver {
    private final AudioManager audioManager;
    private final SeekBar seekBarVolume;
    private final ImageButton btnVolume;

    public VolumeReceiver(AudioManager audioManager, SeekBar seekBarVolume, ImageButton btnVolume) {
        this.audioManager = audioManager;
        this.seekBarVolume = seekBarVolume;
        this.btnVolume = btnVolume;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        seekBarVolume.setProgress(currentVolume);
        updateVolumeIcon(currentVolume);
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
}

