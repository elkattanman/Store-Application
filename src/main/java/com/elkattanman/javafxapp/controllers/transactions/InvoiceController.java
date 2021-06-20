package com.elkattanman.javafxapp.controllers.transactions;

import com.elkattanman.javafxapp.DTO.Order;
import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.controllers.transactions.receipts.*;
import com.elkattanman.javafxapp.domain.*;
import com.elkattanman.javafxapp.repositories.CustomerRepository;
import com.elkattanman.javafxapp.repositories.DeptOutRepository;
import com.elkattanman.javafxapp.repositories.InvoiceRepository;
import com.elkattanman.javafxapp.repositories.StoreProductRepository;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import java.util.*;

@Component
@Slf4j
@FxmlView("/FXML/tranactions/invoice.fxml")
public class InvoiceController<T> implements Initializable, CallBack<Boolean, T> {

    @Autowired
    private FxWeaver fxWeaver;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DeptOutRepository deptOutRepository;

    @Autowired
    private StoreProductRepository storeProductRepository;

    @Autowired
    private ReportService reportService;

    @FXML private StackPane rootPane;
    @FXML public BorderPane mainContainer;

    @FXML
    private JFXTextField customerIdTF, customerNameTF, customerPhoneTF,storeIdTF, idTF, storeNameTF, productIdTF,
            productNameTF, productTypeTF, productPriceTF, qntStoreTF, productQntTF, totalTF, allTotalTF, paidTF, remainTF;
    @FXML
    private JFXDatePicker dateTF;

    @FXML
    private JFXTimePicker timeTF;

    @FXML
    private TableView<Order> table;

    @FXML
    private TableColumn<Order, Integer> productIdCol, productQntCol;

    @FXML
    private TableColumn<Order, String> productNameCol, productTypeCol;

    @FXML
    private TableColumn<Order, Double> productPriceCol, totalCol;

    private ObservableList<Order> list = FXCollections.observableArrayList();


    private Product myProduct;
    private InvoiceHeader myInvoiceHeader;
    private Customer myCustomer;
    private Store myStore;
    private double total;
    private int qntStore;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newInvoice();
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
        table.setPlaceholder(new Label("لا يوجد منتجات تم بيعها"));
    }
    @Override
    public Boolean callBack(T object) {
        if (object instanceof Product) {
            Product product = (Product) object;
            initProductTF(product);
            productQntTF.requestFocus();
            StoreHasProduct byProduct_idAndStore_id = storeProductRepository.findByProduct_IdAndStore_Id(product.getId(), myStore.getId());
            qntStore=byProduct_idAndStore_id.getQuantity();
            setQntStoreTF();
        } else if (object instanceof Store) {
            Store store = (Store) object;
            initStoreTF(store);
        } else if (object instanceof Customer) {
            Customer customer= (Customer) object;
            initCustomerTF(customer);
        } else if (object instanceof InvoiceHeader) {
            InvoiceHeader invoice = (InvoiceHeader) object;
            initInvoicetTF(invoice);
        } else {
            AlertMaker.showErrorMessage(new Exception("Can't convert this object"));
        }
        return true;
    }

    private void initInvoicetTF(InvoiceHeader invoice) {
        myInvoiceHeader= invoice;
        initCustomerTF(invoice.getCustomer());
        initProductTF(new Product());
        initTable(invoice.getInvoiceItems());
        initStoreTF(invoice.getStore());
        idTF.setText(invoice.getId() == null ? "جديد" : getIntValue(invoice.getId()));
        dateTF.setValue(invoice.getDate());
        timeTF.setValue(invoice.getTime());
        allTotalTF.setText("" + invoice.getTotalPrice());
        total=invoice.getTotalPrice();
        remainTF.setText("" + (int) invoice.getRemain());
        paidTF.setText("" + (int) invoice.getPaid());
    }

    private void initTable(Set<InvoiceItem> invoiceItems) {
        list.clear();
        invoiceItems.forEach(invoiceItem -> list.add(toOrder(invoiceItem)));
    }

    private void initCustomerTF(Customer customer) {
        myCustomer=customer;
        customerIdTF.setText(getIntValue(customer.getId()));
        customerNameTF.setText(customer.getName());
        customerPhoneTF.setText(customer.getPhone());
    }

    private void initStoreTF(Store store) {
        myStore = store;
        storeIdTF.setText(getIntValue(store.getId()));
        storeNameTF.setText(store.getName());
    }

    private void initProductTF(Product product) {
        myProduct=product;
        productIdTF.setText(getIntValue(product.getId()));
        productNameTF.setText(product.getName());
        productPriceTF.setText("" + product.getPrice());
        productQntTF.setText("1");
        totalTF.setText("" + product.getPrice());
        productTypeTF.setText(product.getType());
    }
    private String getIntValue(Integer n) {
        return n == null ? "" : "" + n;
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

    private Order toOrder(InvoiceItem invoiceItem) {
        return Order.builder()
                .productId(invoiceItem.getProduct().getId())
                .productName(invoiceItem.getProduct().getName())
                .productType(invoiceItem.getProduct().getType())
                .productPrice(invoiceItem.getProduct().getPrice())
                .productQnt(invoiceItem.getQuantity())
                .total(invoiceItem.anTotal())
                .build();
    }

    @FXML
    public void newInvoice() {
        InvoiceHeader build = InvoiceHeader.builder()
                .store(new Store())
                .customer(new Customer())
                .invoiceItems(new HashSet<>())
                .time(LocalTime.now())
                .date(LocalDate.now())
                .totalPrice(0)
                .build();
        initInvoicetTF(build);
    }
    @FXML
    public void deleteRecipt() {
        JFXButton noButton = new JFXButton("NO");
        noButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            JFXButton button = new JFXButton("That's Okay");
            AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "Issue Cancelled", null);
        });
        JFXButton yesButton = new JFXButton("YES");
        yesButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
            if(myInvoiceHeader.getId() != null){
                invoiceRepository.delete(myInvoiceHeader);
                Set<InvoiceItem> invoiceItems = myInvoiceHeader.getInvoiceItems();
                invoiceItems.stream().forEach(this::returnProductToStore);
                if(myInvoiceHeader.getRemain()>0){
                    try {
                        DeptOut byDescriptionContaining = deptOutRepository.findByDescriptionContaining("دين فاتورة بيع رقم " + myInvoiceHeader.getId());
                        byDescriptionContaining.getCustomer().subDept(byDescriptionContaining.getDept());
                        myCustomer=customerRepository.save(byDescriptionContaining.getCustomer());
                        deptOutRepository.delete(byDescriptionContaining);
                    } catch (Exception exception) {
                        AlertMaker.showSimpleAlert("خطا", "لا يوجد هذه الديون");
                    }
                }
                myInvoiceHeader.setId(null);
                idTF.setText("فاتورة مرتجعه");
                JFXButton button = new JFXButton("That's Okay");
                AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "تم ارتجاع الفاتورة", " يمكنك الان تعديل عليها");
            }else{
                JFXButton button = new JFXButton("That's Okay");
                AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "يرجى اختيار فاتورة للارتجاع", null);
            }
        });
        AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(yesButton, noButton), "Confirm Issue",
                String.format("هل تريد ارتجاع فاتورة برقم %d ?", myInvoiceHeader.getId()));
    }
    private void returnProductToStore(InvoiceItem invoiceItem){
        InvoiceHeader invoiceHeader = invoiceItem.getInvoiceHeader();
        log.info("Saving item with id:{} in store id:{}",invoiceItem.getProduct().getId(),invoiceHeader.getStore().getId());
        StoreHasProduct byProductAndStore = storeProductRepository.findByProduct_IdAndStore_Id(invoiceItem.getProduct().getId(), invoiceHeader.getStore().getId());
        byProductAndStore.addQuantity(invoiceItem.getQuantity());
        storeProductRepository.save(byProductAndStore);
    }
    @FXML
    public void popupCustomers() {
        FxControllerAndView<AllCustomers, Parent> load = fxWeaver.load(AllCustomers.class);
        load.getController().setCallBack(this);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
    }
    @FXML
    public void popupStores() {
        FxControllerAndView<AllStores, Parent> load = fxWeaver.load(AllStores.class);
        load.getController().setCallBack(this);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
    }
    @FXML
    public void popupRecipts() {
        FxControllerAndView<AllInvoices, Parent> load = fxWeaver.load(AllInvoices.class);
        load.getController().setCallBack(this);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
    }
    @FXML
    public void popupProducts() {
        if(myStore.getId()==null){
            JFXButton button = new JFXButton("That's Okay");
            AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "يجب اختيار مخزن لعرض المنتجات", null);
            return;
        }
        FxControllerAndView<AllProducts, Parent> load = fxWeaver.load(AllProducts.class);
        load.getController().setCallBack(this, myStore);
        AssistantUtil.loadInternalWindow(getStage(), load.getView().get(), Modality.WINDOW_MODAL);
    }

    @FXML
    public void enterPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            addProduct();
        }
    }

    @FXML
    public void addProduct() {
        try {
            Order order1 = initOrder();
            for (int i = 0; i < list.size(); ++i) {
                Order order = list.get(i);
                if (order.getProductId().equals(order1.getProductId())) {
                    if(order.getProductQnt()+order1.getProductQnt()>qntStore){
                        AlertMaker.showErrorMessage("خطا", "الكمية المطلوبة غير كافيه فى المخزن");
                        return;
                    }
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
            if(order1.getProductQnt()>qntStore){
                AlertMaker.showErrorMessage("خطا", "الكمية المطلوبة غير كافيه فى المخزن");
                return;
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
    private void setTotalTF() {
        allTotalTF.setText("" + total);
        remainTF.setText("" + total);
        paidTF.setText("");
    }

    private void setQntStoreTF(){
        qntStoreTF.setText(""+qntStore);
    }

    @FXML
    public void removeOrder() {
        Order selectedItem = table.getSelectionModel().getSelectedItem();
        total-=selectedItem.getTotal();
        setTotalTF();
        list.remove(selectedItem);
    }

    @FXML
    void payAll() {
        paidTF.setText("" + (int) Double.parseDouble(allTotalTF.getText()));
    }

    public void save() {
        if(myInvoiceHeader.getId()!=null) {
            AlertMaker.showErrorMessage("خطا", "لا يمكن تعديل الفاتورة");
            return;
        }

        if (myStore.getId() == null || myCustomer.getId() == null) {
            AlertMaker.showErrorMessage("خطا", "برجاء ادخال جميع البيانات");
            return;
        }
        if (list.isEmpty()) {
            AlertMaker.showErrorMessage("خطا", "برجاء ادخال منتج واحد على الاقل");
            return;
        }
        
        InvoiceHeader invoiceHeader= InvoiceHeader.builder()
                .customer(myCustomer)
                .store(myStore)
                .time(timeTF.getValue())
                .date(dateTF.getValue())
                .totalPrice(Double.parseDouble(allTotalTF.getText()))
                .paid(Double.parseDouble(paidTF.getText().equals("") ? "0" : paidTF.getText()))
                .remain(Double.parseDouble(remainTF.getText()))
                .build();
        invoiceHeader.setInvoiceItems(toInvoiceItems(invoiceHeader));
        InvoiceHeader saved = invoiceRepository.save(invoiceHeader);
        Set<InvoiceItem> invoiceItems=invoiceHeader.getInvoiceItems();
        invoiceItems.stream().forEach(this::productOutStore);
        if(invoiceHeader.getRemain()>0){
            log.info("saving dept in Customer :"+myCustomer.getName());
            DeptOut deptOut= DeptOut.builder()
                    .description("دين فاتورة بيع رقم "+ invoiceHeader.getId())
                    .customer(invoiceHeader.getCustomer())
                    .date(invoiceHeader.getDate())
                    .build();
            deptOut.addDept(invoiceHeader.getRemain());
            myCustomer=customerRepository.save(deptOut.getCustomer());
            deptOutRepository.save(deptOut);
        }
        initInvoicetTF(saved);
        AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "تم الحفظ", null);
    }
    void productOutStore(InvoiceItem invoiceItem){
        InvoiceHeader invoiceHeader= invoiceItem.getInvoiceHeader();
        log.info("Saving item with id:{} in store id:{}",invoiceItem.getProduct().getId(),invoiceHeader.getStore().getId());
        StoreHasProduct byProductAndStore = storeProductRepository.findByProduct_IdAndStore_Id(invoiceItem.getProduct().getId(), invoiceHeader.getStore().getId());
        try {
            byProductAndStore.subQuantity(invoiceItem.getQuantity());
        } catch (Exception exception) {
            log.error("[productOutStore] error occur {}", exception);
            AlertMaker.showErrorMessage(exception);
        }
        storeProductRepository.save(byProductAndStore);
    }

    private Set<InvoiceItem> toInvoiceItems(InvoiceHeader invoiceHeader) {
        Set<InvoiceItem> ret = new HashSet<>();
        list.forEach(order -> {
            ret.add(toInvoiceItem(order, invoiceHeader));
        });
        return ret;
    }

    private InvoiceItem toInvoiceItem(Order order, InvoiceHeader invoiceHeader) {
        return InvoiceItem.builder()
                .product(new Product(order.getProductId(), order.getProductName(), order.getProductType(), order.getProductPrice()))
                .quantity(order.getProductQnt())
                .invoiceHeader(invoiceHeader)
                .build();
    }

    public void print() {
        if(myInvoiceHeader.getId()==null){
            JFXButton button = new JFXButton("That's Okay");
            AlertMaker.showMaterialDialog(rootPane, mainContainer, Arrays.asList(button), "يرجى اختيار او حفظ فاتورة للطباعة", null);
            return;
        }
        try {
            reportService.exportReport("saleInvoice","invoiceHeader", myInvoiceHeader, myInvoiceHeader.getInvoiceItems());
        } catch (Exception exception) {
            log.error("[Generate PDF] Error occur : {}",exception);
            AlertMaker.showErrorMessage(exception);
        }
    }

    private Stage getStage() {
        return (Stage) mainContainer.getScene().getWindow();
    }
}
