package com.elkattanman.javafxapp.controllers.basics.suppliers;


import com.elkattanman.javafxapp.controllers.basics.CallBack;
import com.elkattanman.javafxapp.domain.Supplier;
import com.elkattanman.javafxapp.repositories.SupplierRepository;
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
@FxmlView("/FXML/basics/suppliers/add_supplier.fxml")
public class SupplierAddController implements Initializable {

    @FXML
    private JFXTextField nameTF, addressTF, phoneTF , branchTF;
    private CallBack callBack;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private final SupplierRepository supplierRepository;
    private Boolean isInEditMode = Boolean.FALSE;
    private Supplier mySupplier;

    public SupplierAddController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private boolean makeSupplierr(){
        String name = StringUtils.trimToEmpty(nameTF.getText());
        String email = StringUtils.trimToEmpty(branchTF.getText());
        String address = StringUtils.trimToEmpty(addressTF.getText());
        String phone = StringUtils.trimToEmpty(phoneTF.getText());
        String company = " " ;
          for(int i =0 ; i < phone.length() ; ++ i ){
              if(phone.charAt(i) > '9' ) {
                  AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "ؤقم الهاتف يجب ان يكون ارقام فقط");
                  return false;
              }
          }

        if (name.isEmpty() || address.isEmpty() || email.isEmpty()  || phone.isEmpty() ) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return false;
        }
        mySupplier.setName(name);
        mySupplier.setCity(address);
        mySupplier.setPhone(phone);
        mySupplier.setEmail(email);
        mySupplier.setCompany(company);
        return true;
    }

    @FXML
    private void addSupplier(ActionEvent event) {
        if (!makeSupplierr())return;

        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Supplier savedSupplier = supplierRepository.save(mySupplier);
        callBack.callBack(savedSupplier);
        mySupplier = new Supplier();
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

    public void inflateUI(Supplier supplier) {
        mySupplier = supplier;
        nameTF.setText(supplier.getName());
        addressTF.setText(supplier.getCity());
        phoneTF.setText(supplier.getPhone());
        branchTF.setText(supplier.getEmail());
    }

    private void clearEntries() {
        nameTF.clear();
        addressTF.clear();
        phoneTF.clear();
        branchTF.clear();
    }

    private void handleEditOperation() {
        if(!makeSupplierr())return;
        Supplier savedSupplier = supplierRepository.save(mySupplier);
        callBack.callBack(savedSupplier);
    }
}
