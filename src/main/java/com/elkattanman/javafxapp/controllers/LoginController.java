package com.elkattanman.javafxapp.controllers;

import com.elkattanman.javafxapp.util.AlertMaker;
import com.elkattanman.javafxapp.util.AssistantUtil;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/FXML/Login.fxml")
public class LoginController implements Initializable {

    @Autowired private FxWeaver fxWeaver;

    @FXML private AnchorPane rootPane;

    @FXML
    private JFXTextField txtFld;
    @FXML
    private JFXPasswordField txtPass;

    public LoginController() {}

    private void Animation() {
        FadeTransition fadeTransition1 = new FadeTransition(Duration.seconds(1.5), rootPane);
        fadeTransition1.setFromValue(0);
        fadeTransition1.setToValue(1);
        fadeTransition1.play();
    }

    @FXML
    private void loginAction(){
        doLogin();
    }
    void doLogin(){
        if (txtFld.getText().equals("admin") && txtPass.getText().equals("123456")) {
            AssistantUtil.loadWindow(AssistantUtil.getStage(rootPane), fxWeaver.loadView(MainController.class));
        } else {
            AlertMaker.showSimpleAlert("Email or password not valid", "اسم المستخدم او كلمة المرور خطأ");
            txtFld.getStyleClass().add("wrong-credentials");
            txtPass.getStyleClass().add("wrong-credentials");
        }
    }

    @FXML
    private void closeButtonAction(){
        Stage st=(Stage)rootPane.getScene().getWindow();st.close();
    }

    @FXML
    private void minimizeButtonAction(){
        Stage st=(Stage)rootPane.getScene().getWindow();st.setIconified(true);
    }

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Animation();
    }


    public void pressEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            doLogin();
        }
    }
}
