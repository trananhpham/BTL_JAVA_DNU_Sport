package com.clup.repo;

import java.util.*;
import com.clup.model.*;

public class ResultRepository extends BaseCsvRepository<Result> {
    private List<Result> list = new ArrayList<>();
    public ResultRepository(String path){ super(path); }
    public List<Result> all(){ return list; }
    public void add(Result r){ list.add(r); }

    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Result r : list){
            rows.add(new String[]{r.getCompetitionId(), r.getMemberId(),
                Integer.toString(r.getScore()), Integer.toString(r.getRank())});
        }
        return rows;
    }
    public void deserializeAll(List<String[]> rows){
        list.clear();
        for (String[] r : rows){
            if (r.length < 4) continue;
            list.add(new Result(r[0], r[1], Integer.parseInt(r[2]), Integer.parseInt(r[3])));
        }
    }
}
