package com.elkattanman.javafxapp.controller.bars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/bars/titleBar.fxml")
public class TitleBarController implements Initializable {
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    @FXML
    public void closeScreen(MouseEvent mouseEvent) {
        final Stage stage = getStage(mouseEvent);
        stage.close();
    }

    private Stage getStage(MouseEvent mouseEvent) {
        final Node source = (Node) mouseEvent.getSource();
        return (Stage) source.getScene().getWindow();
    }

    @FXML
    public void minimizeScreen(MouseEvent mouseEvent) {
        final Stage stage = getStage(mouseEvent);
        stage.setIconified(true);
    }

    public void fullScreen(MouseEvent mouseEvent) {
        final Stage stage = getStage(mouseEvent);
        stage.setFullScreen(!stage.isFullScreen());
    }
}
