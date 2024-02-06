// * Copyright (c) 2023. trefas@yandex.ru
package ru.sysadminvlg;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
public class Test extends Application {
    public static ObservableList list;
    {
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlite:bdd.db");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select donors.id, donors.surname, donors.name, donors.patronim, "+
                    "donors.bday, donors.bgroup, donors.phone, donors.work, addresses.region, addresses.district, "+
                    "addresses.city, addresses.street, addresses.house, addresses.corp, addresses.room, "+
                    "documents.name as typedoc, documents.serial, documents.number, documents.issued, "+
                    "documents.released, (select count(id) from bloodletting where donors.id=bloodletting.donor) "+
                    "as num, (select max(date) from bloodletting where bloodletting.donor=donors.id) "+
                    "as last from donors left join addresses on donors.id=addresses.id left join documents on donors.id=documents.id");
            list = FXCollections.observableArrayList(dbArrayList(rs));
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private ArrayList dbArrayList(ResultSet rs)  throws SQLException {
        ArrayList<Donor> data = new ArrayList<>();
        while (rs.next()){
            Donor donor = new Donor(rs.getInt("id"),rs.getString("surname"),
                    rs.getString("name"), rs.getString("patronim"),
                    LocalDate.parse(rs.getString("bday")),
                    new Bloodgroup(rs.getInt("bgroup")),
                    (rs.getString("phone")==null)? "":rs.getString("phone"),
                    (rs.getString("work")==null)? "":rs.getString("work"),
                            new Address((rs.getString("region")==null)? "":rs.getString("region"),
                                    (rs.getString("district")==null)? "":rs.getString("district"),
                                    (rs.getString("city")==null)? "":rs.getString("city"),
                                    (rs.getString("street")==null)? "":rs.getString("street"),
                                    (rs.getString("house")==null)? "":rs.getString("house"),
                                    rs.getInt("corp"),
                                    rs.getInt("room")),
                            new Document(TypeDoc.fromString(rs.getString("typedoc")),
                                    (rs.getString("serial")==null)? "":rs.getString("serial"),
                                    rs.getInt("number"),
                                    (rs.getString("issued")==null)? "":rs.getString("issued"),
                                    (rs.getString("released")==null)? null:LocalDate.parse(rs.getString("released"))),
                    rs.getInt("num"), ((rs.getString("last")==null)? null:LocalDate.parse(rs.getString("last"))));
            data.add(donor);
        }
        return data;
    }
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("test.fxml"));
        Scene scene = new Scene(mainLoader.load());
        stage.setTitle("База данных доноров");
        stage.setScene(scene);
        stage.show();
    }
    public static void main(String[] args){ launch(); }
}