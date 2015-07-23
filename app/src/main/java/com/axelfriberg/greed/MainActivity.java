package com.axelfriberg.greed;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


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
    static final String STATE_ROUND_SCORE_TEXT ="currentRoundScore";
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
                    //Start a new round and update the GUI
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
                if(score < Greed.FIRST_THROW_LIMIT && greed.getToss() == 1){ //Check if the first throw limit is crossed
                    newRound();
                    mRoundTextView.setText("Round: " + greed.getRound());
                }else if (score == 0) { //Start new round if the no points are recived on a throw
                    newRound();
                    mRoundTextView.setText("Round: " + greed.getRound());
                } else if(greed.getTotalScore() >= Greed.WIN_LIMIT) { //Check if the player has won
                    win();
                }else {
                    //Updates the GUI to reflect what dice has been saved. Check if all have been saved.
                    mRoundScoreTextView.setText("Round score: " + greed.getRoundScore());
                    boolean[] saved = greed.getSaved();
                    if (!allSaved(saved)) {
                        for (int i = 0; i < 6; i++) {
                            if (saved[i]) {
                                setDiceImage("grey" + greed.getDice()[i], mDiceButtons[i]);
                                mDiceButtons[i].setEnabled(false);
                            }
                        }
                    } else {
                        greed.allSaved();
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
                //Throw the dice and update the GUI to reflect the changes.
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

        //Create all the dice image buttons.
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

        //Initial state of the buttons
        mSaveButton.setEnabled(false);
        mScoreButton.setEnabled(false);
    }

    //Make sure the user wants to exit the app
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
        savedInstanceState.putBoolean(STATE_SAVE_BUTTON, mSaveButton.isEnabled());
        savedInstanceState.putBoolean(STATE_SCORE_BUTTON, mScoreButton.isEnabled());
        savedInstanceState.putBoolean(STATE_THROW_BUTTON, mThrowButton.isEnabled());
        //save the state of the text views
        savedInstanceState.putString(STATE_SCORE_TEXT, mScoreTextView.getText().toString());
        savedInstanceState.putString(STATE_ROUND_TEXT, mRoundTextView.getText().toString());
        savedInstanceState.putString(STATE_ROUND_SCORE_TEXT, mRoundScoreTextView.getText().toString());
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
        mRoundScoreTextView.setText(savedInstanceState.getString(STATE_ROUND_SCORE_TEXT));
        // Restore the state of the selected array
        selected = savedInstanceState.getBooleanArray(STATE_SELECTED_ARRAY);

        // Restore the correct state and images of the dice buttons.
        boolean[] saved = greed.getSaved();
        for (int i = 0; i < 6; i++) {
            int diceVal = greed.getDie(i);
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

    //Used when the player wins the game. Starts the new activity.
    private void win() {
        Intent intent = new Intent(this, WinningActivity.class);
        String message = "You got "+greed.getTotalScore()+" points after "+greed.getRound()+" turns. Press back to start a new game.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();
    }

    //Used to update the GUI to reflect that a die has been selected.
    private void select(int index, ImageButton ib) {
        if (!selected[index]){
            setDiceImage("red"+greed.getDice()[index],ib);
            selected[index] = true;
        } else {
            setDiceImage("white"+greed.getDice()[index],ib);
            selected[index] = false;
        }
    }

    //Starts a new round and updates the GUI to the default state.
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

    //Return true if all of the booleans in the array are true. Updates the GUI if that is the case.
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

    //Updates a single dice button image to a certain image
    private void setDiceImage(String s, ImageButton button){
        int resID = getResources().getIdentifier(s,"drawable",getPackageName());
        button.setImageResource(resID);
    }
}
