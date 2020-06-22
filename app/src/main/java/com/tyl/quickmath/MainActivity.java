package com.tyl.quickmath;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String gameMode;
    GlobalClass global;
    SharedPreferences sharedPreferences;
    Button sound,music;
    Button easyLvlP1,easyLvlP2,
            medLvlP1,medLvlP2,
            hardLvlP1,hardLvlP2;
    Button topTable;
    ImageView logo_iv;
    LinearLayout sound_lt;
    Animation from_top,from_bottom,fade,btnAnim;
    private TextView easyTV,medTV,hardTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo_iv = findViewById(R.id.logo_iv);
        sound_lt = findViewById(R.id.sound_layout);
        easyLvlP1 = findViewById(R.id.onePlayerEasy);
        easyLvlP2 = findViewById(R.id.twoPlayerEasy);
        medLvlP1 = findViewById(R.id.onePlayerMid);
        medLvlP2 = findViewById(R.id.twoPlayerMid);
        hardLvlP1 = findViewById(R.id.onePlayerHard);
        hardLvlP2 = findViewById(R.id.twoPlayerHard);
        topTable = findViewById(R.id.top_table_activity);

         easyTV = findViewById(R.id.easy_level);
         medTV = findViewById(R.id.med_level);
         hardTV = findViewById(R.id.hard_level);
        animStart();

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



        easyLvlP1.setOnClickListener(this);
        easyLvlP2.setOnClickListener(this);
        medLvlP1.setOnClickListener(this);
        medLvlP2.setOnClickListener(this);
        hardLvlP1.setOnClickListener(this);
        hardLvlP2.setOnClickListener(this);
        topTable.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intentP1 = new Intent(MainActivity.this, GameActivitySolo.class);
        Intent intentP2 = new Intent(MainActivity.this, GameActivity.class);
        Intent top_intent = new Intent(MainActivity.this, TopTableActivity.class);
        switch (v.getId()) {
            case R.id.onePlayerEasy:
                gameMode = "easyP1";
                intentP1.putExtra("level", gameMode);
                startActivity(intentP1);
                break;
            case R.id.twoPlayerEasy:
                gameMode = "easyP2";
                intentP2.putExtra("level", gameMode);
                startActivity(intentP2);
                break;
            case R.id.onePlayerMid:
                gameMode = "mediumP1";
                intentP1.putExtra("level", gameMode);
                startActivity(intentP1);
                break;
            case R.id.twoPlayerMid:
                gameMode = "mediumP2";
                intentP2.putExtra("level", gameMode);
                startActivity(intentP2);
                break;
            case R.id.onePlayerHard:
                gameMode = "hardP1";
                intentP1.putExtra("level", gameMode);
                startActivity(intentP1);
                break;
            case R.id.twoPlayerHard:
                gameMode = "hardP2";
                intentP2.putExtra("level", gameMode);
                startActivity(intentP2);
                break;
            case R.id.top_table_activity:
                startActivity(top_intent);
                break;
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
    private void animStart() {
        fade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        from_top = AnimationUtils.loadAnimation(this,R.anim.from_top);
        from_bottom = AnimationUtils.loadAnimation(this,R.anim.from_bottom);
        btnAnim = AnimationUtils.loadAnimation(this, R.anim.btns_anim);

        logo_iv.setAnimation(from_top);
        sound_lt.setAnimation(from_bottom);

        easyTV.setAnimation(fade);
        medTV.setAnimation(fade);
        hardTV.setAnimation(fade);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                easyTV.setAnimation(btnAnim);
                medTV.setAnimation(btnAnim);
                hardTV.setAnimation(btnAnim);
            }
        }, 1500);

    }

}
