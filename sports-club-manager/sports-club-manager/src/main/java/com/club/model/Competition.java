
package com.club.model;

import java.time.LocalDate;
import java.util.UUID;

public abstract class Competition {
    protected String id;
    protected String name;
    protected LocalDate date;

    public Competition(String id, String name, LocalDate date) {
        this.id = id == null ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.date = date;
    }
    public String getId(){ return id; }
    public String getName(){ return name; }
    public LocalDate getDate(){ return date; }
    public abstract String getType();
}
