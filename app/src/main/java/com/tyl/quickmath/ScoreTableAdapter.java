package com.tyl.quickmath;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoreTableAdapter extends RecyclerView.Adapter<ScoreTableAdapter.ScoreViewHolder> {

    private List<Person> topPlayers;

    public ScoreTableAdapter(List<Person> topPlayers) {
        this.topPlayers = topPlayers;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_score_cell,parent,false);
        ScoreViewHolder scoreViewHolder = new ScoreViewHolder(view);
        return scoreViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
    Person person = topPlayers.get(position);
    holder.nameTV.setText(person.getName());
    holder.scoreTV.setText(person.getScore()+"");
    }

    @Override
    public int getItemCount() {
        return topPlayers.size();
    }

    public class ScoreViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV,scoreTV;

        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.player_name_cell);
            scoreTV = itemView.findViewById(R.id.player_score_cell);
        }
    }
}
