package com.elkattanman.javafxapp.controllers.basics.stores;


import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Store;
import com.elkattanman.javafxapp.repositories.StoreRepository;
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
@FxmlView("/FXML/basics/stores/add_store.fxml")
public class StoreAddController implements Initializable {

    @FXML
    private JFXTextField nameTF, addressTF, phoneTF , branchTF;
    private CallBack callBack;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private final StoreRepository storeRepository;
    private Boolean isInEditMode = Boolean.FALSE;
    private Store myStore;

    public StoreAddController(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }


    private boolean makeStore(){
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
        myStore.setName(name);
        myStore.setAddress(address);
        myStore.setPhone(phone);
        myStore.setBranch(branch);
        return true;
    }

    @FXML
    private void addStore(ActionEvent event) {
        if (!makeStore())return;

        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Store savedStore = storeRepository.save(myStore);
        callBack.callBack(savedStore);
        myStore = new Store();
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

    public void inflateUI(Store store) {
        myStore=store;
        nameTF.setText(store.getName());
        addressTF.setText(store.getAddress());
        phoneTF.setText(store.getPhone());
        branchTF.setText(store.getBranch());
    }

    private void clearEntries() {
        nameTF.clear();
        addressTF.clear();
        phoneTF.clear();
        branchTF.clear();
    }

    private void handleEditOperation() {
        if(!makeStore())return;
        Store savedStore = storeRepository.save(myStore);
        callBack.callBack(savedStore);
    }
}
