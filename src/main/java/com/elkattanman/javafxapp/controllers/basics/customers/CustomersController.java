package com.elkattanman.javafxapp.controllers.basics.customers;

import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Customer;
import com.elkattanman.javafxapp.repositories.CustomerRepository;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/basics/customers/customers.fxml")
public class CustomersController implements Initializable , CallBack< Boolean, Customer > {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    TableView<Customer> table ;
    @FXML
    private TableColumn<Customer, Integer> idCol;
    @FXML
    private TableColumn<Customer, String>  nameCol, cityCol , phoneCol , emailCol;
    @FXML
    private JFXTextField searchTF;

    private ObservableList<Customer> list = FXCollections.observableArrayList();
    private final CustomerRepository customerRepository ;

    public CustomersController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository ;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       initCol();
       list.setAll(customerRepository.findAll()) ;
       table.setItems(list);
       MakeMyFilter();
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        FxControllerAndView<CustomerAddController, Parent> load = fxWeaver.load(CustomerAddController.class);
        CustomerAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Customer());
        controller.resetEditToAdd();
        AssistantUtil.loadWindow(null, load.getView().get());
    }
    @FXML
    void edit(ActionEvent event) {
        Customer selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<CustomerAddController, Parent> load = fxWeaver.load(CustomerAddController.class);
        CustomerAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.resetAddToEdit();
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Customer selectedItem = table.getSelectionModel().getSelectedItem();
        customerRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(customerRepository.findAll());
    }
    private void MakeMyFilter() {
        FilteredList<Customer> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(customer -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (customer.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                return false;
            });

        });

        SortedList<Customer> sortedCustomers = new SortedList<>(filteredData) ;
        sortedCustomers.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedCustomers);

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    }


    @Override
    public Boolean callBack(Customer object) {

        for (int i=0 ; i < list.size(); ++i) {
            Customer customer= list.get(i);
            if (customer.getId().equals(object.getId())) {
                list.set(i, object);
                return true;
            }
        }

        list.add(object) ;
        return true;
    }
}