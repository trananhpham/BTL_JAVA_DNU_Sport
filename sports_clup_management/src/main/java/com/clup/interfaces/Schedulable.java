package com.clup.interfaces;

public interface Schedulable {
    Schedule schedule(Schedule s);
    List<Schedule> findConflicts(Schedule candidate);
}
