// * Copyright (c) 2023. trefas@yandex.ru
package ru.sysadminvlg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;
public class AddrController extends VBox {
    public Address addr;
    @FXML
    private Button btn;
    public Button getBtn() {
        return btn;
    }
    @FXML
    private TextField city;
    @FXML
    private TextField corp;
    @FXML
    private TextField district;
    @FXML
    private TextField house;
    @FXML
    private TextField region;
    @FXML
    private TextField room;
    @FXML
    private TextField street;
    public AddrController(Address addr) {
        this.addr = addr;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addrForm.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить форму addrForm.fxml");
            throw new RuntimeException(e);
        }
        region.setText(addr.getRegion());
        district.setText(addr.getDistrict());
        city.setText(addr.getCity());
        street.setText(addr.getStreet());
        house.setText(addr.getHouse());
        corp.setText(String.valueOf(addr.getCorp()));
        room.setText(String.valueOf(addr.getRoom()));
    }
    public Address getAddr() {
        addr.setRegion(region.getText());
        addr.setDistrict(district.getText());
        addr.setCity(city.getText());
        addr.setStreet(street.getText());
        addr.setHouse(house.getText());
        try {
            addr.setCorp(Integer.parseInt(corp.getText()));
        } catch (NumberFormatException e) {
            addr.setCorp(0);
        }
        try {
             addr.setRoom(Integer.parseInt(room.getText()));
        } catch (NumberFormatException e) {
            addr.setRoom(0);
        }
        return addr;
    }
}
