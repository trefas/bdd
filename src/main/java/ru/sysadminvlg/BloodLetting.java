// * Copyright (c) 2024. trefas@yandex.ru

package ru.sysadminvlg;

import java.time.LocalDate;

public class BloodLetting {
    public BloodLetting(Integer id, LocalDate date, String mark, String reason) {
        this.id = id;
        this.date = date;
        this.mark = mark;
        this.reason = reason;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getMark() {
        return mark;
    }

    @Override
    public String toString() {
        return "BloodLetting{" +
                "id=" + id +
                ", date=" + date +
                ", mark='" + mark + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer id;
    public LocalDate date;
    public String mark;
    public String reason;

}