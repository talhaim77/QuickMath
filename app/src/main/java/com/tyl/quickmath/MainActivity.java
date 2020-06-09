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


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private String gameMode;
    GlobalClass global;
    SharedPreferences sharedPreferences;
    Button sound,music;
    Button easyLvl,medLvl,hardLvl,topTable;
    ImageView logo_iv;
    LinearLayout sound_lt;
    Animation from_top,from_bottom,fade,btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        logo_iv = findViewById(R.id.logo_iv);
        sound_lt = findViewById(R.id.sound_layout);
        easyLvl = findViewById(R.id.easy_level);
        medLvl = findViewById(R.id.med_level);
        hardLvl = findViewById(R.id.hard_level);

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
        Button button1 = findViewById(R.id.easy_level);
        Button button2 = findViewById(R.id.med_level);
        Button button3 = findViewById(R.id.hard_level);
        Button topTable = findViewById(R.id.top_table_activity);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        topTable.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this, GameActivity.class);
        Intent top_intent = new Intent(MainActivity.this, TopTableActivity.class);
        switch (v.getId()) {
            case R.id.easy_level:
                gameMode = "easy";
                intent.putExtra("level", gameMode);
                startActivity(intent);
                break;
            case R.id.med_level:
                gameMode = "medium";
                intent.putExtra("level", gameMode);
                startActivity(intent);
                break;
            case R.id.hard_level:
                gameMode = "hard";
                intent.putExtra("level", gameMode);
                startActivity(intent);
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
        easyLvl.setAnimation(fade);
        medLvl.setAnimation(fade);
        hardLvl.setAnimation(fade);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                easyLvl.setAnimation(btnAnim);
                medLvl.setAnimation(btnAnim);
                hardLvl.setAnimation(btnAnim);
            }
        }, 1500);

    }

}
