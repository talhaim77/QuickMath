package com.tyl.quickmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String gameMode;
    GlobalClass global;
    SharedPreferences sharedPreferences;
    Button sound,music;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        global = GlobalClass.getInstance();
        global.initializeMediaPlayer(this);
        sharedPreferences = this.getSharedPreferences("sound",this.MODE_PRIVATE);
        global.muteBackgroudMusic(sharedPreferences.getBoolean("mute_sound", false));

        sound = findViewById(R.id.soundBtn);
        music =findViewById(R.id.musicBtn);
        if(sharedPreferences.getBoolean("mute_tone",false)){
            sound.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_music_on_24dp),null,null,null);
        } else {
            sound.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_music_on_24dp),null,null,null);
        }
        if(sharedPreferences.getBoolean("mute_music",false)){
            music.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_music_off),null,null,null);
        }else{
            music.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_music_on),null,null,null);
        }
        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharedPreferences.getBoolean("mute_music", false)) {
                    music.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_music_on), null, null, null);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("mute_music", false);
                    editor.commit();
                    global.muteBackgroudMusic(false);
                    global.playSound(MainActivity.this);
                } else {
                    music.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_music_off), null, null, null);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("mute_music", true);
                    editor.commit();
                    global.muteBackgroudMusic(true);
                    global.pauseSong();
                }
            }
        });
        Button button1 = findViewById(R.id.easy_level);
        Button button2 = findViewById(R.id.med_level);
        Button button3 = findViewById(R.id.hard_level);

        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        switch (v.getId()) {
            case R.id.easy_level:
                gameMode = "easy";
                intent.putExtra("level", gameMode);
                startActivity(intent);
                break;
            case R.id.med_level:
                gameMode = "medium";
                intent.putExtra("level", gameMode);
                startActivity(intent);                break;
            case R.id.hard_level:
                gameMode = "hard";
                intent.putExtra("level", gameMode);
                startActivity(intent);
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        global.pauseSong();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume","in");
        global.playSound(this);
    }

}
