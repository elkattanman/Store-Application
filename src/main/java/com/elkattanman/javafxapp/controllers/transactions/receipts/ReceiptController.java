package com.elkattanman.javafxapp.controllers.transactions.receipts;

import com.elkattanman.javafxapp.DTO.Order;
import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.*;
import com.elkattanman.javafxapp.repositories.DeptInRepository;
import com.elkattanman.javafxapp.repositories.ReceiptRepository;
import com.elkattanman.javafxapp.repositories.StoreProductRepository;
import com.elkattanman.javafxapp.repositories.SupplierRepository;
import com.elkattanman.javafxapp.services.ReportService;
import com.elkattanman.javafxapp.util.AlertMaker;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.elkattanman.javafxapp.util.textfilter.AlphaNumericTextFormatter;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

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

    @Autowired
    private DeptInRepository deptInRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private ReportService reportService;

    @FXML
    private StackPane rootPane;
    @FXML
    private BorderPane mainContainer;

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
        table.setPlaceholder(new Label("لا يوجد منتجات تم شرائها"));
    }

    @Override
    public Boolean callBack(T object) {
        if (object instanceof Product) {
            Product product = (Product) object;
            initProductTF(product);
            productQntTF.requestFocus();
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
        FxControllerAndView<AllReceipts, Parent> load = fxWeaver.load(AllReceipts.class);
        load.getController().setCallBack(this);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
    }

    @FXML
    private void popupStores(MouseEvent event) {
        FxControllerAndView<AllStores, Parent> load = fxWeaver.load(AllStores.class);
        load.getController().setCallBack(this);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
    }

    @FXML
    private void popupSuppliers(MouseEvent event) {
        FxControllerAndView<AllSuppliers, Parent> load = fxWeaver.load(AllSuppliers.class);
        load.getController().setCallBack(this);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
    }

    public void popupProducts(MouseEvent mouseEvent) {
        FxControllerAndView<AllProducts, Parent> load = fxWeaver.load(AllProducts.class);
        load.getController().setCallBack(this, null);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
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
        total=receiptHeader.getTotalPrice();
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
    void payAll() {
        paidTF.setText("" + (int) Double.parseDouble(allTotalTF.getText()));
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
        ReceiptHeader receiptHeader = ReceiptHeader.builder()
                .supplier(mySupplier)
                .store(myStore)
                .time(timeTF.getValue())
                .date(dateTF.getValue())
                .totalPrice(Double.parseDouble(allTotalTF.getText()))
                .paid(Double.parseDouble(paidTF.getText().equals("") ? "0" : paidTF.getText()))
                .remain(Double.parseDouble(remainTF.getText()))
                .build();
        receiptHeader.setReceiptItems(toReceiptItems(receiptHeader));
        ReceiptHeader saved = receiptRepository.save(receiptHeader);
        Set<ReceiptItem> receiptItems = receiptHeader.getReceiptItems();
        receiptItems.stream().forEach(this::addProductInStore);
        if(receiptHeader.getRemain()>0){
            DeptIn deptIn= DeptIn.builder()
                    .description("دين فاتورة شراء رقم"+ receiptHeader.getId())
                    .supplier(receiptHeader.getSupplier())
//                    .dept(receiptHeader.getRemain())
                    .date(receiptHeader.getDate())
                    .build();
            deptIn.addDept(receiptHeader.getRemain());
            mySupplier=supplierRepository.save(deptIn.getSupplier());
            deptInRepository.save(deptIn);
        }
        initReceiptTF(saved);
        AlertMaker.showSimpleAlert("Success", "Save complete");
    }

    private void addProductInStore(ReceiptItem receiptItem){
        ReceiptHeader receiptHeader = receiptItem.getReceiptHeader();
        
    }

    public void enterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            addProduct();
        }
    }
    public void addProduct() {
        try {
            Order order1 = initOrder();
            for (int i = 0; i < list.size(); ++i) {
                Order order = list.get(i);
                if (order.getProductId().equals(order1.getProductId())) {
                    total -= order.getTotal();
                    order.setProductQnt(order.getProductQnt() + order1.getProductQnt());
                    order.setTotal(order.getTotal() + order1.getTotal());
                    total += order.getTotal();
                    setTotalTF();
                    paidTF.clear();
                    list.set(i, order);
                    return;
                }
            }
            total += order1.getTotal();
            setTotalTF();
            paidTF.clear();
            list.add(order1);
        } catch (Exception exception) {
//            AlertMaker.showErrorMessage(exception);
            AlertMaker.showErrorMessage("خطا", "من فضلك ادخل جميع الحقول");
        }
    }

    @FXML
    void deleteRecipt() {
        JFXButton noButton = new JFXButton("NO");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            JFXButton button = new JFXButton("That's Okay");
            AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "Issue Cancelled", null);
        });
        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            if (myReceiptHeader.getId() != null){
                receiptRepository.delete(myReceiptHeader);
                Set<ReceiptItem> receiptItems = myReceiptHeader.getReceiptItems();
                receiptItems.stream().forEach(this::removeProductInStore);
                if(myReceiptHeader.getRemain()>0){
                    try {
                        DeptIn byDescriptionContaining = deptInRepository.findByDescriptionContaining("دين فاتورة شراء رقم" + myReceiptHeader.getId());
                        byDescriptionContaining.getSupplier().subDept(byDescriptionContaining.getDept());
                        mySupplier=supplierRepository.save(byDescriptionContaining.getSupplier());
                        deptInRepository.delete(byDescriptionContaining);
                    } catch (Exception exception) {
                        AlertMaker.showSimpleAlert("خطا", "لا يوجد هذه الديون");
                    }
                }
                myReceiptHeader.setId(null);
                idTF.setText("فاتورة مرتجعه");
                JFXButton button = new JFXButton("That's Okay");
                AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "تم ارتجاع الفاتورة", " يمكنك الان تعديل عليها");
            }else{
                JFXButton button = new JFXButton("That's Okay");
                AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "يرجى اختيار فاتورة للارتجاع", null);
            }
        });

        AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(yesButton, noButton), "Confirm Issue",
                String.format("هل تريد ارتجاع فاتورة برقم %d ?", myReceiptHeader.getId()));
    }

    private void removeProductInStore(ReceiptItem receiptItem){
        ReceiptHeader receiptHeader = receiptItem.getReceiptHeader();
        log.info("delete item with id:{} in store id:{}",receiptItem.getId(),receiptHeader.getStore().getId());
        StoreHasProduct byProductAndStore = storeProductRepository.findByProduct_IdAndStore_Id(receiptItem.getProduct().getId(), receiptHeader.getStore().getId());
        try {
            byProductAndStore.subQuantity(receiptItem.getQuantity());
            storeProductRepository.save(byProductAndStore);
        } catch (Exception exception) {
            log.error("[remove Product Logger] error occurs :{}", exception);
            AlertMaker.showErrorMessage(exception);
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
                .totalPrice(0)
                .build();
        initReceiptTF(build);
    }

    @FXML
    public void print() {
//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        directoryChooser.setTitle("Select Location to Create Backup");
//        File selectedDirectory = directoryChooser.showDialog(getStage());
//        if (selectedDirectory == null) {
//            AlertMaker.showErrorMessage("Export cancelled", "No Valid Directory Found");
//        } else {
        if(myReceiptHeader.getId()==null){
            JFXButton button = new JFXButton("That's Okay");
            AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "يرجى اختيار او حفظ فاتورة للطباعة", null);
            return;
        }
            try {
                reportService.exportReport("buyReceipt","receiptHeader", myReceiptHeader, myReceiptHeader.getReceiptItems());
//                pdfGenaratorUtil.createPdf("index", Map.of("receipt",myReceiptHeader), selectedDirectory);
            } catch (Exception exception) {
                log.error("[Generate PDF] Error occur : {}",exception);
                AlertMaker.showErrorMessage(exception);
            }
//        }

    }

    @FXML
    void removeOrder() {
        Order selectedItem = table.getSelectionModel().getSelectedItem();
        total-=selectedItem.getTotal();
        setTotalTF();
        list.remove(selectedItem);
    }

    private void setTotalTF() {
        allTotalTF.setText("" + total);
        remainTF.setText("" + total);
        paidTF.setText("");
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
