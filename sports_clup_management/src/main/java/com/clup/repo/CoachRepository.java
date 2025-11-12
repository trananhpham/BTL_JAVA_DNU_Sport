package com.clup.repo;

import com.clup.model.*;
import java.util.*;
public class CoachRepository extends BaseCsvRepository<Coach> {
    private Map<String, Coach> map = new LinkedHashMap<>();
    public CoachRepository(String path){ super(path); }
    public Collection<Coach> all(){ return map.values(); }
    public Coach get(String id){ return map.get(id); }
    public void add(Coach c){ map.put(c.getId(), c); }
    public void remove(String id){ map.remove(id); }

    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Coach c : map.values()){
            rows.add(new String[]{c.getId(), c.getName(), c.getPhone(),
                String.join(";", c.getSportIds())});
        }
        return rows;
    }
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 4) continue;
            Coach c = new Coach(r[0], r[1], r[2]);
            if (!r[3].isEmpty()){
                for (String s : r[3].split(";")) c.addSport(s);
            }
            add(c);
        }
    }
}
