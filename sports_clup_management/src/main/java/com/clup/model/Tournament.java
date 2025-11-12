package com.clup.model;

import java.time.LocalDate;

public class Tournament extends Competition {

    public Tournament(String id, String name, LocalDate date) {
        super(id, name, date);
    }

    public String getType() {
        return "TOURNAMENT";
    }
}
