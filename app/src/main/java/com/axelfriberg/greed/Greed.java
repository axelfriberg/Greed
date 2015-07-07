package com.axelfriberg.greed;

import java.util.Arrays;
import java.util.Random;

/**
 * Simulates a game of Greed.
 *
 * Created by Axel Friberg on 2015-07-07.
 */
public class Greed {
    private int[] dice;
    private boolean[] saved;
    private int max;
    private int min;
    private Random rand;
    private int totalScore;
    private int roundScore;
    private int round;
    private int toss;
    public static final int WIN_LIMIT= 5000;
    public static final int FIRST_THROW_LIMIT = 200;

    public Greed(){
        rand = new Random();
        max = 6;
        min = 1;
        dice = new int[6];
        totalScore = 0;
        roundScore = 0;
        round = 1;
        toss = 0;
        saved = new boolean[6];
        for (int i = 0; i < 6; i++) {
            dice[i] = i+1;
        }
    }

    /**
     * Used to simulate a new throw by the user.
     * Each die that has not been previously used for points will be thrown.
     */
    public void newThrow(){
        for (int i = 0; i < 6; i++) {
            if(!saved[i]){
                dice[i] = randInterval(rand, max, min);
            }
        }
        toss++;
    }

    /**
     * Calculates the score for a given set of dice, selected by the player.
     * It checks for a ladder, triplets and dice one and five.
     * @param selected Which of the six dice that should be used for calculation of the score for this throw.
     * @return The score accumulated this throw.
     */

    public int score(boolean[] selected){
        int score = 0;
        int[] chosenDice = new int[6];

        for (int i = 0; i < 6; i++){
            if(selected[i] && !saved[i]){
                chosenDice[i] = dice[i];
            }
        }

        //Check if the player has gotten a ladder
        int[] sorted = new int[6];
        System.arraycopy(chosenDice,0,sorted,0,chosenDice.length);
        Arrays.sort(sorted);

        boolean ladder = true;
        for (int i = 0; i < 6; i++) {
            if(sorted[i] != i+1){
                ladder = false;
            }
        }

        if(ladder){
            Arrays.fill(saved, true);
            score = 1000;
            roundScore += score;
            return score;
        }

        //Check if the player has gotten three of a kind
        for (int i = 0; i < 4; i++) {
            boolean tok = false;
            int index1 = -1;
            int index2 = -1;
            for (int j = i+1; j < chosenDice.length; j++) {
                if(chosenDice[j] == chosenDice[i] && chosenDice[j] != 0) {
                    if(index1 == -1){
                        index1 = j;
                    } else {
                        index2 = j;
                        tok = true;
                        break;
                    }
                }
            }

            if(tok){
                if (chosenDice[i] == 1) {
                    score += 1000;
                } else{
                    score += 100 * chosenDice[i];
                }

                //Set to 0 so the dice are not used twice
                chosenDice[i] = 0;
                saved[i] = true;
                chosenDice[index1] = 0;
                saved[index1] = true;
                chosenDice[index2] = 0;
                saved[index2] = true;
            }
        }

        //Check if the player has gotten singles of dice one and/or five.
        for(int i = 0; i < chosenDice.length; i++){
            if(chosenDice[i] == 1){
                score += 100;
                chosenDice[i] = 0;
                saved[i] = true;
            } else if(chosenDice[i] == 5){
                chosenDice[i] = 0;
                score += 50;
                saved[i] = true;
            }
        }

        if(score == 0 || (score < FIRST_THROW_LIMIT && toss == 1)){
            roundScore = 0;
        } else {
            roundScore += score;
        }

        if(roundScore+totalScore >=WIN_LIMIT){
            totalScore += roundScore;
        }
        return score;
    }

    /**
     * Sets up a new round for the player.
     */
    public void newRound(){
        totalScore += roundScore;
        round++;
        toss = 0;
        roundScore = 0;
        Arrays.fill(saved,false);
    }

    /**
     * Returns the total score the player currently has.
     * @return The total score.
     */
    public int getTotalScore(){
        return totalScore;
    }

    /**
     * Returns an array of what values the dice currently has.
     * @return The values of the dice.
     */
    public int[] getDice(){
        return dice;
    }

    /**
     * Returns the score the player has accumulated this round.
     * @return The accumulated score this round.
     */
    public int getRoundScore(){
        return roundScore;
    }

    /**
     * Returns which dice are currently saved and has been used for points for a throw.
     * @return Which dice are currently saved.
     */
    public boolean[] getSaved(){
        return saved;
    }

    /**
     * Returns which round the game is at.
     * @return The current round.
     */
    public int getRound(){
        return round;
    }

    /**
     * Returns which throw the player is at for a given round.
     * @return The current throw.
     */
    public int getToss(){
        return toss;
    }

    /**
     * Used when all dice have been saved and need to be unsaved.
     */
    public void allSaved(){
        Arrays.fill(saved,false);
    }

    private int randInterval(Random r, int max, int min){
         return r.nextInt(max - min + 1) + min;
    }
}
