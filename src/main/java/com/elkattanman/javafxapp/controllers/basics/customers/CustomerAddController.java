package com.elkattanman.javafxapp.controllers.basics.customers;


import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Customer;
import com.elkattanman.javafxapp.repositories.CustomerRepository;
import com.elkattanman.javafxapp.util.AlertMaker;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/basics/customers/add_customer.fxml")
public class CustomerAddController implements Initializable {

    @FXML
    private JFXTextField nameTF, addressTF, phoneTF , branchTF;
    private CallBack callBack;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private final CustomerRepository customerRepository;
    private Boolean isInEditMode = Boolean.FALSE;
    private Customer myCustomer;

    public CustomerAddController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    private boolean makeCustomer(){
        String name = StringUtils.trimToEmpty(nameTF.getText());
        String branch = StringUtils.trimToEmpty(branchTF.getText());
        String address = StringUtils.trimToEmpty(addressTF.getText());
        String phone = StringUtils.trimToEmpty(phoneTF.getText());

          for(int i =0 ; i < phone.length() ; ++ i ){
              if(phone.charAt(i) > '9' ) {
                  AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "ؤقم الهاتف يجب ان يكون ارقام فقط");
                  return false;
              }
          }

        if (name.isEmpty() || address.isEmpty() || branch.isEmpty()  || phone.isEmpty() ) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return false;
        }
        myCustomer.setName(name);
        myCustomer.setCity(address);
        myCustomer.setPhone(phone);
        myCustomer.setEmail(branch);
        return true;
    }

    @FXML
    private void addStore(ActionEvent event) {
        if (!makeCustomer())return;

        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Customer savedCustomer = customerRepository.save(myCustomer);
        callBack.callBack(savedCustomer);
        myCustomer = new Customer();
        clearEntries();
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success operation", "تمت عمليه الادخال");
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
    public void resetEditToAdd(){
        isInEditMode = Boolean.FALSE;
    }
    public void resetAddToEdit(){
        isInEditMode = Boolean.TRUE;
    }

    public void inflateUI(Customer customer) {
        myCustomer= customer;
        nameTF.setText(customer.getName());
        addressTF.setText(customer.getCity());
        phoneTF.setText(customer.getPhone());
        branchTF.setText(customer.getEmail());
    }

    private void clearEntries() {
        nameTF.clear();
        addressTF.clear();
        phoneTF.clear();
        branchTF.clear();
    }

    private void handleEditOperation() {
        if(!makeCustomer())return;
        Customer savedCustomer = customerRepository.save(myCustomer);
        callBack.callBack(savedCustomer);
        Stage stage= (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
