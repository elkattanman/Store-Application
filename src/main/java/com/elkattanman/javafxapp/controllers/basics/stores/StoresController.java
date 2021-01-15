package com.elkattanman.javafxapp.controllers.basics.stores;

import com.elkattanman.javafxapp.controllers.basics.CallBack;
import com.elkattanman.javafxapp.domain.Store;
import com.elkattanman.javafxapp.repositories.StoreRepository;
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
@FxmlView("/FXML/basics/stores/stores.fxml")
public class StoresController  implements Initializable , CallBack<Boolean , Store> {
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        list.setAll(storeRepository.findAll());
        table.setItems(list);
        MakeMyFilter();
    }
    public StoresController(StoreRepository storeRepository){
        this.storeRepository = storeRepository ;
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        FxControllerAndView<StoreAddController, Parent> load = fxWeaver.load(StoreAddController.class);
        StoreAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Store());
        controller.resetEditToAdd();
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void edit(ActionEvent event) {
        Store selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<StoreAddController, Parent> load = fxWeaver.load(StoreAddController.class);
        StoreAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.resetAddToEdit();
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void remove(ActionEvent event){
        Store selectedItem = table.getSelectionModel().getSelectedItem();
        storeRepository.delete(selectedItem);
        list.remove(selectedItem) ;
    }
    @FXML
    void refresh(ActionEvent event){
        list.setAll(storeRepository.findAll());
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

    @Override
    public Boolean callBack(Store object) {

        for (int i=0 ; i < list.size(); ++i) {
            Store store= list.get(i);
            if (store.getId().equals(object.getId())) {
                list.set(i, object);
                return true;
            }
        }

        list.add(object) ;
        return true;
    }
}
