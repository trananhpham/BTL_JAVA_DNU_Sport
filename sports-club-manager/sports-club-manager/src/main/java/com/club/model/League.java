package com.club.model; import java.time.LocalDate;
public class League extends Competition {
    public League(String id, String name, LocalDate date){ super(id,name,date); }
    public String getType(){ return "LEAGUE"; }
}