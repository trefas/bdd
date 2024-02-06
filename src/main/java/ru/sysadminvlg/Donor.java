// * Copyright (c) 2024. trefas@yandex.ru

package ru.sysadminvlg;

import java.time.LocalDate;

public class Donor {
    private Integer id;
    private String surname;
    private String name;

    public void setBllast(LocalDate bllast) {
        this.bllast = bllast;
    }

    public void setBlcount(Integer blcount) {
        this.blcount = blcount;
    }

    private String patronim;
    private LocalDate bday;
    private Bloodgroup bgroup;
    private String phone;
    private String work;
    private Address addr;
    private Document doc;

    private Integer blcount;

    private LocalDate bllast;
    public Donor(Integer id, String surname, String name, String patronim, LocalDate bday, Bloodgroup bgroup, String phone, String work, Address addr, Document doc, Integer blcount, LocalDate bllast) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.patronim = patronim;
        this.bday = bday;
        this.bgroup = bgroup;
        this.phone = phone;
        this.work = work;
        this.addr = addr;
        this.doc = doc;
        this.blcount = blcount;
        this.bllast = bllast;
    }
    public Integer getId() {
        return id;
    }
    public String getSurname() {
        return surname;
    }
    public String getName() {
        return name;
    }
    public String getPatronim() {
        return patronim;
    }
    public LocalDate getBday() {
        return bday;
    }
    public Bloodgroup getBgroup() {
        return bgroup;
    }
    public String getPhone() {
        return phone;
    }
    public String getWork() {
        return work;
    }
    public Address getAddr() {
        return addr;
    }
    public Document getDoc() {
        return doc;
    }
    public String getFullName(){
        return surname+" "+name+" "+patronim;
    }
    public String getBloodGroupe(){
        return bgroup.getFullText();
    }
    public Integer getBlcount() { return blcount;  }
    public LocalDate getBllast() { return bllast;  }
}
