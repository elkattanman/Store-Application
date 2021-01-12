package com.elkattanman.javafxapp.controller.Mustafa;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/Mustafa/Home.fxml")
public class Home implements Initializable {

    @FXML
    public AnchorPane rootPane, opacityPane, drawerPane;
    @FXML
    public ImageView drawerImage;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        opacityPane.setVisible(false);

        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),opacityPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();


//        pane1.setStyle("-fx-background-image: url(\"/sample/image/11.jpg\")");
//        pane2.setStyle("-fx-background-image: url(\"/sample/image/22.jpg\")");
//        pane3.setStyle("-fx-background-image: url(\"/sample/image/33.jpg\")");
//        pane4.setStyle("-fx-background-image: url(\"/sample/image/44.jpg\")");

//        Animation();


        drawerImage.setOnMouseClicked(event -> {

            opacityPane.setVisible(true);

            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(+600);
            translateTransition1.play();
        });

        opacityPane.setOnMouseClicked(event -> {



            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0.15);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                opacityPane.setVisible(false);
            });


            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(-600);
            translateTransition1.play();
        });
    }

    @FXML
    public void BTN_Action(ActionEvent actionEvent) {

    }

    @FXML
    private void closeButtonAction(){
        Stage stage= (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void minimizeButtonAction(){
        Stage stage= (Stage) rootPane.getScene().getWindow();
        stage.setIconified(true);
    }
}
