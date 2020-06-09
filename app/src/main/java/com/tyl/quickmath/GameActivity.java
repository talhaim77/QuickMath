package com.tyl.quickmath;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.jinatonic.confetti.CommonConfetti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pl.droidsonroids.gif.GifTextView;

public class GameActivity extends AppCompatActivity {
    GlobalClass global;
    MediaPlayer mediaPlayer;
    TextView timeCountDownP1,timeCountDownP2;
    TextView scoreTextP1,scoreTextP2,tieTVP1;
    TextView questionTextP1,questionTextP2;
    TextView questionCounterP1,questionCounterP2;
    LinearLayout answerLayoutP1,answerLayoutP2,timerLayoutP1,timerLayoutP2, labelsP1, labelsP2;
    ImageButton playAgainButton;
    GifTextView gifImageView;
    CountDownTimer countDown;
    boolean isActive;
    int answerIndex, minOfTop10;
    int count_the_question,count_tie_question;
    String gameLvl;
    boolean isHard,tieScore;
    ArrayList<Integer> questionArray;
    ArrayList<Integer> answerArray ;
    ArrayList<Integer> answerArrayHigh;
    ArrayList<Integer> answerArrayLow;
    private int scoreP2=0;
    private int scoreP1=0;
    SharedPreferences sharedPreferences;
    private boolean new_high_score;
    private List<Person> scoresArray;
    private int maxScore;
    final Handler hndlr = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        global = GlobalClass.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        String game_level = getIntent().getStringExtra("level");
        gameLvl = game_level;
        sharedPreferences = this.getSharedPreferences("sound", this.MODE_PRIVATE);
        global.muteBackgroudMusic(sharedPreferences.getBoolean("mute_sound", false));

        timeCountDownP1 = findViewById(R.id.timeCountDownP1);
        timeCountDownP2 = findViewById(R.id.timeCountDownP2);
        questionTextP1 = findViewById(R.id.questionTextP1);
        questionTextP2 = findViewById(R.id.questionTextP2);
        timerLayoutP1 = findViewById(R.id.layout_p1);
        timerLayoutP2 = findViewById(R.id.layout_p2);

        questionCounterP1 = findViewById(R.id.questionCounterP1);
        questionCounterP2=findViewById(R.id.questionCounterP2);
        answerLayoutP1 = findViewById(R.id.answersButtonsP1);
        answerLayoutP2 = findViewById(R.id.answersButtonsP2);

        tieTVP1 = findViewById(R.id.tieQP1);
        labelsP1 = findViewById(R.id.labelsP1);
        labelsP2 = findViewById(R.id.labelsP2);

        gifImageView = findViewById(R.id.start_gif);
        playAgainButton = findViewById(R.id.imgBtnPlayAgain);
        scoreTextP1 = findViewById(R.id.scoreP1);
        scoreTextP2 = findViewById(R.id.scoreP2);
        isHard = gameLvl.equals("hard") ;
        setArrayValues(game_level);
        scoresArray = new ArrayList<>();
        initScoresTable();
        startGame();
    }

    public void playAgainTie() {

        showView(tieTVP1);
        enableBtns();
        startGame();
    }

    private void setArrayValues(String level) {
        switch (level) {
            case "easy":
                questionArray = new ArrayList<>(Arrays.asList(3,5,6,7,4,8,9));
                answerArray = new ArrayList<>(Arrays.asList(32,12,53,50,23,45,21,57,23,10,6,5,7,9));
                break;
            case "medium":
                questionArray = new ArrayList<>(Arrays.asList(8,12,11,14,15,16,18,9));
                answerArray = new ArrayList<>(Arrays.asList(102,312,103,89,45,72,57,23,10,260,250,9));
                break;
            case "hard":
                questionArray = new ArrayList<>(Arrays.asList(51,79,16,27,35,28,44,55,72));
                answerArrayHigh = new ArrayList<>(Arrays.asList(3123,2124,1415,5189,3961,2453,3332,4010,2777,2876,3468,5235));
                answerArrayLow =  new ArrayList<>(Arrays.asList(13,24,145,89,318,243,332,110,59,58,96,31));
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        global.pauseSong();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            global.playSound(this);
        }
    }

    private void updateHighScore() {

        Collections.sort(scoresArray);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("highscore1",scoresArray.get(0).getScore());
        editor.putInt("highscore2",scoresArray.get(1).getScore());
        editor.putInt("highscore3",scoresArray.get(2).getScore());
        editor.putInt("highscore4",scoresArray.get(3).getScore());
        editor.putInt("highscore5",scoresArray.get(4).getScore());
        editor.putInt("highscore6",scoresArray.get(5).getScore());
        editor.putInt("highscore7",scoresArray.get(6).getScore());
        editor.putInt("highscore8",scoresArray.get(7).getScore());
        editor.putInt("highscore9",scoresArray.get(8).getScore());
        editor.putInt("highscore10",scoresArray.get(9).getScore());

        editor.putString("score1_name",scoresArray.get(0).getName());
        editor.putString("score2_name",scoresArray.get(1).getName());
        editor.putString("score3_name",scoresArray.get(2).getName());
        editor.putString("score4_name",scoresArray.get(3).getName());
        editor.putString("score5_name",scoresArray.get(4).getName());
        editor.putString("score6_name",scoresArray.get(5).getName());
        editor.putString("score7_name",scoresArray.get(6).getName());
        editor.putString("score8_name",scoresArray.get(7).getName());
        editor.putString("score9_name",scoresArray.get(8).getName());
        editor.putString("score10_name",scoresArray.get(9).getName());
        editor.commit();
    }

    private void newHighScoreCheck() {
        //int maxScore = Integer.max(scoreP1,scoreP2);
        maxScore = (scoreP1 > scoreP2 ? scoreP1 : scoreP2) ;
        minOfTop10 = sharedPreferences.getInt("highscore10",0);

        if ( maxScore > minOfTop10)
            new_high_score = true;

        if (new_high_score) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                    final View dialogview = getLayoutInflater().inflate(R.layout.new_high_score, null);
                    builder.setView(dialogview).setCancelable(false);
                    TextView dialogScoreTv = dialogview.findViewById(R.id.myhighscore);
                    dialogScoreTv.setText(Integer.toString(maxScore));
                    final EditText userEt = dialogview.findViewById(R.id.username);
                    final AlertDialog dialog = builder.create();
                    dialog.show();


                    final RelativeLayout container = dialogview.findViewById(R.id.highscore_layout);

                    final Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            CommonConfetti.rainingConfetti(container, new int[] { Color.BLUE ,Color.YELLOW, Color.RED, Color.GREEN})
                                    .infinite();
                        }
                    };
                    hndlr.postDelayed(r,600 );

                    ImageButton returnBtn = dialogview.findViewById(R.id.return_to_game);
                    returnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //to do : update shared prefferences with name and picture
                            String username = userEt.getText().toString();
                            scoresArray.add(5,new Person(username,maxScore));
                            updateHighScore();
                            maxScore = 0;
                            new_high_score = false;
                            dialog.dismiss();
                        }
                    });

                }
            }, 0);
        }
        else //game over,do nothing
            {
            return;
        }


    }

    private void initScoresTable() {
        minOfTop10 = sharedPreferences.getInt("highscore10",0);
        scoresArray.add(0,new Person(sharedPreferences.getString("score1_name","Player"),sharedPreferences.getInt("highscore1",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score2_name","Player"),sharedPreferences.getInt("highscore2",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score3_name","Player"),sharedPreferences.getInt("highscore3",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score4_name","Player"),sharedPreferences.getInt("highscore4",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score5_name","Player"),sharedPreferences.getInt("highscore5",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score6_name","Player"),sharedPreferences.getInt("highscore6",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score7_name","Player"),sharedPreferences.getInt("highscore7",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score8_name","Player"),sharedPreferences.getInt("highscore8",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score9_name","Player"),sharedPreferences.getInt("highscore9",0)));
        scoresArray.add(new Person(sharedPreferences.getString("score10_name","Player"),sharedPreferences.getInt("highscore10",0)));
    }

    private void startGame() {
        playGif();
        //Hide useless buttons
        hideView(playAgainButton);


//        showView(labelsP1);
//        showView(labelsP2);
        if(!tieScore) {
            //3,2,1 -->go
            hideViewsForAnim();
            showView(gifImageView);
            hideView(tieTVP1);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isActive = true; //set game state to active
                    hideView(gifImageView);
                    showViewsForAnim();
                    startTimer();
                    getNextQuestionAnswer();
                }
            }, 5000);
        }
        else{
            getNextQuestionAnswer();
        }


    }

    private void hideViewsForAnim() {
        hideView(labelsP1);
        hideView(labelsP2);
        hideView(timerLayoutP1);
        hideView(timerLayoutP2);
        hideView(questionTextP1);
        hideView(questionTextP2);
        hideView(answerLayoutP1);
        hideView(answerLayoutP2);
    }
    private void showViewsForAnim() {
        showView(labelsP1);
        showView(labelsP2);
        showView(timerLayoutP1);
        showView(timerLayoutP2);
        showView(questionTextP1);
        showView(questionTextP2);
        showView(answerLayoutP1);
        showView(answerLayoutP2);
    }

    public void checkAnswerP1(View view) {
        disableBtns();
        if (view.getTag().toString().equals(Integer.toString(answerIndex))) {
            scoreP1++;
        } else {
            scoreP2++;
        }
        scoreTextP1.setText(Integer.toString(scoreP1));
        scoreTextP2.setText(Integer.toString(scoreP2));

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //loading next question after 250ms
                getNextQuestionAnswer();
                enableBtns();
            }
        }, 250);

    }

    public void checkAnswerP2(View view) {
        disableBtns();
        if (view.getTag().toString().equals(Integer.toString(answerIndex))) {
            scoreP2++;
        } else {
            scoreP1++;
        }
        scoreTextP1.setText(Integer.toString(scoreP1));
        scoreTextP2.setText(Integer.toString(scoreP2));
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //loading next question after 250ms
                getNextQuestionAnswer();
                enableBtns();
            }
        }, 250);
    }

    private void getNextQuestionAnswer() {
        if (count_the_question > 30) {
            endGame();
        }
        else if(tieScore&&count_tie_question==5){
            tieScore=false;
            count_tie_question=0;
            //Show playAgain button
            hideView(tieTVP1);
            newHighScoreCheck();
            showView(playAgainButton);
            //Set gamestate to inactive
            isActive = false;
        }
        else {
            Random random = new Random();
            // 0 is addition, 1 is subtraction, 2 is multiplication and 3 is divide
            int questionType = random.nextInt(4); // get question type
            answerIndex = random.nextInt(4); // index of correct option
            int firstQnInt = questionArray.get(random.nextInt(questionArray.size()));
            int secondQnInt = questionArray.get(random.nextInt(questionArray.size()));
            //to make it harder,when first==second --> random again one of them.
            if(isHard&&(firstQnInt == secondQnInt))
            {
                Log.d("isHard","first==second");
                do
                   firstQnInt = questionArray.get(random.nextInt(questionArray.size()));
                  while(firstQnInt == secondQnInt);
            }
                String operation = "";
            int answer = 0;
            switch(questionType) {
                case 0:
                    //addition
                    answer = firstQnInt + secondQnInt;
                    operation = "+";
                    break;
                case 1:
                    //subtraction
                    answer = firstQnInt - secondQnInt;
                    operation = "-";
                    break;
                case 2:
                    //multiplication
                    answer = firstQnInt * secondQnInt;
                    operation = "x";
                    break;
                case 3:
                    //division
                    if(firstQnInt < secondQnInt){
                        //if first < second then swap them
                        firstQnInt = getItself(secondQnInt, secondQnInt = firstQnInt);
                    }

                    answer = firstQnInt / secondQnInt;
                    operation = "/";
                    break;
            }
            for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
                Button childButtonP1 = (Button) answerLayoutP1.getChildAt(i);
                Button childButtonP2 = (Button) answerLayoutP2.getChildAt(i);
                Integer randomValue;
                if(gameLvl.equals("hard")){
                    if(answer>350)
                        randomValue = answerArrayHigh.get(random.nextInt(answerArrayHigh.size()));
                    else
                        randomValue = answerArrayLow.get(random.nextInt(answerArrayLow.size()));
                }
                else
                    randomValue = answerArray.get(random.nextInt(answerArray.size()));

                if (childButtonP1.getTag().toString().equals(Integer.toString(answerIndex))) {
                    childButtonP1.setText(String.valueOf(answer));
                } else {
                    if (randomValue == answer) {
                        randomValue += random.nextInt(5);
                    }
                    childButtonP1.setText(String.valueOf(randomValue));
                }
                // second player-set a 4 numbers that can be the answer.
                if (childButtonP2.getTag().toString().equals(Integer.toString(answerIndex))) {
                    childButtonP2.setText(String.valueOf(answer));
                } else {
                    if (randomValue == answer) {
                        randomValue += random.nextInt(5);
                    }
                    childButtonP2.setText(String.valueOf(randomValue));
                }
            }

            //Set qn text
            questionTextP1.setText(String.format(Locale.ENGLISH, "%d %s %d", firstQnInt, operation, secondQnInt));
            questionTextP2.setText(String.format(Locale.ENGLISH, "%d %s %d", firstQnInt, operation, secondQnInt));
            if(tieScore){
                tieTVP1.setText(String.format(Locale.ENGLISH, "Round%d",count_tie_question+1 ));
            }
            //Set qn counter
            String qnCounterTxt = String.format(Locale.ENGLISH, "%d/20", count_the_question);
            questionCounterP1.setText(qnCounterTxt);
            questionCounterP2.setText(qnCounterTxt);
            count_the_question++;
            if(tieScore){
                count_tie_question++;
            }
        }
    }



    private void endGame() {
        countDown.cancel();
        countDown.onFinish();
    }

    // a trick to swap by reference
    public static int getItself(int itself, int dummy)
    {
        return itself;
    }



    private void startTimer() {

        int millisInFuture = 0;
        switch (gameLvl) {
            case "easy":
                millisInFuture = 5000;
                break;
            case "medium":
                millisInFuture = 10000;
                break;
            case "hard":
                millisInFuture = 15000;
                break;
        }

        countDown = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //amend timeCountDown on every tick
                timeCountDownP1.setText(String.valueOf(millisUntilFinished / 1000));
                timeCountDownP2.setText(String.valueOf(millisUntilFinished / 1000));
                if(millisUntilFinished == 10000){

                }
            }

            @Override
            public void onFinish() {
                disableBtns();
                if(scoreP1==scoreP2){
                tieScore = true;
                    playAgainTie();
                    enableBtns();
                    return;
                }
                else{
                    //Show playAgain button
                    newHighScoreCheck();
                    showView(playAgainButton);
                    //Set gamestate to inactive
                    isActive = false;
                }

            }
        }.start();
    }

    public void disableBtns() {
        //Disable answer buttons from being pressed
        for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
            answerLayoutP1.getChildAt(i).setEnabled(false);
            answerLayoutP2.getChildAt(i).setEnabled(false);
        }
    }

    public void playAgain(View view) {
        //to be executed by playAgainButton
        showView(timeCountDownP1);
        showView(timeCountDownP2);
        count_the_question = 0;
//reset the 2 players score
        scoreP1 = 0;
        scoreP2 = 0;
        scoreTextP1.setText(Integer.toString(scoreP1));
        scoreTextP2.setText(Integer.toString(scoreP2));
        enableBtns();
        startGame();
    }



    private void enableBtns() {
        for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
            answerLayoutP1.getChildAt(i).setEnabled(true);
            answerLayoutP2.getChildAt(i).setEnabled(true);
        }
    }

    private void hideView(View view) {
        view.setEnabled(false);
        view.setAlpha(0.0f);
    }

    private void showView(View view) {
        view.setEnabled(true);
        view.setAlpha(1.0f);
    }

    public void playGif(){
        Animation fadeout = new AlphaAnimation(1.f, 1.f);
        fadeout.setDuration(5000); // You can modify the duration here
        fadeout.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                gifImageView.setBackgroundResource(R.drawable.go321);//your gif file
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });
        gifImageView.startAnimation(fadeout);
    }

}
