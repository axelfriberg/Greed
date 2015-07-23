package com.axelfriberg.greed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private Greed greed;
    private Button mSaveButton;
    private Button mScoreButton;
    private Button mThrowButton;
    private TextView mScoreTextView;
    private TextView mRoundTextView;
    private TextView mRoundScoreTextView;
    private ImageButton[] mDiceButtons;
    private boolean[] selected;
    public final static String EXTRA_MESSAGE = "com.axelfriberg.greed.WINNING";
    static final String STATE_GREED = "greed";
    static final String STATE_SAVE_BUTTON ="saveButton";
    static final String STATE_SCORE_BUTTON ="scoreButton";
    static final String STATE_THROW_BUTTON ="throwButton";
    static final String STATE_SCORE_TEXT ="currentScore";
    static final String STATE_ROUND_TEXT ="currentRound";
    static final String STATE_ROUNDSCORE_TEXT ="currentRoundScore";
    static final String STATE_SELECTED_ARRAY ="selectedArray";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        greed = new Greed();
        selected = new boolean[6];
        mDiceButtons = new ImageButton[6];

        setContentView(R.layout.activity_main);
        mScoreTextView = (TextView) findViewById(R.id.score_TextView);
        mRoundTextView = (TextView) findViewById(R.id.round_TextView);
        mRoundScoreTextView = (TextView) findViewById(R.id.round_score_TextView);

        mSaveButton = (Button)findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    newRound();
                    mScoreTextView.setText("Score: " + greed.getTotalScore());
                    mRoundTextView.setText("Round: "+greed.getRound());
            }
        });

        mScoreButton = (Button) findViewById(R.id.score_button);
        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int score = greed.score(selected);
                if(score < Greed.FIRST_THROW_LIMIT && greed.getToss() == 1){
                    newRound();
                    mRoundTextView.setText("Round: " + greed.getRound());
                    Toast.makeText(getApplicationContext(), "You got less than "+Greed.FIRST_THROW_LIMIT+" points on your first throw and a new round has begun", Toast.LENGTH_SHORT).show();
                }else if (score == 0) {
                    newRound();
                    mRoundTextView.setText("Round: " + greed.getRound());
                    Toast.makeText(getApplicationContext(), "You did not get any points this throw and a new round has begun", Toast.LENGTH_SHORT).show();
                } else if(greed.getTotalScore() >= Greed.WIN_LIMIT) {
                    win();
                }else {
                    mRoundScoreTextView.setText("Round score: " + greed.getRoundScore());
                    boolean[] saved = greed.getSaved();
                    if (!allSaved(saved)) {
                        for (int i = 0; i < 6; i++) {
                            if (saved[i]) {
                                setDiceImage("grey" + greed.getDice()[i], mDiceButtons[i]);
                                mDiceButtons[i].setEnabled(false);
                                Toast.makeText(getApplicationContext(), "You got " + score + " points.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        greed.allSaved();
                        Toast.makeText(getApplicationContext(), "You got " + score + " points, and get to throw all the dice again.", Toast.LENGTH_SHORT).show();
                    }
                    mThrowButton.setEnabled(true);
                    mScoreButton.setEnabled(false);
                    mSaveButton.setEnabled(true);
                }
            }
        });

        mThrowButton = (Button) findViewById(R.id.throw_button);
        mThrowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                greed.newThrow();
                boolean[] saved = greed.getSaved();
                for (int i = 0; i < 6; i++) {
                    selected[i] = false;
                    if (!saved[i]) {
                        setDiceImage("white" + greed.getDice()[i],mDiceButtons[i]);
                    }
                }
                mThrowButton.setEnabled(false);
                mScoreButton.setEnabled(true);
                mSaveButton.setEnabled(false);
            }
        });

        for (int i = 0; i < 6; i++) {
            String name = "die";
            name += i;
            int idNum = getResources().getIdentifier(name,"id",getPackageName());
            mDiceButtons[i] = (ImageButton) findViewById(idNum);
            final int finalI = i;
            mDiceButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    select(finalI, mDiceButtons[finalI]);
                }
            });
        }

        mSaveButton.setEnabled(false);
        mScoreButton.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        //save the state of the game
        savedInstanceState.putParcelable(STATE_GREED, greed);
        //save the state of the buttons
        savedInstanceState.putBoolean(STATE_SAVE_BUTTON,mSaveButton.isEnabled());
        savedInstanceState.putBoolean(STATE_SCORE_BUTTON,mScoreButton.isEnabled());
        savedInstanceState.putBoolean(STATE_THROW_BUTTON, mThrowButton.isEnabled());
        //save the state of the text views
        savedInstanceState.putString(STATE_SCORE_TEXT, mScoreTextView.getText().toString());
        savedInstanceState.putString(STATE_ROUND_TEXT, mRoundTextView.getText().toString());
        savedInstanceState.putString(STATE_ROUNDSCORE_TEXT, mRoundScoreTextView.getText().toString());
        //save which of the die buttons are selected
        savedInstanceState.putBooleanArray(STATE_SELECTED_ARRAY,selected);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the state of the game
        greed = savedInstanceState.getParcelable(STATE_GREED);
        // Restore state of the buttons
        mSaveButton.setEnabled(savedInstanceState.getBoolean(STATE_SAVE_BUTTON));
        mScoreButton.setEnabled(savedInstanceState.getBoolean(STATE_SCORE_BUTTON));
        mThrowButton.setEnabled(savedInstanceState.getBoolean(STATE_THROW_BUTTON));
        // Restore the state of the text views
        mScoreTextView.setText(savedInstanceState.getString(STATE_SCORE_TEXT));
        mRoundTextView.setText(savedInstanceState.getString(STATE_ROUND_TEXT));
        mRoundScoreTextView.setText(savedInstanceState.getString(STATE_ROUNDSCORE_TEXT));
        // Restore the state of the selected array
        selected = savedInstanceState.getBooleanArray(STATE_SELECTED_ARRAY);

        boolean[] saved = greed.getSaved();
        for (int i = 0; i < 6; i++) {
            int diceVal = greed.getDice(i);
            if(saved[i]){
                setDiceImage("grey"+diceVal,mDiceButtons[i]);
                mDiceButtons[i].setEnabled(false);
            } else if(selected[i]){
                setDiceImage("red"+diceVal,mDiceButtons[i]);
            } else {
                setDiceImage("white"+diceVal,mDiceButtons[i]);
            }
        }
    }

    private void win() {
        Intent intent = new Intent(this, WinningActivity.class);
        String message = "You got "+greed.getTotalScore()+" points after "+greed.getRound()+" turns. Press back to start a new game.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();
    }

    private void select(int index, ImageButton ib) {
        if (!selected[index]){
            setDiceImage("red"+greed.getDice()[index],ib);
            selected[index] = true;
        } else {
            setDiceImage("white"+greed.getDice()[index],ib);
            selected[index] = false;
        }
    }

    private void newRound(){
        mRoundScoreTextView.setText("Round score: 0");
        for (int i = 0; i < 6; i++) {
            selected[i] = false;
            mDiceButtons[i].setEnabled(true);
            setDiceImage("white" + greed.getDice()[i], mDiceButtons[i]);
        }
        greed.newRound();
        mSaveButton.setEnabled(false);
        mScoreButton.setEnabled(false);
        mThrowButton.setEnabled(true);
    }

    private boolean allSaved(boolean[] s){
        for(boolean b : s){
            if(!b){
                return false;
            }
        }
        for (int i = 0; i < 6; i++) {
            setDiceImage("white"+greed.getDice()[i], mDiceButtons[i]);
            mDiceButtons[i].setEnabled(true);
            selected[i] = false;
        }
        return true;
    }

    private void setDiceImage(String s, ImageButton button){
        int resID = getResources().getIdentifier(s,"drawable",getPackageName());
        button.setImageResource(resID);
    }
}
