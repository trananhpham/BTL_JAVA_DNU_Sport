
package com.club.model;

import java.time.LocalDate;

public class Achievement {
    private String id;
    private String memberId;
    private String title;
    private int points;
    private String competitionId; // optional
    private LocalDate date;

    public Achievement(String id, String memberId, String title, int points, String competitionId, LocalDate date){
        this.id = id == null ? java.util.UUID.randomUUID().toString() : id;
        this.memberId = memberId; this.title = title; this.points = points; this.competitionId = competitionId; this.date = date;
    }
    public String getId(){ return id; }
    public String getMemberId(){ return memberId; }
    public String getTitle(){ return title; }
    public int getPoints(){ return points; }
    public String getCompetitionId(){ return competitionId; }
    public LocalDate getDate(){ return date; }
}
