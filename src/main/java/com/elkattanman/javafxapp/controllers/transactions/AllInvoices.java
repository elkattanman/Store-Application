package com.elkattanman.javafxapp.controllers.transactions;

import com.elkattanman.javafxapp.DTO.ReceiptDTO;
import com.elkattanman.javafxapp.controllers.CallBack;
import com.elkattanman.javafxapp.domain.InvoiceHeader;
import com.elkattanman.javafxapp.repositories.InvoiceRepository;
import com.jfoenix.controls.JFXDatePicker;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Component
@FxmlView("/FXML/tranactions/all_invoices.fxml")
public class AllInvoices implements Initializable {
    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private JFXDatePicker dateFromDP, dateToDP;

    @FXML
    private JFXTextField searchTF;

    @FXML
    private TableView<ReceiptDTO> table;
    @FXML
    private TableColumn<ReceiptDTO, Integer> idCol;
    @FXML
    private TableColumn<ReceiptDTO, LocalDate> dateCol;

    @FXML
    private TableColumn<ReceiptDTO, String> supplierCol, storeCol;

    @FXML
    private TableColumn<ReceiptDTO, Double> totalCol;

    private CallBack callBack;

    private ObservableList<ReceiptDTO> list = FXCollections.observableArrayList();

    private final InvoiceRepository receiptRepository;

    public AllInvoices(InvoiceRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initCol();
        List<InvoiceHeader> all = receiptRepository.findAll();
        List<ReceiptDTO> receiptDTOS=receiptDTOS = all.stream().map(this::toDTO).collect(Collectors.toList());
        list.setAll(receiptDTOS);
        table.setItems(list);
        MakeMyFilter();
        rowDoubleClick();
    }

    private void rowDoubleClick() {
        table.setRowFactory(tableView -> {
            TableRow<ReceiptDTO> row =new TableRow<>();
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

    private ReceiptDTO toDTO(InvoiceHeader receiptHeader){
//        double total=0;
//        for (InvoiceItem receiptItem : receiptHeader.getInvoiceItems()) {
//            total+=receiptItem.anTotal();
//        }
        return ReceiptDTO.builder()
                .id(receiptHeader.getId())
                .date(receiptHeader.getDate())
                .storeName(receiptHeader.getStore().getName())
                .supplierName(receiptHeader.getCustomer().getName())
                .Total(receiptHeader.getTotalPrice())
                .build();
    }


    private void MakeMyFilter() {
        FilteredList<ReceiptDTO> filteredData = new FilteredList<>(list, s -> true);

        searchTF.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(receiptDTO -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if(receiptDTO.getSupplierName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                }
                return false;
            });

        });

        SortedList<ReceiptDTO> sortedInvoices = new SortedList<>(filteredData) ;
        sortedInvoices.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedInvoices);
    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        supplierCol.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        storeCol.setCellValueFactory(new PropertyValueFactory<>("storeName"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    @FXML
    void SelectAction() {
        ReceiptDTO selectedItem = table.getSelectionModel().getSelectedItem();
        callBack.callBack(receiptRepository.findById(selectedItem.getId()).get());
        getStage().close();
    }

    private Stage getStage() {
        return (Stage) table.getScene().getWindow();
    }

    public void closeStage(ActionEvent actionEvent) {
        getStage().close();
    }

    @FXML
    void handleRefresh(ActionEvent event) {

    }
}
