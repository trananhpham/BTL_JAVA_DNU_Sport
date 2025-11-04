
package com.club.model;

import java.time.*;

public class Schedule {
    private String id;
    private String sportId;
    private String coachId;
    private String memberIds; // join by ;
    private LocalDate date;
    private LocalTime time;
    private int durationMinutes;
    private ScheduleStatus status;

    public Schedule(String id, String sportId, String coachId, String memberIds,
                    LocalDate date, LocalTime time, int durationMinutes, ScheduleStatus status) {
        this.id = id == null ? java.util.UUID.randomUUID().toString() : id;
        this.sportId = sportId;
        this.coachId = coachId;
        this.memberIds = memberIds;
        this.date = date;
        this.time = time;
        this.durationMinutes = durationMinutes;
        this.status = status == null ? ScheduleStatus.SCHEDULED : status;
    }
    public String getId(){ return id; }
    public String getSportId(){ return sportId; }
    public String getCoachId(){ return coachId; }
    public String getMemberIds(){ return memberIds; }
    public LocalDate getDate(){ return date; }
    public LocalTime getTime(){ return time; }
    public int getDurationMinutes(){ return durationMinutes; }
    public ScheduleStatus getStatus(){ return status; }
    public void setStatus(ScheduleStatus s){ this.status = s; }

    public LocalDateTime start(){ return LocalDateTime.of(date, time); }
    public LocalDateTime end(){ return start().plusMinutes(durationMinutes); }

    @Override
    public String toString(){
        return String.format("Schedule{%s sport=%s coach=%s date=%s %s %d' status=%s}",
            id, sportId, coachId, date, time, durationMinutes, status);
    }
}
