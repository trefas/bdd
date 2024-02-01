// * Copyright (c) 2023. trefas@yandex.ru
package ru.sysadminvlg;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
public class Bgpopup extends AnchorPane {
    private int Code;
    private String Text;
    private Bloodgroup bgroup;
    @FXML
    private ToggleGroup bg;
    @FXML
    private CheckBox rh;
    @FXML
    private CheckBox AgC;
    @FXML
    private CheckBox Agc;
    @FXML
    private CheckBox AgE;
    @FXML
    private CheckBox Age;
    @FXML
    private CheckBox AgK;
    @FXML
    private Button btn;
    public Button getBtn() {
        return btn;
    }
    public int getCode() {
        Code = (bg.getToggles().get(2).isSelected())? 128:0;
        Code += (bg.getToggles().get(1).isSelected())? 64:0;
        Code += (bg.getToggles().get(3).isSelected())? 192:0;
        Code += (rh.isSelected())? 1:0;
        Code += (AgK.isSelected())? 2:0;
        Code += (Age.isSelected())? 4:0;
        Code += (Agc.isSelected())? 8:0;
        Code += (AgE.isSelected())? 16:0;
        Code += (AgC.isSelected())? 32:0;
        return Code;
    }
    public String getText() {
        RadioButton rb = (RadioButton) bg.getSelectedToggle();
        Text = rb.getText() + "Rh" + ((rh.isSelected())? "+":"-") + " ";
        Text += (rh.isSelected())? "D+":"D-";
        Text += (AgC.isSelected())? "C+":"C-";
        Text += (AgE.isSelected())? "E+":"E-";
        Text += (Agc.isSelected())? "c+":"c-";
        Text += (Age.isSelected())? "e+":"e-";
        Text += (AgK.isSelected())? "K+":"K-";
        return Text;
    }
    public Bgpopup(int code){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("bgpopup.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException e) {
            System.out.println("Не удалось загрузить форму bgpopup.fxml");
            throw new RuntimeException(e);
        }
        Code = code;
        bgroup = new Bloodgroup(code);
        Text = bgroup.getFullText();
        rh.setSelected(bgroup.getRh());
        boolean[] antigens = bgroup.getPh().antigens;
        AgC.setSelected(antigens[0]);
        AgE.setSelected(antigens[1]);
        Agc.setSelected(antigens[2]);
        Age.setSelected(antigens[3]);
        AgK.setSelected(antigens[4]);
        bg.getToggles().get(bgroup.getGroup()).setSelected(true);
    }
}