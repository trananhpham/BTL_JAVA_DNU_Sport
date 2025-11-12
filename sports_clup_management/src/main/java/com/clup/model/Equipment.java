package com.clup.model;

import java.time.LocalDate;
import java.util.UUID;

public class Equipment {

    private String id;
    private String name;
    private String category;
    private int quantity;
    private EquipmentStatus status;
    private LocalDate purchaseDate;
    private LocalDate lastMaintenanceDate;
    private String location;
    private double price;

    public Equipment(String id, String name, String category, int quantity, EquipmentStatus status,
            LocalDate purchaseDate, LocalDate lastMaintenanceDate, String location, double price) {
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.status = status;
        this.purchaseDate = purchaseDate;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.location = location;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public EquipmentStatus getStatus() {
        return status;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public LocalDate getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public String getLocation() {
        return location;
    }

    public double getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(EquipmentStatus status) {
        this.status = status;
    }

    public void setLastMaintenanceDate(LocalDate date) {
        this.lastMaintenanceDate = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean needsMaintenance(int monthsThreshold) {
        if (lastMaintenanceDate == null) {
            return true;
        }
        return LocalDate.now().minusMonths(monthsThreshold).isAfter(lastMaintenanceDate);
    }

    @Override
    public String toString() {
        return "Equipment{id=" + id + ", name=" + name + ", category=" + category
                + ", qty=" + quantity + ", status=" + status + ", location=" + location
                + ", price=" + price + ", purchased=" + purchaseDate + "}";
    }

    public String toCsv() {
        String lastMaint = (lastMaintenanceDate != null) ? lastMaintenanceDate.toString() : "";
        return String.join(",", id, name, category, String.valueOf(quantity), status.name(),
                purchaseDate.toString(), lastMaint, location, String.valueOf(price));
    }

    public static Equipment fromCsv(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 9) {
            return null;
        }
        LocalDate lastMaint = p[6].isBlank() ? null : LocalDate.parse(p[6]);
        return new Equipment(p[0], p[1], p[2], Integer.parseInt(p[3]), EquipmentStatus.valueOf(p[4]),
                LocalDate.parse(p[5]), lastMaint, p[7], Double.parseDouble(p[8]));
    }
}
