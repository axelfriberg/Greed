package com.axelfriberg.greed;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;


public class WinningActivity extends AppCompatActivity {
    private TextView mWinTextView;
    static final String STATE_WIN_TEXT ="winTextMessage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning);
        //Get the score from the prev activity and set the text
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        mWinTextView = (TextView) findViewById(R.id.finished_score_TextView);
        mWinTextView.setText(message);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class)); //start a new game
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current win text
        savedInstanceState.putString(STATE_WIN_TEXT, mWinTextView.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore the text
        String winText = savedInstanceState.getString(STATE_WIN_TEXT);
        // Update the screen
        mWinTextView.setText(winText);
    }
}
