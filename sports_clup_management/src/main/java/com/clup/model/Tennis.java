package com.clup.model;

public class Tennis extends Sport {

    public Tennis(String id, String name, String desc, String coachId) {
        super(id, name, desc, coachId);
    }

    public String getTrainingSchedule() {
        return "Tennis: T4/T7 17:00";
    }
}
