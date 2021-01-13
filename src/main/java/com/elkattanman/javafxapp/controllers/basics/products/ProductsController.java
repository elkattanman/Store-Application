package com.elkattanman.javafxapp.controllers.basics.products;

import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.repositories.ProductRepository;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/basics/products/products.fxml")
public class ProductsController implements Initializable {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    private TableView<Product> table;

    @FXML
    private TableColumn<Product, Integer> idCol;
    @FXML
    private TableColumn<Product, String>  nameCol, typeCol;
    @FXML
    private TableColumn<Product, Double>  priceCol;

    private ObservableList<Product> list = FXCollections.observableArrayList();

    private final ProductRepository productRepository;


    @FXML
    private JFXTextField searchTF;

    public ProductsController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.addAll(productRepository.findAll());
        table.setItems(list);
    }

    public void add(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(null, fxWeaver.loadView(ProductAddController.class));
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }
}
