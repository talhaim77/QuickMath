package com.tyl.quickmath;


import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.tyl.quickmath.databinding.ActivityGame2Binding;
import com.tyl.quickmath.fragments.EasyFragment;
import com.tyl.quickmath.fragments.FragmentViewPagerActivity;
import com.tyl.quickmath.fragments.HardFragment;
import com.tyl.quickmath.fragments.MediumFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pl.droidsonroids.gif.GifTextView;

////

public class GameActivity extends AppCompatActivity {
    LottieAnimationView winnerP1,winnerP2;
    GlobalClass global;
    MediaPlayer mediaPlayer;
    TextView timeCountDownP1,timeCountDownP2;
    TextView scoreTextP1,scoreTextP2,tieTVP1;
    TextView questionTextP1,questionTextP2,questionTextP3;
    TextView questionCounterP1,questionCounterP2;
    LinearLayout answerLayoutP1,answerLayoutP2,timerLayoutP1,timerLayoutP2, labelsP1, labelsP2;
    Button playAgainButton;
    GifTextView gifImageView;
    CountDownTimer countDown;
    boolean isActive;
    int answerIndex, minOfTop10;
    int count_the_question,count_tie_question;
    String gameLvl;
    boolean isEasy,isMed,isHard,tieScore;
    ArrayList<Integer> questionArray;
    Button backDialog;
    ActivityGame2Binding binding;
    AnswerArrays numArrays,medArrays,hardArrays;
    //ArrayList<Integer> answerArrayLow;
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
        setContentView(R.layout.activity_game2);
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
        answerLayoutP1 = findViewById(R.id.answersButtons);
        answerLayoutP2 = findViewById(R.id.answersButtonsP2);

        tieTVP1 = findViewById(R.id.tieQP1);
        labelsP1 = findViewById(R.id.labelsP1);
        labelsP2 = findViewById(R.id.labelsP2);

        backDialog = findViewById(R.id.return_to_menu);
        gifImageView = findViewById(R.id.start_gif);
        winnerP1 = findViewById(R.id.win);
        winnerP2 = findViewById(R.id.win2);
        playAgainButton = findViewById(R.id.imgBtnPlayAgain);
        scoreTextP1 = findViewById(R.id.scoreP1);
        scoreTextP2 = findViewById(R.id.scoreP2);
        isEasy = gameLvl.equals("easyP2");
        isMed = gameLvl.equals("mediumP2");
        isHard = gameLvl.equals("hardP2");
        setArrayValues(game_level);
        scoresArray = new ArrayList<>();
        initScoresTable();
        initArraysLvl();
        startGame();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initArraysLvl() {
        //init arrays of the easy level
          numArrays = new AnswerArrays();

        //init arrays of the medium level
          medArrays = new AnswerArrays();

    }

    public void playAgainTie() {

        showView(tieTVP1);
        enableBtns();
        startGame();
    }

    private void setArrayValues(String level) {
        switch (level) {
            case "easyP2":
                questionArray = new ArrayList<>(Arrays.asList(1,2,3,4,6,8,10,11,12,13,14,15,16,17,18,19,20));
                break;
            case "mediumP2":
                questionArray = new ArrayList<>(Arrays.asList(21,22,24,25,27,28,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60));
                break;
            case "hardP2":
                questionArray = new ArrayList<>(Arrays.asList(13,17,19,23,27,33,29,46,93,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,
                       71,72,73,74,75,76, 77,78,79,80,81,82,83,84,85,86,87,88,89,90,91,92,101,93,94,95,96,97,98,99,100,101,102,103));
                break;
        }
    }

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
        if(isEasy){
            editor.putInt("e_score1",scoresArray.get(0).getScore());
            editor.putInt("e_score2",scoresArray.get(1).getScore());
            editor.putInt("e_score3",scoresArray.get(2).getScore());
            editor.putInt("e_score4",scoresArray.get(3).getScore());
            editor.putInt("e_score5",scoresArray.get(4).getScore());
            editor.putInt("e_score6",scoresArray.get(5).getScore());
            editor.putInt("e_score7",scoresArray.get(6).getScore());
            editor.putInt("e_score8",scoresArray.get(7).getScore());
            editor.putInt("e_score9",scoresArray.get(8).getScore());
            editor.putInt("e_score10",scoresArray.get(9).getScore());

            editor.putString("e_score1_name",scoresArray.get(0).getName());
            editor.putString("e_score2_name",scoresArray.get(1).getName());
            editor.putString("e_score3_name",scoresArray.get(2).getName());
            editor.putString("e_score4_name",scoresArray.get(3).getName());
            editor.putString("e_score5_name",scoresArray.get(4).getName());
            editor.putString("e_score6_name",scoresArray.get(5).getName());
            editor.putString("e_score7_name",scoresArray.get(6).getName());
            editor.putString("e_score8_name",scoresArray.get(7).getName());
            editor.putString("e_score9_name",scoresArray.get(8).getName());
            editor.putString("e_score10_name",scoresArray.get(9).getName());
        }
        else if(isMed){
            editor.putInt("m_score1",scoresArray.get(0).getScore());
            editor.putInt("m_score2",scoresArray.get(1).getScore());
            editor.putInt("m_score3",scoresArray.get(2).getScore());
            editor.putInt("m_score4",scoresArray.get(3).getScore());
            editor.putInt("m_score5",scoresArray.get(4).getScore());
            editor.putInt("m_score6",scoresArray.get(5).getScore());
            editor.putInt("m_score7",scoresArray.get(6).getScore());
            editor.putInt("m_score8",scoresArray.get(7).getScore());
            editor.putInt("m_score9",scoresArray.get(8).getScore());
            editor.putInt("m_score10",scoresArray.get(9).getScore());

            editor.putString("m_score1_name",scoresArray.get(0).getName());
            editor.putString("m_score2_name",scoresArray.get(1).getName());
            editor.putString("m_score3_name",scoresArray.get(2).getName());
            editor.putString("m_score4_name",scoresArray.get(3).getName());
            editor.putString("m_score5_name",scoresArray.get(4).getName());
            editor.putString("m_score6_name",scoresArray.get(5).getName());
            editor.putString("m_score7_name",scoresArray.get(6).getName());
            editor.putString("m_score8_name",scoresArray.get(7).getName());
            editor.putString("m_score9_name",scoresArray.get(8).getName());
            editor.putString("m_score10_name",scoresArray.get(9).getName());
        }
        else{
            editor.putInt("h_score1",scoresArray.get(0).getScore());
            editor.putInt("h_score2",scoresArray.get(1).getScore());
            editor.putInt("h_score3",scoresArray.get(2).getScore());
            editor.putInt("h_score4",scoresArray.get(3).getScore());
            editor.putInt("h_score5",scoresArray.get(4).getScore());
            editor.putInt("h_score6",scoresArray.get(5).getScore());
            editor.putInt("h_score7",scoresArray.get(6).getScore());
            editor.putInt("h_score8",scoresArray.get(7).getScore());
            editor.putInt("h_score9",scoresArray.get(8).getScore());
            editor.putInt("h_score10",scoresArray.get(9).getScore());

            editor.putString("h_score1_name",scoresArray.get(0).getName());
            editor.putString("h_score2_name",scoresArray.get(1).getName());
            editor.putString("h_score3_name",scoresArray.get(2).getName());
            editor.putString("h_score4_name",scoresArray.get(3).getName());
            editor.putString("h_score5_name",scoresArray.get(4).getName());
            editor.putString("h_score6_name",scoresArray.get(5).getName());
            editor.putString("h_score7_name",scoresArray.get(6).getName());
            editor.putString("h_score8_name",scoresArray.get(7).getName());
            editor.putString("h_score9_name",scoresArray.get(8).getName());
            editor.putString("h_score10_name",scoresArray.get(9).getName());
        }

        editor.commit();
    }

    private void newHighScoreCheck() {
        //int maxScore = Integer.max(scoreP1,scoreP2);
        maxScore = (scoreP1 > scoreP2 ? scoreP1 : scoreP2) ;
        if(isEasy)
        minOfTop10 = sharedPreferences.getInt("e_score10",0);
        else if(isMed)
            minOfTop10 = sharedPreferences.getInt("m_score10",0);
        else
            minOfTop10 = sharedPreferences.getInt("h_score10",0);


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


                    Button returnBtn = dialogview.findViewById(R.id.return_to_game);


                    returnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String username = userEt.getText().toString();
                            scoresArray.add(5,new Person(username,maxScore));
                            updateHighScore();
                            maxScore = 0;
                            new_high_score = false;
                            dialog.dismiss();
                            //try to open specific-fragment
                            Intent i = new Intent(GameActivity.this, FragmentViewPagerActivity.class);
                            if(isEasy){
                                i.putExtra("frgToLoad",0);
                            }
                            else if(isMed)
                                i.putExtra("frgToLoad", 1);
                            else
                                i.putExtra("frgToLoad", 2);

                            i.putExtra("fromMain",false);
                            startActivity(i);

                            //end
                            //startActivity(new Intent(GameActivity.this, FragmentViewPagerActivity.class));
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
            winnerAnim();
        }
        else {
            Random random = new Random();
            // 0 is addition, 1 is subtraction, 2 is multiplication and 3 is divide
            answerIndex = random.nextInt(4); // index of correct option
            int firstQnInt = questionArray.get(random.nextInt(questionArray.size()));
            int secondQnInt = questionArray.get(random.nextInt(questionArray.size()));
            if(isHard&&(firstQnInt == secondQnInt))
            {
                Log.d("isHard","first==second");
                do
                    firstQnInt = questionArray.get(random.nextInt(questionArray.size()));
                while(firstQnInt == secondQnInt);
            }
            String operation = "";
            int answer = 0;
            double hardAnswer = 0;
            int questionType = 0;
            if(gameLvl.equals("easyP2")){
                questionType = random.nextInt(2); // get question type
                switch(questionType) {
                    case 0:
                        //addition
                        answer = firstQnInt + secondQnInt;
                        operation = "+";
                        break;
                    case 1:
                        //subtraction
                        if (firstQnInt < secondQnInt) {
                            firstQnInt = getItself(secondQnInt, secondQnInt = firstQnInt);
                        }
                        answer = firstQnInt - secondQnInt;
                        operation = "-";
                        break;
                }
            }

            else if(gameLvl.equals("mediumP2")){
                questionType = random.nextInt(3); // get question type
                switch(questionType) {
                    case 0:
                        //addition
                        answer = firstQnInt + secondQnInt;
                        operation = "+";
                        break;
                    case 1:
                        //subtraction
                        if (firstQnInt < secondQnInt) {
                            firstQnInt = getItself(secondQnInt, secondQnInt = firstQnInt);
                        }
                        answer = firstQnInt - secondQnInt;
                        operation = "-";
                        break;
                    case 2:
                        //multiplication
                        answer = firstQnInt * secondQnInt;
                        operation = "x";
                        break;
                }
            }
            else {
                questionType = random.nextInt(2); // get question type
                switch(questionType) {
                    case 0:
                        //multiplication
                        answer = firstQnInt * secondQnInt;
                        operation = "x";
                        break;
                    case 1:
                        if (firstQnInt < secondQnInt) {
                            //if first < second then swap them
                            firstQnInt = getItself((int) secondQnInt, secondQnInt = (int) firstQnInt);
                        }
                        hardAnswer = (double)firstQnInt / secondQnInt;
                        operation = "/";
                        break;
                }
            }

            Random r = new Random();
            int rnd = 0;
            if(isHard){
                numArrays.doubleRandomNumbers = r.doubles(8, answer,answer+2).toArray();
                numArrays.RandomNumbers = r.ints(8, answer+1,answer+8).toArray();
            }
            else{
                    numArrays.RandomNumbers = r.ints(8, answer+1,answer+8).toArray();
            }
            uniqueArrays();
            if(isHard){
                for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
                    Button childButtonP1 = (Button) answerLayoutP1.getChildAt(i);
                    Button childButtonP2 = (Button) answerLayoutP2.getChildAt(i);
                    Double randomValueHard = 0.0 ;

                    if(questionType == 0) // is '*'
                        randomValueHard =(double)numArrays.RandomNumbers[rnd++];
                    else
                    randomValueHard = numArrays.doubleRandomNumbers[rnd++];

                    if (childButtonP1.getTag().toString().equals(Integer.toString(answerIndex))) {
                        if(questionType == 0) { // is '*'
                           // hardAnswer =(double) answer;
                            childButtonP1.setText(String.valueOf(answer));
                        }
                        else // is '/'
                        childButtonP1.setText(new DecimalFormat("##.###").format(hardAnswer));
                    } else {
                        if (randomValueHard == hardAnswer) {
                            randomValueHard += random.nextInt(5);
                        }
                        if(questionType == 0) // is '*'
                        {
                            int temp=randomValueHard.intValue();
                            childButtonP1.setText(String.valueOf(temp));
                        }
                        else
                            childButtonP1.setText((new DecimalFormat("##.###").format(randomValueHard)));

                    }
                    // second player-set a 4 numbers that can be the answer.
                    if (childButtonP2.getTag().toString().equals(Integer.toString(answerIndex))) {
                        if(questionType == 0) { // is '*'
                            //hardAnswer = (double) answer;
                            childButtonP2.setText(String.valueOf(answer));
                        }
                        else
                            childButtonP2.setText(new DecimalFormat("##.###").format(hardAnswer));
                    } else {
                        if (randomValueHard == hardAnswer) {
                            randomValueHard += random.nextInt(5);
                        }
                        if(questionType == 0) // is '*'
                        {
                            int temp=randomValueHard.intValue();
                            childButtonP2.setText(String.valueOf(temp));
                        }
                        else
                        childButtonP2.setText((new DecimalFormat("##.###").format(randomValueHard)));
                    }
                }
            }
            else{
                for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
                    Button childButtonP1 = (Button) answerLayoutP1.getChildAt(i);
                    Button childButtonP2 = (Button) answerLayoutP2.getChildAt(i);
                    Integer randomValue = 0;
                    if (childButtonP1.getTag().toString().equals(Integer.toString(answerIndex))) {
                        childButtonP1.setText(String.valueOf(answer));
                    } else {
                        randomValue = numArrays.RandomNumbers[++rnd];
                        if (randomValue == answer) {
                            randomValue += random.nextInt(5);
                        }
                        childButtonP1.setText(String.valueOf(randomValue));
                    }
                    // second player-set a 4 numbers that can be the answer.
                    if (childButtonP2.getTag().toString().equals(Integer.toString(answerIndex))) {
                        childButtonP2.setText(String.valueOf(answer));
                    } else {
                        childButtonP2.setText(String.valueOf(randomValue));
                    }
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


    private void uniqueArrays() {
        if(numArrays.RandomNumbers != null){
            Arrays.sort(numArrays.RandomNumbers);
            for(int i=0;i<numArrays.RandomNumbers.length - 1;i++){
                    numArrays.RandomNumbers[i] = numArrays.RandomNumbers[i]+i ;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startGame();
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
            case "easyP2":
                millisInFuture = 20000;
                break;
            case "mediumP2":
                millisInFuture = 10000;
                break;
            case "hardP2":
                millisInFuture = 10000;
                break;
        }

        countDown = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //amend timeCountDown on every tick
                timeCountDownP1.setText(String.valueOf(millisUntilFinished / 1000));
                timeCountDownP2.setText(String.valueOf(millisUntilFinished / 1000));
//                if (millisUntilFinished == 10000) {
//
//                }
            }


            @Override
            public void onFinish() {
                if (scoreP1 == scoreP2){
                    tieScore = true;
                    tieScore = true;
                    playAgainTie();
                    return;
                }
                else {
                winnerAnim();
                }
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        newHighScoreCheck();
                    }
                }, 2050);

                    //Show playAgain button
                    showView(playAgainButton);
                    //Set gamestate to inactive
                    isActive = false;
                }
        }.start();
    }

    private void winnerAnim() {
        hideView(answerLayoutP1);
        hideView(answerLayoutP2);
        hideView(questionTextP1);
        hideView(questionTextP2);
        hideView(timerLayoutP1);
        hideView(timerLayoutP2);
        hideView(labelsP1);
        hideView(labelsP2);
        playAgainButton.setVisibility(View.INVISIBLE);
        if (scoreP1 > scoreP2) {
            winnerP1.setVisibility(View.VISIBLE);
            showView(winnerP1);
            winnerP1.playAnimation();
        } else {  //(scoreP1>scoreP2)
            winnerP2.setVisibility(View.VISIBLE);
            showView(winnerP2);
            winnerP2.playAnimation();
        }
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scoreP1 > scoreP2)
                winnerP1.setVisibility(View.INVISIBLE);
                else
                    winnerP2.setVisibility(View.INVISIBLE);
                playAgainButton.setVisibility(View.VISIBLE);

            }
        }, 2000);

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
