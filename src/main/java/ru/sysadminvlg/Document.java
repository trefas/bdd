// * Copyright (c) 2023. trefas@yandex.ru
package ru.sysadminvlg;

import java.sql.Date;
import java.time.LocalDate;

public class Document {
    public TypeDoc name;
    public String serial;
    public Integer number;
    public String issued;
    public LocalDate released;

    public Document(TypeDoc name, String serial, Integer number, String issued, LocalDate released) {
        this.name = name;
        this.serial = serial;
        this.number = number;
        this.issued = issued;
        this.released = released;
    }


    @Override
    public String toString() {
        String text;
        text = (name==null)? "":name + ": ";
        text += (serial=="")? "":serial +" ";
        text += (number==0)? "":number + ", выдан: ";
        text += (issued=="")? "":issued + " ";
        text += (released==null)? "":released;
        return text;
    }
}
