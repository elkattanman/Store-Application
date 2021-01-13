package com.elkattanman.javafxapp.controllers.basics.products;

import com.elkattanman.javafxapp.util.AssistantUtil;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/basics/products/products.fxml")
public class ProductsController implements Initializable {
    @Autowired
    private FxWeaver fxWeaver;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void add(ActionEvent actionEvent) {
        AssistantUtil.loadWindow(null, fxWeaver.loadView(ProductAddController.class));
    }
}
