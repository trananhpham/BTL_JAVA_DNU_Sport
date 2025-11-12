package com.clup.repo;

import java.util.*;
import java.time.*;

import com.clup.model.*;

public class ScheduleRepository extends BaseCsvRepository<Schedule> {
    private Map<String, Schedule> map = new LinkedHashMap<>();
    public ScheduleRepository(String path){ super(path); }
    public Collection<Schedule> all(){ return map.values(); }
    public Schedule get(String id){ return map.get(id); }
    public void add(Schedule s){ map.put(s.getId(), s); }
    public void remove(String id){ map.remove(id); }

    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Schedule s : map.values()){
            rows.add(new String[]{s.getId(), s.getSportId(), s.getCoachId(), s.getMemberIds(),
                s.getDate().toString(), s.getTime().toString(),
                Integer.toString(s.getDurationMinutes()), s.getStatus().name()});
        }
        return rows;
    }
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 8) continue;
            Schedule s = new Schedule(r[0], r[1], r[2], r[3],
                LocalDate.parse(r[4]), LocalTime.parse(r[5]), Integer.parseInt(r[6]),
                ScheduleStatus.valueOf(r[7]));
            add(s);
        }
    }
}

