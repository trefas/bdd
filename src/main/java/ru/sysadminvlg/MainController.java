// * Copyright (c) 2024. trefas@yandex.ru
package ru.sysadminvlg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button btn_add;
    @FXML
    private Button btn_create;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_search;
    @FXML
    private Addrpicker ce_addr;
    @FXML
    private Bgpicker ce_bgroupe;
    @FXML
    private Docpicker ce_doc;
    @FXML
    private TableColumn col_bday;
    @FXML
    private TableColumn col_bgroupe;
    @FXML
    private TableColumn col_date;
    @FXML
    private TableColumn col_mark;
    @FXML
    private TableColumn col_fio;
    @FXML
    private TableColumn col_med;
    @FXML
    private TableColumn col_phone;
    @FXML
    private DatePicker dp_bday;
    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_patronim;
    @FXML
    private TextField tf_phone;
    @FXML
    private TextField tf_surname;
    @FXML
    private TextField tf_work;
    @FXML
    private TableView blood;
    @FXML
    private TableView donors;
    private Donor selDonor;
    private ObservableList list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_fio.setCellValueFactory(new PropertyValueFactory<Donor, String>("FullName"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Donor, String>("phone"));
        col_bday.setCellValueFactory(new PropertyValueFactory<Donor, String>("bday"));
        col_bgroupe.setCellValueFactory(new PropertyValueFactory<Donor, String>("BloodGroupe"));
        FilteredList<Donor> fdata = new FilteredList<>(FXCollections.observableArrayList(Test.list));
        donors.setItems(fdata);
        tf_surname.textProperty().addListener((observable, oldValue, newValue) ->
                donors.setItems(filterList(Test.list, newValue.toLowerCase())));
    }

    private ObservableList<Donor> filterList(List<Donor> list, String lowerCase) {
        List<Donor> fList = new ArrayList<>();
        for (Donor order:list){
            if(order.getSurname().toLowerCase().contains(lowerCase)) fList.add(order);
        }
        return FXCollections.observableList(fList);
    }
    public void onDonorSelect(MouseEvent mouseEvent) {
        selDonor = (Donor) donors.getSelectionModel().getSelectedItem();
        tf_surname.setText(selDonor.getSurname());
        tf_name.setText(selDonor.getName());
        tf_patronim.setText(selDonor.getPatronim());
        dp_bday.setValue(selDonor.getBday());
        tf_phone.setText(selDonor.getPhone());
        ce_addr.setAddr(selDonor.getAddr());
        ce_addr.address.setText(selDonor.getAddr().getFullTxt());
        ce_doc.setDoc(selDonor.getDoc());
        ce_doc.document.setText(selDonor.getDoc().toString());
        ce_bgroupe.setCode(selDonor.getBgroup().getCode());
        ce_bgroupe.getTf().setText(selDonor.getBloodGroupe());
        tf_work.setText(selDonor.getWork());
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite::resource:bdd.db");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM bloodletting left join prohibitions on bloodletting.id = prohibitions.blid where bloodletting.donor = "+selDonor.getId());
            list = FXCollections.observableArrayList(dbArrayList(rs));
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        col_date.setCellValueFactory(new PropertyValueFactory<BloodLetting, String>("date"));
        col_med.setCellValueFactory(new PropertyValueFactory<BloodLetting, String>("reason"));
        col_mark.setCellValueFactory(new PropertyValueFactory<BloodLetting, String>("mark"));
        FilteredList<BloodLetting> fbllist = new FilteredList<>(FXCollections.observableArrayList(list));
        blood.setItems(fbllist);
        btn_edit.setDisable(false);
    }

    private ArrayList dbArrayList(ResultSet rs) throws SQLException{
        ArrayList<BloodLetting> data = new ArrayList<>();
        while (rs.next()){
            BloodLetting bl = new BloodLetting(rs.getInt(1),
                    LocalDate.parse(rs.getString("date")),
                    rs.getString("mark"),
                    rs.getString("reason"));
            data.add(bl);
        }
        return data;
    }

    public void onDonorClear(ActionEvent actionEvent) {
        tf_surname.setText("");
        tf_name.setText("");
        tf_patronim.setText("");
        dp_bday.setValue(LocalDate.now());
        dp_bday.getEditor().setText("");
        tf_phone.setText("");
        ce_addr.address.setText("");
        ce_addr.setAddr(new Address("","","","","",0,0));
        ce_doc.document.setText("");
        ce_doc.setDoc(new Document(TypeDoc.PAS,"",0,"",LocalDate.now()));
        ce_bgroupe.getTf().setText("");
        ce_bgroupe.setCode(0);
        tf_work.setText("");
        btn_add.setDisable(true);
        btn_edit.setDisable(true);
        list.clear();
        blood.setItems(list);
        donors.setItems(Test.list);
    }
    public void onSearchBtnClick(ActionEvent actionEvent) {
        String sbgroupe = new Bloodgroup(ce_bgroupe.getCode()).getGroupText();
        donors.setItems(filterGroupe(Test.list, sbgroupe));
    }
    private ObservableList<Donor> filterGroupe(List<Donor> list, String searched) {
        List<Donor> fList = new ArrayList<>();
        for (Donor order:list){
            if(order.getBgroup().getGroupText().contains(searched)) fList.add(order);
        }
        return FXCollections.observableList(fList);
    }
}