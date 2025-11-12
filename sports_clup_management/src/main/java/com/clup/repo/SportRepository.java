package com.clup.repo;

import com.clup.model.*;
import java.util.*;

public class SportRepository extends BaseCsvRepository<Sport> {
    private Map<String, Sport> map = new LinkedHashMap<>();
    public SportRepository(String path){ super(path); }
    public Collection<Sport> all(){ return map.values(); }
    public Sport get(String id){ return map.get(id); }
    public void add(Sport s){ map.put(s.getId(), s); }
    public void remove(String id){ map.remove(id); }

    private String typeOf(Sport s){
        if (s instanceof Football) return "Football";
        if (s instanceof Basketball) return "Basketball";
        if (s instanceof Tennis) return "Tennis";
        if (s instanceof Badminton) return "Badminton";
        return "Sport";
    }

    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Sport s : map.values()){
            rows.add(new String[]{s.getId(), typeOf(s), s.getName(), s.getDescription(), s.getCoachId()==null?"":s.getCoachId()});
        }
        return rows;
    }
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 5) continue;
            String id=r[0], type=r[1], name=r[2], desc=r[3], coachId=r[4].isEmpty()?null:r[4];
            Sport s;
            switch (type){
                case "Football" -> s = new Football(id, name, desc, coachId);
                case "Basketball" -> s = new Basketball(id, name, desc, coachId);
                case "Tennis" -> s = new Tennis(id, name, desc, coachId);
                case "Badminton" -> s = new Badminton(id, name, desc, coachId);
                default -> s = new Football(id, name, desc, coachId);
            }
            add(s);
        }
    }
}