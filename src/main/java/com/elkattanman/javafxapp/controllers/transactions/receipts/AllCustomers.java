package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Customer;
import com.elkattanman.javafxapp.domain.Supplier;
import com.elkattanman.javafxapp.repositories.CustomerRepository;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/tranactions/receipt/all_customers.fxml")
public class AllCustomers implements Initializable {

    @Autowired
    private FxWeaver fxWeaver;

    @FXML private StackPane rootPane;
    @FXML private AnchorPane contentPane;

    @FXML
    TableView<Customer> table ;
    @FXML
    private TableColumn<Customer, Integer> idCol;
    @FXML
    private TableColumn<Customer, String>  nameCol , phoneCol , emailCol , cityCol;
    @FXML
    private JFXTextField searchTF;

    private CallBack callBack;

    private ObservableList<Customer> list = FXCollections.observableArrayList();
    private final CustomerRepository customerRepository ;

    public AllCustomers(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(customerRepository.findAll() );
        table.setItems(list);
        MakeMyFilter();
        rowDoubleClick();
    }

    private void rowDoubleClick() {
        table.setRowFactory(tableView -> {
            TableRow<Customer> row =new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount()==2 && !row.isEmpty()){
                    SelectAction();
                }
            });
            return row;
        });
    }
    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void MakeMyFilter() {
        FilteredList<Customer> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (customer.getName().toLowerCase().contains(lowerCaseFilter)|| customer.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });

        });

        SortedList<Customer> sortedcustomers = new SortedList<>(filteredData) ;
        sortedcustomers.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedcustomers);

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));

    }

    public void handleRefresh(ActionEvent actionEvent) {
        list.setAll(customerRepository.findAll());
    }

    public void SelectAction() {
        Customer customer = table.getSelectionModel().getSelectedItem();
        callBack.callBack(customer);
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) table.getScene().getWindow();
    }

    public void closeStage(ActionEvent actionEvent) {
        getStage().close();
    }
}
