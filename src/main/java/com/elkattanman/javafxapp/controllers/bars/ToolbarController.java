package com.elkattanman.javafxapp.controllers.bars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@FxmlView("/FXML/bars/toolbar.fxml")
public class ToolbarController implements Initializable {



    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void loadAddMember(ActionEvent event) {
//        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addmember/member_add.fxml"), "Add New Member", null);
    }

    @FXML
    private void loadAddBook(ActionEvent event) {
//        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addbook/add_book.fxml"), "Add New Book", null);
    }

    @FXML
    private void loadMemberTable(ActionEvent event) {
//        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listmember/member_list.fxml"), "Member List", null);
    }

    @FXML
    private void loadBookTable(ActionEvent event) {
//        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listbook/book_list.fxml"), "Book List", null);
    }

    @FXML
    private void loadSettings(ActionEvent event) {
//        LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/settings/settings.fxml"), "Settings", null);
    }

    @FXML
    private void loadIssuedBookList(ActionEvent event) {
//        Object controller = LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/issuedlist/issued_list.fxml"), "Issued Book List", null);
//        if (controller != null) {
//            IssuedListController cont = (IssuedListController) controller;
//            cont.setBookReturnCallback(callback);
//        }
    }

}
