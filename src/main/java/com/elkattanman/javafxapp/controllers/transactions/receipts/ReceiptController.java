package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.DTO.Order;
import com.elkattanman.javafxapp.DTO.ReceiptDTO;
import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.*;
import com.elkattanman.javafxapp.repositories.ReceiptRepository;
import com.elkattanman.javafxapp.repositories.StoreProductRepository;
import com.elkattanman.javafxapp.util.AlertMaker;
import com.elkattanman.javafxapp.util.textfilter.AlphaNumericTextFormatter;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Component
@Slf4j
@FxmlView("/FXML/tranactions/receipt/receipt.fxml")
public class ReceiptController<T> implements Initializable, CallBack<Boolean, T> {

    @Autowired
    private FxWeaver fxWeaver;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @FXML
    private JFXTextField supplierIdTF, supplierNameTF, supplierPhoneTF, storeIdTF, idTF, storeNameTF,
            productIdTF, productNameTF, productTypeTF, productPriceTF, productQntTF, totalTF, allTotalTF, paidTF, remainTF;

    @FXML
    private JFXDatePicker dateTF;
    @FXML
    private JFXTimePicker timeTF;

    @FXML
    private TableColumn<Order, Integer> productIdCol, productQntCol;

    @FXML
    private TableColumn<Order, String> productNameCol, productTypeCol;

    @FXML
    private TableColumn<Order, Double> productPriceCol, totalCol;

    @FXML
    private TableView<Order> table;

    private ObservableList<Order> list = FXCollections.observableArrayList();


    //    private Product myProduct;
    private ReceiptHeader myReceiptHeader;
    private Supplier mySupplier;
    private Store myStore;
    private double total;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newRecipt();
        productQntTF.setTextFormatter(new AlphaNumericTextFormatter(4));
        paidTF.setTextFormatter(new AlphaNumericTextFormatter(7));
        productQntTF.textProperty().addListener((observableValue, old, nw) -> {
            double total = Double.parseDouble(productPriceTF.getText()) * Integer.parseInt(nw.equals("") ? "0" : nw);
            totalTF.setText("" + total);
        });
        paidTF.textProperty().addListener((observableValue, old, nw) -> {
            double remain = Double.parseDouble(allTotalTF.getText()) - Double.parseDouble(nw.equals("") ? "0" : nw);
            remainTF.setText("" + remain);
        });
        initCol();
        table.setItems(list);
    }

    private void initCol() {
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productTypeCol.setCellValueFactory(new PropertyValueFactory<>("productType"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("productPrice"));
        productQntCol.setCellValueFactory(new PropertyValueFactory<>("productQnt"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    @Override
    public Boolean callBack(T object) {
        if (object instanceof Product) {
            Product product = (Product) object;
            initProductTF(product);
        } else if (object instanceof Store) {
            Store store = (Store) object;
            initStoreTF(store);
        } else if (object instanceof Supplier) {
            Supplier supplier = (Supplier) object;
            initSupplierTF(supplier);
        } else if (object instanceof ReceiptHeader) {
            ReceiptHeader receipt = (ReceiptHeader) object;
            initReceiptTF(receipt);
        } else {
            AlertMaker.showErrorMessage(new Exception("Can't convert this object"));
        }
        return true;
    }

    @FXML
    private void popupRecipts(MouseEvent event) {
        AllReceipts allReceipts = fxWeaver.loadController(AllReceipts.class);
        allReceipts.setCallBack(this);
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
        AllSuppliers allSuppliers = fxWeaver.loadController(AllSuppliers.class);
        allSuppliers.setCallBack(this);
        AssistantUtil.loadWindow(null, fxWeaver.loadView(AllSuppliers.class));
    }

    public void popupProducts(MouseEvent mouseEvent) {
        AllProducts allProducts = fxWeaver.loadController(AllProducts.class);
        allProducts.setCallBack(this);
        AssistantUtil.loadWindow(null, fxWeaver.loadView(AllProducts.class));
    }

    private Order initOrder() {
        return Order.builder()
                .productId(Integer.parseInt(productIdTF.getText()))
                .productName(productNameTF.getText())
                .productType(productTypeTF.getText())
                .productPrice(Double.parseDouble(productPriceTF.getText()))
                .productQnt(Integer.parseInt(productQntTF.getText()))
                .total(Double.parseDouble(totalTF.getText()))
                .build();
    }

    private void initReceiptTF(ReceiptHeader receiptHeader) {
        myReceiptHeader = receiptHeader;
        initSupplierTF(receiptHeader.getSupplier());
        initProductTF(new Product());
        initTable(receiptHeader.getReceiptItems());
        initStoreTF(receiptHeader.getStore());
        idTF.setText(receiptHeader.getId() == null ? "جديد" : getIntValue(receiptHeader.getId()));
        dateTF.setValue(receiptHeader.getDate());
        timeTF.setValue(receiptHeader.getTime());
        allTotalTF.setText("" + receiptHeader.getTotalPrice());
        remainTF.setText("" + (int) receiptHeader.getRemain());
        paidTF.setText("" + (int) receiptHeader.getPaid());
    }

    private void initTable(Set<ReceiptItem> receiptItems) {
        list.clear();
        receiptItems.forEach(receiptItem -> list.add(toOrder(receiptItem)));
    }

    private void initProductTF(Product product) {
        productIdTF.setText(getIntValue(product.getId()));
        productNameTF.setText(product.getName());
        productPriceTF.setText("" + product.getPrice());
        productQntTF.setText("1");
        totalTF.setText("" + product.getPrice());
        productTypeTF.setText(product.getType());
    }

    private void initSupplierTF(Supplier supplier) {
        mySupplier = supplier;
        supplierIdTF.setText(getIntValue(supplier.getId()));
        supplierNameTF.setText(supplier.getName());
        supplierPhoneTF.setText(supplier.getPhone());
    }

    private void initStoreTF(Store store) {
        myStore = store;
        storeIdTF.setText(getIntValue(store.getId()));
        storeNameTF.setText(store.getName());
    }

    private String getIntValue(Integer n) {
        return n == null ? "" : "" + n;
    }

    @FXML
    void payAll(MouseEvent event) {
        paidTF.setText("" + (int) Double.parseDouble(allTotalTF.getText()));
    }

    public void addProduct(ActionEvent actionEvent) {
        try {
            Order order1 = initOrder();
            for (int i = 0; i < list.size(); ++i) {
                Order order = list.get(i);
                if (order.getProductId().equals(order1.getProductId())) {
                    total -= order.getTotal();
                    order.setProductQnt(order.getProductQnt() + order1.getProductQnt());
                    order.setTotal(order.getTotal() + order1.getTotal());
                    total += order.getTotal();
                    allTotalTF.setText("" + total);
                    remainTF.setText("" + total);
                    paidTF.clear();
                    list.set(i, order);
                    return;
                }
            }
            total += order1.getTotal();
            allTotalTF.setText("" + total);
            remainTF.setText("" + total);
            paidTF.clear();
            list.add(order1);
        } catch (Exception exception) {
//            AlertMaker.showErrorMessage(exception);
            AlertMaker.showErrorMessage("خطا", "من فضلك ادخل جميع الحقول");
        }
    }

    @FXML
    void save() {
        if(myReceiptHeader.getId()!=null) {
            AlertMaker.showErrorMessage("خطا", "لا يمكن تعديل الفاتورة");
            return;
        }

        if (myStore.getId() == null || mySupplier.getId() == null) {
            AlertMaker.showErrorMessage("خطا", "برجاء ادخال جميع البيانات");
            return;
        }
        if (list.isEmpty()) {
            AlertMaker.showErrorMessage("خطا", "برجاء ادخال منتج واحد على الاقل");
            return;
        }
        ReceiptHeader receiptHeader = new ReceiptHeader();
        receiptHeader.setSupplier(mySupplier);
        receiptHeader.setStore(myStore);
        receiptHeader.setTime(LocalTime.now());
        receiptHeader.setDate(LocalDate.now());
        receiptHeader.setTotalPrice(Double.parseDouble(allTotalTF.getText()));
        receiptHeader.setPaid(Double.parseDouble(paidTF.getText().equals("") ? "0" : paidTF.getText()));
        receiptHeader.setRemain(Double.parseDouble(remainTF.getText()));
        receiptHeader.setReceiptItems(toReceiptItems(receiptHeader));
        ReceiptHeader saved = receiptRepository.save(receiptHeader);
        initReceiptTF(saved);
        AlertMaker.showSimpleAlert("Sucess", "Save complete");
    }

    @FXML
    void deleteRecipt() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("هل تريد ارتجاع هذه الفاتورة؟");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            if (myReceiptHeader.getId() != null) {
                try {
                    for (ReceiptItem receiptItem : myReceiptHeader.getReceiptItems()) {
                        StoreHasProduct byProductAndStore = storeProductRepository.findByProductAndStore(receiptItem.getProduct(), myReceiptHeader.getStore());
                        byProductAndStore.subQuantity(receiptItem.getQuantity());
                        storeProductRepository.save(byProductAndStore);
                    }
                } catch (Exception exception) {
                    AlertMaker.showErrorMessage(exception);
                }
                receiptRepository.delete(myReceiptHeader);
                AlertMaker.showSimpleAlert("مهمه ناجحه", "تم الحذف بنجاح");

            }
        } else {
            // ... user chose CANCEL or closed the dialog
        }
    }

    @FXML
    void newRecipt() {
        ReceiptHeader build = ReceiptHeader.builder()
                .store(new Store())
                .supplier(new Supplier())
                .receiptItems(new HashSet<>())
                .time(LocalTime.now())
                .date(LocalDate.now())
                .build();
        initReceiptTF(build);
    }


    @FXML
    void removeOrder() {
        Order selectedItem = table.getSelectionModel().getSelectedItem();
        list.remove(selectedItem);
    }

    private Set<ReceiptItem> toReceiptItems(ReceiptHeader receiptHeader) {
        Set<ReceiptItem> ret = new HashSet<>();
        list.forEach(order -> {
            ret.add(toReceiptItem(order, receiptHeader));
        });
        return ret;
    }

    private ReceiptItem toReceiptItem(Order order, ReceiptHeader receiptHeader) {
        ReceiptItem receiptItem = ReceiptItem.builder()
                .product(new Product(order.getProductId(), order.getProductName(), order.getProductType(), order.getProductPrice()))
                .quantity(order.getProductQnt())
                .receiptHeader(receiptHeader)
                .build();
        StoreHasProduct byProductAndStore = storeProductRepository.findByProductAndStore(receiptItem.getProduct(), receiptHeader.getStore());
        if (byProductAndStore == null) {
            byProductAndStore = StoreHasProduct.builder()
                    .storeHasProductID(StoreHasProductID.builder().productId(receiptItem.getProduct().getId()).storeId(receiptHeader.getStore().getId()).build())
                    .product(receiptItem.getProduct())
                    .store(receiptHeader.getStore())
                    .quantity(order.getProductQnt())
                    .build();
        } else {
            byProductAndStore.addQuantity(order.getProductQnt());
        }
        storeProductRepository.save(byProductAndStore);
        return receiptItem;
    }

    private Order toOrder(ReceiptItem receiptItem) {
        return Order.builder()
                .productId(receiptItem.getProduct().getId())
                .productName(receiptItem.getProduct().getName())
                .productType(receiptItem.getProduct().getType())
                .productPrice(receiptItem.getProduct().getPrice())
                .productQnt(receiptItem.getQuantity())
                .total(receiptItem.anTotal())
                .build();
    }

    private Stage getStage() {
        return (Stage) table.getScene().getWindow();
    }


}
