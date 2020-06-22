package com.tyl.quickmath;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.util.Log;

public class GlobalClass extends Application {

    private static GlobalClass instance;
    public boolean Pause;
    public boolean muteSound;
    public boolean muteBackgroudMusic ;
    public MediaPlayer musicPlayer;

    static {
        instance = new GlobalClass();
    }
    private GlobalClass() {

    }
    public static GlobalClass getInstance(){
        return GlobalClass.instance;
    }

    public void setAppPause(boolean pause) {
        Pause = pause;
    }


    public void initializeMediaPlayer(Context context) {
        Log.d("initializeMediaPlayer","in");
        this.musicPlayer = new MediaPlayer();
      musicPlayer = MediaPlayer.create(context,R.raw.music2);
      musicPlayer.setLooping(true);
      playSound(context);
    }

    public boolean isPause() {
        return Pause;
    }

    public void pauseSong()
    {
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
    }

    public boolean isMuteSound() {
        return muteSound;
    }

    public void setMuteSound(boolean muteSound) {
        this.muteSound = muteSound;
    }

    public boolean isMusicPlayerMute() {
        return muteBackgroudMusic;
    }

    public void muteBackgroudMusic(boolean muteBackgroudMusic) {
        this.muteBackgroudMusic = muteBackgroudMusic;
    }

    public void playSound(Context context) {
        Log.d("playSound","global");

        if (isPause())
        {
            Log.d("playSound","isPause");
            return;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("sound", this.MODE_PRIVATE);
        setMuteSound(sharedPreferences.getBoolean("mute_tone", false));
        if (isMuteSound())
        {
            Log.d("playSound","exit");
            return;
        }
        muteBackgroudMusic( sharedPreferences.getBoolean("mute_music",false) );
        if(isMusicPlayerMute()){
            Log.d("playSound","isMusicPlayerMute");
            return;
        }

        if(musicPlayer != null){
            Log.d("playSound","start");
            musicPlayer.start();
        }


    }
}
