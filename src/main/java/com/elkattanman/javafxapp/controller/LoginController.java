package com.elkattanman.javafxapp.controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/FXML/Login.fxml")
public class LoginController implements Initializable {

    private final FxWeaver fxWeaver;
    
    @FXML private AnchorPane rootPane;
    @FXML
    private FontAwesomeIconView closeButton;

    @FXML
    private FontAwesomeIconView minimizeButton;

    @FXML
    private JFXTextField txtFld;
    @FXML
    private JFXPasswordField txtPass;

    public LoginController(FxWeaver fxWeaver) {
        this.fxWeaver=fxWeaver;
    }

    @FXML
    private void loginAction(){
        System.out.println("HEllo World");
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
    
    
}
