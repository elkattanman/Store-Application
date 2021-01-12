 
package com.elkattanman.javafxapp.controller;


import com.elkattanman.javafxapp.controller.bars.ToolbarController;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTabPane;
import javafx.event.ActionEvent;
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

import static com.elkattanman.javafxapp.util.AssistantUtil.*;

/**
 * FXML Controller class
 *
 * @author AMORY
 */

@Component
@FxmlView("/FXML/main.fxml")
public class MainController implements Initializable {

    @FXML private StackPane rootPane;
    @FXML public AnchorPane rootAnchorPane;
    @FXML public JFXTabPane mainTabPane;

    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;


    @Autowired private FxWeaver fxWeaver;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        makeDraggable(rootPane);
        initComponents();
        initDrawer(drawer, hamburger, fxWeaver.loadView(ToolbarController.class));
    }

    private void initComponents() {
        mainTabPane.tabMinWidthProperty().bind(rootAnchorPane.widthProperty().divide(mainTabPane.getTabs().size()).subtract(15));
    }

    @FXML
    void hrkatHandleAction(ActionEvent event) {
        loadWindow(getStage(rootPane),fxWeaver.loadView(HrkatController.class));
    }

    public void close_app(){
        getStage(rootPane).close();
    }


}
