package com.axelfriberg.greed;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class MainActivity extends ActionBarActivity {
    private Greed greed;
    private Button mSaveButton;
    private Button mScoreButton;
    private Button mThrowButton;
    private ImageButton[] mDiceButtons;
    private boolean[] selected;
    private boolean[] throwDice;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        greed = new Greed();
        selected = new boolean[6];
        throwDice = new boolean[6];
        mDiceButtons = new ImageButton[6];
        for(int i = 0; i < 6; i++){
            throwDice[i] = true;
        }
        setContentView(R.layout.activity_main);

        mSaveButton = (Button)findViewById(R.id.save_button);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greed.save();
            }
        });

        mScoreButton = (Button) findViewById(R.id.score_button);
        mScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                greed.score();
            }
        });

        mThrowButton = (Button) findViewById(R.id.throw_button);
        mThrowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                greed.newThrow(throwDice);
                String s;
                int resID;
                for(int i = 0; i < 6; i++){
                    s = "white"+greed.getDice()[i];
                    resID = getResources().getIdentifier(s,"drawable",getPackageName());
                    mDiceButtons[i].setImageResource(resID);
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
}
