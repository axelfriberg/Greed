package com.axelfriberg.greed;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;


public class WinningActivity extends ActionBarActivity {
    private int mScore;
    private int mTurnScore;
    private TextView mWinTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);
        mScore = 0;
        mTurnScore = 0;
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        mWinTextView = (TextView) findViewById(R.id.finished_score_TextView);
        mWinTextView.setText(message);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}
