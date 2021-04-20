package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.domain.ReceiptHeader;
import com.elkattanman.javafxapp.domain.Store;
import com.elkattanman.javafxapp.domain.Supplier;
import com.elkattanman.javafxapp.util.AlertMaker;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/tranactions/receipt/receipt.fxml")
public class ReceiptController<T> implements Initializable, CallBack<Boolean, T> {
    @Autowired
    private FxWeaver fxWeaver;


    @FXML
    private JFXTextField supplierIdTF, supplierNameTF, supplierPhoneTF, storeIdTF, idTF, storeNameTF,
            productIdTF, productNameTF, productTypeTF, productPriceTF, productQntTF, totalTF;

    @FXML
    private JFXDatePicker dateTF;
    @FXML
    private JFXTimePicker timeTF;

    private Product myProduct;
    private Supplier mySupplier;
    private Store myStore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateTF.setValue(LocalDate.now());
        timeTF.setValue(LocalTime.now());
        initStoreTF(new Store());
        initProductTF(new Product());
        initSupplierTF(new Supplier());
    }

    @Override
    public Boolean callBack(T object) {
        if(object instanceof Product){
            Product product= (Product) object;
            initProductTF(product);
        }else if(object instanceof Store){
            Store store=(Store) object;
            initStoreTF(store);
        }else if(object instanceof Supplier){
            Supplier supplier=(Supplier) object;
            initSupplierTF(supplier);
        }else if(object instanceof ReceiptHeader){
            ReceiptHeader receipt= (ReceiptHeader) object;
        }else{
            AlertMaker.showErrorMessage(new Exception("Can't convert this object"));
        }
        return true;
    }

    public void addProduct(ActionEvent actionEvent) {

    }
    @FXML
    private void popupRecipts(MouseEvent event) {
        AssistantUtil.loadWindow(null, fxWeaver.loadView(AllReceipts.class));
    }

    @FXML
    private void popupStores(MouseEvent event) {
        AllStores allStores = fxWeaver.loadController(AllStores.class);
        allStores.setCallBack(this);
        AssistantUtil.loadWindow(null, fxWeaver.loadView(AllStores.class));
    }

    @FXML
    private void popupSuppliers(MouseEvent event) {
        AllSuppliers allSuppliers=fxWeaver.loadController(AllSuppliers.class);
        allSuppliers.setCallBack(this);
        AssistantUtil.loadWindow(null, fxWeaver.loadView(AllSuppliers.class));
    }

    public void popupProducts(MouseEvent mouseEvent) {
        AllProducts allProducts = fxWeaver.loadController(AllProducts.class);
        allProducts.setCallBack(this);
        AssistantUtil.loadWindow(null, fxWeaver.loadView(AllProducts.class));
    }


    private void initReceiptTF(ReceiptHeader receiptHeader){
        initSupplierTF(receiptHeader.getSupplier());
//        initTable(receiptHeader.getReceiptItems().);
        initStoreTF(receiptHeader.getStore());
        idTF.setText(""+receiptHeader.getId());
    }
    private void initProductTF(Product product){
        myProduct=product;
        productIdTF.setText(getIntValue(product.getId()));
        productNameTF.setText(product.getName());
        productPriceTF.setText(""+product.getPrice());
        productTypeTF.setText(product.getType());
    }
    private void initSupplierTF(Supplier supplier){
        mySupplier=supplier;
        supplierIdTF.setText(getIntValue(supplier.getId()));
        supplierNameTF.setText(supplier.getName());
        supplierPhoneTF.setText(supplier.getPhone());
    }
    private void initStoreTF(Store store){
        myStore=store;
        storeIdTF.setText(getIntValue(store.getId()));
        storeNameTF.setText(store.getName());
    }

    private String getIntValue(Integer n){
        return n==null?"":""+n;
    }
}
