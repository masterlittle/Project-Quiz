package com.project.quiz.customclasses;

/**
 * Created by Shitij on 23/09/15.
 */
public class CardScore {
    private int currentScore;
    private int teamNum;
    private int increaseScore;

    public int getCurrentScore(){
        return currentScore;
    }

    public int getTeamNum(){
        return  teamNum;
    }

    public int getIncreaseScore() {
        return increaseScore;
    }

    public void setCurrentScore(int currentScore) {
        this.currentScore = currentScore;
    }

    public void setIncreaseScore(int increaseScore) {
        this.increaseScore = increaseScore;
    }

    public void setTeamNum(int teamNum) {
        this.teamNum = teamNum;
    }
}
