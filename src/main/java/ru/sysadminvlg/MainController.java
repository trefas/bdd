// * Copyright (c) 2024. trefas@yandex.ru
package ru.sysadminvlg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private TableColumn col_blcount;
    @FXML
    private TableColumn col_bllast;
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
    @FXML
    private Label numbl;
    private Donor selDonor;
    private Donor appDonor;
    private ObservableList list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        col_fio.setCellValueFactory(new PropertyValueFactory<Donor, String>("FullName"));
        col_phone.setCellValueFactory(new PropertyValueFactory<Donor, String>("phone"));
        col_bday.setCellValueFactory(new PropertyValueFactory<Donor, String>("bday"));
        col_bgroupe.setCellValueFactory(new PropertyValueFactory<Donor, String>("BloodGroupe"));
        col_blcount.setCellValueFactory(new PropertyValueFactory<Donor, Integer>("blcount"));
        col_bllast.setCellValueFactory(new PropertyValueFactory<Donor, String>("bllast"));
        FilteredList<Donor> fdata = new FilteredList<>(FXCollections.observableArrayList(Test.list));
        SortedList<Donor> sdata = new SortedList<>(fdata);
        sdata.comparatorProperty().bind(donors.comparatorProperty());
        donors.setItems(sdata);
        tf_surname.textProperty().addListener((observable, oldValue, newValue) ->
                donors.setItems(filterList(Test.list, newValue.toLowerCase())));
        tf_surname.focusedProperty().addListener((ov, oldV, newV) -> {
            if(!newV && !tf_surname.getText().isEmpty() && selDonor==null){
                btn_add.setDisable(false);
            }
        });
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
        if(selDonor!=null) {
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
                Connection con = DriverManager.getConnection("jdbc:sqlite:bdd.db");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM bloodletting left join prohibitions on bloodletting.id = prohibitions.blid where bloodletting.donor = " + selDonor.getId());
                this.list = FXCollections.observableArrayList(dbArrayList(rs));
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
            btn_add.setDisable(true);
            btn_create.setDisable(false);
        }
    }

    private ArrayList dbArrayList(ResultSet rs) throws SQLException{
        ArrayList<BloodLetting> data = new ArrayList<>();
        while (rs.next()){
            BloodLetting bl = new BloodLetting(rs.getInt(1),
                    LocalDate.parse(rs.getString("date")),
                    rs.getString("mark"),
                    (rs.getString("reason")==null)? "Пр.здоров(а)":rs.getString("reason"));
            data.add(bl);
        }
        numbl.setText("Найдено: "+selDonor.getBlcount());
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
        ce_addr.setAddr(null);
        ce_doc.document.setText("");
        ce_doc.setDoc(null);
        ce_bgroupe.getTf().setText("");
        ce_bgroupe.setCode(0);
        tf_work.setText("");
        btn_add.setDisable(true);
        btn_edit.setDisable(true);
        numbl.setText("");
        if (selDonor!=null&&this.list!=null) {
            this.list.clear();
            blood.setItems(list);
        }
        donors.setItems(Test.list);
        donors.getSelectionModel().clearSelection();
        selDonor = null;
        btn_create.setDisable(true);
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
    public void onDonorAppend(ActionEvent actionEvent) {
        btn_add.setDisable(true);
        Integer ndid;
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:bdd.db");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select seq from sqlite_sequence where name=\"donors\"");
            ndid = rs.getInt("seq") + 1;
            appDonor = new Donor(ndid,
                    tf_surname.getText(), tf_name.getText(),
                    tf_patronim.getText(), ((dp_bday.getValue()==null)? LocalDate.parse("1970-01-01"):dp_bday.getValue()),
                    ce_bgroupe.getBg(), tf_phone.getText(),
                    tf_work.getText(), ce_addr.getAddr(),
                    ce_doc.getDoc(), 0, null);
            Test.list.add(appDonor);
            st.executeUpdate("insert into donors (id,surname,name,patronim,bday,bgroup,phone,work)"
                    +" values ("+ndid+", '"+appDonor.getSurname()+"', '"
                    +appDonor.getName()+"', '"+appDonor.getPatronim()+"', '"
                    +((appDonor.getBday()==null)? "1970-01-01":appDonor.getBday().toString())+"', "
                    +appDonor.getBgroup().getCode()+", '"+appDonor.getPhone()
                    +"', '"+appDonor.getWork()+"')");
            if(appDonor.getAddr().getCity().equals("")&&appDonor.getAddr().getDistrict().equals("")
            &&appDonor.getAddr().getRegion().equals("")&&appDonor.getAddr().getStreet().equals("")
            &&appDonor.getAddr().getHouse().equals("")){
                Alert al = new Alert(Alert.AlertType.WARNING);
                al.setTitle("Адрес донора не добавлен!");
                al.setHeaderText("Адрес донора не введен в базу.");
                al.setContentText("Для корректного хранения информации о донорах указывайте адрес полностью.");
                al.show();
            } else {
                st.executeUpdate("insert into addresses (id,region,district,city,street,house,corp,room)" +
                        " values("+ndid+", '"+appDonor.getAddr().getRegion()+"', '"
                        +appDonor.getAddr().getDistrict()+"', '"+appDonor.getAddr().getCity()+"', '"
                        +appDonor.getAddr().getStreet()+"', '"+appDonor.getAddr().getHouse()+"', "
                        +appDonor.getAddr().getCorp()+", "+appDonor.getAddr().getRoom()+")");
            }
            if(appDonor.getDoc().number.equals(0)&&appDonor.getDoc().serial.equals("")){
                Alert al = new Alert(Alert.AlertType.WARNING);
                al.setTitle("Документ донора не добавлен!");
                al.setHeaderText("Удостоверение личности донора не добавлено в базу.");
                al.setContentText("Для корректного хранения информации о донорах указывайте удостоверение личности.");
                al.show();
            } else {
                st.executeUpdate("insert into documents (id,name,serial,number,issued,released) values("+
                        ndid+", '"+appDonor.getDoc().name.toString()+"', '"+
                        appDonor.getDoc().serial+"', "+appDonor.getDoc().number+
                        ", '"+appDonor.getDoc().issued+"', '"+
                        ((appDonor.getDoc().released==null)? "":appDonor.getDoc().released.toString())+ "')");
            }
            con.close();
            selDonor = appDonor;
            btn_create.setDisable(false);
            donors.refresh();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void onDonorUpdate(ActionEvent actionEvent) {
        Integer did = selDonor.getId();
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:bdd.db");
            Statement st = con.createStatement();
            st.executeUpdate("update donors set surname='"+tf_surname.getText()+"', "+
                    "name='"+tf_name.getText()+"', patronim='"+tf_patronim.getText()+
                    "', bday='"+((dp_bday.getValue()==null)? "1970-01-01":dp_bday.getValue().toString())+
                    "', bgroup="+
                    ce_bgroupe.getCode()+", phone='"+tf_phone.getText()+"', work='"+
                    tf_work.getText()+"' where id="+did);
            ResultSet rs = st.executeQuery("select (select count(id) from documents where id="+did+
                    ") as doc,(select count(id) from addresses where id="+did+") as addr");
            boolean doc = rs.getInt("doc")==1; boolean addr = rs.getInt("addr")==1;
            if(doc){
                st.executeUpdate("update documents set name='"+ce_doc.getDoc().name.toString()+
                        "', serial='" + ce_doc.getDoc().serial + "', number=" +
                        ce_doc.getDoc().number + ", issued='"+
                        ce_doc.getDoc().issued + "', released='"+
                        ((ce_doc.getDoc().released==null)? "":ce_doc.getDoc().released.toString())+
                        "' where id="+did);
            } else if(!selDonor.getDoc().number.equals(0)|| !selDonor.getDoc().serial.equals("")){
                st.executeUpdate("insert into documents (id,name,serial,number,issued,released) values ("+
                        did+", '"+ce_doc.getDoc().name.toString()+
                        "', '" + ce_doc.getDoc().serial + "', " +
                        ce_doc.getDoc().number + ", '"+
                        ce_doc.getDoc().issued + "', '"+
                        ((ce_doc.getDoc().released==null)? "":ce_doc.getDoc().released.toString())+"')");
            }
            if (addr){
                st.executeUpdate("update addresses set region='"+ce_addr.getAddr().getRegion()+
                        "', district='"+ce_addr.getAddr().getDistrict()+
                        "', city='"+ce_addr.getAddr().getCity()+"', street='"+
                        ce_addr.getAddr().getStreet()+"', house='"+
                        ce_addr.getAddr().getHouse()+"', corp="+ce_addr.getAddr().getCorp()+
                        ", room="+ce_addr.getAddr().getRoom()+" where id="+did);
            } else if(!selDonor.getAddr().getCity().equals("") || !selDonor.getAddr().getDistrict().equals("")
                    || !selDonor.getAddr().getRegion().equals("") || !selDonor.getAddr().getStreet().equals("")
                    || !selDonor.getAddr().getHouse().equals("")) {
                st.executeUpdate("insert into addresses (id,region,district,city,street,house,corp,room) values ("+
                        did+", '"+ce_addr.getAddr().getRegion()+
                        "', '"+ce_addr.getAddr().getDistrict()+
                        "', '"+ce_addr.getAddr().getCity()+"', '"+
                        ce_addr.getAddr().getStreet()+"', '"+
                        ce_addr.getAddr().getHouse()+"', "+ce_addr.getAddr().getCorp()+
                        ", "+ce_addr.getAddr().getRoom()+")");
            }
            Test.list.remove(selDonor);
            appDonor = new Donor(did,
                    tf_surname.getText(), tf_name.getText(),
                    tf_patronim.getText(), dp_bday.getValue(),
                    ce_bgroupe.getBg(), tf_phone.getText(),
                    tf_work.getText(), ce_addr.getAddr(),
                    ce_doc.getDoc(), selDonor.getBlcount(), selDonor.getBllast());
            Test.list.add(appDonor);
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void onBlCreate(ActionEvent actionEvent) {
        this.list = FXCollections.observableArrayList();
        Stage popup = new Stage();
        BlController root = new BlController();
        HBox popupRoot = new HBox(root);
        Scene popupScene = new Scene(popupRoot);
        popup.setScene(popupScene);
        popup.setTitle("Кроводача");
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.show();
        root.getBtn().setOnAction(e -> {
            try {
                Connection con = DriverManager.getConnection("jdbc:sqlite:bdd.db");
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("select seq from sqlite_sequence where name=\"bloodletting\"");
                Integer ndid = rs.getInt("seq") + 1;
                st.executeUpdate("insert into bloodletting (id,donor,date,mark) values ("+
                        ndid+", "+selDonor.getId()+", '"+root.getDp_date().getValue().toString()+
                        "', '"+root.getTf_mark().getText()+"')");
                selDonor.setBlcount(selDonor.getBlcount()+1);
                selDonor.setBllast(LocalDate.now());
                this.list.add(new BloodLetting(ndid,LocalDate.now(),root.getTf_mark().getText(),(root.getTf_reason().getText().equals(""))? "Пр.здоров(а)":root.getTf_reason().getText()));
                blood.setItems(this.list);
                numbl.setText("Найдено: "+selDonor.getBlcount().toString());
                donors.refresh();
                if(!root.getTf_reason().getText().isEmpty()) {
                    st.executeUpdate("insert into prohibitions (blid,reason,temp) values ("+
                            ndid+", '"+root.getTf_reason().getText()+"', 1)");
                }
                con.close();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            new TThread().start();
            popup.close();
        });
    }

    private class TThread extends Thread{

        @Override
        public void run() {
            try {
                XWPFDocument doc = new XWPFDocument(OPCPackage.open("templ.docx"));
                for (XWPFParagraph p : doc.getParagraphs()){
                    List<XWPFRun> runs = p.getRuns();
                    if(runs != null){
                        for (XWPFRun r : runs){
                            String txt = r.getText(0);
                            if(Objects.equals(txt, "curdate"))r.setText(new SimpleDateFormat("dd MMMM y").format(new Date()),0);
                            if(Objects.equals(txt, "surname"))r.setText(selDonor.getSurname(),0);
                            if(Objects.equals(txt, "name"))r.setText(selDonor.getName(),0);
                            if(Objects.equals(txt, "patronim"))r.setText(selDonor.getPatronim(),0);
                            if(Objects.equals(txt, "bday"))r.setText(selDonor.getBday().format(DateTimeFormatter.ofPattern("dd MMMM y")),0);
                            if(Objects.equals(txt, "addr"))r.setText(selDonor.getAddr().getFullTxt(),0);
                            if(Objects.equals(txt, "work"))r.setText(selDonor.getWork(),0);
                            if(Objects.equals(txt, "doc"))r.setText(selDonor.getDoc().toString(),0);
                            if(Objects.equals(txt, "phone"))r.setText(selDonor.getPhone(),0);
                            if(Objects.equals(txt, "fio"))r.setText(selDonor.getFullName(),0);
                            }
                    }
                }
                for (XWPFTable tbl : doc.getTables()) {
                    for (XWPFTableRow row : tbl.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            for (XWPFParagraph p : cell.getParagraphs()) {
                                for (XWPFRun r : p.getRuns()) {
                                    String txt = r.getText(0);
                                    if(Objects.equals(txt, "pheno"))r.setText(selDonor.getBgroup().getPh().getTextCode(),0);
                                    if(Objects.equals(txt, "bgtext"))r.setText(selDonor.getBgroup().getGroupTxt(),0);
                                    if(Objects.equals(txt, "rh"))r.setText((selDonor.getBgroup().getRh())? "Rh+":"Rh-",0);
                                    }
                                }
                            }
                        }
                    }
                doc.write(new FileOutputStream("output.docx"));
            } catch (InvalidFormatException | IOException ex) {
                throw new RuntimeException(ex);
            }
            try {
                Desktop.getDesktop().open(new File("output.docx"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            super.run();
        }
    }
    public void onBloodLetSelect(MouseEvent mouseEvent) {
        BloodLetting selBL = (BloodLetting) blood.getSelectionModel().getSelectedItem();
        if(selBL!=null){
            Stage popup = new Stage();
            BlController root = new BlController();
            HBox popupRoot = new HBox(root);
            Scene popupScene = new Scene(popupRoot);
            popup.setScene(popupScene);
            popup.setTitle("Кроводача");
            popup.initModality(Modality.APPLICATION_MODAL);
            root.getBtn().setText("Изменить");
            popup.show();
            blood.getSelectionModel().clearSelection();
            root.getTf_mark().setText(selBL.getMark());
            root.getDp_date().setValue(selBL.getDate());
            root.getTf_reason().setText(selBL.getReason());
            root.getBtn().setOnAction(e -> {
                try {
                    Connection con = DriverManager.getConnection("jdbc:sqlite:bdd.db");
                    Statement st = con.createStatement();
                    st.executeUpdate("update bloodletting set date='"
                            +root.getDp_date().getValue().toString()+"', mark='"+
                            root.getTf_mark().getText()+"' where id="+selBL.getId());
                    ResultSet rs = st.executeQuery("select count(id) from prohibitions where blid="+selBL.getId());
                    if(rs.getInt(1)==1){
                        st.executeUpdate("update prohibitions set reason='"+
                                root.getTf_reason().getText()+"' where blid="+selBL.getId());
                    } else if(!root.getTf_reason().getText().equals("Пр.здоров(а)")) {
                        st.executeUpdate("insert into prohibitions (blid,reason,temp)values ("+
                                selBL.getId()+", '"+root.getTf_reason().getText()+"', 1)");
                    }
                    this.list.remove(selBL);
                    this.list.add(new BloodLetting(selBL.getId(),root.getDp_date().getValue(),
                            root.getTf_mark().getText(),root.getTf_reason().getText()));
                    blood.setItems(this.list);
                    con.close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                popup.close();
            });
        }
    }
}