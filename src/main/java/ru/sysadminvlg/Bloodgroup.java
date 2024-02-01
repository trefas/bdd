// * Copyright (c) 2023. trefas@yandex.ru

package ru.sysadminvlg;

import java.util.Objects;

public class Bloodgroup {
    public int Code;
    public Phenotype Ph;
    public Bloodgroup(int code){
        Code = code;
        Ph = new Phenotype();
        Ph.setPartCode(code&63);
    }
    public Bloodgroup(String fullCode){
        String[] parts = fullCode.split(" ");
        Ph = new Phenotype();
        String[] gr = parts[0].split("\\)");
        if(parts.length > 1) Ph.setTextCode(parts[1]);
         else Ph.setPartCode((gr.length > 1 && Objects.equals(gr[1], "Rh+"))? 1:0);
        switch (gr[0]){
            case "A(II": Code = 64; break;
            case "B(III": Code = 128; break;
            case "AB(IV": Code = 192; break;
            case "0(I": Code = 0; break;
        }
        Code += Ph.getPartCode();
    }
    public int getCode() { return Code; }
    public Phenotype getPh() { return Ph; }
    public int getGroup() { return Code>>6; }
    public boolean getRh() { return Code%2 == 1; }
    public String getGroupText(){
        String[] groups = { "0(I)","A(II)","B(III)","AB(IV)" };
        String txt = groups[getGroup()];
        txt += (Code%2 == 1)? "Rh+":"Rh-";
        return txt;
    }
    public String getFullText(){ return getGroupText() + " " + Ph.getTextCode(); }
}