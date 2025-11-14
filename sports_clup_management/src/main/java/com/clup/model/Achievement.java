package com.clup.model;

import java.time.LocalDate;

public class Achievement {
    private String id;
    private String memberID;
    private String title;
    private int points;
    private String competitionID;
    private LocalDate date;

    // Constructor khớp với cách dùng trong Repository và DataBootstrap:
    // (id, memberId, title, points, competitionId, date)
    public Achievement(String id, String memberId, String title, int points, String competitionId, LocalDate date) {
        this.id = (id == null || id.isBlank()) ? java.util.UUID.randomUUID().toString() : id;
        this.memberID = memberId;
        this.title = title;
        this.points = points;
        this.competitionID = competitionId;
        this.date = date;
    }

    

    public String getId() {
        return id;
    }

    // Giữ cả 2 getter để tương thích tên phương thức
    public String getMemberID() { return memberID; }
    public String getMemberId() { return memberID; }

    public String getTitle() {
        return title;
    }

    public int getPoints() {
        return points;
    }

    public String getCompetitionID() { return competitionID; }
    public String getCompetitionId() { return competitionID; }

    public LocalDate getDate() {
        return date;
    }


}
