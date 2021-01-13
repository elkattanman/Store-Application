package com.elkattanman.javafxapp.controllers.basics;

import com.elkattanman.javafxapp.controllers.bars.ToolbarController;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;


@Component
@FxmlView("/FXML/basics/basics.fxml")
public class BasicsController implements Initializable {


    @Autowired private FxWeaver fxWeaver;
    @FXML StackPane rootPane;
    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML public AnchorPane rootAnchorPane;
    @FXML public JFXTabPane mainTabPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AssistantUtil.makeDraggable(rootPane);
        AssistantUtil.initDrawer(drawer, hamburger, fxWeaver.loadView(ToolbarController.class));
        initComponents();
    }
    private void initComponents() {
        mainTabPane.tabMinWidthProperty().bind(rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
    }
}
