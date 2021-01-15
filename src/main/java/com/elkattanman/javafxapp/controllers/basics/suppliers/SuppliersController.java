package com.elkattanman.javafxapp.controllers.basics.suppliers;

import com.elkattanman.javafxapp.controllers.basics.CallBack;
import com.elkattanman.javafxapp.domain.Supplier;
import com.elkattanman.javafxapp.repositories.SupplierRepository;
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
@FxmlView("/FXML/basics/suppliers/supplier.fxml")
public class SuppliersController implements Initializable , CallBack<Boolean , Supplier> {
    @Autowired
    private FxWeaver fxWeaver;

    @FXML
    TableView<Supplier> table ;
    @FXML
    private TableColumn<Supplier, Integer> idCol;
    @FXML
    private TableColumn<Supplier, String>  nameCol, cityCol , phoneCol , emailCol , companyCol;
    @FXML
    private JFXTextField searchTF;

    private ObservableList<Supplier> list = FXCollections.observableArrayList();
    private final SupplierRepository supplierRepository ;

    public SuppliersController(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     initCol();
     list.setAll(supplierRepository.findAll() );
     table.setItems(list);
     MakeMyFilter();
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        FxControllerAndView<SupplierAddController, Parent> load = fxWeaver.load(SupplierAddController.class);
        SupplierAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Supplier());
        controller.resetEditToAdd();
        AssistantUtil.loadWindow(null, load.getView().get());
    }
    @FXML
    void edit(ActionEvent event) {
        Supplier selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<SupplierAddController, Parent> load = fxWeaver.load(SupplierAddController.class);
        SupplierAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        controller.resetAddToEdit();
        AssistantUtil.loadWindow(null, load.getView().get());
    }
    @FXML
    void remove(ActionEvent event){
        Supplier selectedItem = table.getSelectionModel().getSelectedItem();
        supplierRepository.delete(selectedItem);
        list.remove(selectedItem) ;
}
    @FXML
    void refresh(ActionEvent event){
        list.setAll(supplierRepository.findAll());
    }

    private void MakeMyFilter() {
        FilteredList<Supplier> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (supplier.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                return false;
            });

        });

        SortedList<Supplier> sortedsuppliers = new SortedList<>(filteredData) ;
        sortedsuppliers.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedsuppliers);

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        companyCol.setCellValueFactory(new PropertyValueFactory<>("company"));

    }


    @Override
    public Boolean callBack(Supplier object) {

        for (int i=0 ; i < list.size(); ++i) {
            Supplier supllier = list.get(i);
            if (supllier.getId().equals(object.getId())) {
                list.set(i, object);
                return true;
            }
        }

        list.add(object) ;
        return true;
    }
}
