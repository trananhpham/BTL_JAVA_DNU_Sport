package com.clup.repo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.clup.model.*;


public class AttendanceRepository extends BaseCsvRepository<Attendance> {
    private Map<String, Attendance> map = new LinkedHashMap<>();
    
    public AttendanceRepository(String filePath){
        super(filePath);
    }

    public Collection<Attendance> all(){ return map.values(); }
    public Attendance get(String id){ return map.get(id); }
    public void add(Attendance att){ map.put(att.getId(), att); }
    public void remove(String id){ map.remove(id); }

    @Override
    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Attendance att : map.values()){
            String checkOut = (att.getCheckOutTime() != null) ? att.getCheckOutTime().toString() : "";
            rows.add(new String[]{att.getId(), att.getScheduleId(), att.getMemberId(), 
                                 att.getDate().toString(), att.getCheckInTime().toString(), 
                                 checkOut, att.getStatus().name(), att.getNote()});
        }
        return rows;
    }

    @Override
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 8) continue;
            Attendance att = Attendance.fromCsv(String.join(",", r));
            if (att != null) add(att);
        }
    }

    public List<Attendance> getByMember(String memberId){
        return all().stream()
                .filter(a -> a.getMemberId().equals(memberId))
                .collect(Collectors.toList());
    }

    public List<Attendance> getBySchedule(String scheduleId){
        return all().stream()
                .filter(a -> a.getScheduleId().equals(scheduleId))
                .collect(Collectors.toList());
    }

    public List<Attendance> getByDateRange(LocalDate from, LocalDate to){
        return all().stream()
                .filter(a -> !a.getDate().isBefore(from) && !a.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    public double getAttendanceRate(String memberId){
        List<Attendance> records = getByMember(memberId);
        if (records.isEmpty()) return 0.0;
        long present = records.stream()
                .filter(a -> a.getStatus() == AttendanceStatus.PRESENT || a.getStatus() == AttendanceStatus.LATE)
                .count();
        return (double) present / records.size() * 100;
    }
}
