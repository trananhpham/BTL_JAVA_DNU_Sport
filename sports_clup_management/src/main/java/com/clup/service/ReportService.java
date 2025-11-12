package com.clup.service;
import com.clup.repo.*;
import com.clup.model.*;
import java.util.*;
import java.util.stream.Collectors;

public class ReportService {
    private final MemberRepository memberRepo;
    private final CoachRepository coachRepo;
    private final SportRepository sportRepo;
    private final ScheduleRepository scheduleRepo;
    private final CompetitionRepository competitionRepo;
    private final ResultRepository resultRepo;
    private final AchievementRepository achievementRepo;

    public ReportService(MemberRepository memberRepo, CoachRepository coachRepo, SportRepository sportRepo,
                         ScheduleRepository scheduleRepo, CompetitionRepository competitionRepo,
                         ResultRepository resultRepo, AchievementRepository achievementRepo){
        this.memberRepo = memberRepo; this.coachRepo = coachRepo; this.sportRepo = sportRepo;
        this.scheduleRepo = scheduleRepo; this.competitionRepo = competitionRepo; this.resultRepo = resultRepo;
        this.achievementRepo = achievementRepo;
    }

    // Thống kê số thành viên tham gia từng môn (từ lịch tập)
    public Map<String, Long> membersPerSport(){
        Map<String, Set<String>> map = new LinkedHashMap<>();
        for (Schedule s : scheduleRepo.all()){
            map.putIfAbsent(s.getSportId(), new LinkedHashSet<>());
            for (String mId : s.getMemberIds().split(";")){
                if (!mId.isBlank()) map.get(s.getSportId()).add(mId);
            }
        }
        Map<String, Long> res = new LinkedHashMap<>();
        for (Map.Entry<String, Set<String>> e : map.entrySet()){
            String sportName = Optional.ofNullable(sportRepo.get(e.getKey())).map(Sport::getName).orElse(e.getKey());
            res.put(sportName, (long)e.getValue().size());
        }
        return res;
    }

    // Kết quả & xếp hạng giải đấu gần nhất
    public List<Result> latestCompetitionRanking(){
        Competition latest = competitionRepo.all().stream().max(Comparator.comparing(Competition::getDate)).orElse(null);
        if (latest == null) return List.of();
        String cid = latest.getId();
        return resultRepo.all().stream().filter(r -> r.getCompetitionId().equals(cid))
                .sorted(Comparator.comparingInt(Result::getRank))
                .collect(Collectors.toList());
    }

    // Top 5 thành viên theo tổng điểm thành tích
    public List<Map.Entry<String,Integer>> top5MembersAchievements(){
        Map<String, Integer> points = new HashMap<>();
        for (Achievement a : achievementRepo.all()){
            points.merge(a.getMemberId(), a.getPoints(), Integer::sum);
        }
        return points.entrySet().stream()
                .sorted((a,b)->Integer.compare(b.getValue(), a.getValue()))
                .limit(5)
                .collect(Collectors.toList());
    }
}
