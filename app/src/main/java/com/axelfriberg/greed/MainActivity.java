package com.axelfriberg.greed;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private Greed greed;
    private Button mSaveButton;
    private Button mScoreButton;
    private Button mThrowButton;
    private TextView mScoreTextView;
    private TextView mRoundTextView;
    private TextView mRoundScoreTextView;
    private ImageButton[] mDiceButtons;
    private boolean[] selected;
    private boolean[] throwDice;
    private static final String TAG = "MainActivity";
    private static final int LIMIT = 100;
    private boolean scorePressed;
    private boolean thrown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        greed = new Greed();
        selected = new boolean[6];
        throwDice = new boolean[6];
        mDiceButtons = new ImageButton[6];
        scorePressed = true;
        for(int i = 0; i < 6; i++){
            throwDice[i] = true;
        }

        setContentView(R.layout.activity_main);
        mScoreTextView = (TextView) findViewById(R.id.score_TextView);
        mRoundTextView = (TextView) findViewById(R.id.round_TextView);
        mRoundScoreTextView = (TextView) findViewById(R.id.round_score_TextView);

        mSaveButton = (Button)findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newRound();
                mScoreTextView.setText("Score: "+greed.getScore());
            }
        });

        mScoreButton = (Button) findViewById(R.id.score_button);
        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(thrown) {
                    scorePressed = true;
                    int score = greed.score(selected);
                    if(score < LIMIT && greed.getToss() == 1){
                        newRound();
                        mRoundTextView.setText("Round: " + greed.getRound());
                        Toast.makeText(getApplicationContext(), "You got less than "+LIMIT+" points on your first throw and a new round has begun", Toast.LENGTH_SHORT).show();
                    }else if (score == 0) {
                        newRound();
                        mRoundTextView.setText("Round: " + greed.getRound());
                        Toast.makeText(getApplicationContext(), "You did not get any points this throw and a new round has begun", Toast.LENGTH_SHORT).show();
                    } else {
                        mRoundScoreTextView.setText("Round score: " + greed.getRoundScore());

                        boolean[] saved = greed.getSaved();
                        if(!allSaved(saved)){
                            for (int i = 0; i < 6; i++) {
                                if (saved[i]) {
                                    String s;
                                    int resID;
                                    s = "grey" + greed.getDice()[i];
                                    resID = getResources().getIdentifier(s, "drawable", getPackageName());
                                    mDiceButtons[i].setImageResource(resID);
                                    mDiceButtons[i].setEnabled(false);
                                    Toast.makeText(getApplicationContext(), "You got " + score + " points.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            greed.allSaved();
                            Toast.makeText(getApplicationContext(), "You got " + score + " points, and get to throw all the dice again.", Toast.LENGTH_SHORT).show();
                        }

                    }
                    thrown = false;
                } else {
                    Toast.makeText(getApplicationContext(), "You need to throw the dice first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mThrowButton = (Button) findViewById(R.id.throw_button);
        mThrowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(scorePressed) {
                    greed.newThrow();
                    thrown = true;
                    String s;
                    int resID;
                    boolean[] saved = greed.getSaved();
                    for (int i = 0; i < 6; i++) {
                        if (!saved[i]) {
                            s = "white" + greed.getDice()[i];
                            resID = getResources().getIdentifier(s, "drawable", getPackageName());
                            mDiceButtons[i].setImageResource(resID);
                        }
                    }
                    thrown = true;
                } else {
                    Toast.makeText(getApplicationContext(), "You need to press score first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mDiceButtons[0] = (ImageButton) findViewById(R.id.die0);
        mDiceButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(0, mDiceButtons[0]);
            }
        });

        mDiceButtons[1] = (ImageButton) findViewById(R.id.die1);
        mDiceButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(1, mDiceButtons[1]);
            }
        });

        mDiceButtons[2] = (ImageButton) findViewById(R.id.die2);
        mDiceButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(2, mDiceButtons[2]);
            }
        });

        mDiceButtons[3] = (ImageButton) findViewById(R.id.die3);
        mDiceButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(3, mDiceButtons[3]);
            }
        });

        mDiceButtons[4] = (ImageButton) findViewById(R.id.die4);
        mDiceButtons[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(4, mDiceButtons[4]);
            }
        });

        mDiceButtons[5] = (ImageButton) findViewById(R.id.die5);
        mDiceButtons[5].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(5, mDiceButtons[5]);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void select(int index, ImageButton ib) {
        String s;
        int resID;
        if (!selected[index]){
            s = "red"+greed.getDice()[index];
            resID = getResources().getIdentifier(s,"drawable",getPackageName());
            ib.setImageResource(resID);
            selected[index] = true;
        } else {
            s = "white"+greed.getDice()[index];
            resID = getResources().getIdentifier(s,"drawable",getPackageName());
            ib.setImageResource(resID);
            selected[index] = false;
        }
    }

    private void newRound(){
        scorePressed = true;
        thrown = false;
        String s;
        int resID;
        mRoundScoreTextView.setText("Round score: 0");
        for (int i = 0; i < 6; i++) {
            mDiceButtons[i].setEnabled(true);
            s = "white" + greed.getDice()[i];
            resID = getResources().getIdentifier(s, "drawable", getPackageName());
            mDiceButtons[i].setImageResource(resID);
            selected[i] = false;
        }

        greed.newRound();
    }

    private boolean allSaved(boolean[] s){
        for(boolean b : s){
            if(!b){
                return false;
            }
        }
        String str;
        int resID;
        for (int i = 0; i < 6; i++) {
            str = "white"+greed.getDice()[i];
            resID = getResources().getIdentifier(str,"drawable",getPackageName());
            mDiceButtons[i].setImageResource(resID);
            mDiceButtons[i].setEnabled(true);
            selected[i] = false;
        }
        return true;
    }
}
