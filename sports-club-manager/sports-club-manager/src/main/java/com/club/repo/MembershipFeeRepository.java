
package com.club.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.club.model.MembershipFee;

public class MembershipFeeRepository extends BaseCsvRepository<MembershipFee> {
    private Map<String, MembershipFee> map = new LinkedHashMap<>();
    
    public MembershipFeeRepository(String filePath){
        super(filePath);
    }

    public Collection<MembershipFee> all(){ return map.values(); }
    public MembershipFee get(String id){ return map.get(id); }
    public void add(MembershipFee fee){ map.put(fee.getId(), fee); }
    public void remove(String id){ map.remove(id); }

    @Override
    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (MembershipFee fee : map.values()){
            String checkOut = (fee.getExpiryDate() != null) ? fee.getExpiryDate().toString() : "";
            rows.add(new String[]{fee.getId(), fee.getMemberId(), String.valueOf(fee.getAmount()), 
                                 fee.getPaymentDate().toString(), checkOut, fee.getPaymentMethod(), 
                                 fee.getStatus().name()});
        }
        return rows;
    }

    @Override
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 7) continue;
            MembershipFee fee = MembershipFee.fromCsv(String.join(",", r));
            if (fee != null) add(fee);
        }
    }

    public List<MembershipFee> getByMember(String memberId){
        List<MembershipFee> result = new ArrayList<>();
        for (MembershipFee fee : all()){
            if (fee.getMemberId().equals(memberId)){
                result.add(fee);
            }
        }
        return result;
    }

    public MembershipFee getLatestByMember(String memberId){
        return getByMember(memberId).stream()
                .max(Comparator.comparing(MembershipFee::getPaymentDate))
                .orElse(null);
    }
}

