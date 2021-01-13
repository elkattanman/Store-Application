package com.elkattanman.javafxapp.controllers.basics;

import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/basics/stores.fxml")
public class StoresController  implements Initializable {
    @Autowired
    private FxWeaver fxWeaver;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
