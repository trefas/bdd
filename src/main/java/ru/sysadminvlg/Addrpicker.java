// * Copyright (c) 2023. trefas@yandex.ru
package ru.sysadminvlg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
public class Addrpicker extends AnchorPane {
    @FXML
    public TextField address;
    public Address addr;
    public Addrpicker() {
        addr = new Address("","","","","",0,0);
        loadForm();
    }
    public Addrpicker(Address addr) {
        this.addr = addr;
        loadForm();
    }
    private void loadForm() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addrpicker.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить кастомный элемент AddressPicker");
            throw new RuntimeException(e);
        }
        address.onMouseClickedProperty().set(e -> elemClick());
        address.onKeyPressedProperty().set(e -> {
            if(e.getCode() != KeyCode.TAB) elemClick();
        });
    }
    private void elemClick() {
        Stage popup = new Stage();
        AddrController root = new AddrController(getAddr());
        VBox popupRoot = new VBox(root);
        Scene popupScene = new Scene(popupRoot);
        popup.setScene(popupScene);
        popup.setTitle("Домашний адрес донора");
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.show();
        root.getBtn().setOnAction(e -> {
            addr = root.getAddr();
            address.setText(addr.getFullTxt());
            popup.close();
        });
    }
    public Address getAddr() {
        return addr;
    }
    public void setAddr(Address addr) {
        this.addr = addr;
    }
}