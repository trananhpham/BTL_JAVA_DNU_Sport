package com.clup.model;

public class Badminton extends Sport {
    public Badminton(String id, String name, String desc, String coachId){
        super(id,name,desc,coachId);
    }
    public String getTrainingSchedule(){
        return "Cầu lông: CN 08:00"; }
}
