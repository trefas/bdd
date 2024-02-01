// * Copyright (c) 2024. trefas@yandex.ru

package ru.sysadminvlg;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DocController extends VBox {
    public Document doc;
    @FXML
    private Button btn;
    public Button getBtn() {
        return btn;
    }
    @FXML
    private ComboBox<TypeDoc> doctype;
    @FXML
    private TextField issued;
    @FXML
    private TextField number;
    @FXML
    private DatePicker released;
    @FXML
    private TextField serial;
    public DocController(Document doc){
        this.doc = doc;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("docForm.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить форму docForm.fxml");
            throw new RuntimeException(e);
        }
        issued.setText(doc.issued);
        serial.setText(doc.serial);
        number.setText(String.valueOf(doc.number));
        released.setValue(doc.released);
        doctype.setValue(doc.name);
        doctype.setItems(FXCollections.observableArrayList(TypeDoc.PAS,TypeDoc.MIL));
    }

    public Document getDoc() {
        doc.name = doctype.getValue();
        doc.issued = issued.getText();
        doc.serial = serial.getText();
        doc.released = released.getValue();
        try {
            doc.number = Integer.parseInt(number.getText());
        } catch (NumberFormatException e) {
            doc.number = 0;
        }
        return doc;
    }
}
