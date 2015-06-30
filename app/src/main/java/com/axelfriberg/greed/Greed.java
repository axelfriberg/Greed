package com.axelfriberg.greed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by Axel on 2015-06-25.
 */
public class Greed {
    private int[] dice;
    private boolean[] saved;
    private int max;
    private int min;
    private Random rand;
    private int score;
    private int roundScore;
    private int round;
    private int toss;

    public Greed(){
        rand = new Random();
        max = 6;
        min = 1;
        dice = new int[6];
        score = 0;
        roundScore = 0;
        round = 1;
        toss = 0;
        for (int i = 0; i < 6; i++) {
            dice[i] = i+1;
        }
    }

    public void newThrow(){
        for (int i = 0; i < 6; i++) {
            if(!saved[0]){
                dice[i] = randInterval(rand, max, min);
            }
        }
        toss++;
    }

    public int score(boolean[] selected){
        int score = 0;
        ArrayList<Integer> choosen = new ArrayList<>();
        ArrayList<Integer> tempList = new ArrayList<>();

        for (int i = 0; i < 6; i++){
            if(selected[i]){
                choosen.add(dice[i]);
            }
        }

        for (int i = 1; i < 7; i++) {
            tempList.add(i);
        }

        //check for ladder
        boolean ladder = choosen.containsAll(tempList);

        if(ladder){
            score = 1000;
            roundScore += score;
            return score;
        }

        for (int i = 1; i < 7; i++) {
            tempList.clear();
            for (int j = 0; j < 3; j++) {
                tempList.add(i);
            }

            if(choosen.containsAll(tempList)){
                score+= 100*i;
            }
        }

        if(score == 0){
            roundScore = 0;
        }

        roundScore += score;
        return score;
    }

    public void newRound(){
        score += roundScore;
        round++;
        toss = 0;
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

    public int getRound(){
        return round;
    }

    private int randInterval(Random r, int max, int min){
         return r.nextInt(max - min + 1) + min;
    }



}
