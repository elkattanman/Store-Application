package com.elkattanman.javafxapp.controllers.basics.products;


import com.elkattanman.javafxapp.domain.Product;
import com.elkattanman.javafxapp.repositories.ProductRepository;
import com.elkattanman.javafxapp.util.AlertMaker;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/basics/products/add_product.fxml")
public class ProductAddController implements Initializable {

    @FXML
    private JFXTextField nameTF, typeTF, priceTF;

    @FXML
    private JFXButton saveButton, cancelButton;
    @FXML
    private StackPane rootPane;
    @FXML
    private AnchorPane mainContainer;

    private final ProductRepository productRepository;

    private Boolean isInEditMode = Boolean.FALSE;

    public ProductAddController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

//        return Product.builder().name(name).type(type).price(price).build();

    @FXML
    private void addProduct(ActionEvent event) {
        String name=StringUtils.trimToEmpty(nameTF.getText());
        String type=StringUtils.trimToEmpty(typeTF.getText());
        String priceString=StringUtils.trimToEmpty(priceTF.getText());
        double price=0;
        try {
            price=Double.parseDouble(priceString.isEmpty()?"0":priceString);
        }catch (Exception ex){
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "من فضلك ادخل رقم  فى السعر");
            return;
        }
        if (name.isEmpty() || type.isEmpty() || priceString.isEmpty()) {
            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Insufficient Data", "Please enter data in all fields.");
            return;
        }

        if (isInEditMode) {
            handleEditOperation();
            return;
        }
        Product product=Product.builder().name(name).type(type).price(price).build();
        Product savedProduct = productRepository.save(product);

//        if (DataHelper.isBookExists(bookID)) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Duplicate book id", "Book with same Book ID exists.\nPlease use new ID");
//            return;
//        }
//
//        Book book = new Book(bookID, bookName, bookAuthor, bookPublisher, Boolean.TRUE);
//        boolean result = DataHelper.insertNewBook(book);
//        if (result) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "New book added", bookName + " has been added");
//            clearEntries();
//        } else {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed to add new book", "Check all the entries and try again");
//        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    private void checkData() {
        String qu = "SELECT title FROM BOOK";
//        ResultSet rs = databaseHandler.execQuery(qu);
//        try {
//            while (rs.next()) {
//                String titlex = rs.getString("title");
//                System.out.println(titlex);
//            }
//        } catch (SQLException ex) {
//            Logger.getLogger(BookAddController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

//    public void inflateUI(BookListController.Book book) {
//        title.setText(book.getTitle());
//        id.setText(book.getId());
//        author.setText(book.getAuthor());
//        publisher.setText(book.getPublisher());
//        id.setEditable(false);
//        isInEditMode = Boolean.TRUE;
//    }

    private void clearEntries() {
        nameTF.clear();
        typeTF.clear();
        priceTF.clear();
    }

    private void handleEditOperation() {
//        BookListController.Book book = new BookListController.Book(title.getText(), id.getText(), author.getText(), publisher.getText(), true);
//        if (databaseHandler.updateBook(book)) {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Success", "Update complete");
//        } else {
//            AlertMaker.showMaterialDialog(rootPane, mainContainer, new ArrayList<>(), "Failed", "Could not update data");
//        }
    }
}
