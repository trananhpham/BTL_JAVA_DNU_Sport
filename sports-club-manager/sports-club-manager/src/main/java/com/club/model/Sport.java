
package com.club.model;

import java.util.UUID;

public abstract class Sport {
    protected String id;
    protected String name;
    protected String description;
    protected String coachId;

    public Sport(String id, String name, String description, String coachId) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.description = description;
        this.coachId = coachId;
    }
    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getDescription(){ return description; }
    public String getCoachId(){ return coachId; }
    public void setCoachId(String coachId){ this.coachId = coachId; }

    public abstract String getTrainingSchedule(); // demo text
}
