// * Copyright (c) 2023. trefas@yandex.ru
package ru.sysadminvlg;
public class Address {
    public String region;
    public String district;
    public String city;
    public String street;
    public String house;
    public int corp;
    public int room;
    public Address(String region, String district, String city, String street, String house, int corp, int room) {
        this.region = region;
        this.district = district;
        this.city = city;
        this.street = street;
        this.house = house;
        this.corp = corp;
        this.room = room;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getRegion() {
        return region;
    }
    public void setRegion(String region) {
        this.region = region;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getHouse() {
        return house;
    }
    public void setHouse(String house) {
        this.house = house;
    }
    public int getCorp() {
        return corp;
    }
    public void setCorp(int corp) {
        this.corp = corp;
    }
    public int getRoom() {
        return room;
    }
    public void setRoom(int room) {
        this.room = room;
    }
    public String getFullTxt() {
        String s = "";
        s += (region.isEmpty())? "":region + ", ";
        s += (district.isEmpty())? "":district + ", ";
        s += (city.isEmpty())? "":city + ", ";
        s += (street.isEmpty())? "":street + ", ";
        s += (house.isEmpty())? "":house + ", ";
        s += (corp==0)? "":String.valueOf(corp) + ", ";
        s += (room==0)? "":String.valueOf(room);
        return s;
    }
}
