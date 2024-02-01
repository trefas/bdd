// * Copyright (c) 2024. trefas@yandex.ru

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
import java.time.LocalDate;

public class Docpicker extends AnchorPane {
    @FXML
    public TextField document;
    public Document doc;
    public Docpicker(){
        doc = new Document(TypeDoc.PAS,"",0,"", LocalDate.now());
        loadForm();
    }
    public Docpicker(Document doc){
        this.doc = doc;
        loadForm();
    }
    private void loadForm() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("docpicker.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить кастомный элемент DocPicker");
            throw new RuntimeException(e);
        }
        document.onMouseClickedProperty().set(e -> elemclick());
        document.onKeyPressedProperty().set(e -> {
            if(e.getCode() != KeyCode.TAB) elemclick();
        });
    }
    private void elemclick() {
        Stage popup = new Stage();
        DocController root = new DocController(getDoc());
        VBox popupRoot = new VBox(root);
        Scene popupScene = new Scene(popupRoot);
        popup.setScene(popupScene);
        popup.setTitle("Удостоверение личности донора");
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.show();
        root.getBtn().setOnAction(e -> {
            doc = root.getDoc();
            document.setText(doc.toString());
            popup.close();
        });
    }
    public Document getDoc() {
        return doc;
    }
    public void setDoc(Document doc) {
        this.doc = doc;
    }
}
