package com.clup.model;

import java.time.LocalDate;

public class Achievement {
    private String id;
    private String memberID;
    private String title;
    private int points;
    private String competitionID;
    private LocalDate date;

    public Achievement(String competitionID, LocalDate date, String id, String memberID, int points, String title) {
        this.competitionID = competitionID;
        this.date = date;
        this.id = id == null ? java.util.UUID.randomUUID().toString() : id;
        this.memberID = memberID;
        this.points = points;
        this.title = title;
    }

    

    public String getId() {
        return id;
    }

    public String getMemberID() {
        return memberID;
    }

    public String getTitle() {
        return title;
    }

    public int getPoints() {
        return points;
    }

    public String getCompetitionID() {
        return competitionID;
    }

    public LocalDate getDate() {
        return date;
    }


}
