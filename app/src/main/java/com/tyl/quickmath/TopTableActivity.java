package com.tyl.quickmath;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class TopTableActivity extends AppCompatActivity {
    GlobalClass global;
    SharedPreferences sharedPreferences;
    List<Person> personList;
    String game_level;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10);
        game_level = getIntent().getStringExtra("level");
        global = GlobalClass.getInstance();
        sharedPreferences = this.getSharedPreferences("sound", this.MODE_PRIVATE);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button newGame = findViewById(R.id.top_table_back);
        Button returnHome = findViewById(R.id.top_table_backtomenu);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personList = new ArrayList<>();
//        for (Integer i = 1; i <= 5; i++) {
//        personList.add(i,new Person(sharedPreferences.getString("score"+i.toString()+"_name",""),sharedPreferences.getInt("highscore"+i.toString(),0)));
//        }

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentP1 = new Intent(TopTableActivity.this, GameActivitySolo.class);
                Intent intentP2 = new Intent(TopTableActivity.this, GameActivity.class);
                switch (game_level) {
                    case "easyP1":
                        intentP1.putExtra("level", game_level);
                        startActivity(intentP1);
                        break;
                    case "easyP2":
                        intentP2.putExtra("level", game_level);
                        startActivity(intentP2);
                        break;
                    case "mediumP1":
                        intentP1.putExtra("level", game_level);
                        startActivity(intentP1);
                        break;
                    case "mediumP2":
                        intentP2.putExtra("level", game_level);
                        startActivity(intentP2);
                        break;
                    case "hardP1":
                        intentP1.putExtra("level", game_level);
                        startActivity(intentP1);
                        break;
                    case "hardP2":
                        intentP2.putExtra("level", game_level);
                        startActivity(intentP2);
                        break;
                }


            }
        });
        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopTableActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //Top5

        personList.add(0,new Person(sharedPreferences.getString("score1_name",""),sharedPreferences.getInt("highscore1",0)));
        personList.add(1,new Person(sharedPreferences.getString("score2_name",""),sharedPreferences.getInt("highscore2",0)));
        personList.add(2,new Person(sharedPreferences.getString("score3_name",""),sharedPreferences.getInt("highscore3",0)));
        personList.add(3,new Person(sharedPreferences.getString("score4_name",""),sharedPreferences.getInt("highscore4",0)));
        personList.add(4,new Person(sharedPreferences.getString("score5_name",""),sharedPreferences.getInt("highscore5",0)));

//        personList.add(5,new Person(sharedPreferences.getString("score6_name",""),sharedPreferences.getInt("highscore6",0)));
//        personList.add(6,new Person(sharedPreferences.getString("score7_name",""),sharedPreferences.getInt("highscore7",0)));
//        personList.add(7,new Person(sharedPreferences.getString("score8_name",""),sharedPreferences.getInt("highscore8",0)));
//        personList.add(8,new Person(sharedPreferences.getString("score9_name",""),sharedPreferences.getInt("highscore9",0)));
//        personList.add(9,new Person(sharedPreferences.getString("score10_name",""),sharedPreferences.getInt("highscore10",0)));

        ScoreTableAdapter scoreTableAdapter = new ScoreTableAdapter(personList);
        recyclerView.setAdapter(scoreTableAdapter);


    }

}
