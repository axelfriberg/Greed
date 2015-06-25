package com.axelfriberg.greed;

import java.util.Random;

/**
 * Created by Axel on 2015-06-25.
 */
public class Greed {
    private int[] dice;
    private int max;
    private int min;
    private Random rand;
    private int score;
    private int roundScore;
    private int round;

    public Greed(){
        rand = new Random();
        max = 6;
        min = 1;
        dice = new int[6];
        score = 0;
        roundScore = 0;
        round = 1;
        for (int i = 0; i < 6; i++) {
            dice[i] = randInterval(rand, max, min);
        }
    }

    public void newThrow(boolean d0, boolean d1, boolean d2, boolean d3, boolean d4, boolean d5){
        if(d0){
            dice[0] = randInterval(rand, max, min);
        }

        if(d1){
            dice[1] = randInterval(rand, max, min);
        }

        if(d2){
            dice[2] = randInterval(rand, max, min);
        }

        if(d3){
            dice[3] = randInterval(rand, max, min);
        }

        if(d4){
            dice[4] = randInterval(rand, max, min);
        }

        if(d5){
            dice[5] = randInterval(rand, max, min);
        }

    }

    public void save(){
        score += roundScore;
        round++;
    }

    public int getScore(){
        return score;
    }

    public int getRoundScore(){
        return roundScore;
    }

    private int randInterval(Random r, int max, int min){
         return r.nextInt(max - min + 1) + min;
    }

    private int calculateScore(){
        return 0;
    }

}
