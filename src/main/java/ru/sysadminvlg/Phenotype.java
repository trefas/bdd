// * Copyright (c) 2023. trefas@yandex.ru

package ru.sysadminvlg;
public class Phenotype {
    private int PartCode;
    private String TextCode;
    private boolean source;
    public boolean[] antigens;
    public int getPartCode() { return PartCode; }
    public String getTextCode() { return TextCode; }
    public void setPartCode(int partCode) {
        PartCode = partCode;
        source = true;
        setAntigens();
        Ag2textCode();
    }
    public void setTextCode(String textCode) {
        TextCode = textCode;
        source = false;
        setAntigens();
        Ag2partCode();
    }
    private void setAntigens() {
        antigens = new boolean[6];
        if (source) {
            String bitCode = Integer.toBinaryString(PartCode+64);
            char[] sym = bitCode.toCharArray();
            for(int i=0;i<6;i++) antigens[i] = sym[i + 1] == '1';
        } else {
            char[] sym = TextCode.toCharArray();
            char tmp = sym[1];
            for(int i=1;i<10;i+=2) sym[i] = sym[i+2];
            sym[11] = tmp;
            for(int i=1;i<7;i++) antigens[i-1] = sym[i * 2 - 1] == '+';
        }
    }
    private void Ag2textCode() {
        String txt = (antigens[5])? "D+":"D-";
        txt += (antigens[0])? "C+":"C-";
        txt += (antigens[1])? "E+":"E-";
        txt += (antigens[2])? "c+":"c-";
        txt += (antigens[3])? "e+":"e-";
        txt += (antigens[4])? "K+":"K-";
        TextCode = txt;
    }
    private void Ag2partCode() {
        StringBuilder binValue = new StringBuilder();
        for(boolean x: antigens) binValue.append((x) ? "1" : "0");
        PartCode = Integer.parseInt(binValue.toString(),2);
    }
}