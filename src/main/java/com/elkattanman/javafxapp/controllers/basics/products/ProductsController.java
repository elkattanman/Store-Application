package com.elkattanman.javafxapp.controllers.basics.products;

import com.elkattanman.javafxapp.controllers.basics.CallBack;
import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.repositories.ProductRepository;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@FxmlView("/FXML/basics/products/products.fxml")
public class ProductsController implements Initializable, CallBack<Boolean, Product> {
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
        list.setAll(productRepository.findAll());
        table.setItems(list);
    }

    @FXML
    public void add(ActionEvent actionEvent) {
        FxControllerAndView<ProductAddController, Parent> load = fxWeaver.load(ProductAddController.class);
        ProductAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(new Product());
        AssistantUtil.loadWindow(null, load.getView().get());
    }
    @FXML
    void edit(ActionEvent event) {
        Product selectedItem = table.getSelectionModel().getSelectedItem();
        FxControllerAndView<ProductAddController, Parent> load = fxWeaver.load(ProductAddController.class);
        ProductAddController controller = load.getController();
        controller.setCallBack(this);
        controller.inflateUI(selectedItem);
        AssistantUtil.loadWindow(null, load.getView().get());
    }

    @FXML
    void refresh(ActionEvent event) {

    }

    @FXML
    void remove(ActionEvent event) {

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        FilteredList<Product> filteredData = new FilteredList<>(list, p -> true);

        searchTF.textProperty().addListener( (observable, oldValue, newValue) -> {
//            filteredData.setPredicate((TreeItem<Product> t) -> t.getValue().getName().contains(newValue) );
//            table.s
        });
    }


    @Override
    public Boolean callBack(Product object) {
        for (int i=0 ; i < list.size(); ++i) {
            Product product= list.get(i);
            if (product.getId().equals(object.getId())) {
                list.set(i, object);
                return true;
            }
        }
        list.add(object);
        return true;
    }

}
