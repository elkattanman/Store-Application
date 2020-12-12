package com.elkattanman.javafxapp.controller.learningSample.tiles;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

/**
 * SimpleTileController.
 *
 * @author Rene Gielen
 */

@Component
@FxmlView("/FXML/learningSample/SimpleDialog.fxml")
public class SimpleTileController {

    @FXML
    public Label label;

    @FXML
    public void initialize() {
        label.setText(label.getText() + " initialized");
    }

}


