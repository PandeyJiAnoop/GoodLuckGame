package com.akp.aonestar;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class BackgroundSoundService extends Service {
    MediaPlayer musicPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = MediaPlayer.create(this, R.raw.mus);
        musicPlayer.setLooping(true);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicPlayer.start();
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        musicPlayer.stop();
        Toast.makeText(this, "Music Service destroyed by user.", Toast.LENGTH_LONG).show();
    }
}