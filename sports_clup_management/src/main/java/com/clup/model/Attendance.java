package com.clup.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

public class Attendance {

    private String id;
    private String scheduleId;
    private String memberId;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private AttendanceStatus status;
    private String note;

    public Attendance(String id, String scheduleId, String memberId, LocalDate date,
            LocalTime checkInTime, LocalTime checkOutTime, AttendanceStatus status, String note) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
        this.note = note == null ? "" : note;
    }

    public String getId() {
        return id;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public AttendanceStatus getStatus() {
        return status;
    }

    public String getNote() {
        return note;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public void setStatus(AttendanceStatus status) {
        this.status = status;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Attendance{id=" + id + ", scheduleId=" + scheduleId + ", memberId=" + memberId
                + ", date=" + date + ", checkIn=" + checkInTime + ", checkOut=" + checkOutTime
                + ", status=" + status + ", note=" + note + "}";
    }

    public String toCsv() {
        String checkOut = (checkOutTime != null) ? checkOutTime.toString() : "";
        return String.join(",", id, scheduleId, memberId, date.toString(),
                checkInTime.toString(), checkOut, status.name(), note.replace(",", ";"));
    }

    public static Attendance fromCsv(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 8) {
            return null;
        }
        LocalTime checkOut = p[5].isBlank() ? null : LocalTime.parse(p[5]);
        return new Attendance(p[0], p[1], p[2], LocalDate.parse(p[3]),
                LocalTime.parse(p[4]), checkOut, AttendanceStatus.valueOf(p[6]), p[7]);
    }
}
