package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.domain.Store;
import com.elkattanman.javafxapp.repositories.StoreRepository;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
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
@FxmlView("/FXML/tranactions/receipt/all_stores.fxml")
public class AllStores implements Initializable {

    @Autowired
    private FxWeaver fxWeaver;
    @FXML
    TableView<Store> table ;
    @FXML
    private TableColumn<Store, Integer> idCol;
    @FXML
    private TableColumn<Store, String>  nameCol, addressCol , phoneCol , branchCol;
    @FXML
    private JFXTextField searchTF;

    private ObservableList<Store> list = FXCollections.observableArrayList();

    private final StoreRepository storeRepository ;

    private CallBack callBack;

    public AllStores(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(storeRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    private void MakeMyFilter(){
        FilteredList<Store> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener( (observable, oldValue, newValue) -> {
            filteredData.setPredicate(store->{

                if(newValue == null || newValue.isEmpty() ){
                    return true ;
                }
                String lowerCaseFilter = newValue.toLowerCase() ;
                if(store.getName().toLowerCase().indexOf(lowerCaseFilter) != -1 ){
                    return true ;
                }
                return false ;
            });

        });

        SortedList<Store> sortedStores = new SortedList<>(filteredData) ;
        sortedStores.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedStores);

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        branchCol.setCellValueFactory(new PropertyValueFactory<>("branch"));
    }

    public void handleRefresh(ActionEvent actionEvent) {
        list.setAll(storeRepository.findAll());
    }

    private Stage getStage() {
        return (Stage) table.getScene().getWindow();
    }

    public void closeStage() {
        getStage().close();
    }

    public void SelectAction(ActionEvent actionEvent) {
        Store store=table.getSelectionModel().getSelectedItem();
        callBack.callBack(store);
        getStage().close();
    }
}
