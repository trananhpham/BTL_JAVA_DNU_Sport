
package com.club.repo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.club.model.Equipment;
import com.club.model.EquipmentStatus;

public class EquipmentRepository extends BaseCsvRepository<Equipment> {
    private Map<String, Equipment> map = new LinkedHashMap<>();
    
    public EquipmentRepository(String filePath){
        super(filePath);
    }

    public Collection<Equipment> all(){ return map.values(); }
    public Equipment get(String id){ return map.get(id); }
    public void add(Equipment eq){ map.put(eq.getId(), eq); }
    public void remove(String id){ map.remove(id); }

    @Override
    public List<String[]> serializeAll(){
        List<String[]> rows = new ArrayList<>();
        for (Equipment eq : map.values()){
            String lastMaint = (eq.getLastMaintenanceDate() != null) ? eq.getLastMaintenanceDate().toString() : "";
            rows.add(new String[]{eq.getId(), eq.getName(), eq.getCategory(), 
                                 String.valueOf(eq.getQuantity()), eq.getStatus().name(), 
                                 eq.getPurchaseDate().toString(), lastMaint, eq.getLocation(), 
                                 String.valueOf(eq.getPrice())});
        }
        return rows;
    }

    @Override
    public void deserializeAll(List<String[]> rows){
        map.clear();
        for (String[] r : rows){
            if (r.length < 9) continue;
            Equipment eq = Equipment.fromCsv(String.join(",", r));
            if (eq != null) add(eq);
        }
    }

    public List<Equipment> getByCategory(String category){
        return all().stream()
                .filter(new java.util.function.Predicate<Equipment>() {
                    @Override
                    public boolean test(Equipment e) {
                        return e.getCategory().equalsIgnoreCase(category);
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Equipment> getByStatus(EquipmentStatus status){
        return all().stream()
                .filter(new java.util.function.Predicate<Equipment>() {
                    @Override
                    public boolean test(Equipment e) {
                        return e.getStatus() == status;
                    }
                })
                .collect(Collectors.toList());
    }

    public List<Equipment> getNeedingMaintenance(int monthsThreshold){
        return all().stream()
                .filter(new java.util.function.Predicate<Equipment>() {
                    @Override
                    public boolean test(Equipment e) {
                        return e.needsMaintenance(monthsThreshold);
                    }
                })
                .collect(Collectors.toList());
    }

    public int getTotalQuantity(){
        return all().stream().mapToInt(Equipment::getQuantity).sum();
    }
}

