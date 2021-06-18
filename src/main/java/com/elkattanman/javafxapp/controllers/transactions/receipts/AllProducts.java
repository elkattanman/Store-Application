package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.repositories.ProductRepository;
import com.elkattanman.javafxapp.repositories.ReceiptRepository;
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
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

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


    @FXML
    private JFXTextField searchTF;


    public AllProducts(ReceiptRepository receiptRepository, ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(productRepository.findAll());
        table.setItems(list);
        MakeMyFilter() ;
        rowDoubleClick();
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

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
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
