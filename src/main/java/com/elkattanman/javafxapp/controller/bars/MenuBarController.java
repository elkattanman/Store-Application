package com.elkattanman.javafxapp.controller.bars;

import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/bars/menuBar.fxml")
public class MenuBarController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
