package com.tyl.quickmath;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TopTableActivity extends AppCompatActivity {
    GlobalClass global;
    SharedPreferences sharedPreferences;
    List<Person> personList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top5);
        global = GlobalClass.getInstance();
        sharedPreferences = this.getSharedPreferences("sound", this.MODE_PRIVATE);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        personList = new ArrayList<>();
//        for (Integer i = 1; i <= 5; i++) {
//        personList.add(i,new Person(sharedPreferences.getString("score"+i.toString()+"_name",""),sharedPreferences.getInt("highscore"+i.toString(),0)));
//        }
        //Top10
        personList.add(0,new Person(sharedPreferences.getString("score1_name",""),sharedPreferences.getInt("highscore1",0)));
        personList.add(1,new Person(sharedPreferences.getString("score2_name",""),sharedPreferences.getInt("highscore2",0)));
        personList.add(2,new Person(sharedPreferences.getString("score3_name",""),sharedPreferences.getInt("highscore3",0)));
        personList.add(3,new Person(sharedPreferences.getString("score4_name",""),sharedPreferences.getInt("highscore4",0)));
        personList.add(4,new Person(sharedPreferences.getString("score5_name",""),sharedPreferences.getInt("highscore5",0)));

        personList.add(5,new Person(sharedPreferences.getString("score6_name",""),sharedPreferences.getInt("highscore6",0)));
        personList.add(6,new Person(sharedPreferences.getString("score7_name",""),sharedPreferences.getInt("highscore7",0)));
        personList.add(7,new Person(sharedPreferences.getString("score8_name",""),sharedPreferences.getInt("highscore8",0)));
        personList.add(8,new Person(sharedPreferences.getString("score9_name",""),sharedPreferences.getInt("highscore9",0)));
        personList.add(9,new Person(sharedPreferences.getString("score10_name",""),sharedPreferences.getInt("highscore10",0)));

        ScoreTableAdapter scoreTableAdapter = new ScoreTableAdapter(personList);
        recyclerView.setAdapter(scoreTableAdapter);


    }

}
