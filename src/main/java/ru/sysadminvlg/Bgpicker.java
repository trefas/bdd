// * Copyright (c) 2023. trefas@yandex.ru
package ru.sysadminvlg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class Bgpicker extends AnchorPane {
    @FXML
    private TextField tf;

    public TextField getTf() {
        return tf;
    }

    public void setCode(int code) {
        Code = code;
    }

    private int Code;
    public int getCode() {
        return Code;
    }
    public Bgpicker(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bgpicker.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить кастомный элемент BGPicker");
            throw new RuntimeException(e);
        }
        tf.onMouseClickedProperty().set(e -> btnClick());
        tf.setOnAction(e -> txtEnter());
        tf.onKeyPressedProperty().set(e -> {
            if(e.getCode() == KeyCode.ENTER) btnClick();
        });
        Code = 0;
    }
    private void btnClick() {
        if(tf.getText().matches("^(0\\(I\\)|A\\(II\\)|B\\(III\\)|AB\\(IV\\))(Rh[+-])? ?" +
                "(D[+-]C[+-]E[+-]c[+-]e[+-]K[+-])?$")){
            Bloodgroup bg = new Bloodgroup(tf.getText());
            Code = bg.getCode();}
        Stage popup = new Stage();
        Bgpopup root = new Bgpopup(getCode());
        AnchorPane popupRoot = new AnchorPane(root);
        Scene popupScene = new Scene(popupRoot);
        popup.setScene(popupScene);
        popup.setTitle("Группа крови и фенотип");
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.show();
        root.getBtn().setOnAction(e -> {
            Code = root.getCode();
            tf.setText(root.getText());
            popup.close();
        });
    }
    private void txtEnter() {
        String s = tf.getText();
        if(s.matches("^(0\\(I\\)|A\\(II\\)|B\\(III\\)|AB\\(IV\\))(Rh[+-])? ?" +
                "(D[+-]C[+-]E[+-]c[+-]e[+-]K[+-])?$")){
            Bloodgroup bg = new Bloodgroup(tf.getText());
            Code = bg.getCode();
        } else { if (!s.isEmpty()) {
            Alert al = new Alert(Alert.AlertType.WARNING);
            al.setTitle("Ошибка ручного добавления!");
            al.setHeaderText("Ошибка ручного ввода группы или фенотипа.");
            al.setContentText("Для корректного введения воспользуйтесь формой ввода группы " +
                    "и фенотипа нажав кнопку рядом с полем ввода");
            al.show();}
        }
    }
}