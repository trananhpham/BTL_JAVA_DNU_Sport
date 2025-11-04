
package com.club.model;

import java.time.LocalDate;
import java.util.UUID;

public class MembershipFee {
    private String id;
    private String memberId;
    private double amount;
    private LocalDate paymentDate;
    private LocalDate expiryDate;
    private String paymentMethod;
    private FeeStatus status;

    public MembershipFee(String id, String memberId, double amount, LocalDate paymentDate, 
                         LocalDate expiryDate, String paymentMethod, FeeStatus status){
        this.id = (id == null || id.isBlank()) ? UUID.randomUUID().toString() : id;
        this.memberId = memberId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.expiryDate = expiryDate;
        this.paymentMethod = paymentMethod;
        this.status = status;
    }

    public String getId() { return id; }
    public String getMemberId() { return memberId; }
    public double getAmount() { return amount; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public FeeStatus getStatus() { return status; }

    public void setStatus(FeeStatus status) { this.status = status; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isExpiringSoon(int days) {
        return !isExpired() && LocalDate.now().plusDays(days).isAfter(expiryDate);
    }

    @Override
    public String toString(){
        return "MembershipFee{id=" + id + ", memberId=" + memberId + ", amount=" + amount + 
               ", paymentDate=" + paymentDate + ", expiryDate=" + expiryDate + 
               ", method=" + paymentMethod + ", status=" + status + "}";
    }

    public String toCsv(){
        return String.join(",", id, memberId, String.valueOf(amount), paymentDate.toString(), 
                          expiryDate.toString(), paymentMethod, status.name());
    }

    public static MembershipFee fromCsv(String line){
        String[] p = line.split(",");
        if (p.length < 7) return null;
        return new MembershipFee(p[0], p[1], Double.parseDouble(p[2]), LocalDate.parse(p[3]), 
                                LocalDate.parse(p[4]), p[5], FeeStatus.valueOf(p[6]));
    }
}

