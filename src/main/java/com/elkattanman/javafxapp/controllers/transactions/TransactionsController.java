package com.elkattanman.javafxapp.controllers.transactions;


import com.elkattanman.javafxapp.controllers.bars.ToolbarController;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

import static com.elkattanman.javafxapp.util.AssistantUtil.*;


@Slf4j
@Component
@FxmlView("/FXML/tranactions/transactions.fxml")
public class TransactionsController implements Initializable {

    @FXML StackPane rootPane;
    @Autowired private FxWeaver fxWeaver;
    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML public AnchorPane rootAnchorPane;
    @FXML public JFXTabPane mainTabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable(rootPane);
        initDrawer(drawer, hamburger, fxWeaver.loadView(ToolbarController.class));
        initComponents();

    }
    private void initComponents() {
        mainTabPane.tabMinWidthProperty().bind(rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
    }

}
