package com.clup.repo;

import com.clup.model.*;
import java.util.*;
import java.time.*;

public class CompetitionRepository extends BaseCsvRepository<Competition> {
    private Map<String, Competition> map = new LinkedHashMap<>();
    public CompetitionRepository(String path){ super(path); }
    public Collection<Competition> all(){ return map.values(); }
    public Competition get(String id){ return map.get(id); }
    public void add(Competition c){ map.put(c.getId(), c); }
    public void remove(String id){ map.remove(id); }

    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Competition c : map.values()){
            rows.add(new String[]{c.getId(), c.getName(), c.getDate().toString(),
                (c instanceof League) ? "LEAGUE" : "TOURNAMENT"});
        }
        return rows;
    }
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 4) continue;
            String id=r[0], name=r[1]; LocalDate d=LocalDate.parse(r[2]); String type=r[3];
            Competition c = "LEAGUE".equals(type) ? new League(id, name, d) : new Tournament(id, name, d);
            add(c);
        }
    }
}

