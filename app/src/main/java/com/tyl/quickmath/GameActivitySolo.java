package com.tyl.quickmath;

import android.content.Intent;
import android.content.SharedPreferences;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import pl.droidsonroids.gif.GifTextView;

public class GameActivitySolo extends AppCompatActivity {
    GlobalClass global;
    MediaPlayer mediaPlayer;
    TextView timeCountDownP1;
    TextView scoreTextP1, tieTVP1;
    TextView questionTextP1, questionTextP3;
    TextView questionCounterP1;
    LinearLayout answerLayoutP1, timerLayoutP1, labelsP1;
    ImageButton playAgainButton;
    GifTextView gifImageView;
    CountDownTimer countDown;
    boolean isActive;
    int answerIndex, minOfTop10;
    int count_the_question, count_tie_question;
    String gameLvl;
    boolean isHard, tieScore;
    ArrayList<Integer> questionArray;
    ImageButton backDialog;

    AnswerArrays numArrays, medArrays, hardArrays;
    //ArrayList<Integer> answerArrayLow;
    private int scoreP1 = 0;
    SharedPreferences sharedPreferences;
    private boolean new_high_score;
    private List<Person> scoresArray;
    private int maxScore;
    final Handler hndlr = new Handler();

    protected void onCreate(Bundle savedInstanceState) {
        global = GlobalClass.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game1);
        String game_level = getIntent().getStringExtra("level");
        gameLvl = game_level;
        sharedPreferences = this.getSharedPreferences("sound", this.MODE_PRIVATE);
        global.muteBackgroudMusic(sharedPreferences.getBoolean("mute_sound", false));

        timeCountDownP1 = findViewById(R.id.timeCountDownP1);
        questionTextP1 = findViewById(R.id.questionTextP1);
        timerLayoutP1 = findViewById(R.id.layout_p1);

        questionCounterP1 = findViewById(R.id.questionCounterP1);
        answerLayoutP1 = findViewById(R.id.answersButtons);

        labelsP1 = findViewById(R.id.labelsP1);

        backDialog = findViewById(R.id.return_to_menu);
        gifImageView = findViewById(R.id.start_gif);

        playAgainButton = findViewById(R.id.imgBtnPlayAgain);
        scoreTextP1 = findViewById(R.id.scoreP1);
        isHard = gameLvl.equals("hardP1");
        setArrayValues(game_level);
        scoresArray = new ArrayList<>();
        initScoresTable();
        initArraysLvl();
        startGame();
    }

    private void startGame() {
//        playGif();
        //Hide useless buttons
        hideView(playAgainButton);
        //3,2,1 -->go
//        hideViewsForAnim();
//        showView(gifImageView);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isActive = true; //set game state to active
//                hideView(gifImageView);
//                showViewsForAnim();
                startTimer();
                getNextQuestionAnswer();
            }
        }, 1000);

    }

    private void endGame() {
        countDown.cancel();
        countDown.onFinish();
    }
    public void disableBtns() {
        //Disable answer buttons from being pressed
        for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
            answerLayoutP1.getChildAt(i).setEnabled(false);
        }
    }
    private void enableBtns() {
        for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
            answerLayoutP1.getChildAt(i).setEnabled(true);
        }
    }
//    private void winnerAnim() {
//        hideView(answerLayoutP1);
//        hideView(questionTextP1);
//        hideView(timerLayoutP1);
//        hideView(labelsP1);
//        playAgainButton.setVisibility(View.INVISIBLE);
//        if (scoreP1 > currentScore) {
//            winnerP1.setVisibility(View.VISIBLE);
//            showView(winnerP1);
//            winnerP1.playAnimation();
//
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (scoreP1 > scoreP2)
//                        winnerP1.setVisibility(View.INVISIBLE);
//                    else
//                        winnerP2.setVisibility(View.INVISIBLE);
//                    playAgainButton.setVisibility(View.VISIBLE);
//
//                }
//            }, 2000);
//
//        }
//
//    }
public void checkAnswerP1(View view) {
    disableBtns();
    if (view.getTag().toString().equals(Integer.toString(answerIndex))) {
        scoreP1++;
    }
    scoreTextP1.setText(Integer.toString(scoreP1));

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

    private void startTimer () {

        int millisInFuture = 0;
        switch (gameLvl) {
            case "easyP1":
                millisInFuture = 20000;
                break;
            case "mediumP1":
                millisInFuture = 10000;
                break;
            case "hardP1":
                millisInFuture = 10000;
                break;
        }
        countDown = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //amend timeCountDown on every tick
                timeCountDownP1.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

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
    private void newHighScoreCheck () {

        minOfTop10 = sharedPreferences.getInt("highscore10", 0);

        if (scoreP1 > minOfTop10)
            new_high_score = true;

        if (new_high_score) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivitySolo.this);
                    final View dialogview = getLayoutInflater().inflate(R.layout.new_high_score, null);
                    builder.setView(dialogview).setCancelable(false);
                    TextView dialogScoreTv = dialogview.findViewById(R.id.myhighscore);
                    dialogScoreTv.setText(Integer.toString(scoreP1));
                    final EditText userEt = dialogview.findViewById(R.id.username);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    final RelativeLayout container = dialogview.findViewById(R.id.highscore_layout);


                    ImageButton returnBtn = dialogview.findViewById(R.id.return_to_game);
                    final Intent top_intent = new Intent(GameActivitySolo.this, TopTableActivity.class);
                    top_intent.putExtra("level", gameLvl);

                    returnBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //to do : update shared prefferences with name and picture
                            String username = userEt.getText().toString();
                            scoresArray.add(5, new Person(username, scoreP1));
                            updateHighScore();
                            scoreP1 = 0;
                            new_high_score = false;
                            dialog.dismiss();
                            startActivity(top_intent);
                        }
                    });

                }
            }, 0);
        } else //game over,do nothing
        {
            return;
        }


    }

    private void updateHighScore () {

        Collections.sort(scoresArray);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("highscore1", scoresArray.get(0).getScore());
        editor.putInt("highscore2", scoresArray.get(1).getScore());
        editor.putInt("highscore3", scoresArray.get(2).getScore());
        editor.putInt("highscore4", scoresArray.get(3).getScore());
        editor.putInt("highscore5", scoresArray.get(4).getScore());
        editor.putInt("highscore6", scoresArray.get(5).getScore());
        editor.putInt("highscore7", scoresArray.get(6).getScore());
        editor.putInt("highscore8", scoresArray.get(7).getScore());
        editor.putInt("highscore9", scoresArray.get(8).getScore());
        editor.putInt("highscore10", scoresArray.get(9).getScore());

        editor.putString("score1_name", scoresArray.get(0).getName());
        editor.putString("score2_name", scoresArray.get(1).getName());
        editor.putString("score3_name", scoresArray.get(2).getName());
        editor.putString("score4_name", scoresArray.get(3).getName());
        editor.putString("score5_name", scoresArray.get(4).getName());
        editor.putString("score6_name", scoresArray.get(5).getName());
        editor.putString("score7_name", scoresArray.get(6).getName());
        editor.putString("score8_name", scoresArray.get(7).getName());
        editor.putString("score9_name", scoresArray.get(8).getName());
        editor.putString("score10_name", scoresArray.get(9).getName());
        editor.commit();
    }

    private void showViewsForAnim () {
        showView(labelsP1);
        showView(timerLayoutP1);
        showView(questionTextP1);
        showView(answerLayoutP1);
    }

    private void hideViewsForAnim () {
        hideView(labelsP1);
        hideView(timerLayoutP1);
        hideView(questionTextP1);
        hideView(answerLayoutP1);
    }


    public void playGif () {
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

    private void setArrayValues (String level){
        switch (level) {
            case "easyP1":
                questionArray = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 6, 8, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20));
                break;
            case "mediumP1":
                questionArray = new ArrayList<>(Arrays.asList(21, 22, 24, 25, 27, 28, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60));
                break;
            case "hardP1":
                questionArray = new ArrayList<>(Arrays.asList(13, 17, 19, 23, 27, 33, 29, 46, 93, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
                        71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 101, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103));
                break;
        }
    }


    private void initArraysLvl () {
        //init arrays of the easy level
        numArrays = new AnswerArrays();
        //init arrays of the medium level
        medArrays = new AnswerArrays();

    }

    private void hideView (View view){
        view.setEnabled(false);
        view.setAlpha(0.0f);
    }

    private void showView (View view){
        view.setEnabled(true);
        view.setAlpha(1.0f);
    }

    private void initScoresTable () {
        minOfTop10 = sharedPreferences.getInt("highscore10", 0);
        scoresArray.add(0, new Person(sharedPreferences.getString("score1_name", "Player"), sharedPreferences.getInt("highscore1", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score2_name", "Player"), sharedPreferences.getInt("highscore2", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score3_name", "Player"), sharedPreferences.getInt("highscore3", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score4_name", "Player"), sharedPreferences.getInt("highscore4", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score5_name", "Player"), sharedPreferences.getInt("highscore5", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score6_name", "Player"), sharedPreferences.getInt("highscore6", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score7_name", "Player"), sharedPreferences.getInt("highscore7", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score8_name", "Player"), sharedPreferences.getInt("highscore8", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score9_name", "Player"), sharedPreferences.getInt("highscore9", 0)));
        scoresArray.add(new Person(sharedPreferences.getString("score10_name", "Player"), sharedPreferences.getInt("highscore10", 0)));
    }


    private void getNextQuestionAnswer () {
        if (count_the_question > 30) {
            endGame();
        }
        else {
            Random random = new Random();
            // 0 is addition, 1 is subtraction, 2 is multiplication and 3 is divide
            answerIndex = random.nextInt(4); // index of correct option
            int firstQnInt = questionArray.get(random.nextInt(questionArray.size()));
            int secondQnInt = questionArray.get(random.nextInt(questionArray.size()));
            if (isHard && (firstQnInt == secondQnInt)) {
                Log.d("isHard", "first==second");
                do
                    firstQnInt = questionArray.get(random.nextInt(questionArray.size()));
                while (firstQnInt == secondQnInt);
            }
            String operation = "";
            int answer = 0;
            double hardAnswer = 0;
            int questionType = 0;
            if (gameLvl.equals("easyP1")) {
                questionType = random.nextInt(2); // get question type
                switch (questionType) {
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
            } else if (gameLvl.equals("mediumP1")) {
                questionType = random.nextInt(3); // get question type
                switch (questionType) {
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
            } else {
                questionType = random.nextInt(2); // get question type
                switch (questionType) {
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
                        hardAnswer = (double) firstQnInt / secondQnInt;
                        operation = "/";
                        break;
                }
            }

            Random r = new Random();
            int rnd = 0;
            if (isHard) {
                numArrays.doubleRandomNumbers = r.doubles(8, answer, answer + 2).toArray();
                numArrays.RandomNumbers = r.ints(8, answer + 1, answer + 8).toArray();
            } else {
                numArrays.RandomNumbers = r.ints(8, answer + 1, answer + 8).toArray();
            }
            uniqueArrays();
            if (isHard) {
                for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
                    Button childButtonP1 = (Button) answerLayoutP1.getChildAt(i);
                    Double randomValueHard = 0.0;

                    if (questionType == 0) // is '*'
                        randomValueHard = (double) numArrays.RandomNumbers[rnd++];
                    else
                        randomValueHard = numArrays.doubleRandomNumbers[rnd++];

                    if (childButtonP1.getTag().toString().equals(Integer.toString(answerIndex))) {
                        if (questionType == 0) { // is '*'
                            // hardAnswer =(double) answer;
                            childButtonP1.setText(String.valueOf(answer));
                        } else // is '/'
                            childButtonP1.setText(new DecimalFormat("##.###").format(hardAnswer));
                    } else {
                        if (randomValueHard == hardAnswer) {
                            randomValueHard += random.nextInt(5);
                        }
                        if (questionType == 0) // is '*'
                        {
                            int temp = randomValueHard.intValue();
                            childButtonP1.setText(String.valueOf(temp));
                        } else
                            childButtonP1.setText((new DecimalFormat("##.###").format(randomValueHard)));

                    }
                }
            }
            else {
                for (int i = 0; i < answerLayoutP1.getChildCount(); i++) {
                    Button childButtonP1 = (Button) answerLayoutP1.getChildAt(i);
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

                }
            }

            //Set qn text
            questionTextP1.setText(String.format(Locale.ENGLISH, "%d %s %d", firstQnInt, operation, secondQnInt));

            if (tieScore) {
                tieTVP1.setText(String.format(Locale.ENGLISH, "Round%d", count_tie_question + 1));
            }
            //Set qn counter
            String qnCounterTxt = String.format(Locale.ENGLISH, "%d/20", count_the_question);
            questionCounterP1.setText(qnCounterTxt);
            count_the_question++;
        }
    }
    public static int getItself(int itself, int dummy)
    {
        return itself;
    }
    private void uniqueArrays() {
        if(numArrays.RandomNumbers != null){
            Arrays.sort(numArrays.RandomNumbers);
            for(int i=0;i<numArrays.RandomNumbers.length - 1;i++){
                numArrays.RandomNumbers[i] = numArrays.RandomNumbers[i]+i ;
            }
        }
    }

}
