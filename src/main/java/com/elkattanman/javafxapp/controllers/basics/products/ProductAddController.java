package com.elkattanman.javafxapp.controllers.basics.products;


import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.repositories.ProductRepository;
import com.elkattanman.javafxapp.util.AlertMaker;
import com.jfoenix.controls.JFXButton;
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
@FxmlView("/FXML/basics/products/add_product.fxml")
public class ProductAddController implements Initializable {

    @FXML
    private JFXTextField nameTF, typeTF, priceTF;

    private CallBack callBack;

    @FXML
    private JFXButton saveButton, cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private final ProductRepository productRepository;

    private Boolean isInEditMode = Boolean.FALSE;

    public ProductAddController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private Product myProduct;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    //        return Product.builder().name(name).type(type).price(price).build();


    private boolean makeProduct(){
        String name=StringUtils.trimToEmpty(nameTF.getText());
        String type=StringUtils.trimToEmpty(typeTF.getText());
        String priceString=StringUtils.trimToEmpty(priceTF.getText());
        double price=0;
        try {
            price=Double.parseDouble(priceString.isEmpty()?"0":priceString);
        }catch (Exception ex){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل رقم  فى السعر");
            return false;
        }
        if (name.isEmpty() || type.isEmpty() || priceString.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return false;
        }
        myProduct.setName(name);
        myProduct.setType(type);
        myProduct.setPrice(price);
        return true;
    }

    @FXML
    private void addProduct(ActionEvent event) {
        if (!makeProduct())return;

        if (isInEditMode) {
            handleEditOperation();
            return;
        }

        Product savedProduct = productRepository.save(myProduct);
        callBack.callBack(savedProduct);
        myProduct = new Product() ;
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
    public void inflateUI(Product product) {
        myProduct=product;
        nameTF.setText(product.getName());
        typeTF.setText(product.getType());
        priceTF.setText(""+product.getPrice());
    }

    private void clearEntries() {
        nameTF.clear();
        typeTF.clear();
        priceTF.clear();
    }

    private void handleEditOperation() {
        if(!makeProduct())return;
        Product savedProduct = productRepository.save(myProduct);
        callBack.callBack(savedProduct);
        Stage stage= (Stage) rootPane.getScene().getWindow();
        stage.close();
    }
}
