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
        saved = new boolean[6];
        for (int i = 0; i < 6; i++) {
            dice[i] = i+1;
        }
    }

    public void newThrow(){
        for (int i = 0; i < 6; i++) {
            if(!saved[i]){
                dice[i] = randInterval(rand, max, min);
            }
        }
        toss++;
    }

    public int score(boolean[] selected){
        //saved = selected;
        int score = 0;
        int[] chosenDice = new int[6];


        for (int i = 0; i < 6; i++){
            if(selected[i]){
                chosenDice[i] = dice[i];
            }
        }

        int[] unSorted = chosenDice;

        Arrays.sort(chosenDice);

        boolean ladder = true;
        for (int i = 0; i < 6; i++) {
            if(chosenDice[i] != i){
                ladder = false;
            }
        }

        if(ladder){
            score = 1000;
            roundScore += score;
            return score;
        }

        //tripplets
        for (int i = 0; i < 4; i++) {
            boolean tok = false;
            int index1 = -1;
            int index2 = -1;
            for (int j = i+1; j < unSorted.length; j++) {
                if(index2 == -1){
                    if(unSorted[i+j] == unSorted[i]) {
                        if(index1 == -1){
                            index1 = i+j;
                        } else {
                            index2 = i+j;
                        }
                        tok = true;
                    } else {
                        tok = false;
                    }
                } else {
                    j = unSorted.length;
                }
            }

            if (tok && chosenDice[i] == 1) {
                score += 1000;
            } else if (tok) {
                score += 100 * chosenDice[i];
            }

            if(tok){
                chosenDice[i] = 0;
                chosenDice[index1] = 0;
                chosenDice[index2] = 0;
            }
        }


        for(int i = 0; i < chosenDice.length; i++){
            if(chosenDice[i] == 1){
                score += 100;
                chosenDice[i] = 0;
            } else if(chosenDice[i] == 5){
                chosenDice[i] = 0;
                score += 50;
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

    public boolean[] getSaved(){
        return saved;
    }

    public int getRound(){
        return round;
    }

    public int getToss(){
        return toss;
    }

    private int randInterval(Random r, int max, int min){
         return r.nextInt(max - min + 1) + min;
    }





}
