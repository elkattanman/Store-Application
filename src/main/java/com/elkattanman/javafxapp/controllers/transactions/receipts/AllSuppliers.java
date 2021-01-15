package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.domain.Supplier;
import com.elkattanman.javafxapp.repositories.ReceiptRepository;
import com.elkattanman.javafxapp.repositories.SupplierRepository;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/tranactions/receipt/all_suppliers.fxml")
public class AllSuppliers implements Initializable {

    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    TableView<Supplier> table ;
    @FXML
    private TableColumn<Supplier, Integer> idCol;
    @FXML
    private TableColumn<Supplier, String>  nameCol , phoneCol , emailCol , companyCol;
    @FXML
    private JFXTextField searchTF;

    private ObservableList<Supplier> list = FXCollections.observableArrayList();
    private final SupplierRepository supplierRepository ;

    public AllSuppliers(ReceiptRepository receiptRepository, SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(supplierRepository.findAll() );
        table.setItems(list);
        MakeMyFilter();
    }

    private void MakeMyFilter() {
        FilteredList<Supplier> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                return false;
            });

        });

        SortedList<Supplier> sortedsuppliers = new SortedList<>(filteredData) ;
        sortedsuppliers.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedsuppliers);

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));

    }

    public void handleRefresh(ActionEvent actionEvent) {
        list.setAll(supplierRepository.findAll());
    }

    public void SelectAction(ActionEvent actionEvent) {

    }

    private Stage getStage() {
        return (Stage) table.getScene().getWindow();
    }

    public void closeStage(ActionEvent actionEvent) {
        getStage().close();
    }
}
