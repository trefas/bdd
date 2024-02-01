// * Copyright (c) 2024. trefas@yandex.ru

package ru.sysadminvlg;

import java.time.LocalDate;

public class Donor {
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

    public Donor(Integer id, String surname, String name, String patronim, LocalDate bday, Bloodgroup bgroup, String phone, String work, Address addr, Document doc) {
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
    }

    public Integer id;
    public String surname;
    public String name;
    public String patronim;
    public LocalDate bday;
    public Bloodgroup bgroup;
    public String phone;
    public String work;
    public Address addr;
    public Document doc;
}
