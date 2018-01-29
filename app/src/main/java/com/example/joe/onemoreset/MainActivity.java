package com.example.joe.onemoreset;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView t;
    TextView time;
    Button b;
    CountDownTimer c;
    boolean isStarted = false;
    boolean isQuitter = false;
    boolean isSnapped = false;
    int score;
    int secondsLeft;
    int quitCountdown;
    int timer;
    int w;
    int last;
    int timePerRep = 5;
    final String[] workouts = {"Push ups", "Burpees", "Crunches",
            "High Knees", "Jumping Jacks", "Frog Jumps", "Mountain Climbers",
            "Lunges"};
    // ^^ This array is scalable as the code uses .length variables ^^
    int[][] reps = new int[workouts.length][2]; //reps[to-do][total-done]

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        resetReps();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView)findViewById(R.id.txtWorkout);
        b = (Button)findViewById(R.id.btnConfirm);
        time = (TextView)findViewById(R.id.txtTimer); //used for testing time

        b.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                if (isStarted) {
                    c.cancel();
                    if (!isQuitter) {
                        reps[last][1] += reps[last][0]; //adds the reps already done previously
                        reps[last][0] += 1; //adds 1 to the number of reps to be done next time
                    }
                }
                else {
                    isStarted = true;
                    b.setText(getString(R.string.next));
                }
                w = (int) (Math.random() * workouts.length);
                while (w == last) {
                    w = (int) (Math.random() * workouts.length); //picks a workout
                }

                t.setText(reps[w][0] + " " + workouts[w]);

                //TIMER
                timer = reps[w][0] * timePerRep;
                startTimer(timer);

                //DONE BEFORE THE NEXT SET
                last = w;
                if (isQuitter){
                    endWorkout();
                }
            }
        });
        b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (isStarted && !isQuitter) {
                    isQuitter = true;
                    b.setText(getString(R.string.quit));
                }
                return true;
            }
        });
    }

    public void startTimer(int s){
        time.setText("" + s);
        c = new CountDownTimer((s+1) * 1000, 1000) {
            @Override
            public void onTick(long l) {
                secondsLeft = (int)l/1000;
                time.setText("Time: " + secondsLeft);
                if (isQuitter){
                    if (!isSnapped){
                        quitCountdown = secondsLeft;
                        isSnapped = true;
                    }
                    else{
                        if (secondsLeft == (quitCountdown-5)){
                            isQuitter = false;
                            isSnapped = false;
                            b.setText(getString(R.string.next));
                        }
                    }
                }
            }
            @Override
            public void onFinish() {
                endWorkout();
            }
        }.start();
    }

    public void resetReps(){
        for (int i = 0; i < reps.length; i++){
            reps[i][0] = 5; //assigns a default 5 reps to each workout
            reps[i][1] = 0; //resets the amount of reps done to zero
        }
    }

    public String tally(){
        String s = "";
        for (int i = 0; i <workouts.length; i++){
            s += (workouts[i] + ": " + reps[i][1] + "\n");
        }
        return s;
    }

    public void endWorkout(){
        c.cancel();
        c = null;
        time.setText("");
        t.setText(tally());
        b.setText(getString(R.string.start));
        resetReps();
        isStarted = false;
        isQuitter = false;
        isSnapped = false;
    }
}
