package com.elkattanman.javafxapp.controller.learningSample;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


/**
 * @author <a href="moustafaQattan8@gmail.com">Mustafa Khaled</a>
 */
@Component
@FxmlView("/FXML/learningSample/TiledDialogController.fxml")
public class TiledDialogController {

    private Stage stage;
    @FXML
    private VBox dialog;
    @FXML
    public Button closeButton;


    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setScene(new Scene(dialog));
    }

    public void show() {
        stage.show();
        closeButton.setOnAction(
                a -> stage.close()
        );
    }

}
