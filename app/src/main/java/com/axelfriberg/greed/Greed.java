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
            dice[i] = i+1;
        }
    }

    public void newThrow(boolean[] d){
        if(d[0]){
            dice[0] = randInterval(rand, max, min);
        }

        if(d[1]){
            dice[1] = randInterval(rand, max, min);
        }

        if(d[2]){
            dice[2] = randInterval(rand, max, min);
        }

        if(d[3]){
            dice[3] = randInterval(rand, max, min);
        }

        if(d[4]){
            dice[4] = randInterval(rand, max, min);
        }

        if(d[5]){
            dice[5] = randInterval(rand, max, min);
        }
    }

    public void save(){
        score += roundScore;
        round++;
    }

    public int score(){
        return -1;
    }

    public int getScore(){
        return score;
    }

    public int[] getDice(){
        return dice;
    }

    public int getRoundScore(){
        return roundScore;
    }

    private int randInterval(Random r, int max, int min){
         return r.nextInt(max - min + 1) + min;
    }



}
