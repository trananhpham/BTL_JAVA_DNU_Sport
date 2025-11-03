package com.clup.model;

import java.util.*;

public class Coach extends Person {
    private Set<String> sportIds = new HashSet<>(); // Chuyên môn
    public Coach(String id, String name, String phone) {
        super(id, name, phone);
    }
    public Set<String> getSportIds(){ return sportIds; }
    public void addSport(String sportId){ sportIds.add(sportId); }
    public void removeSport(String sportId){ sportIds.remove(sportId); }
    @Override
    public String toString(){
        return "Coach{" + id + ", " + name + ", sports=" + sportIds + "}";
    }
}