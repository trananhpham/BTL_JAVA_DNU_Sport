package com.clup.repo;

import java.util.*;
import com.clup.model.*;
public class MemberRepository extends BaseCsvRepository<Member> {
    private Map<String, Member> map = new LinkedHashMap<>();
    public MemberRepository(String path){ super(path); }
    public Collection<Member> all(){ return map.values(); }
    public Member get(String id){ return map.get(id); }
    public void add(Member m){ map.put(m.getId(), m); }
    public void remove(String id){ map.remove(id); }

    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Member m : map.values()){
            rows.add(new String[]{m.getId(), m.getName(), m.getPhone()});
        }
        return rows;
    }
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 3) continue;
            add(new Member(r[0], r[1], r[2]));
        }
    }
}