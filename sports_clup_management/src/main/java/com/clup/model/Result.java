package com.clup.model;

public class Result {

    private String competitionId;
    private String memberId;
    private int score;
    private int rank;

    public Result(String competitionId, String memberId, int score, int rank) {
        this.competitionId = competitionId;
        this.memberId = memberId;
        this.score = score;
        this.rank = rank;
    }

    public String getCompetitionId() {
        return competitionId;
    }

    public String getMemberId() {
        return memberId;
    }

    public int getScore() {
        return score;
    }

    public int getRank() {
        return rank;
    }
}
