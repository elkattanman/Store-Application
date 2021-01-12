package com.elkattanman.javafxapp.controller;

import com.elkattanman.javafxapp.util.AlertMaker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import static com.elkattanman.javafxapp.util.AssistantUtil.getStage;
import static com.elkattanman.javafxapp.util.AssistantUtil.loadWindow;

@Component
@FxmlView("/FXML/Login.fxml")
public class LoginController implements Initializable {

    private final FxWeaver fxWeaver;

    @FXML private AnchorPane rootPane;

    @FXML
    private JFXTextField txtFld;
    @FXML
    private JFXPasswordField txtPass;

    public LoginController(FxWeaver fxWeaver) {
        this.fxWeaver=fxWeaver;
    }

    @FXML
    private void loginAction(){
        doLogin();
    }
    void doLogin(){
        if (txtFld.getText().equals("admin") && txtPass.getText().equals("123456")) {
            loadWindow(getStage(rootPane), fxWeaver.loadView(MainController.class));
        } else {
            AlertMaker.showSimpleAlert("Email or password not valid", "اسم المستخدم او كلمة المرور خطأ");
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

    }


    public void pressEnter(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            doLogin();
        }
    }
}
