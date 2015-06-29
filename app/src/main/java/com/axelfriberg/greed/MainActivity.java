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
    private ImageButton mDie0;
    private ImageButton mDie1;
    private ImageButton mDie2;
    private ImageButton mDie3;
    private ImageButton mDie4;
    private ImageButton mDie5;
    private boolean[] selected;
    private boolean[] throwDice;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        greed = new Greed();
        selected = new boolean[6];
        throwDice = new boolean[6];
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
            }
        });

        mDie0 = (ImageButton) findViewById(R.id.die0);
        mDie0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(0, mDie0);
            }
        });

        mDie1 = (ImageButton) findViewById(R.id.die1);
        mDie1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(1, mDie1);
            }
        });

        mDie2 = (ImageButton) findViewById(R.id.die2);
        mDie2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(2, mDie2);
            }
        });

        mDie3 = (ImageButton) findViewById(R.id.die3);
        mDie3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(3, mDie3);
            }
        });

        mDie4 = (ImageButton) findViewById(R.id.die4);
        mDie4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(4, mDie4);
            }
        });

        mDie5 = (ImageButton) findViewById(R.id.die5);
        mDie5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select(5, mDie5);
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
