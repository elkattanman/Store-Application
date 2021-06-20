package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.domain.Store;
import com.elkattanman.javafxapp.domain.StoreHasProduct;
import com.elkattanman.javafxapp.repositories.ProductRepository;
import com.elkattanman.javafxapp.repositories.StoreProductRepository;
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
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
@Component
@FxmlView("/FXML/tranactions/receipt/all_products.fxml")
public class AllProducts implements Initializable {


    @Autowired
    private FxWeaver fxWeaver;

    private CallBack callBack;

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

    private final StoreProductRepository storeProductRepository;

    private Store store;

    @FXML
    private JFXTextField searchTF;


    public AllProducts(ProductRepository productRepository, StoreProductRepository storeProductRepository) {
        this.productRepository = productRepository;
        this.storeProductRepository = storeProductRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(getProducts());
        table.setItems(list);
        MakeMyFilter() ;
        rowDoubleClick();
    }

    private List<Product> getProducts(){
        if(store==null){
            log.info("[All Products Window] getting all products");
            return productRepository.findAll();
        } else{
            log.info("[All Products Window] getting all products in store id :{}",store.getId());
            List<StoreHasProduct> allByStore_id = storeProductRepository.findAllByStore_Id(store.getId());
            return allByStore_id.stream().map(StoreHasProduct::getProduct).collect(Collectors.toList());
        }
    }

    private void rowDoubleClick() {
        table.setRowFactory(tableView -> {
            TableRow<Product> row =new TableRow<>();
            row.setOnMouseClicked(mouseEvent -> {
                if(mouseEvent.getClickCount()==2 && !row.isEmpty()){
                    SelectAction();
                }
            });
            return row;
        });
    }

    public void setCallBack(CallBack callBack, Store store) {
        this.callBack = callBack;
        this.store=store;
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

    }

    private void MakeMyFilter(){
        FilteredList<Product> filteredData = new FilteredList<>(list, p -> true);

        searchTF.textProperty().addListener( (observable, oldValue, newValue) -> {
            filteredData.setPredicate(product->{

                if(newValue == null || newValue.isEmpty() ){
                    return true ;
                }

                String lowerCaseFilter = newValue.toLowerCase() ;
                if(product.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true ;
                }
                return false ;
            });

        });

        SortedList<Product> sortedProducts = new SortedList<>(filteredData) ;
        sortedProducts.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedProducts);

    }

    public void handleRefresh(ActionEvent actionEvent) {
        list.setAll(productRepository.findAll());
    }

    public void SelectAction() {
        Product product=table.getSelectionModel().getSelectedItem();
        callBack.callBack(product);
        getStage().close();
    }
    private Stage getStage() {
        return (Stage) table.getScene().getWindow();
    }

    public void closeStage(ActionEvent actionEvent) {
        getStage().close();
    }
}
