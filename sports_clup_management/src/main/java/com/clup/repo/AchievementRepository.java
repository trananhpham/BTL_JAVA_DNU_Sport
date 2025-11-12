package com.clup.repo;

import com.clup.model.Achievement;
import java.util.*;
import java.time.*;


public class AchievementRepository extends BaseCsvRepository<Achievement> {

    private List<Achievement> list = new ArrayList<>();

    public AchievementRepository(String path) {
        super(path);
    }

    public List<Achievement> all() {
        return list;
    }

    public void add(Achievement a) {
        list.add(a);
    }

    public List<String[]> serializeAll() {
        List<String[]> rows = new ArrayList<>();
        for (Achievement a : list) {
            rows.add(new String[]{a.getId(), a.getMemberId(), a.getTitle(),
                Integer.toString(a.getPoints()), a.getCompetitionId() == null ? "" : a.getCompetitionId(),
                a.getDate().toString()});
        }
        return rows;
    }

    public void deserializeAll(List<String[]> rows) {
        list.clear();
        for (String[] r : rows) {
            if (r.length < 6) {
                continue;
            }
            list.add(new Achievement(r[0], r[1], r[2], Integer.parseInt(r[3]),
                    r[4].isEmpty() ? null : r[4], LocalDate.parse(r[5])));
        }
    }
}
