package com.tyl.quickmath.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tyl.quickmath.MainActivity;
import com.tyl.quickmath.Person;
import com.tyl.quickmath.R;
import com.tyl.quickmath.ScoreTableAdapter;

import java.util.ArrayList;
import java.util.List;

public class MediumFragment extends Fragment {

    List<Person> personList;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_toptable, container, false);
        final RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        sharedPreferences = getActivity().getSharedPreferences("sound", getActivity().MODE_PRIVATE);
        personList = new ArrayList<>();
        //Top5
        personList.add(0,new Person(sharedPreferences.getString("m_score1_name",""),sharedPreferences.getInt("m_score1",0)));
        personList.add(1,new Person(sharedPreferences.getString("m_score2_name",""),sharedPreferences.getInt("m_score2",0)));
        personList.add(2,new Person(sharedPreferences.getString("m_score3_name",""),sharedPreferences.getInt("m_score3",0)));
        personList.add(3,new Person(sharedPreferences.getString("m_score4_name",""),sharedPreferences.getInt("m_score4",0)));
        personList.add(4,new Person(sharedPreferences.getString("m_score5_name",""),sharedPreferences.getInt("m_score5",0)));
        personList.add(5,new Person(sharedPreferences.getString("m_score6_name",""),sharedPreferences.getInt("m_score6",0)));
        personList.add(6,new Person(sharedPreferences.getString("m_score7_name",""),sharedPreferences.getInt("m_score7",0)));
        personList.add(7,new Person(sharedPreferences.getString("m_score8_name",""),sharedPreferences.getInt("m_score8",0)));
        personList.add(8,new Person(sharedPreferences.getString("m_score9_name",""),sharedPreferences.getInt("m_score8",0)));
        personList.add(9,new Person(sharedPreferences.getString("m_score10_name",""),sharedPreferences.getInt("m_score10",0)));



        //create an adapter
        ScoreTableAdapter scoreTableAdapter = new ScoreTableAdapter(personList);
        //Set Adapter
        recyclerView.setAdapter(scoreTableAdapter);
        Button homeBtn = rootView.findViewById(R.id.top_table_backtomenu);
        homeBtn.setOnClickListener(v -> {
            getActivity().finishAffinity();
            startActivity(new Intent(getActivity(), MainActivity.class));

        });
        Button backBtn = rootView.findViewById(R.id.top_table_back);
        backBtn.setOnClickListener(v -> {
            getActivity().finish();
        });
        return rootView;
    }

}
