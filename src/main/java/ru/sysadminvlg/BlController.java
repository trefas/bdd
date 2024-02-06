package ru.sysadminvlg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class BlController extends VBox {
    @FXML
    private Button btn;
    @FXML
    private DatePicker dp_date;
    @FXML
    private TextField tf_reason;
    @FXML
    private TextField tf_mark;
    public BlController() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("blForm.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить кастомный элемент blForm");
            throw new RuntimeException(e);
        }
        dp_date.setValue(LocalDate.now());
    }
    public DatePicker getDp_date() {
        return dp_date;
    }
    public TextField getTf_reason() {
        return tf_reason;
    }
    public TextField getTf_mark() {
        return tf_mark;
    }
    public Button getBtn() {
        return btn;
    }
}