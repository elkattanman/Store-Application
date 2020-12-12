package com.elkattanman.javafxapp.controller.learningSample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;


/**
 * @author <a href="moustafaQattan8@gmail.com">Mustafa Khaled</a>
 */
@Component
@FxmlView("/FXML/learningSample/AnotherDialog.fxml")
public class AnotherDialog {

    @FXML
    VBox dialog;

    private Stage stage;

    @FXML
    public void initialize() {
        this.stage = new Stage();
        stage.setTitle("Another Dialog");
        stage.setScene(new Scene(dialog));
    }

    public void show() {
        stage.show();
    }

    @FXML
    void click(ActionEvent actionEvent) {
        stage.close();
    }

}
