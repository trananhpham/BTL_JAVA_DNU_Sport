package com.clup.model;

public class Member extends Person {
    public Member(String id, String name, String phone) {
        super(id, name, phone);
    }
    @Override
    public String toString(){
        return "Member{" + id + ", " + name + ", " + phone + "}";
    }
}