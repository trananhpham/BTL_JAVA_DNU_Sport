package com.clup.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import com.clup.exceptions.*;
import com.clup.model.*;
import com.clup.repo.*;

public class SchedulingService {

    private final ScheduleRepository scheduleRepo;

    public SchedulingService(ScheduleRepository scheduleRepo) {
        this.scheduleRepo = scheduleRepo;
    }

    public Schedule createSchedule(Schedule s) {
        List<Schedule> conflicts = findConflicts(s);
        if (!conflicts.isEmpty()) {
            throw new ScheduleConflictException("Lịch bị trùng với: " + conflicts.stream().map(Schedule::getId).collect(Collectors.joining(",")));
        }
        scheduleRepo.add(s);
        return s;
    }

    public List<Schedule> findConflicts(Schedule candidate) {
        List<Schedule> conflicts = new ArrayList<>();
        for (Schedule s : scheduleRepo.all()) {
            if (s.getStatus() == ScheduleStatus.CANCELLED) {
                continue;
            }
            if (Objects.equals(s.getCoachId(), candidate.getCoachId()) || shareMembers(s, candidate)) {
                if (overlap(s, candidate)) {
                    conflicts.add(s);
                }
            }
        }
        return conflicts;
    }

    private boolean shareMembers(Schedule a, Schedule b) {
        Set<String> aSet = new HashSet<>(Arrays.asList(a.getMemberIds().split(";")));
        Set<String> bSet = new HashSet<>(Arrays.asList(b.getMemberIds().split(";")));
        aSet.retainAll(bSet);
        return !aSet.isEmpty();
    }

    private boolean overlap(Schedule a, Schedule b) {
        LocalDateTime a1 = a.start(), a2 = a.end(), b1 = b.start(), b2 = b.end();
        return !a2.isBefore(b1) && !b2.isBefore(a1);
    }
}
